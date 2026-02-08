package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.items.wrapper.InvWrapper

open class GeneratorContainer(
	blockEntity: BlockEntity,
	val amountInputs: Int
) : ImprovedSimpleContainer(blockEntity, amountInputs + 1) {

	final override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
		return when (slot) {
			INPUT_SLOT -> canPlaceInput(stack)
			UPGRADE_SLOT -> canPlaceUpgrade(stack)
			SECONDARY_INPUT_SLOT -> canPlaceSecondaryInput(stack)
			else -> false
		}
	}

	protected open fun canPlaceInput(stack: ItemStack): Boolean = false
	protected open fun canPlaceUpgrade(stack: ItemStack): Boolean = stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
	protected open fun canPlaceSecondaryInput(stack: ItemStack): Boolean = false

	fun getSpeed(): Int = getItem(UPGRADE_SLOT).count + 1

	open val itemHandler: InvWrapper =
		object : InvWrapper(this) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = canPlaceItem(slot, stack)
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY
		}

	companion object {
		const val INPUT_SLOT = 0
		const val UPGRADE_SLOT = 1
		const val SECONDARY_INPUT_SLOT = 2
	}

}