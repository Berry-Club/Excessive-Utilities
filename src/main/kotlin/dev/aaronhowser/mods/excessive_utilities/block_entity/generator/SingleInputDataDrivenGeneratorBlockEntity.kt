package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleItemFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.SingleInputDataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class SingleInputDataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val specificType: SingleInputDataDrivenGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override val generatorType: GeneratorType = specificType.baseGeneratorType

	protected fun getRecipe(itemStack: ItemStack): SingleItemFuelRecipe? {
		val level = level ?: return null
		return SingleItemFuelRecipe.getRecipe(level, specificType.fuelRecipeType, itemStack)
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
		fun ender(pos: BlockPos, state: BlockState) = SingleInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
			specificType = SingleInputDataDrivenGeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun explosive(pos: BlockPos, state: BlockState) = SingleInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get(),
			specificType = SingleInputDataDrivenGeneratorType.EXPLOSIVE,
			pos = pos,
			blockState = state
		)

		fun pink(pos: BlockPos, state: BlockState) = SingleInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.PINK_GENERATOR.get(),
			specificType = SingleInputDataDrivenGeneratorType.PINK,
			pos = pos,
			blockState = state
		)

		fun frosty(pos: BlockPos, state: BlockState) = SingleInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.FROSTY_GENERATOR.get(),
			specificType = SingleInputDataDrivenGeneratorType.FROSTY,
			pos = pos,
			blockState = state
		)

		fun halitosis(pos: BlockPos, state: BlockState) = SingleInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HALITOSIS_GENERATOR.get(),
			specificType = SingleInputDataDrivenGeneratorType.HALITOSIS,
			pos = pos,
			blockState = state
		)

	}

}