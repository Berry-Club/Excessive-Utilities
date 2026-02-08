package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class EnchanterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.ENCHANTER.get(), pos, blockState) {

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)

	private val itemHandler: IItemHandlerModifiable =
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
				LEFT_INPUT_SLOT -> true
				RIGHT_INPUT_SLOT -> true
				UPGRADE_SLOT -> stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
				OUTPUT_SLOT -> false
				else -> false
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (slot != LEFT_INPUT_SLOT && slot != RIGHT_INPUT_SLOT) return stack
				return super.insertItem(slot, stack, simulate)
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				if (slot != OUTPUT_SLOT) return ItemStack.EMPTY
				return super.extractItem(slot, amount, simulate)
			}
		}

	override fun getGpUsage(): Double {
		TODO("Not yet implemented")
	}

	companion object {
		const val CONTAINER_SIZE = 4
		const val LEFT_INPUT_SLOT = 0
		const val RIGHT_INPUT_SLOT = 1
		const val OUTPUT_SLOT = 2
		const val UPGRADE_SLOT = 3
	}
}