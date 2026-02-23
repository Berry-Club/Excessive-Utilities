package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.giveOrDropStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.level.Level

class EthericSwordItem(properties: Properties) : SwordItem(UnstableTier, properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide && !player.isFakePlayer) {
			val success = SoulDebt.removeSoulFragment(player, 1)

			if (success) {
				player.giveOrDropStack(ModItems.SOUL_FRAGMENT.getDefaultInstance())
				return InteractionResultHolder.success(stack)
			} else {
				player.tell(Component.literal("You don't have enough health to remove any more Soul Fragments!"))
				return InteractionResultHolder.fail(stack)
			}
		}

		return InteractionResultHolder.pass(stack)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(createAttributes(UnstableTier, 3f, -2.4f))
	}

}