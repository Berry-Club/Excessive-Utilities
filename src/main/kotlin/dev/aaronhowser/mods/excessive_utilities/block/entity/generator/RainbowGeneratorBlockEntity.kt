package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class RainbowGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GeneratorBlockEntity(ModBlockEntityTypes.RAINBOW_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.RAINBOW

	override fun tryStartBurning(level: ServerLevel): Boolean {
		return true
	}
}