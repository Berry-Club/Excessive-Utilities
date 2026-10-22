package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block_entity.CreativeHarvestBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.state.BlockBehaviour

class CreativeHarvestBlock(properties: BlockBehaviour.Properties) : Block(properties), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return CreativeHarvestBlockEntity(pos, state)
	}

	override fun useItemOn(
		stack: ItemStack,
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hand: InteractionHand,
		hitResult: BlockHitResult
	): InteractionResult {
		val stackInHand = player.getItemInHand(hand)
		val be = level.getBlockEntity(pos)

		if (be is CreativeHarvestBlockEntity) {
			val success = be.setMimic(stackInHand)
			return if (success) {
				InteractionResult.SUCCESS
			} else {
				InteractionResult.FAIL
			}
		}

		return InteractionResult.PASS
	}

	override fun canHarvestBlock(state: BlockState, level: BlockGetter, pos: BlockPos, player: Player): Boolean {
		val be = level.getBlockEntity(pos)
		if (be is CreativeHarvestBlockEntity) {
			return be.mimicBlockState.canHarvestBlock(level, pos, player)
		}

		return super.canHarvestBlock(state, level, pos, player)
	}

	override fun getDestroyProgress(state: BlockState, player: Player, level: BlockGetter, pos: BlockPos): Float {
		val be = level.getBlockEntity(pos)
		if (be is CreativeHarvestBlockEntity) {
			return be.mimicBlockState.getDestroyProgress(player, level, pos)
		}

		return super.getDestroyProgress(state, player, level, pos)
	}

	override fun playerDestroy(level: Level, player: Player, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?, destroyedWith: ItemStack) {
		if (player.isSecondaryUseActive || level !is ServerLevel || blockEntity !is CreativeHarvestBlockEntity) {
			super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith)
			return
		}

		val tool = player.mainHandItem

		val mimicBlockState = blockEntity.mimicBlockState

		val lootContext = LootParams.Builder(level)
			.withParameter(LootContextParams.ORIGIN, pos.center)
			.withParameter(LootContextParams.TOOL, tool)
			.withParameter(LootContextParams.THIS_ENTITY, player)

		val drops = mimicBlockState.getDrops(lootContext)

		for (drop in drops) {
			popResource(level, pos, drop)
		}
	}

	override fun updateShape(
		state: BlockState,
		level: LevelReader,
		 ticks: ScheduledTickAccess,
		pos: BlockPos,
		 directionToNeighbour: Direction,
		 neighbourPos: BlockPos,
		 neighbourState: BlockState,
		 random: RandomSource
	): BlockState {
		val be = level.getBlockEntity(pos)
		if (be is CreativeHarvestBlockEntity && level is LevelAccessor) {
			be.updateVisibleDirections(level)
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random)
	}

}












