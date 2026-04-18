package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDimensionProvider
import dev.aaronhowser.mods.excessive_utilities.handler.LastMillenniumHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Portal
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.portal.DimensionTransition
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class LastMillenniumPortalBlock : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)), Portal {

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		if (level !is ServerLevel) return InteractionResult.PASS

		val destination = getPortalDestination(level, player, pos)
		player.changeDimension(destination)
		return InteractionResult.SUCCESS
	}

	override fun getPortalDestination(level: ServerLevel, entity: Entity, pos: BlockPos): DimensionTransition {
		return if (level.dimension() == ModDimensionProvider.MILLENNIUM_LEVEL_KEY) {
			getDestinationFromLastMillennium(level, entity)
		} else {
			getDestinationInLastMillennium(level, entity)
		}
	}

	companion object {
		private fun getDestinationFromLastMillennium(level: ServerLevel, entity: Entity): DimensionTransition {

		}

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