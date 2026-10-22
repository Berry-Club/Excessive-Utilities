package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.BlockDropsEvent

class ErosionShovelItem(properties: Properties) : Item(properties) {

	override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
		return if (state.isBlock(ModBlockTagsProvider.EROSION_SHOVEL_TARGET)) {
			100f
		} else {
			0f
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.durability(UnstableTier.durability())
			.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, UnstableTier.attackDamageBonus() + 1.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(
				Attributes.BLOCK_INTERACTION_RANGE,
				AttributeModifier(ExcessiveUtilities.modResource("erosion_shovel_range"), 4.0, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.MAINHAND
			).build())

		fun handleDropEvent(event: BlockDropsEvent) {
			if (!event.tool.isItem(ModItems.EROSION_SHOVEL)) return
			if (!event.state.isBlock(ModBlockTagsProvider.EROSION_SHOVEL_TARGET)) return

			event.isCanceled = true
		}
	}

}




