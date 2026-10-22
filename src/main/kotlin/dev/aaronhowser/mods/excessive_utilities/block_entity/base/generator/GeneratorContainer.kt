package dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.TransactionContext

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

	fun getSpeed(): Int {
		val upgradeStack = getItem(UPGRADE_SLOT)
		if (!canPlaceUpgrade(upgradeStack)) return 1

		return upgradeStack.count + 1
	}

	open val itemHandler: ResourceHandler<ItemResource> =
		object : DelegatingResourceHandler<ItemResource>(VanillaContainerWrapper.of(this)) {
			override fun extract(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int = 0
			override fun extract(resource: ItemResource, amount: Int, transaction: TransactionContext): Int = 0
		}

	companion object {
		const val INPUT_SLOT = 0
		const val UPGRADE_SLOT = 1
		const val SECONDARY_INPUT_SLOT = 2
	}

}
