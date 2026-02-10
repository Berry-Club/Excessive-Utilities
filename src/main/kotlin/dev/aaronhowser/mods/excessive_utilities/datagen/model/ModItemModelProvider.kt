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

	override fun registerModels() {
		lassos()
		unstableTools()
		bewlrs()
		flatTransferNodes()

		basicItems()
	}

	fun flatTransferNodes() {
		val itemNode = ModItems.FLAT_TRANSFER_NODE_ITEMS.get()
		val fluidNode = ModItems.FLAT_TRANSFER_NODE_FLUIDS.get()

		getBuilder(getName(itemNode).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/flat_transfer_node/items"))

		getBuilder(getName(fluidNode).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/flat_transfer_node/fluids"))

		handledItems.add(itemNode)
		handledItems.add(fluidNode)
	}

	fun bewlrs() {
		val bewlrItems = listOf(
			ModItems.OPINIUM_CORE.get()
		)

		for (item in bewlrItems) {
			val name = getName(item)

			getBuilder(name.toString())
				.parent(ModelFile.UncheckedModelFile("builtin/entity"))

			handledItems.add(item)
		}
	}

	fun unstableTools() {
		val axe = ModItems.HEALING_AXE.get()
		val pickaxe = ModItems.DESTRUCTION_PICKAXE.get()
		val hoe = ModItems.REVERSING_HOE.get()
		val shovel = ModItems.EROSION_SHOVEL.get()
		val sword = ModItems.ETHERIC_SWORD.get()
		val shears = ModItems.PRECISION_SHEARS.get()

		getBuilder(getName(axe).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/axe_base"))
			.texture("layer1", modLoc("item/unstable_tools/axe_glow"))

		getBuilder(getName(pickaxe).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/pickaxe_base"))
			.texture("layer1", modLoc("item/unstable_tools/pickaxe_glow"))
			.texture("layer2", modLoc("item/unstable_tools/pickaxe_outline"))

		getBuilder(getName(hoe).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/hoe_base"))
			.texture("layer1", modLoc("item/unstable_tools/hoe_glow"))

		getBuilder(getName(shovel).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/shovel_base"))
			.texture("layer1", modLoc("item/unstable_tools/shovel_glow"))

		getBuilder(getName(sword).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/sword_base"))
			.texture("layer1", modLoc("item/unstable_tools/sword_glow"))

		getBuilder(getName(shears).toString())
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/shears_base"))
			.texture("layer1", modLoc("item/unstable_tools/shears_glow"))

		handledItems.add(axe)
		handledItems.add(pickaxe)
		handledItems.add(hoe)
		handledItems.add(shovel)
		handledItems.add(sword)
		handledItems.add(shears)
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
		val skipThese = setOf(
			ModItems.ANGEL_RING.get(),
			ModItems.SOUL_FRAGMENT.get(),
			ModItems.ENDER_SHARD.get(),
			ModItems.LUNAR_REACTIVE_DUST.get(),
			ModItems.OPINIUM_CORE.get(),
			ModItems.UNSTABLE_INGOT.get(),
			ModItems.SEMI_UNSTABLE_NUGGET.get(),
			ModItems.MOBIUS_INGOT.get(),
			ModItems.KLEIN_BOTTLE.get(),
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