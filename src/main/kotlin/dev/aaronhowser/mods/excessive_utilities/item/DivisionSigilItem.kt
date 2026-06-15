package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.handler.division_sigil.DivisionSigilActivation
import dev.aaronhowser.mods.excessive_utilities.handler.division_sigil.DivisionSigilInversion
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Blocks

class DivisionSigilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		if (level.isClientSide) return InteractionResult.PASS

		val player = context.player ?: return InteractionResult.SUCCESS
		val pos = context.clickedPos
		val stack = context.itemInHand

		if (isInverted(stack)) return InteractionResult.PASS

		if (checkActivationReady(player, pos)) return InteractionResult.SUCCESS
		if (checkInversionReady(player, pos)) return InteractionResult.SUCCESS

		return InteractionResult.PASS
	}

	override fun getName(stack: ItemStack): Component {
		return if (isInverted(stack)) {
			ModItemLang.PSEUDO_INVERSION_SIGIL.toComponent()
		} else {
			super.getName(stack)
		}
	}

	override fun isFoil(stack: ItemStack): Boolean {
		return isInverted(stack)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		if (isInverted(stack)) {
			tooltipComponents += ModMenuLang.INFINITE_USES.toComponent()
			return
		}

		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
		tooltipComponents += ModMenuLang.REMAINING_USES.toComponent(remainingUses)
	}

	override fun isBarVisible(stack: ItemStack): Boolean {
		return !isInverted(stack)
	}

	override fun getBarWidth(stack: ItemStack): Int {
		val maxWidth = 13
		if (isInverted(stack)) return maxWidth

		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
		return (remainingUses * maxWidth) / USES_AFTER_ACTIVATION
	}

	companion object {
		const val USES_AFTER_ACTIVATION = 256

		fun defaultProperties(): Properties {
			return Properties()
				.stacksTo(1)
				.fireResistant()
				.component(ModDataComponents.REMAINING_USES, USES_AFTER_ACTIVATION)
		}

		fun isInverted(stack: ItemStack): Boolean {
			return !stack.has(ModDataComponents.REMAINING_USES)
		}

		private fun checkActivationReady(
			player: Player,
			pos: BlockPos
		): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			if (!level.getBlockState(pos).isBlock(Blocks.ENCHANTING_TABLE)) return false

			val result = DivisionSigilActivation.isValidSetup(level, pos)
			sendMessages(player, result.messages)
			return result.isValid
		}

		private fun checkInversionReady(
			player: Player,
			pos: BlockPos
		): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			if (!level.getBlockState(pos).isBlock(Blocks.BEACON)) return false

			val result = DivisionSigilInversion.getInversionResult(level, pos)
			sendMessages(player, result.messages)
			return result.isValid
		}

		private fun sendMessages(
			player: Player,
			messages: List<Component>
		) {
			for (message in messages) {
				player.tell(message)
			}
		}

	}

}