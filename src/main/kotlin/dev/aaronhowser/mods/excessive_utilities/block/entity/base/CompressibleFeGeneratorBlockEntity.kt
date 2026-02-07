package dev.aaronhowser.mods.excessive_utilities.block.entity.base

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.*

abstract class CompressibleFeGeneratorBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(type, pos, blockState) {

	abstract val compressionLevel: Int
	abstract val generatorType: GeneratorType

	var ownerUuid: UUID? = null
	private var burnTimeRemaining: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	private val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, CONTAINER_SIZE)

	protected open fun serverTick(level: ServerLevel) {

	}

	protected open fun clientTick(level: Level) {}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		ContainerHelper.saveAllItems(tag, container.items, registries)
		tag.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)

		val uuid = ownerUuid
		if (uuid != null) {
			tag.putUUID(OWNER_UUID_NBT, uuid)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ContainerHelper.loadAllItems(tag, container.items, registries)
		burnTimeRemaining = tag.getInt(BURN_TIME_REMAINING_NBT)
		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
		const val BURN_TIME_REMAINING_NBT = "BurnTimeRemaining"

		const val CONTAINER_SIZE = 1
		const val INPUT_SLOT = 0

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: CompressibleFeGeneratorBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			} else {
				blockEntity.clientTick(level)
			}
		}
	}

}