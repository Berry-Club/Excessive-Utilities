package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.trash.EnergyTrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.trash.FluidTrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.trash.TrashCanBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class TrashCanBlock(
	val type: Type
) : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		if (type == Type.CHEST) {
			val be = TrashCanBlockEntity(pos, state)
			be.isChest = true
			return be
		}

		if (type == Type.ITEM) {
			return TrashCanBlockEntity(pos, state)
		}

		if (type == Type.FLUID) {
			return FluidTrashCanBlockEntity(pos, state)
		}

		if (type == Type.ENERGY) {
			return EnergyTrashCanBlockEntity(pos, state)
		}

		return null
	}

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		val blockEntity = level.getBlockEntity(pos)

		if (blockEntity is MenuProvider) {
			player.openMenu(blockEntity)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	enum class Type(
		val id: String
	) : StringRepresentable {
		ITEM("ITEM"),
		CHEST("CHEST"),
		FLUID("FLUID"),
		ENERGY("ENERGY")
		;

		override fun getSerializedName(): String = id
	}

}