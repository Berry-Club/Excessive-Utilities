package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpDrainBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class GpDrainBlock(
	properties: Properties,
) : Block(properties), EntityBlock {

	abstract fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity>

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is GpDrainBlockEntity && placer != null) {
			blockEntity.ownerUuid = placer.uuid
		}
	}

	override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			getBlockEntityType(),
			GpDrainBlockEntity::tick
		)
	}

}