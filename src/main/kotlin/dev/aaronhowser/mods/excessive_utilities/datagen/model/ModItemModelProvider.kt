package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.client.item.ItemColorSource
import dev.aaronhowser.mods.excessive_utilities.client.item.ItemVariantProperty
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.OpiniumCoreBEWLR
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.TesseractBEWLR
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.item.MagicalBoomerangItem
import dev.aaronhowser.mods.excessive_utilities.item.WateringCanItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.renderer.item.ItemModel
import net.minecraft.client.renderer.item.SelectItemModel
import net.minecraft.client.renderer.item.properties.select.DisplayContext
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder

class ModItemModelProvider(
	private val itemModels: ItemModelGenerators
) {

	private val handledItems: MutableSet<Item> = mutableSetOf()

	fun registerModels() {
		handledItems.clear()
		registerExistingModel(ModItems.KIKOKU.get(), modLoc("item/kikoku"))
		lassos()
		unstableItems()
		specialModels()
		flatTransferNodes()
		enderShard()
		wateringCan()
		magicalBoomerang()
		compoundBow()
		sunCrystal()
		biomeMarker()
		paintbrush()
		creativeBuildersWand()
		creativeDestructionWand()
		heatingCoil()
		angelRings()
		handheldItems()
		basicItems()
	}

	private fun angelRings() {
		val item = ModItems.ANGEL_RING.get()
		val baseLocation = createModel(item, mcLoc("item/generated"), emptyMap())
		val entries = mutableListOf<net.minecraft.client.renderer.item.RangeSelectItemModel.Entry>()

		for (type in AngelRingItem.Type.entries) {
			val location = createModel(
				modelLocation(item, "_${type.id}"),
				mcLoc("item/generated"),
				mapOf(TextureSlot.LAYER0 to modLoc("item/angel_ring/${type.id}"))
			)
			entries.add(ItemModelUtils.override(ItemModelUtils.plainModel(location), type.ordinal.toFloat()))
		}

		itemModels.itemModelOutput.accept(
			item,
			ItemModelUtils.rangeSelect(
				ItemVariantProperty(AngelRingItem.RING_TYPE),
				ItemModelUtils.plainModel(baseLocation),
				entries
			)
		)
		handledItems.add(item)
	}

	private fun heatingCoil() {
		val item = ModItems.HEATING_COIL.get()
		val location = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(
				TextureSlot.LAYER0 to modLoc("item/heating_coil/base"),
				TextureSlot.LAYER1 to modLoc("item/heating_coil/glow")
			)
		)
		registerTintedModel(item, location)
	}

	private fun handheldItems() {
		val items = listOf(
			ModItems.FIRE_AXE.get(),
			ModItems.WOODEN_SICKLE.get(),
			ModItems.STONE_SICKLE.get(),
			ModItems.IRON_SICKLE.get(),
			ModItems.GOLDEN_SICKLE.get(),
			ModItems.DIAMOND_SICKLE.get(),
			ModItems.NETHERITE_SICKLE.get()
		)

		for (item in items) {
			itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
			handledItems.add(item)
		}
	}

	private fun creativeBuildersWand() {
		val item = ModItems.CREATIVE_BUILDERS_WAND.get()
		registerLayeredItem(
			item,
			modLoc("item/creative_builders_wand/base"),
			modLoc("item/creative_builders_wand/outline")
		)
	}

	private fun creativeDestructionWand() {
		val item = ModItems.CREATIVE_DESTRUCTION_WAND.get()
		registerLayeredItem(
			item,
			modLoc("item/creative_destruction_wand/base"),
			modLoc("item/creative_destruction_wand/outline")
		)
	}

	private fun paintbrush() {
		val item = ModItems.PAINTBRUSH.get()
		val location = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(
				TextureSlot.LAYER0 to modLoc("item/paintbrush/base"),
				TextureSlot.LAYER1 to modLoc("item/paintbrush/brush")
			)
		)
		registerTintedModel(item, location)
	}

	private fun biomeMarker() {
		val item = ModItems.BIOME_MARKER.get()
		val location = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/biome_marker/biome_marker"))
		)
		registerModel(item, location)
	}

	private fun sunCrystal() {
		val item = ModItems.SUN_CRYSTAL.get()
		val location = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(
				TextureSlot.LAYER0 to modLoc("item/sun_crystal/outline"),
				TextureSlot.LAYER1 to modLoc("item/sun_crystal/glow")
			)
		)
		registerTintedModel(item, location)
	}

	private fun compoundBow() {
		val item = ModItems.COMPOUND_BOW.get()
		val template = ExtendedModelTemplateBuilder.builder()
			.parent(mcLoc("item/generated"))
			.requiredTextureSlot(TextureSlot.LAYER0)
			.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) { transform ->
				transform.rotation(-80f, 260f, -40f)
					.translation(-1f, -2f, 2.5f)
					.scale(0.9f)
			}
			.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) { transform ->
				transform.rotation(-80f, -280f, 40f)
					.translation(-1f, -2f, 2.5f)
					.scale(0.9f)
			}
			.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) { transform ->
				transform.rotation(0f, -90f, 25f)
					.translation(1.13f, 3.2f, 1.13f)
					.scale(0.68f)
			}
			.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) { transform ->
				transform.rotation(0f, 90f, -25f)
					.translation(1.13f, 3.2f, 1.13f)
					.scale(0.68f)
			}
			.build()

		val baseLocation = template.create(
			modelLocation(item),
			TextureMapping.layer0(Material(modLoc("item/compound_bow/base"))),
			itemModels.modelOutput
		)
		val pullModels = mutableListOf<ItemModel.Unbaked>()
		for (index in 0..3) {
			val location = createModel(
				modelLocation(item, "_pull_$index"),
				baseLocation,
				mapOf(TextureSlot.LAYER0 to modLoc("item/compound_bow/pull_$index"))
			)
			pullModels.add(ItemModelUtils.plainModel(location))
		}

		var model = ItemModelUtils.plainModel(baseLocation)
		model = rangeOverride(model, mcLoc("pulling"), 1f, pullModels[0])
		model = rangeOverride(model, mcLoc("pull"), 0.5f, pullModels[0])
		model = rangeOverride(model, mcLoc("pull"), 0.7f, pullModels[1])
		model = rangeOverride(model, mcLoc("pull"), 0.9f, pullModels[2])
		model = rangeOverride(model, mcLoc("pull"), 1f, pullModels[3])
		itemModels.itemModelOutput.accept(item, model)
		handledItems.add(item)
	}

	private fun magicalBoomerang() {
		val item = ModItems.MAGICAL_BOOMERANG.get()
		val baseLocation = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/magical_boomerang/not_thrown"))
		)
		val thrownLocation = createModel(
			modelLocation(item, "_thrown"),
			mcLoc("item/generated"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/magical_boomerang/thrown"))
		)
		itemModels.itemModelOutput.accept(
			item,
			rangeOverride(
				ItemModelUtils.plainModel(baseLocation),
				MagicalBoomerangItem.THROWN_PREDICATE,
				1f,
				ItemModelUtils.plainModel(thrownLocation)
			)
		)
		handledItems.add(item)
	}

	private fun flatTransferNodes() {
		registerSingleLayerHandheld(ModItems.FLAT_TRANSFER_NODE_ITEMS.get(), modLoc("item/flat_transfer_node/items"))
		registerSingleLayerHandheld(ModItems.FLAT_TRANSFER_NODE_FLUIDS.get(), modLoc("item/flat_transfer_node/fluids"))
	}

	private fun specialModels() {
		val opiniumCore = ModItems.OPINIUM_CORE.get()
		val opiniumBase = createModel(opiniumCore, mcLoc("block/block"), emptyMap())
		val cases = mutableListOf<SelectItemModel.SwitchCase<ItemDisplayContext>>()
		for (displayContext in ItemDisplayContext.entries) {
			if (displayContext == ItemDisplayContext.NONE) continue
			cases.add(
				ItemModelUtils.`when`(
					displayContext,
					ItemModelUtils.specialModel(opiniumBase, OpiniumCoreBEWLR.Unbaked(displayContext))
				)
			)
		}
		itemModels.itemModelOutput.accept(
			opiniumCore,
			ItemModelUtils.select(
				DisplayContext(),
				ItemModelUtils.specialModel(opiniumBase, OpiniumCoreBEWLR.Unbaked(ItemDisplayContext.NONE)),
				cases
			)
		)
		handledItems.add(opiniumCore)

		val tesseract = ModItems.TESSERACT.get()
		val tesseractBase = createModel(tesseract, mcLoc("block/block"), emptyMap())
		itemModels.itemModelOutput.accept(
			tesseract,
			ItemModelUtils.select(
				DisplayContext(),
				ItemModelUtils.specialModel(tesseractBase, TesseractBEWLR.Unbaked(false)),
				ItemModelUtils.`when`(
					ItemDisplayContext.GUI,
					ItemModelUtils.specialModel(tesseractBase, TesseractBEWLR.Unbaked(true))
				)
			)
		)
		handledItems.add(tesseract)
	}

	private fun unstableItems() {
		val unstableIngot = ModItems.UNSTABLE_INGOT.get()
		val unstableIngotLocation = createModel(
			unstableIngot,
			mcLoc("item/generated"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/unstable_ingot"))
		)
		registerTintedModel(unstableIngot, unstableIngotLocation)

		registerLayeredHandheld(ModItems.HEALING_AXE.get(), "axe")
		registerLayeredHandheld(ModItems.REVERSING_HOE.get(), "hoe")
		registerLayeredHandheld(ModItems.EROSION_SHOVEL.get(), "shovel")
		registerLayeredHandheld(ModItems.ETHERIC_SWORD.get(), "sword")
		registerLayeredHandheld(ModItems.PRECISION_SHEARS.get(), "shears")

		val pickaxe = ModItems.DESTRUCTION_PICKAXE.get()
		val location = createModel(
			pickaxe,
			mcLoc("item/handheld"),
			mapOf(
				TextureSlot.LAYER0 to modLoc("item/unstable_tools/pickaxe_base"),
				TextureSlot.LAYER1 to modLoc("item/unstable_tools/pickaxe_glow"),
				TextureSlot.LAYER2 to modLoc("item/unstable_tools/pickaxe_outline")
			)
		)
		registerModel(pickaxe, location)
	}

	private fun wateringCan() {
		val item = ModItems.WATERING_CAN.get()
		val baseLocation = createModel(
			item,
			mcLoc("item/handheld"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/watering_can"))
		)
		val brokenLocation = createModel(
			modelLocation(item, "_broken"),
			mcLoc("item/handheld"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/watering_can_broken"))
		)
		itemModels.itemModelOutput.accept(
			item,
			rangeOverride(
				ItemModelUtils.plainModel(baseLocation),
				WateringCanItem.IS_BROKEN_PREDICATE,
				1f,
				ItemModelUtils.plainModel(brokenLocation)
			)
		)
		handledItems.add(item)
	}

	private fun lassos() {
		registerLasso(ModItems.GOLDEN_LASSO.get(), "golden")
		registerLasso(ModItems.CURSED_LASSO.get(), "cursed")
	}

	private fun registerLasso(item: Item, textureName: String) {
		val baseLocation = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/lasso/$textureName"))
		)
		val filledLocation = createModel(
			modelLocation(item, "_filled"),
			baseLocation,
			mapOf(TextureSlot.LAYER1 to modLoc("item/lasso/${textureName}_internal"))
		)
		itemModels.itemModelOutput.accept(
			item,
			rangeOverride(
				ItemModelUtils.plainModel(baseLocation),
				EntityLassoItem.HAS_ENTITY,
				1f,
				ItemModelUtils.plainModel(filledLocation)
			)
		)
		handledItems.add(item)
	}

	private fun enderShard() {
		val item = ModItems.ENDER_SHARD.get()
		val baseLocation = createModel(
			item,
			mcLoc("item/generated"),
			mapOf(TextureSlot.LAYER0 to modLoc("item/ender_shard/1"))
		)
		val entries = mutableListOf<net.minecraft.client.renderer.item.RangeSelectItemModel.Entry>()
		for (count in 2..8) {
			val location = createModel(
				modelLocation(item, "_$count"),
				mcLoc("item/generated"),
				mapOf(TextureSlot.LAYER0 to modLoc("item/ender_shard/$count"))
			)
			entries.add(ItemModelUtils.override(ItemModelUtils.plainModel(location), count.toFloat()))
		}
		itemModels.itemModelOutput.accept(
			item,
			ItemModelUtils.rangeSelect(
				ItemVariantProperty(ENDER_SHARD_COUNT),
				ItemModelUtils.plainModel(baseLocation),
				entries
			)
		)
		handledItems.add(item)
	}

	private fun basicItems() {
		val blockItemsToModel = setOf(ModItems.ENDER_LILY.get(), ModItems.RED_ORCHID.get())
		for (deferredItem in ModItems.ITEM_REGISTRY.entries) {
			val item = deferredItem.get()
			if (item in handledItems) continue
			if (item is BlockItem && item !in blockItemsToModel) continue

			itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM)
			handledItems.add(item)
		}
	}

	private fun registerLayeredHandheld(item: Item, textureName: String) {
		val location = createModel(
			item,
			mcLoc("item/handheld"),
			mapOf(
				TextureSlot.LAYER0 to modLoc("item/unstable_tools/${textureName}_base"),
				TextureSlot.LAYER1 to modLoc("item/unstable_tools/${textureName}_glow")
			)
		)
		registerModel(item, location)
	}

	private fun registerLayeredItem(item: Item, layerZero: Identifier, layerOne: Identifier) {
		val location = ModelTemplates.TWO_LAYERED_ITEM.create(
			item,
			TextureMapping.layered(Material(layerZero), Material(layerOne)),
			itemModels.modelOutput
		)
		registerModel(item, location)
	}

	private fun registerSingleLayerHandheld(item: Item, texture: Identifier) {
		val location = createModel(
			item,
			mcLoc("item/handheld"),
			mapOf(TextureSlot.LAYER0 to texture)
		)
		registerModel(item, location)
	}

	private fun registerModel(item: Item, location: Identifier) {
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(location))
		handledItems.add(item)
	}

	private fun registerTintedModel(item: Item, location: Identifier) {
		itemModels.itemModelOutput.accept(
			item,
			ItemModelUtils.tintedModel(
				location,
				ItemColorSource(0),
				ItemColorSource(1),
				ItemColorSource(2)
			)
		)
		handledItems.add(item)
	}

	private fun registerExistingModel(item: Item, location: Identifier) {
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(location))
		handledItems.add(item)
	}

	private fun rangeOverride(
		fallback: ItemModel.Unbaked,
		predicate: Identifier,
		threshold: Float,
		model: ItemModel.Unbaked
	): ItemModel.Unbaked {
		return ItemModelUtils.rangeSelect(
			ItemVariantProperty(predicate),
			fallback,
			ItemModelUtils.override(model, threshold)
		)
	}

	private fun createModel(
		item: Item,
		parent: Identifier,
		textures: Map<TextureSlot, Identifier>
	): Identifier {
		return createModel(modelLocation(item), parent, textures)
	}

	private fun createModel(
		location: Identifier,
		parent: Identifier,
		textures: Map<TextureSlot, Identifier>
	): Identifier {
		val builder = ExtendedModelTemplateBuilder.builder().parent(parent)
		val textureMapping = TextureMapping()
		for ((slot, texture) in textures) {
			builder.requiredTextureSlot(slot)
			textureMapping.put(slot, Material(texture))
		}
		return builder.build().create(location, textureMapping, itemModels.modelOutput)
	}

	private fun modelLocation(item: Item, suffix: String = ""): Identifier {
		return BuiltInRegistries.ITEM.getKey(item).withPrefix("item/").withSuffix(suffix)
	}

	private fun modLoc(path: String): Identifier {
		return Identifier.fromNamespaceAndPath(ExcessiveUtilities.MOD_ID, path)
	}

	private fun mcLoc(path: String): Identifier = Identifier.withDefaultNamespace(path)

	companion object {
		val ENDER_SHARD_COUNT = ExcessiveUtilities.modResource("ender_shard_count")
	}
}