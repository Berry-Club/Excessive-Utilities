package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderPorcupineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderPorcupineBlock : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return EnderPorcupineBlockEntity(pos, state)
	}

	override fun playerWillDestroy(
		level: Level,
		pos: BlockPos,
		state: BlockState,
		player: Player
	): BlockState {
		if (level.isServerSide) {
			val blockEntity = level.getBlockEntity(pos)

			if (blockEntity is EnderPorcupineBlockEntity) {
				val linkPos = blockEntity.linkedPosition
				if (linkPos != null && level.mayInteract(player, linkPos)) {
					val stateThere = level.getBlockState(linkPos)
					val broken = stateThere.block.playerWillDestroy(level, linkPos, stateThere, player)
				}
			}
		}

		return super.playerWillDestroy(level, pos, state, player)
	}

}