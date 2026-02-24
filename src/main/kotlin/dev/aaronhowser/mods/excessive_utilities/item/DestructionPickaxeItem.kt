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
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.BlockDropsEvent

class DestructionPickaxeItem(properties: Properties) : PickaxeItem(UnstableTier, properties) {

	override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
		return if (state.isBlock(ModBlockTagsProvider.DESTRUCTION_PICKAXE_TARGET)) {
			100f
		} else {
			0f
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(
				createAttributes(UnstableTier, 1f, -2.8f)
					.withModifierAdded(
						Attributes.BLOCK_INTERACTION_RANGE,
						AttributeModifier(
							ExcessiveUtilities.modResource("destruction_pickaxe_range"),
							4.0,
							AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.MAINHAND
					)
			)

		fun handleDropEvent(event: BlockDropsEvent) {
			if (!event.tool.isItem(ModItems.DESTRUCTION_PICKAXE)) return
			if (!event.state.isBlock(ModBlockTagsProvider.DESTRUCTION_PICKAXE_TARGET)) return

			event.isCanceled = true
		}
	}

}