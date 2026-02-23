package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.EnderQuarryUpgradeType
import dev.aaronhowser.mods.excessive_utilities.block.entity.EnderQuarryBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.EnderQuarryUpgradeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
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
	val type: EnderQuarryUpgradeType
) : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		val blockEntity = EnderQuarryUpgradeBlockEntity(pos, state)
		blockEntity.upgradeType = type
		return blockEntity
	}

	override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
		val myBe = level.getBlockEntity(pos)
		if (myBe is EnderQuarryUpgradeBlockEntity) {
			val parentPos = myBe.parentBlock ?: return false
			val parentState = level.getBlockState(parentPos)

			return parentState.isBlock(ModBlockTagsProvider.ENDER_QUARRY_PART)
		}

		for (dir in Direction.entries) {
			val stateThere = level.getBlockState(pos.relative(dir))
			if (stateThere.isBlock(ModBlocks.ENDER_QUARRY)) {
				return true
			}

			val beThere = level.getBlockEntity(pos.relative(dir))
			if (beThere is EnderQuarryUpgradeBlockEntity) {
				val parentPos = beThere.parentBlock ?: continue
				val parentState = level.getBlockState(parentPos)

				if (parentState.isBlock(ModBlockTagsProvider.ENDER_QUARRY_PART)) {
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
		val be = level.getBlockEntity(pos) as? EnderQuarryUpgradeBlockEntity ?: return

		for (dir in Direction.entries) {
			val posThere = pos.relative(dir)

			val stateThere = level.getBlockState(posThere)
			if (stateThere.isBlock(ModBlockTagsProvider.ENDER_QUARRY_PART)) {
				be.parentBlock = posThere
				break
			}
		}

		val quarryPos = be.getQuarryPos() ?: return
		val quarryBe = level.getBlockEntity(quarryPos) as? EnderQuarryBlockEntity ?: return

		if (!quarryBe.addUpgrade(be)) {
			level.destroyBlock(pos, true)
		}
	}

}