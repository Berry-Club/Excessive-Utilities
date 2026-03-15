package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.ItemAndFluidInputDataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

open class ItemAndFluidInputDataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val specificType: ItemAndFluidInputDataDrivenGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override val generatorType: GeneratorType = specificType.baseGeneratorType

	val tank: FluidTank =
		object : FluidTank(1_000_000) {
			override fun isFluidValid(stack: FluidStack): Boolean {
				val level = level ?: return false
				return ItemAndFluidFuelRecipe.isValidFluid(stack, specificType.fuelRecipeType, level.recipeManager)
			}
		}

	protected fun getRecipe(itemStack: ItemStack, fluidStack: FluidStack): ItemAndFluidFuelRecipe? {
		val level = level ?: return null
		return ItemAndFluidFuelRecipe.getRecipe(level, specificType.fuelRecipeType, itemStack, fluidStack)
	}

	override fun isValidInput(itemStack: ItemStack): Boolean {
		val level = level ?: return false
		return ItemAndFluidFuelRecipe.isValidItem(itemStack, specificType.fuelRecipeType, level.recipeManager)
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false
		val container = container ?: return false

		val inputItemStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		val inputFluidStack = tank.fluid
		val recipe = getRecipe(inputItemStack, inputFluidStack) ?: return false

		fePerTick = recipe.fePerTick
		burnTimeRemaining = recipe.duration

		inputItemStack.shrink(1)
		tank.drain(recipe.fluidIngredient.amount(), IFluidHandler.FluidAction.EXECUTE)
		setChanged()

		return true
	}

	companion object {
		fun slimy(pos: BlockPos, state: BlockState) = ItemAndFluidInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.SLIMY_GENERATOR.get(),
			specificType = ItemAndFluidInputDataDrivenGeneratorType.SLIMY,
			pos = pos,
			blockState = state
		)

		fun heatedRedstone(pos: BlockPos, state: BlockState) = ItemAndFluidInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get(),
			specificType = ItemAndFluidInputDataDrivenGeneratorType.HEATED_REDSTONE,
			pos = pos,
			blockState = state
		)

		fun getFluidCapability(blockEntity: ItemAndFluidInputDataDrivenGeneratorBlockEntity, direction: Direction?): IFluidHandler {
			return blockEntity.tank
		}
	}

}