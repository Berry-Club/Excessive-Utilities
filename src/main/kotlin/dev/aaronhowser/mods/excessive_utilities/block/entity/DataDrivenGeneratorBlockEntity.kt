package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.base.entity.CompressibleFeGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class DataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	override val compressionLevel: Int,
	val generatorType: GeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : CompressibleFeGeneratorBlockEntity(type, pos, blockState) {

	override fun tryStartBurning(level: ServerLevel) {
		if (burnTimeRemaining > 0) return

		val fuelMap = generatorType.fuelDataMap

		val inputStack = container.getItem(INPUT_SLOT)
		if (inputStack.isEmpty) return

		val itemFuel = inputStack.item.builtInRegistryHolder().getData(fuelMap) ?: return

		fePerTick = itemFuel.fePerTick
		burnTimeRemaining = itemFuel.burnTime

		inputStack.shrink(1)
		setChanged()
	}

	companion object {
		fun ender(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
			compressionLevel = 1,
			generatorType = GeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun enderEight(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
			compressionLevel = 2,
			generatorType = GeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun enderSixtyFour(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
			compressionLevel = 3,
			generatorType = GeneratorType.ENDER,
			pos = pos,
			blockState = state
		)
	}

}