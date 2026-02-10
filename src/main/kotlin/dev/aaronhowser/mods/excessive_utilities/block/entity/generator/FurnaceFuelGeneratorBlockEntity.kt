package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.FurnaceFuelGeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class FurnaceFuelGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val generatorType: FurnaceFuelGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override fun isValidInput(itemStack: ItemStack): Boolean {
		return super.isValidInput(itemStack)
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		TODO("Not yet implemented")
	}

}