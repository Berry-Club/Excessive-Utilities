package dev.aaronhowser.mods.excessive_utilities.block.entity.base

import dev.aaronhowser.mods.aaron.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.*

abstract class GpSourceBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(type, pos, blockState) {

	protected var ownerUuid: UUID? = null
	protected abstract val gpGeneration: GridPowerContribution

	fun setOwner(uuid: UUID) {
		ownerUuid = uuid
	}

	protected fun tick() {
		val owner = ownerUuid ?: return
		val level = level as? ServerLevel ?: return

		if (gpGeneration.isStillValid() && gpGeneration.getAmount() > 0) {
			val grid = GridPowerHandler.get(level).getGrid(owner)
			grid.addProducer(gpGeneration)
		}
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val uuid = ownerUuid
		if (uuid != null) {
			tag.putUUID(OWNER_UUID_NBT, uuid)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
	}

}