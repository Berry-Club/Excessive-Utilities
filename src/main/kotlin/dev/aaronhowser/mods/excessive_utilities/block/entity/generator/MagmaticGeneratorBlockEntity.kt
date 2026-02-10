package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class MagmaticGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.CULINARY_GENERATOR.get(), pos, blockState) {

	val tank: FluidTank =
		object : FluidTank(1_000_000) {
			override fun isFluidValid(stack: FluidStack): Boolean {
				return super.isFluidValid(stack)
			}
		}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tank.writeToNBT(registries, tag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		tank.readFromNBT(registries, tag)
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		TODO("Not yet implemented")
	}

}