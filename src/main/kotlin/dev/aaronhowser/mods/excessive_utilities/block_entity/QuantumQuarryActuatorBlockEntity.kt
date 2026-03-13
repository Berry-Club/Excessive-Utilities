package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage

class QuantumQuarryActuatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.QUANTUM_QUARRY_ACTUATOR.get(), pos, blockState) {

	var quantumQuarryPos: BlockPos? = null
		set(value) {
			field = value
			setChanged()
		}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val pos = quantumQuarryPos
		if (pos != null) {
			tag.putLong(QUANTUM_QUARRY_POS_NBT, pos.asLong())
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		if (tag.contains(QUANTUM_QUARRY_POS_NBT)) {
			val posLong = tag.getLong(QUANTUM_QUARRY_POS_NBT)
			quantumQuarryPos = BlockPos.of(posLong)
		}
	}

	companion object {
		const val QUANTUM_QUARRY_POS_NBT = "QuantumQuarryPos"

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		fun getEnergyCapability(actuator: QuantumQuarryActuatorBlockEntity, direction: Direction?): IEnergyStorage? {
			val level = actuator.level ?: return null
			val quarryPos = actuator.quantumQuarryPos ?: return null
			return level.getCapability(Capabilities.EnergyStorage.BLOCK, quarryPos, direction)
		}
	}

}