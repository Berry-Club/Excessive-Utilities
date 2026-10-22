package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.server.level.ServerLevel

abstract class GpDrainBlock(
	properties: Properties,
) : Block(properties), EntityBlock {

	abstract fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity>

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is GpDrainBlockEntity && placer != null) {
			blockEntity.ownerUuid = placer.uuid
		}
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		val type = getBlockEntityType()
		return type.create(pos, state)
	}

	override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			getBlockEntityType(),
			GpDrainBlockEntity::tick
		)
	}

	override fun affectNeighborsAfterRemoval(state: BlockState, level: ServerLevel, pos: BlockPos, movedByPiston: Boolean) {
		if (true) {
			val be = level.getBlockEntity(pos)
			if (be is ContainerContainer) {
				be.dropContents(level, pos)
			}
		}

		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston)
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val be = level.getBlockEntity(pos)
		if (be is MenuProvider) {
			player.openMenu(be)
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

}










