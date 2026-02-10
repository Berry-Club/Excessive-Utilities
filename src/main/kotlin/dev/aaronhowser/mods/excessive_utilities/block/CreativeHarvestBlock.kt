package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.CreativeHarvestBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
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

		if (item is BlockItem && !item.block.defaultBlockState().isBlock(ModBlockTagsProvider.CREATIVE_HARVEST_BLACKLIST)) {
			val be = level.getBlockEntity(pos) as? CreativeHarvestBlockEntity

			if (be != null) {
				be.mimicBlockState = item.block.defaultBlockState()
				return ItemInteractionResult.SUCCESS
			}
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
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

	override fun onDestroyedByPlayer(state: BlockState, level: Level, pos: BlockPos, player: Player, willHarvest: Boolean, fluid: FluidState): Boolean {
		val blockEntity = level.getBlockEntity(pos)

		if (!willHarvest
			|| player.isSecondaryUseActive
			|| level !is ServerLevel
			|| blockEntity !is CreativeHarvestBlockEntity
		) {
			return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)
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

		return false
	}

}