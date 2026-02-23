package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.EnderQuarryUpgrade
import dev.aaronhowser.mods.excessive_utilities.block.entity.EnderQuarryUpgradeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
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
			if (stateThere.isBlock(ModBlocks.ENDER_QUARRY)) {
				return true
			}

			val beThere = level.getBlockEntity(pos.relative(dir))
			if (beThere is EnderQuarryUpgradeBlockEntity) {
				val quarryPos = beThere.quarryPos ?: continue
				val stateAtQuarry = level.getBlockState(quarryPos)
				if (stateAtQuarry.isBlock(ModBlocks.ENDER_QUARRY)) {
					return true
				}
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

	override fun setPlacedBy(
		level: Level,
		pos: BlockPos,
		state: BlockState,
		placer: LivingEntity?,
		stack: ItemStack
	) {
		val be = level.getBlockEntity(pos)
		if (be is EnderQuarryUpgradeBlockEntity) {
			for (dir in Direction.entries) {
				val posThere = pos.relative(dir)

				val stateThere = level.getBlockState(posThere)
				if (stateThere.isBlock(ModBlocks.ENDER_QUARRY)) {
					be.quarryPos = posThere
					return
				}

				val beThere = level.getBlockEntity(posThere)
				if (beThere is EnderQuarryUpgradeBlockEntity) {
					be.quarryPos = beThere.quarryPos
					return
				}
			}
		}
	}

}