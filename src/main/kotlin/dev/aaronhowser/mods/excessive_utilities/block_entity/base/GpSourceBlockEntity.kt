package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.putUuidIfNotNull
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.*

abstract class GpSourceBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(type, pos, blockState) {

	var ownerUuid: UUID? = null
	protected open val gpGeneration: GridPowerContribution =
		object : GridPowerContribution {
			override fun getAmount(): Double = getGpGeneration()
			override fun isStillValid(): Boolean = !this@GpSourceBlockEntity.isRemoved

			override fun getDisplayStack(): ItemStack = blockState.block.getDefaultInstance()
			override fun getDisplayName(): Component = blockState.block.name
			override fun getDisplayText(): Component {
				val amount = getAmount()
				return Component.literal("$amount at ${worldPosition.x}, ${worldPosition.y}, ${worldPosition.z}")
			}
		}

	abstract fun getGpGeneration(): Double

	protected open fun serverTick(level: ServerLevel) {
		val owner = ownerUuid ?: return

		if (gpGeneration.isStillValid() && gpGeneration.getAmount() > 0) {
			val grid = GridPowerHandler.get(level).getGrid(owner)
			grid.addProducer(gpGeneration)
		}
	}

	protected open fun clientTick(level: Level) {}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putUuidIfNotNull(OWNER_UUID_NBT, ownerUuid)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: GpSourceBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			} else {
				blockEntity.clientTick(level)
			}
		}
	}

}