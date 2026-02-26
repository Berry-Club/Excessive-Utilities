package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datamap.ReversingHoeConversion
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class ReversingHoeItem(properties: Properties) : HoeItem(UnstableTier, properties) {

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

		val conversion = state.block.builtInRegistryHolder().getData(ReversingHoeConversion.DATA_MAP)
		if (conversion != null) {
			level.setBlockAndUpdate(pos, conversion.outputState)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(
				createAttributes(UnstableTier, -3f, 0f)
					.withModifierAdded(
						Attributes.BLOCK_INTERACTION_RANGE,
						AttributeModifier(
							ExcessiveUtilities.modResource("reversing_hoe_range"),
							4.0,
							AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.MAINHAND
					)
			)
	}

}