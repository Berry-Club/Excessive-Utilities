package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.EnderQuarryUpgrade
import dev.aaronhowser.mods.excessive_utilities.block.entity.EnderQuarryUpgradeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderQuarryUpgradeBlock(
	val type: EnderQuarryUpgrade
) : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		val blockEntity = EnderQuarryUpgradeBlockEntity(pos, state)
		blockEntity.upgrade = type
		return blockEntity
	}

	override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
		for (dir in Direction.entries) {
			val stateThere = level.getBlockState(pos.relative(dir))
			if (stateThere.isBlock(ModBlockTagsProvider.ENDER_QUARRY_PART)) {
				return true
			}
		}

		return false
	}

	override fun updateShape(
		state: BlockState,
		direction: Direction,
		neighborState: BlockState,
		level: LevelAccessor,
		pos: BlockPos,
		neighborPos: BlockPos
	): BlockState {
		return if (canSurvive(state, level, pos)) {
			state
		} else {
			Blocks.AIR.defaultBlockState()
		}
	}

}