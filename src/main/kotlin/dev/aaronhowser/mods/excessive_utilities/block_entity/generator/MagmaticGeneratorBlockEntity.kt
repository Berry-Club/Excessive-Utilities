package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.MagmaticFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GeneratorType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

//TODO: Placing a bucket in the inventory should empty the bucket and fill the tank
class MagmaticGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.MAGMATIC_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.MAGMATIC

	val tank: FluidTank =
		object : FluidTank(1_000_000) {
			override fun isFluidValid(stack: FluidStack): Boolean {
				val level = level ?: return false
				val recipe = MagmaticFuelRecipe.getRecipe(level, stack)
				return recipe != null
			}
		}

	fun getRecipe(): MagmaticFuelRecipe? {
		val level = level ?: return null
		return MagmaticFuelRecipe.getRecipe(level, tank.fluid)
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val fluidInTank = tank.fluid
		if (fluidInTank.isEmpty) return false

		val recipe = getRecipe() ?: return false

		fePerTick = recipe.fePerTick
		burnTimeRemaining = recipe.duration

		tank.drain(recipe.fluidIngredient.amount(), IFluidHandler.FluidAction.EXECUTE)
		setChanged()

		return true
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tank.writeToNBT(registries, tag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		tank.readFromNBT(registries, tag)
	}

	companion object {
		fun getFluidCapability(blockEntity: MagmaticGeneratorBlockEntity, direction: Direction?): IFluidHandler {
			return blockEntity.tank
		}
	}

}