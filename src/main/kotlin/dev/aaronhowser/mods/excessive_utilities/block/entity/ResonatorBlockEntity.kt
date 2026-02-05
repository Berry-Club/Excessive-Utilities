package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.recipe.resonator.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class ResonatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.RESONATOR.get(), pos, blockState) {

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)

	override fun getGpUsage(): Int = getRecipe()?.gpCost ?: 0

	fun getRecipe(): ResonatorRecipe? {
		val level = level ?: return null

		val inputStack = container.getItem(INPUT_SLOT)
		val recipe = ResonatorRecipe.getRecipe(level, inputStack) ?: return null

		val stackInOutput = container.getItem(OUTPUT_SLOT)
		if (stackInOutput.isEmpty) return recipe

		val recipeOutput = recipe.getResultItem(level.registryAccess()).copy()
		val outputCanFit = ItemStack.isSameItemSameComponents(stackInOutput, recipeOutput)
				&& stackInOutput.count + recipeOutput.count <= stackInOutput.maxStackSize

		return if (outputCanFit) recipe else null
	}

	companion object {
		const val CONTAINER_SIZE = 2
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1
	}

}