package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
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

		val (fePerTick, duration) = getFeValues(inputStack)
		if (fePerTick <= 0 || duration <= 0) return false

		this.fePerTick = fePerTick
		this.burnTimeRemaining = duration

		inputStack.shrink(1)
		setChanged()

		return true
	}

	companion object {
		fun getFeValues(itemStack: ItemStack): Pair<Int, Int> {
			val foodProperties = itemStack.getFoodProperties(null) ?: return Pair(0, 0)

			val nutrition = foodProperties.nutrition
			val saturation = foodProperties.saturation

			var feTotal = nutrition * saturation * 8000
			var fePerTick = nutrition * 8

			if (feTotal > 64_000) {
				val tier = Mth.floor((feTotal - 1) / 64_000)
				feTotal /= (tier + 1)
			}

			if (fePerTick > 64) {
				val tier = (fePerTick - 1) / 64
				fePerTick /= (tier + 1)
			}

			val duration = Mth.ceil(feTotal.toDouble() / fePerTick)

			return fePerTick to duration
		}
	}

}