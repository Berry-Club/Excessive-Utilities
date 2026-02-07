package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

sealed class DataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	override val compressionLevel: Int,
	override val generatorType: GeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : CompressibleFeGeneratorBlockEntity(type, pos, blockState) {

	class Ender(pos: BlockPos, blockState: BlockState) : DataDrivenGeneratorBlockEntity(
		type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
		generatorType = GeneratorType.ENDER,
		compressionLevel = 1,
		pos = pos,
		blockState = blockState
	)


}