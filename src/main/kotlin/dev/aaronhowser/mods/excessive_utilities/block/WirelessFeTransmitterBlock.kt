package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.WirelessFeTransmitterBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class WirelessFeTransmitterBlock : Block(Properties.ofFullCopy(Blocks.REDSTONE_BLOCK)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return WirelessFeTransmitterBlockEntity(pos, state)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is WirelessFeTransmitterBlockEntity && placer != null) {
			blockEntity.ownerUuid = placer.uuid
		}
	}

}