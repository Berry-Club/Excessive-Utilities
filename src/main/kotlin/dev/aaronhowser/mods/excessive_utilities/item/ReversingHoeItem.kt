package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class ReversingHoeItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val pos = context.clickedPos
		val state = level.getBlockState(pos)

		val ageProperty = state.properties.find { it.name == "age" }
		if (ageProperty is IntegerProperty) {
			val currentAge = state.getValue(ageProperty)
			val newAge = currentAge - 1
			if (newAge in ageProperty.possibleValues) {
				val newState = state.setValue(ageProperty, newAge)
				level.setBlockAndUpdate(pos, newState)
				return InteractionResult.sidedSuccess(level.isClientSide)
			}
		}

		if (state.hasProperty(BlockStateProperties.STAGE)) {
			if (state.getValue(BlockStateProperties.STAGE) == 1) {
				val newState = state.setValue(BlockStateProperties.STAGE, 0)
				level.setBlockAndUpdate(pos, newState)
				return InteractionResult.sidedSuccess(level.isClientSide)
			}
		}

		val conversion = CONVERSIONS[state.block]
		if (conversion != null) {
			level.setBlockAndUpdate(pos, conversion.defaultBlockState())
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		val CONVERSIONS: MutableMap<Block, Block> =
			mutableMapOf(
				Blocks.GRASS_BLOCK to Blocks.DIRT,
				Blocks.COBBLESTONE to Blocks.STONE,
			)
	}

}