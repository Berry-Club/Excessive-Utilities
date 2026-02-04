package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
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
		lassos()

		basicItems()
	}

	fun lassos() {
		val goldenLasso = ModItems.GOLDEN_LASSO.get()
		val goldenName = getName(goldenLasso).toString()

		val goldenBaseModel = getBuilder(goldenName)
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/lasso/golden"))

		val goldenFilledModel = getBuilder("${goldenName}_filled")
			.parent(goldenBaseModel)
			.texture("layer1", modLoc("item/lasso/golden_internal"))

		goldenBaseModel
			.override()
			.predicate(EntityLassoItem.HAS_ENTITY, 1f)
			.model(goldenFilledModel)
			.end()

		val cursedLasso = ModItems.CURSED_LASSO.get()
		val cursedName = getName(cursedLasso).toString()

		val cursedBaseModel = getBuilder(cursedName)
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/lasso/cursed"))

		val cursedFilledModel = getBuilder("${cursedName}_filled")
			.parent(cursedBaseModel)
			.texture("layer1", modLoc("item/lasso/cursed_internal"))

		cursedBaseModel
			.override()
			.predicate(EntityLassoItem.HAS_ENTITY, 1f)
			.model(cursedFilledModel)
			.end()

		handledItems.add(goldenLasso)
		handledItems.add(cursedLasso)
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

	private fun getName(item: Item): ResourceLocation {
		return BuiltInRegistries.ITEM.getKey(item)
	}

}