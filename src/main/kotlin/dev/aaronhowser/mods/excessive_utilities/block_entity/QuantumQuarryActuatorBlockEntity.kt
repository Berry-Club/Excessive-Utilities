package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.energy.EnergyHandler

class QuantumQuarryActuatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.QUANTUM_QUARRY_ACTUATOR.get(), pos, blockState) {

	var quantumQuarryPos: BlockPos? = null
		set(value) {
			field = value
			setChanged()
		}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		val pos = quantumQuarryPos
		if (pos != null) {
			tag.putLong(QUANTUM_QUARRY_POS_NBT, pos.asLong())
		}
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		tag.getLong(QUANTUM_QUARRY_POS_NBT).ifPresent { quantumQuarryPos = BlockPos.of(it) }
	}

	companion object {
		const val QUANTUM_QUARRY_POS_NBT = "QuantumQuarryPos"

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		fun getEnergyCapability(actuator: QuantumQuarryActuatorBlockEntity, direction: Direction?): EnergyHandler? {
			val level = actuator.level ?: return null
			val quarryPos = actuator.quantumQuarryPos ?: return null
			return level.getCapability(Capabilities.Energy.BLOCK, quarryPos, direction)
		}
	}

}
