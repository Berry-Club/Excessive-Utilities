package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

open class GeneratorContainer(
	blockEntity: BlockEntity
) : ImprovedSimpleContainer(blockEntity, CONTAINER_SIZE) {

	override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
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

	fun getSpeed(): Int = getItem(UPGRADE_SLOT).count

	companion object {
		const val CONTAINER_SIZE = 3
		const val INPUT_SLOT = 0
		const val UPGRADE_SLOT = 1
		const val SECONDARY_INPUT_SLOT = 2
	}

}