package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.aaron.misc.AaronDsls.override
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.item.MagicalBoomerangItem
import dev.aaronhowser.mods.excessive_utilities.item.WateringCanItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
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
		unstableItems()
		bewlrs()
		flatTransferNodes()
		enderShard()
		wateringCan()
		magicalBoomerang()
		compoundBow()
		sunCrystal()
		biomeMarker()
		paintbrush()

		basicItems()
	}

	private fun paintbrush() {
		val item = ModItems.PAINTBRUSH.get()

		withExistingParent(getName(item), mcLoc("item/generated"))
			.texture("layer0", modLoc("item/paintbrush/base"))
			.texture("layer1", modLoc("item/paintbrush/brush"))

		handledItems.add(item)
	}

	private fun biomeMarker() {
		val item = ModItems.BIOME_MARKER.get()

		withExistingParent(getName(item), mcLoc("item/generated"))
			.texture("layer0", modLoc("item/biome_marker/biome_marker"))

		handledItems.add(item)
	}

	private fun sunCrystal() {
		val item = ModItems.SUN_CRYSTAL.get()

		withExistingParent(getName(item), mcLoc("item/generated"))
			.texture("layer0", modLoc("item/sun_crystal/outline"))
			.texture("layer1", modLoc("item/sun_crystal/glow"))

		handledItems.add(item)
	}

	private fun compoundBow() {
		val item = ModItems.COMPOUND_BOW.get()

		val baseModel =
			withExistingParent("compound_bow", mcLoc("item/generated"))
				.texture("layer0", modLoc("item/compound_bow/base"))

		val pull0Model =
			withExistingParent("compound_bow_pull_0", modLoc("item/compound_bow"))
				.texture("layer0", modLoc("item/compound_bow/pull_0"))

		val pull1Model =
			withExistingParent("compound_bow_pull_1", modLoc("item/compound_bow"))
				.texture("layer0", modLoc("item/compound_bow/pull_1"))

		val pull2Model =
			withExistingParent("compound_bow_pull_2", modLoc("item/compound_bow"))
				.texture("layer0", modLoc("item/compound_bow/pull_2"))

		val pull3Model =
			withExistingParent("compound_bow_pull_3", modLoc("item/compound_bow"))
				.texture("layer0", modLoc("item/compound_bow/pull_3"))

		val pull = mcLoc("pull")
		val pulling = mcLoc("pulling")

		baseModel
			.override {
				predicate(pulling, 1f)
				model(pull0Model)
			}

			.override {
				predicate(pull, 0.5f)
				model(pull0Model)
			}

			.override {
				predicate(pull, 0.7f)
				model(pull1Model)
			}

			.override {
				predicate(pull, 0.9f)
				model(pull2Model)
			}

			.override {
				predicate(pull, 1f)
				model(pull3Model)
			}


		handledItems.add(item)
	}

	private fun magicalBoomerang() {
		val item = ModItems.MAGICAL_BOOMERANG.get()

		val baseModel = getBuilder(getName(item))
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/magical_boomerang/not_thrown"))

		val thrownModel = getBuilder(getName(item) + "_thrown")
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/magical_boomerang/thrown"))

		baseModel
			.override {
				predicate(MagicalBoomerangItem.THROWN_PREDICATE, 1f)
				model(thrownModel)
			}

		handledItems.add(item)
	}

	private fun flatTransferNodes() {
		val itemNode = ModItems.FLAT_TRANSFER_NODE_ITEMS.get()
		val fluidNode = ModItems.FLAT_TRANSFER_NODE_FLUIDS.get()

		getBuilder(getName(itemNode))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/flat_transfer_node/items"))

		getBuilder(getName(fluidNode))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/flat_transfer_node/fluids"))

		handledItems.add(itemNode)
		handledItems.add(fluidNode)
	}

	private fun bewlrs() {
		val bewlrItems = listOf(
			ModItems.OPINIUM_CORE.get()
		)

		for (item in bewlrItems) {
			val name = getName(item)

			getBuilder(name)
				.parent(ModelFile.UncheckedModelFile("builtin/entity"))

			handledItems.add(item)
		}
	}

	//TODO: Make the glows emmissive, and make the ingot's glow red when about to explode
	private fun unstableItems() {
		val axe = ModItems.HEALING_AXE.get()
		val pickaxe = ModItems.DESTRUCTION_PICKAXE.get()
		val hoe = ModItems.REVERSING_HOE.get()
		val shovel = ModItems.EROSION_SHOVEL.get()
		val sword = ModItems.ETHERIC_SWORD.get()
		val shears = ModItems.PRECISION_SHEARS.get()
		val ingot = ModItems.UNSTABLE_INGOT.get()
		val nugget = ModItems.SEMI_UNSTABLE_NUGGET.get()

		getBuilder(getName(ingot))
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/unstable_ingot/base"))
			.texture("layer1", modLoc("item/unstable_ingot/glow"))

		getBuilder(getName(nugget))
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/unstable_nugget/base"))
			.texture("layer1", modLoc("item/unstable_nugget/glow"))

		getBuilder(getName(axe))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/axe_base"))
			.texture("layer1", modLoc("item/unstable_tools/axe_glow"))

		getBuilder(getName(pickaxe))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/pickaxe_base"))
			.texture("layer1", modLoc("item/unstable_tools/pickaxe_glow"))
			.texture("layer2", modLoc("item/unstable_tools/pickaxe_outline"))

		getBuilder(getName(hoe))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/hoe_base"))
			.texture("layer1", modLoc("item/unstable_tools/hoe_glow"))

		getBuilder(getName(shovel))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/shovel_base"))
			.texture("layer1", modLoc("item/unstable_tools/shovel_glow"))

		getBuilder(getName(sword))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/unstable_tools/sword_base"))
			.texture("layer1", modLoc("item/unstable_tools/sword_glow"))

		getBuilder(getName(shears))
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

	private fun wateringCan() {
		val item = ModItems.WATERING_CAN.get()

		val base = getBuilder(getName(item))
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/watering_can"))

		val broken = getBuilder(getName(item) + "_broken")
			.parent(ModelFile.UncheckedModelFile("item/handheld"))
			.texture("layer0", modLoc("item/watering_can_broken"))

		base
			.override {
				predicate(WateringCanItem.IS_BROKEN_PREDICATE, 1f)
				model(broken)
			}

		handledItems.add(item)
	}

	private fun lassos() {
		val goldenLasso = ModItems.GOLDEN_LASSO.get()
		val goldenName = getName(goldenLasso)

		val goldenBaseModel = getBuilder(goldenName)
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/lasso/golden"))

		val goldenFilledModel = getBuilder("${goldenName}_filled")
			.parent(goldenBaseModel)
			.texture("layer1", modLoc("item/lasso/golden_internal"))

		goldenBaseModel
			.override {
				predicate(EntityLassoItem.HAS_ENTITY, 1f)
				model(goldenFilledModel)
			}

		val cursedLasso = ModItems.CURSED_LASSO.get()
		val cursedName = getName(cursedLasso)

		val cursedBaseModel = getBuilder(cursedName)
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/lasso/cursed"))

		val cursedFilledModel = getBuilder("${cursedName}_filled")
			.parent(cursedBaseModel)
			.texture("layer1", modLoc("item/lasso/cursed_internal"))

		cursedBaseModel
			.override {
				predicate(EntityLassoItem.HAS_ENTITY, 1f)
				model(cursedFilledModel)
			}

		handledItems.add(goldenLasso)
		handledItems.add(cursedLasso)
	}

	private fun enderShard() {
		val item = ModItems.ENDER_SHARD.get()

		fun getModelForCount(count: Int): ItemModelBuilder {
			return getBuilder(getName(item) + "_" + count)
				.parent(ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0", modLoc("item/ender_shard/$count"))
		}

		val model = getBuilder(getName(item))
			.parent(ModelFile.UncheckedModelFile("item/generated"))
			.texture("layer0", modLoc("item/ender_shard/1"))

		for (i in 2..8) {
			model
				.override {
					predicate(ENDER_SHARD_COUNT, (i).toFloat())
					model(getModelForCount(i))
				}
		}

		handledItems.add(item)
	}

	private fun basicItems() {
		val skipThese = setOf(
			ModItems.UNSTABLE_INGOT.get(),
			ModItems.SEMI_UNSTABLE_NUGGET.get(),
			ModItems.KLEIN_BOTTLE.get(),
			ModItems.BIOME_MARKER.get(),
			ModItems.SUN_CRYSTAL.get(),
			ModItems.KIKOKU.get(),
			ModItems.LUX_SABER.get(),
			ModItems.MAGICAL_BOOMERANG.get()
		)

		val blockItemsToModel = listOf(
			ModItems.ENDER_LILY.get(),
			ModItems.RED_ORCHID.get()
		)

		for (deferred in ModItems.ITEM_REGISTRY.entries) {
			val item = deferred.get()
			if (item in handledItems || item in skipThese) continue

			if (item !is BlockItem || item in blockItemsToModel) {
				basicItem(item)
			}
		}
	}

	companion object {
		val ENDER_SHARD_COUNT = ExcessiveUtilities.modResource("ender_shard_count")
		private fun getName(item: Item): String = BuiltInRegistries.ITEM.getKey(item).toString()
	}

}