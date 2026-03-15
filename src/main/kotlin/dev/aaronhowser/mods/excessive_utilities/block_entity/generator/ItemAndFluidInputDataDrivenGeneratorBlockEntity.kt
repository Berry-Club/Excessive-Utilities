package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.*
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class ItemAndFluidInputDataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val itemAndFluidInputDataDrivenGeneratorType: ItemAndFluidInputDataDrivenGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override val generatorType: GeneratorType = itemAndFluidInputDataDrivenGeneratorType.baseGeneratorType

	protected fun getRecipe(itemStack: ItemStack): ItemAndFluidFuelRecipe? {
		val level = level ?: return null
		return ItemAndFluidFuelRecipe.getRecipe(level, itemAndFluidInputDataDrivenGeneratorType.fuelRecipeType, itemStack)
	}

	override fun isValidInput(itemStack: ItemStack): Boolean {
		return getRecipe(itemStack) != null
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false
		val container = container ?: return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val recipe = getRecipe(inputStack) ?: return false

		fePerTick = recipe.fePerTick
		burnTimeRemaining = recipe.duration

		inputStack.shrink(1)
		setChanged()

		return true
	}

	companion object {
		fun slimy(pos: BlockPos, state: BlockState) = ItemAndFluidInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.SLIMY_GENERATOR.get(),
			itemAndFluidInputDataDrivenGeneratorType = ItemAndFluidInputDataDrivenGeneratorType.SLIMY,
			pos = pos,
			blockState = state
		)

		fun heatedRedstone(pos: BlockPos, state: BlockState) = ItemAndFluidInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get(),
			itemAndFluidInputDataDrivenGeneratorType = ItemAndFluidInputDataDrivenGeneratorType.HEATED_REDSTONE,
			pos = pos,
			blockState = state
		)

	}

}