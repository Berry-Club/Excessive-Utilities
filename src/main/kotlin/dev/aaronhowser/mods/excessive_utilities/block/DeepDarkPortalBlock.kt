package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DeepDarkConstants
import dev.aaronhowser.mods.excessive_utilities.handler.DeepDarkHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class DeepDarkPortalBlock : Block(Properties.ofFullCopy(Blocks.STONE)) {

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		if (level !is ServerLevel) return InteractionResult.PASS

		val handler = DeepDarkHandler.get(level)

		if (level.dimension() == DeepDarkConstants.LEVEL_KEY) {
			handler.returnFromDimension(player)
		} else {
			handler.teleportIntoDimension(player, level, pos)
		}

		return InteractionResult.SUCCESS
	}

}
