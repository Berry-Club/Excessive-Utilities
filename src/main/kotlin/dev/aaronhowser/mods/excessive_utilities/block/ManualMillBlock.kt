package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpSourceBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.ManualMillBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class ManualMillBlock : GpSourceBlock(Properties.ofFullCopy(Blocks.STONE)) {

	override fun getBlockEntityType(): BlockEntityType<out GpSourceBlockEntity> {
		return ModBlockEntityTypes.MANUAL_MILL.get()
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return ManualMillBlockEntity(pos, state)
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is ManualMillBlockEntity) {
			blockEntity.startCranking(player)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

}