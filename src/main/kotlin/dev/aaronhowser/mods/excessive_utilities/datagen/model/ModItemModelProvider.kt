package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(
	output: PackOutput,
	existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	private val handledItems: MutableSet<Item> = mutableSetOf()

	override fun registerModels() {
		basicItems()
	}

	private fun basicItems() {
		for (deferred in ModItems.ITEM_REGISTRY.entries) {
			val item = deferred.get()
			if (item in handledItems) continue

			if (item !is BlockItem) {
				basicItem(item)
			}
		}
	}

}