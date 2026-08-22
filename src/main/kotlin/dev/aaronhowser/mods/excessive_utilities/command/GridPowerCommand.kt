package dev.aaronhowser.mods.excessive_utilities.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridGroupResult
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.server.level.ServerPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

object GridPowerCommand : AaronCommandHelper {

	const val PLAYER = "player"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("grid") {
			thenLiteral("invite") {
				thenArgument(PLAYER, EntityArgument.player()) {
					suggests { context, builder -> suggestInvitablePlayers(context.source, builder) }

					executes {
						invite(it.source, EntityArgument.getPlayer(it, PLAYER))
					}
				}
			}

			thenLiteral("accept") {
				executes { accept(it.source) }
			}

			thenLiteral("decline") {
				executes { decline(it.source) }
			}

			thenLiteral("leave") {
				executes { leave(it.source) }
			}

			thenLiteral("kick") {
				thenArgument(PLAYER, StringArgumentType.word()) {
					suggests { context, builder -> suggestGroupMembers(context.source, builder) }

					executes {
						kick(it.source, StringArgumentType.getString(it, PLAYER))
					}
				}
			}

			thenLiteral("members") {
				executes { members(it.source) }
			}
		}
	}

	private fun invite(source: CommandSourceStack, invitedPlayer: ServerPlayer): Int {
		val inviter = source.playerOrException
		val handler = GridPowerHandler.get(inviter.serverLevel())
		val result = handler.invite(inviter.uuid, invitedPlayer.uuid)

		when (result.status) {
			GridGroupResult.Status.SUCCESS -> Unit
			GridGroupResult.Status.CANNOT_TARGET_SELF -> {
				source.sendFailure(ModMessageLang.GRID_INVITE_SELF.toComponent())
				return 0
			}

			GridGroupResult.Status.ALREADY_IN_SAME_GROUP -> {
				source.sendFailure(ModMessageLang.GRID_ALREADY_MEMBER.toComponent())
				return 0
			}

			GridGroupResult.Status.TARGET_IN_ANOTHER_GROUP -> {
				source.sendFailure(ModMessageLang.GRID_TARGET_GROUPED.toComponent())
				return 0
			}

			GridGroupResult.Status.NOT_GROUP_OWNER -> {
				source.sendFailure(ModMessageLang.GRID_INVITE_NOT_OWNER.toComponent())
				return 0
			}

			else -> {
				source.sendFailure(ModMessageLang.GRID_INVITE_FAILED.toComponent())
				return 0
			}
		}

		source.sendSuccess(
			{ ModMessageLang.GRID_INVITE_SENT.toComponent(getPlayerName(invitedPlayer)) },
			false
		)
		invitedPlayer.tell(
			ModMessageLang.GRID_INVITE_RECEIVED.toComponent(getPlayerName(inviter))
		)
		return 1
	}

	private fun accept(source: CommandSourceStack): Int {
		val player = source.playerOrException
		val handler = GridPowerHandler.get(player.serverLevel())
		val result = handler.acceptInvitation(player.uuid)

		if (!result.isSuccess) {
			val message = when (result.status) {
				GridGroupResult.Status.NO_INVITATION -> ModMessageLang.GRID_NO_INVITATION.toComponent()
				else -> ModMessageLang.GRID_INVALID_INVITATION.toComponent()
			}

			source.sendFailure(message)
			return 0
		}

		val inviterUuid = result.inviterUuid ?: return 0
		val inviterName = getPlayerName(source, inviterUuid)
		source.sendSuccess({ ModMessageLang.GRID_JOINED.toComponent(inviterName) }, false)

		val inviter = source.server.playerList.getPlayer(inviterUuid)
		if (inviter != null) {
			inviter.tell(ModMessageLang.GRID_PLAYER_JOINED.toComponent(getPlayerName(player)))
		}
		return 1
	}

	private fun decline(source: CommandSourceStack): Int {
		val player = source.playerOrException
		val handler = GridPowerHandler.get(player.serverLevel())
		val result = handler.declineInvitation(player.uuid)

		if (!result.isSuccess) {
			source.sendFailure(ModMessageLang.GRID_NO_INVITATION.toComponent())
			return 0
		}

		val inviterUuid = result.inviterUuid ?: return 0
		val inviterName = getPlayerName(source, inviterUuid)
		source.sendSuccess({ ModMessageLang.GRID_INVITATION_DECLINED.toComponent(inviterName) }, false)
		return 1
	}

	private fun leave(source: CommandSourceStack): Int {
		val player = source.playerOrException
		val handler = GridPowerHandler.get(player.serverLevel())
		val result = handler.leaveGroup(player.uuid)

		if (!result.isSuccess) {
			source.sendFailure(ModMessageLang.GRID_NOT_SHARED.toComponent())
			return 0
		}

		source.sendSuccess({ ModMessageLang.GRID_LEFT.toComponent() }, false)
		return 1
	}

	private fun members(source: CommandSourceStack): Int {
		val player = source.playerOrException
		val handler = GridPowerHandler.get(player.serverLevel())
		val memberUuids = handler.getMemberUuids(player.uuid)
			.sortedBy { getPlayerName(source, it).string }
		val memberNames = ComponentUtils.formatList(memberUuids) { getPlayerName(source, it) }

		source.sendSuccess(
			{ ModMessageLang.GRID_MEMBERS.toComponent(memberNames) },
			false
		)
		return memberUuids.size
	}

	private fun kick(source: CommandSourceStack, memberName: String): Int {
		val owner = source.playerOrException
		val handler = GridPowerHandler.get(owner.serverLevel())
		val memberUuid = findMemberUuid(source, memberName)

		if (memberUuid == null) {
			source.sendFailure(ModMessageLang.GRID_TARGET_NOT_MEMBER.toComponent())
			return 0
		}

		val result = handler.removeMember(owner.uuid, memberUuid)

		if (!result.isSuccess) {
			val message = when (result.status) {
				GridGroupResult.Status.CANNOT_TARGET_SELF -> ModMessageLang.GRID_KICK_SELF.toComponent()
				GridGroupResult.Status.NOT_GROUP_OWNER -> ModMessageLang.GRID_KICK_NOT_OWNER.toComponent()
				GridGroupResult.Status.NOT_IN_SHARED_GROUP -> ModMessageLang.GRID_NOT_SHARED.toComponent()
				GridGroupResult.Status.TARGET_NOT_IN_GROUP -> ModMessageLang.GRID_TARGET_NOT_MEMBER.toComponent()
				else -> ModMessageLang.GRID_KICK_FAILED.toComponent()
			}

			source.sendFailure(message)
			return 0
		}

		source.sendSuccess(
			{ ModMessageLang.GRID_MEMBER_REMOVED.toComponent(getPlayerName(source, memberUuid)) },
			false
		)

		val member = source.server.playerList.getPlayer(memberUuid)
		if (member != null) {
			member.tell(ModMessageLang.GRID_REMOVED.toComponent(getPlayerName(owner)))
		}

		return 1
	}

	private fun suggestInvitablePlayers(
		source: CommandSourceStack,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val player = source.player ?: return builder.buildFuture()
		val handler = GridPowerHandler.get(player.serverLevel())
		if (!handler.canInvite(player.uuid)) return builder.buildFuture()

		val currentMemberUuids = handler.getMemberUuids(player.uuid)
		val names = mutableListOf<String>()

		for (otherPlayer in source.server.playerList.players) {
			if (currentMemberUuids.contains(otherPlayer.uuid)) continue
			if (handler.getMemberUuids(otherPlayer.uuid).size > 1) continue

			names.add(otherPlayer.gameProfile.name)
		}

		return SharedSuggestionProvider.suggest(names, builder)
	}

	private fun suggestGroupMembers(
		source: CommandSourceStack,
		builder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val player = source.player ?: return builder.buildFuture()
		val handler = GridPowerHandler.get(player.serverLevel())
		if (!handler.isGroupOwner(player.uuid)) return builder.buildFuture()

		val names = mutableListOf<String>()

		for (memberUuid in handler.getMemberUuids(player.uuid)) {
			if (memberUuid == player.uuid) continue
			names.add(getProfileName(source, memberUuid) ?: memberUuid.toString())
		}

		return SharedSuggestionProvider.suggest(names, builder)
	}

	private fun findMemberUuid(source: CommandSourceStack, memberName: String): UUID? {
		val player = source.playerOrException
		val handler = GridPowerHandler.get(player.serverLevel())

		for (memberUuid in handler.getMemberUuids(player.uuid)) {
			if (memberUuid.toString().equals(memberName, ignoreCase = true)) return memberUuid

			val profileName = getProfileName(source, memberUuid) ?: continue
			if (profileName.equals(memberName, ignoreCase = true)) return memberUuid
		}

		return null
	}

	private fun getPlayerName(source: CommandSourceStack, playerUuid: UUID): Component {
		val onlinePlayer = source.server.playerList.getPlayer(playerUuid)
		if (onlinePlayer != null) return getPlayerName(onlinePlayer)

		val profileName = getProfileName(source, playerUuid)
		if (profileName != null) return Component.literal(profileName)

		return Component.literal(playerUuid.toString())
	}

	private fun getProfileName(source: CommandSourceStack, playerUuid: UUID): String? {
		val profileCache = source.server.profileCache ?: return null
		return profileCache.get(playerUuid).orElse(null)?.name
	}

	private fun getPlayerName(player: ServerPlayer): Component {
		return player.displayName ?: player.name
	}
}