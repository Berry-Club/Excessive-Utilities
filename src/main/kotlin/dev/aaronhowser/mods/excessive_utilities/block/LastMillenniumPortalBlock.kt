package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block_entity.LastMillenniumPortalBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDimensionProvider
import dev.aaronhowser.mods.excessive_utilities.handler.LastMillenniumHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.portal.DimensionTransition
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class LastMillenniumPortalBlock : Block(
	Properties
		.ofFullCopy(Blocks.IRON_BLOCK)
), EntityBlock {

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		if (level !is ServerLevel) return InteractionResult.PASS

		val handler = LastMillenniumHandler.get(level)
		val blockEntity = level.getBlockEntity(pos) as? LastMillenniumPortalBlockEntity ?: return InteractionResult.PASS

		if (level.dimension() == ModDimensionProvider.MILLENNIUM_LEVEL_KEY) {
			handler.returnFromDimension(player)
		} else {
			handler.teleportIntoDimension(player, blockEntity.owner)
		}

		return InteractionResult.SUCCESS
	}

	override fun getDestroyProgress(
		state: BlockState,
		player: Player,
		level: BlockGetter,
		pos: BlockPos
	): Float {
		if (level is Level && level.dimension() == ModDimensionProvider.MILLENNIUM_LEVEL_KEY) {
			return 0f
		}

		return super.getDestroyProgress(state, player, level, pos)
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return LastMillenniumPortalBlockEntity(pos, state)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		if (placer != null && level is ServerLevel) {
			val blockEntity = level.getBlockEntity(pos)
			if (blockEntity is LastMillenniumPortalBlockEntity) {
				blockEntity.owner = placer.uuid
			}
		}
	}

	companion object {

		private fun getDestinationInLastMillennium(level: ServerLevel, entity: Entity): DimensionTransition {
			val targetLevel = LastMillenniumHandler.getLastMillenniumLevel(level)
			val chunkPos = LastMillenniumHandler.get(level).getChunk(entity)

			val post = DimensionTransition.PLACE_PORTAL_TICKET
			return DimensionTransition(
				targetLevel,
				chunkPos.getMiddleBlockPosition(64).bottomCenter,
				Vec3.ZERO,
				0f,
				0f,
				false,
				post
			)
		}
	}

}