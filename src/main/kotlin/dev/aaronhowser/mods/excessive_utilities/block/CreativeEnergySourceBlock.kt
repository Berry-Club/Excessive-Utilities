package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.CreativeEnergySourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class CreativeEnergySourceBlock : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return CreativeEnergySourceBlockEntity(pos, state)
	}

	override fun <T : BlockEntity> getTicker(
		level: Level,
		state: BlockState,
		blockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			ModBlockEntityTypes.CREATIVE_ENERGY_SOURCE.get(),
			CreativeEnergySourceBlockEntity::tick
		)
	}

}