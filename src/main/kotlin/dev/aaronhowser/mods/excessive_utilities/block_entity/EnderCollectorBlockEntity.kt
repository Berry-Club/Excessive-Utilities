package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.excessive_utilities.block.EnderCollectorBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class EnderCollectorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SyncingBlockEntity(ModBlockEntityTypes.ENDER_COLLECTOR.get(), pos, blockState) {

	override val syncImmediately: Boolean = true

	var radius: Double = 0.5
		private set(value) {
			field = value
			setChanged()
		}

	fun cycleRadius(reverse: Boolean) {
		if (reverse) {
			val new = radius - 0.5
			radius = if (new < MIN_RADIUS) MAX_RADIUS else new
		} else {
			val new = radius + 0.5
			radius = if (new > MAX_RADIUS) MIN_RADIUS else new
		}
	}

	private fun serverTick(level: ServerLevel) {
		val powered = blockState.getValue(EnderCollectorBlock.POWERED)
		if (powered) return

		val itemHandler = getItemHandler(level) ?: return

		val aabb = AABB(blockPos).inflate(radius)
		val itemEntities = level.getEntitiesOfClass(ItemEntity::class.java, aabb)

		for (itemEntity in itemEntities) {
			val stack = itemEntity.item
			val countBefore = stack.count

			val remaining = ItemHandlerHelper.insertItemStacked(itemHandler, stack, false)
			val countAfter = remaining.count

			if (countBefore == countAfter) continue

			level.playSound(
				null,
				this.blockPos,
				SoundEvents.ITEM_PICKUP,
				SoundSource.BLOCKS,
				0.2f,
				level.random.nextRange(0.6f, 3.4f)
			)

			if (remaining.isEmpty) {
				itemEntity.discard()
			} else {
				itemEntity.item = remaining
			}
		}
	}

	private fun getItemHandler(level: ServerLevel): IItemHandler? {
		val facing = blockState.getValue(EnderCollectorBlock.FACING)
		val targetPos = worldPosition.relative(facing.opposite)
		return level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, facing)
	}

	companion object {
		const val MIN_RADIUS = 0.5
		const val MAX_RADIUS = 4.0

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: EnderCollectorBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}
	}

}