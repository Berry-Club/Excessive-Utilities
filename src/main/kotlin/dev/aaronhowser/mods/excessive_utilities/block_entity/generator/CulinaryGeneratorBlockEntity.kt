package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.util.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.util.GeneratorType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class CulinaryGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.CULINARY_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.CULINARY

	override fun isValidInput(itemStack: ItemStack): Boolean {
		val foodValue = itemStack.getFoodProperties(null)
		return foodValue != null && (foodValue.nutrition > 0 || foodValue.saturation > 0f)
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0 || container == null) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val foodProperties = inputStack.getFoodProperties(null) ?: return false

		val foodValue = foodProperties.nutrition
		val saturationValue = foodProperties.saturation

		val fePerFoodValue = ServerConfig.CONFIG.culinaryFePerFoodValue.get()
		val ticksPerSaturationValue = ServerConfig.CONFIG.culinaryTicksPerSaturationValue.get()

		fePerTick = (foodValue * fePerFoodValue).toInt()
		burnTimeRemaining = (saturationValue * ticksPerSaturationValue).toInt()

		inputStack.shrink(1)
		setChanged()

		return true
	}

}