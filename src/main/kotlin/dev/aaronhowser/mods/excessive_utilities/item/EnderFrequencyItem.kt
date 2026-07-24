package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency.EnderFrequencyMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class EnderFrequencyItem(
	val role: Role,
	properties: Properties
) : Item(properties) {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val constructor = MenuConstructor { containerId, inventory, _ ->
				EnderFrequencyMenu(containerId, inventory, usedHand)
			}

			player.openMenu(SimpleMenuProvider(constructor, stack.hoverName)) { data ->
				data.writeEnum(usedHand)
				val frequency = stack.get(ModDataComponents.ENDER_FREQUENCY)
				data.writeUtf(frequency?.name ?: "", EnderFrequencyComponent.MAX_NAME_LENGTH)
				data.writeBoolean(frequency?.isPrivate == true)
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
	}

	override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltip: MutableList<Component>, flag: TooltipFlag) {
		val frequency = stack.get(ModDataComponents.ENDER_FREQUENCY) ?: return
		tooltip.add(Component.literal(frequency.name).withStyle(ChatFormatting.LIGHT_PURPLE))
		tooltip.add(
			Component.translatable(
				if (frequency.isPrivate) ModMenuLang.ENDER_FREQUENCY_PRIVATE_TOOLTIP else ModMenuLang.ENDER_FREQUENCY_PUBLIC_TOOLTIP
			).withStyle(ChatFormatting.GRAY)
		)
	}

	enum class Role {
		TRANSMITTER,
		RECEIVER
	}
}