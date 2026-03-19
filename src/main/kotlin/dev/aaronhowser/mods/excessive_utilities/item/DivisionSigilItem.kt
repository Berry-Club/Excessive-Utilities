package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

class DivisionSigilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		if (level.isClientSide) return InteractionResult.SUCCESS

		val player = context.player ?: return InteractionResult.SUCCESS

		val pos = context.clickedPos
		val stack = context.itemInHand

		// If inverted already, don't do anything
		if (stack.getOrDefault(ModDataComponents.REMAINING_USES, 0) < 0) {
			return InteractionResult.SUCCESS
		}

		if (activate(level, player, pos, stack)) {
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

	override fun getName(stack: ItemStack): Component {
		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)

		return if (remainingUses < 0) {
			ModItemLang.PSEUDO_INVERSION_SIGIL.toComponent()
		} else {
			super.getName(stack)
		}
	}

	override fun isFoil(stack: ItemStack): Boolean {
		return stack.getOrDefault(ModDataComponents.REMAINING_USES, 0) < 0
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)

		val component = if (remainingUses < 0) {
			Component.literal("Infinite Uses")
		} else {
			Component.literal("$remainingUses Uses Remaining")
		}

		tooltipComponents.add(component)
	}

	companion object {
		const val USES_AFTER_ACTIVATION = 256
		const val USES_AFTER_INVERSION = -1

		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.fireResistant()
					.component(ModDataComponents.REMAINING_USES, 0)
			}

		fun activate(
			level: Level,
			player: Player,
			pos: BlockPos,
			stack: ItemStack
		): Boolean {
			val state = level.getBlockState(pos)

			if (!state.isBlock(Blocks.ENCHANTING_TABLE)) return false
			if (!level.getBiome(pos).isHolder(Tags.Biomes.IS_OVERWORLD)) {
				player.tell(Component.literal("You can only activate the Division Sigil in the Overworld!"))
				return false
			}

			return true
		}
	}

}