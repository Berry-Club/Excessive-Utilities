package dev.aaronhowser.mods.excessive_utilities.command

import com.mojang.authlib.GameProfile
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.handler.LastMillenniumHandler
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ChunkPos
import java.util.*

object LastMillenniumCommand : AaronCommandHelper {

	const val PLAYER = "player"
	const val CHUNK_X = "chunk_x"
	const val CHUNK_Z = "chunk_z"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("last-millennium") {
			requires { it.hasPermission(2) }

			thenLiteral("query") {
				executes {
					query(it.source, it.source.playerOrException.gameProfile)
				}

				thenArgument(PLAYER, GameProfileArgument.gameProfile()) {
					executes {
						val profiles = GameProfileArgument.getGameProfiles(it, PLAYER)
						query(it.source, getSingleProfile(it.source, profiles))
					}
				}
			}

			thenLiteral("assign") {
				executes {
					assign(it.source, it.source.playerOrException.gameProfile, chunkPos = null)
				}

				thenArgument(PLAYER, GameProfileArgument.gameProfile()) {
					executes {
						val profiles = GameProfileArgument.getGameProfiles(it, PLAYER)
						assign(it.source, getSingleProfile(it.source, profiles), chunkPos = null)
					}

					thenArgument(CHUNK_X, IntegerArgumentType.integer()) {
						thenArgument(CHUNK_Z, IntegerArgumentType.integer()) {
							executes {
								val profiles = GameProfileArgument.getGameProfiles(it, PLAYER)
								assign(it.source, getSingleProfile(it.source, profiles), getChunkPos(it))
							}
						}
					}
				}

				thenArgument(CHUNK_X, IntegerArgumentType.integer()) {
					thenArgument(CHUNK_Z, IntegerArgumentType.integer()) {
						executes {
							assign(it.source, it.source.playerOrException.gameProfile, getChunkPos(it))
						}
					}
				}
			}
		}
	}

	private fun query(
		source: CommandSourceStack,
		profile: GameProfile?
	): Int {
		if (profile == null) return 0

		val handler = LastMillenniumHandler.get(source.level)
		val chunkPos = handler.getAssignedChunk(profile.id)
		val playerName = getPlayerName(profile)

		if (chunkPos == null) {
			source.sendFailure(ModMessageLang.TLM_QUERY_NONE.toComponent(playerName))
			return 0
		}

		source.sendSuccess(
			{ ModMessageLang.TLM_QUERY.toComponent(playerName, chunkPos.x, chunkPos.z) },
			false
		)
		return 1
	}

	private fun assign(source: CommandSourceStack, profile: GameProfile?, chunkPos: ChunkPos?): Int {
		if (profile == null) return 0

		val destinationChunk = if (chunkPos == null) {
			val player = source.playerOrException
			if (player.level().dimension() != LastMillenniumHandler.LEVEL_KEY) {
				source.sendFailure(ModMessageLang.TLM_ASSIGN_WRONG_DIMENSION.toComponent())
				return 0
			}

			ChunkPos(player.blockPosition())
		} else {
			chunkPos
		}

		val handler = LastMillenniumHandler.get(source.level)
		val assignedPlayer = handler.getPlayerAtChunk(destinationChunk)

		if (assignedPlayer != null && assignedPlayer != profile.id) {
			val assignedPlayerName = getPlayerName(source, assignedPlayer)
			source.sendFailure(
				ModMessageLang.TLM_ASSIGN_OCCUPIED.toComponent(
					destinationChunk.x,
					destinationChunk.z,
					assignedPlayerName
				)
			)
			return 0
		}

		handler.assignChunk(profile.id, destinationChunk)
		source.sendSuccess(
			{
				ModMessageLang.TLM_ASSIGN_SUCCESS.toComponent(
					getPlayerName(profile),
					destinationChunk.x,
					destinationChunk.z
				)
			},
			true
		)
		return 1
	}

	private fun getChunkPos(context: CommandContext<CommandSourceStack>): ChunkPos {
		val chunkX = IntegerArgumentType.getInteger(context, CHUNK_X)
		val chunkZ = IntegerArgumentType.getInteger(context, CHUNK_Z)
		return ChunkPos(chunkX, chunkZ)
	}

	private fun getSingleProfile(source: CommandSourceStack, profiles: Collection<GameProfile>): GameProfile? {
		if (profiles.size == 1) return profiles.first()

		source.sendFailure(ModMessageLang.TLM_SINGLE_PLAYER.toComponent())
		return null
	}

	private fun getPlayerName(profile: GameProfile): Component {
		val name = profile.name
		if (name != null) return Component.literal(name)

		return Component.literal(profile.id.toString())
	}

	private fun getPlayerName(source: CommandSourceStack, playerUuid: UUID): Component {
		val onlinePlayer = source.server.playerList.getPlayer(playerUuid)
		if (onlinePlayer != null) return onlinePlayer.displayName ?: onlinePlayer.name

		val profileCache = source.server.profileCache
		val profile = profileCache?.get(playerUuid)?.orElse(null)
		if (profile != null) return getPlayerName(profile)

		return Component.literal(playerUuid.toString())
	}

}