package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.CreativeHarvestBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult

class CreativeHarvestBlock : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

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
	): ItemInteractionResult {
		val stackInHand = player.getItemInHand(hand)
		val item = stackInHand.item

		if (item is BlockItem && level is ServerLevel) {
			val be = level.getBlockEntity(pos) as? CreativeHarvestBlockEntity

			if (be != null) {
				be.mimicBlockState = item.block.defaultBlockState()
				return ItemInteractionResult.SUCCESS
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
	}

	override fun playerDestroy(
		level: Level,
		player: Player,
		pos: BlockPos,
		state: BlockState,
		blockEntity: BlockEntity?,
		tool: ItemStack
	) {
		if (level !is ServerLevel || blockEntity !is CreativeHarvestBlockEntity) return

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

	override fun onDestroyedByPlayer(state: BlockState, level: Level, pos: BlockPos, player: Player, willHarvest: Boolean, fluid: FluidState): Boolean {
		return player.isSecondaryUseActive
	}

}