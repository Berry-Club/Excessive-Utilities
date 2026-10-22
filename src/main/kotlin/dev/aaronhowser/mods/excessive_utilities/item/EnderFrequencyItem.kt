package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency.EnderFrequencyMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.level.Level

class EnderFrequencyItem(
	val role: Role,
	properties: Properties
) : Item(properties), TooltipItem {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResult {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val constructor = MenuConstructor { containerId, inventory, _ ->
				EnderFrequencyMenu(containerId, inventory, usedHand)
			}

			player.openMenu(SimpleMenuProvider(constructor, stack.hoverName)) { data ->
				data.writeEnum(usedHand)
			}
		}

		return InteractionResult.SUCCESS
	}

	override fun appendTooltip(stack: ItemStack, context: TooltipContext, display: TooltipDisplay, builder: java.util.function.Consumer<Component>, tooltipFlag: TooltipFlag) {
		val frequency = stack.get(ModDataComponents.ENDER_FREQUENCY) ?: return
		builder.accept(Component.literal(frequency.name).withStyle(ChatFormatting.LIGHT_PURPLE))
		builder.accept(
			if (frequency.isPrivate) {
				ModMenuLang.ENDER_FREQUENCY_PRIVATE_TOOLTIP
			} else {
				ModMenuLang.ENDER_FREQUENCY_PUBLIC_TOOLTIP
			}.toGrayComponent()
		)
	}

	enum class Role {
		TRANSMITTER,
		RECEIVER
	}
}







