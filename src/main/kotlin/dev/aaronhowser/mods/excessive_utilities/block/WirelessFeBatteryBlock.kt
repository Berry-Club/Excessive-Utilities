package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.WirelessFeBatteryBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class WirelessFeBatteryBlock : Block(Properties.ofFullCopy(Blocks.REDSTONE_BLOCK)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return WirelessFeBatteryBlockEntity(pos, state)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is WirelessFeBatteryBlockEntity && placer != null) {
			blockEntity.ownerUuid = placer.uuid
		}
	}

}