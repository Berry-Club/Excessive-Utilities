package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.giveOrDropStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class EthericSwordItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResult {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide && !player.isFakePlayer) {
			val success = SoulDebt.removeSoulFragment(player, 1)

			if (success) {
				player.giveOrDropStack(ModItems.SOUL_FRAGMENT.getDefaultInstance())
				return InteractionResult.SUCCESS
			} else {
				player.tell(ModMessageLang.NO_REMOVE_SOUL_FRAGMENT.toComponent())
				return InteractionResult.FAIL
			}
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.durability(UnstableTier.durability())
	}

}




