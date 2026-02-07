package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.entity.DataDrivenGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.world.level.block.entity.BlockEntityType

class EnderGeneratorBlock(tier: Int) : DataDrivenGeneratorBlock(tier, GeneratorType.ENDER) {

	override fun getBlockEntityType(): BlockEntityType<out DataDrivenGeneratorBlockEntity> {
		return when (tier) {
			1 -> ModBlockEntityTypes.ENDER_GENERATOR.get()
			2 -> ModBlockEntityTypes.ENDER_GENERATOR_MK2.get()
			3 -> ModBlockEntityTypes.ENDER_GENERATOR_MK3.get()
			else -> error("Invalid tier: $tier")
		}
	}

}