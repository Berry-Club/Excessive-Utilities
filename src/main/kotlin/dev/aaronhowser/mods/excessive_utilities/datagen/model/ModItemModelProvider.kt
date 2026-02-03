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

	private val skipThese = setOf(
		ModItems.ANGEL_RING.get(),
		ModItems.SOUL_FRAGMENT.get(),
		ModItems.ENDER_SHARD.get(),
		ModItems.LUNAR_REACTIVE_DUST.get(),
		ModItems.OPINIUM_CORE.get(),
		ModItems.UNSTABLE_INGOT.get(),
		ModItems.SEMI_UNSTABLE_NUGGET.get(),
		ModItems.MOBIUS_INGOT.get(),
		ModItems.KLEIN_FLASK.get(),
		ModItems.BIOME_MARKER.get(),
		ModItems.COMPOUND_BOW.get(),
		ModItems.CREATIVE_BUILDERS_WAND.get(),
		ModItems.CREATIVE_DESTRUCTION_WAND.get(),
		ModItems.FLAT_TRANSFER_NODE_ITEMS.get(),
		ModItems.FLAT_TRANSFER_NODE_FLUIDS.get(),
		ModItems.CURSED_LASSO.get(),
		ModItems.GOLDEN_LASSO.get(),
		ModItems.SUN_CRYSTAL.get(),
		ModItems.HEALING_AXE.get(),
		ModItems.REVERSING_HOE.get(),
		ModItems.DESTRUCTION_PICKAXE.get(),
		ModItems.PRECISION_SHEARS.get(),
		ModItems.EROSION_SHOVEL.get(),
		ModItems.ETHERIC_SWORD.get(),
		ModItems.PAINTBRUSH.get(),
		ModItems.KIKOKU.get(),
		ModItems.LUX_SABER.get(),
		ModItems.MAGICAL_BOOMERANG.get()
	)

	override fun registerModels() {
		basicItems()
	}

	private fun basicItems() {
		for (deferred in ModItems.ITEM_REGISTRY.entries) {
			val item = deferred.get()
			if (item in handledItems || item in skipThese) continue

			if (item !is BlockItem) {
				basicItem(item)
			}
		}
	}

}