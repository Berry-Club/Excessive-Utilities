package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.GpPanelBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlock(
	val isDay: Boolean
) : Block(Properties.ofFullCopy(Blocks.DAYLIGHT_DETECTOR)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return GpPanelBlockEntity(pos, state, isDay)
	}

	override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			if (isDay) {
				ModBlockEntityTypes.SOLAR_PANEL.get()
			} else {
				ModBlockEntityTypes.LUNAR_PANEL.get()
			},
			GpPanelBlockEntity::tick
		)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is GpPanelBlockEntity && placer != null) {
			blockEntity.setOwner(placer.uuid)
		}
	}

}