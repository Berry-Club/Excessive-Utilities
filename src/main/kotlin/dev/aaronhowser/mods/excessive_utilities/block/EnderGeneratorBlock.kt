package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.entity.DataDrivenGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class EnderGeneratorBlock(
	val compressionLevel: Int
) : DataDrivenGeneratorBlock() {

	override fun getBlockEntityType(): BlockEntityType<out DataDrivenGeneratorBlockEntity> {
		return when (compressionLevel) {
			1 -> ModBlockEntityTypes.ENDER_GENERATOR.get()
			2 -> ModBlockEntityTypes.ENDER_GENERATOR_MK2.get()
			3 -> ModBlockEntityTypes.ENDER_GENERATOR_MK3.get()
			else -> error("Invalid compression level: $compressionLevel")
		}
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return DataDrivenGeneratorBlockEntity(
			getBlockEntityType(),
			compressionLevel,
			GeneratorType.ENDER,
			pos,
			state
		)
	}

}