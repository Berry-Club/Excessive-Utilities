package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.putUuidIfNotNull
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.recipe.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class ResonatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.RESONATOR.get(), pos, blockState), ContainerContainer {

	override fun getGpUsage(): Double {
		val level = level ?: return 0.0
		return getRecipe(level)?.gpCost ?: 0.0
	}

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)
	override fun getContainer(): Container = container

	private val itemHandler: IItemHandlerModifiable =
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = slot == INPUT_SLOT

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (slot != INPUT_SLOT) return stack
				return super.insertItem(slot, stack, simulate)
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				if (slot != OUTPUT_SLOT) return ItemStack.EMPTY
				return super.extractItem(slot, amount, simulate)
			}
		}

	fun getItemHandler(direction: Direction?): IItemHandlerModifiable = itemHandler

	private var progress: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		val recipe = getRecipe(level)

		if (recipe == null) {
			progress = 0
			return
		}

		progress++

		if (progress >= CRAFT_TIME) {
			craftItem(level, recipe)
			progress = 0
		}
	}

	private fun craftItem(level: ServerLevel, recipe: ResonatorRecipe) {
		val inputStack = container.getItem(INPUT_SLOT)

		val recipeOutput = recipe.getResultItem(level.registryAccess()).copy()
		val stackInOutput = container.getItem(OUTPUT_SLOT)
		if (stackInOutput.isNotEmpty()) {
			stackInOutput.grow(recipeOutput.count)
		} else {
			container.setItem(OUTPUT_SLOT, recipeOutput)
		}

		inputStack.shrink(1)
	}

	fun getRecipe(level: Level): ResonatorRecipe? {
		val inputStack = container.getItem(INPUT_SLOT)
		val recipe = ResonatorRecipe.getRecipe(level, inputStack) ?: return null

		val stackInOutput = container.getItem(OUTPUT_SLOT)
		if (stackInOutput.isEmpty) return recipe

		val recipeOutput = recipe.getResultItem(level.registryAccess()).copy()
		val outputCanFit = ItemStack.isSameItemSameComponents(stackInOutput, recipeOutput)
				&& stackInOutput.count + recipeOutput.count <= stackInOutput.maxStackSize

		return if (outputCanFit) recipe else null
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		ContainerHelper.saveAllItems(tag, container.items, registries)
		tag.putInt(PROGRESS_NBT, progress)
		tag.putUuidIfNotNull(OWNER_UUID_NBT, ownerUuid)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ContainerHelper.loadAllItems(tag, container.items, registries)
		progress = tag.getInt(PROGRESS_NBT)
	}

	companion object {
		const val PROGRESS_NBT = "Progress"

		const val CONTAINER_SIZE = 2
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1

		const val CRAFT_TIME = 20 * 10
	}

}