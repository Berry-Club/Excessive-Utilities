package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datamap.ReversingHoeConversion
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.context.UseOnContext
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
				return InteractionResult.SUCCESS
			}
		}

		if (state.hasProperty(BlockStateProperties.STAGE)) {
			if (state.getValue(BlockStateProperties.STAGE) == 1) {
				val newState = state.setValue(BlockStateProperties.STAGE, 0)
				level.setBlockAndUpdate(pos, newState)
				return InteractionResult.SUCCESS
			}
		}

		val convertedState = ReversingHoeConversion.getResult(state)
		if (convertedState != null) {
			level.setBlockAndUpdate(pos, convertedState)
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.durability(UnstableTier.durability())
			.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, UnstableTier.attackDamageBonus() - 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 0.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(
				Attributes.BLOCK_INTERACTION_RANGE,
				AttributeModifier(ExcessiveUtilities.modResource("reversing_hoe_range"), 4.0, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND
			).build())
	}

}




