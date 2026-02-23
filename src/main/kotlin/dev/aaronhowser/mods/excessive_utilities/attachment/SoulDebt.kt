package dev.aaronhowser.mods.excessive_utilities.attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttachmentTypes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.event.entity.player.PlayerEvent

data class SoulDebt(
	val netSoulFragments: Int
) {

	constructor() : this(0)

	companion object {
		val CODEC: Codec<SoulDebt> =
			Codec.INT.xmap(::SoulDebt, SoulDebt::netSoulFragments)

		val STREAM_CODEC: StreamCodec<ByteBuf, SoulDebt> =
			ByteBufCodecs.VAR_INT.map(::SoulDebt, SoulDebt::netSoulFragments)

		val ATTRIBUTE_MODIFIER_NAME = ExcessiveUtilities.modResource("soul_debt")

		fun absorbSoulFragment(player: Player, amount: Int) {
			changeSoulFragmentAmount(player, amount)
		}

		fun removeSoulFragment(player: Player, amount: Int) {
			changeSoulFragmentAmount(player, -amount)
		}

		fun changeSoulFragmentAmount(player: Player, amount: Int) {
			val current = player.getData(ModAttachmentTypes.SOUL_DEBT).netSoulFragments
			player.setData(ModAttachmentTypes.SOUL_DEBT, SoulDebt(current + amount))

			updateSoulDebt(player)
		}

		fun updateSoulDebt(player: Player) {
			val maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH) ?: return
			val netSoulFragments = player.getData(ModAttachmentTypes.SOUL_DEBT).netSoulFragments

			if (maxHealthAttribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)) {
				maxHealthAttribute.removeModifier(ATTRIBUTE_MODIFIER_NAME)
			}

			if (netSoulFragments == 0) return

			val modifier = AttributeModifier(
				ATTRIBUTE_MODIFIER_NAME,
				netSoulFragments.toDouble() * ServerConfig.CONFIG.healthPerSoulFragment.get(),
				AttributeModifier.Operation.ADD_VALUE
			)

			maxHealthAttribute.addPermanentModifier(modifier)
		}

		fun onRespawn(event: PlayerEvent.PlayerRespawnEvent) {
			if (event.isEndConquered) return

			val player = event.entity
			val debt = player.getData(ModAttachmentTypes.SOUL_DEBT)

			when (ServerConfig.CONFIG.soulFragmentResetOnDeath.get()) {
				OnDeathConfig.KEEP -> {
					// do nothing
				}

				OnDeathConfig.RESET -> {
					player.setData(ModAttachmentTypes.SOUL_DEBT, SoulDebt(0))
					updateSoulDebt(player)
				}

				OnDeathConfig.REMOVE_NEGATIVE -> {
					if (debt.netSoulFragments < 0) {
						player.setData(ModAttachmentTypes.SOUL_DEBT, SoulDebt(0))
						updateSoulDebt(player)
					}
				}
			}
		}
	}

	enum class OnDeathConfig { KEEP, RESET, REMOVE_NEGATIVE }

}