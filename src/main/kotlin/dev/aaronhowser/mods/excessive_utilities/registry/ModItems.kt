package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tiers
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems : AaronItemRegistry() {

	val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(ExcessiveUtilities.MOD_ID)
	override fun getItemRegistry(): DeferredRegister.Items = ITEM_REGISTRY

	// Ingredients

	val BEDROCKIUM_INGOT: DeferredItem<BedrockiumIngotItem> =
		register("bedrockium_ingot", ::BedrockiumIngotItem)
	val SOUL_FRAGMENT =
		basic("soul_fragment")
	val ENDER_SHARD: DeferredItem<Item> =
		basic("ender_shard")
	val DEMON_INGOT: DeferredItem<Item> = // TODO: Throw Gold in Lava in the Nether
		basic("demon_ingot")
	val DROP_OF_EVIL =
		basic("drop_of_evil")
	val ENCHANTED_INGOT: DeferredItem<Item> =
		basic("enchanted_ingot")
	val EVIL_INFUSED_IRON_INGOT: DeferredItem<Item> =
		basic("evil_infused_iron_ingot")
	val EYE_OF_REDSTONE: DeferredItem<Item> =
		basic("eye_of_redstone")
	val LUNAR_REACTIVE_DUST: DeferredItem<Item> =
		basic("lunar_reactive_dust")
	val MOON_STONE = // TODO: Drop from hidden ores that look like stone during the day
		basic("moon_stone")
	val RED_COAL =
		basic("red_coal")
	val REDSTONE_GEAR: DeferredItem<Item> =
		basic("redstone_gear")
	val RESONATING_REDSTONE_CRYSTAL: DeferredItem<Item> = // TODO: Drop from redstone ore
		basic("resonating_redstone_crystal")
	val UPGRADE_BASE: DeferredItem<Item> =
		basic("upgrade_base")
	val OPINIUM_CORE: DeferredItem<OpiniumCoreItem> =
		register("opinium_core", ::OpiniumCoreItem, OpiniumCoreItem.DEFAULT_PROPERTIES)
	val UNSTABLE_INGOT =
		basic("unstable_ingot")
	val SEMI_UNSTABLE_NUGGET =
		basic("semi_unstable_nugget")
	val MOBIUS_INGOT =
		basic("mobius_ingot")
	val KLEIN_BOTTLE: DeferredItem<Item> =
		basic("klein_bottle")

	// Plants

	val ENDER_LILY =
		basic("ender_lily")

	// Tools

	val DESTRUCTION_PICKAXE =
		basic("destruction_pickaxe")
	val EROSION_SHOVEL =
		basic("erosion_shovel")
	val ETHERIC_SWORD =
		basic("etheric_sword")
	val HEALING_AXE =
		basic("healing_axe")
	val REVERSING_HOE: DeferredItem<ReversingHoeItem> =
		register("reversing_hoe", ::ReversingHoeItem, ReversingHoeItem.DEFAULT_PROPERTIES)
	val PRECISION_SHEARS =
		basic("precision_shears")
	val PAINTBRUSH =
		basic("paintbrush")
	val GLASS_CUTTER =
		basic("glass_cutter")
	val TROWEL: DeferredItem<TrowelItem> =
		register("trowel", ::TrowelItem, TrowelItem.DEFAULT_PROPERTIES)
	val WRENCH =
		basic("wrench")
	val WATERING_CAN: DeferredItem<WateringCanItem> =
		register("watering_can", { WateringCanItem(isReinforced = false, it) }, WateringCanItem.DEFAULT_PROPERTIES)
	val REINFORCED_WATERING_CAN: DeferredItem<WateringCanItem> =
		register("reinforced_watering_can", { WateringCanItem(isReinforced = true, it) }, WateringCanItem.DEFAULT_REINFORCED_PROPERTIES)
	val GOLDEN_LASSO: DeferredItem<EntityLassoItem> =
		register("golden_lasso", { EntityLassoItem(canHoldHostileMobs = false, it) }, EntityLassoItem.DEFAULT_PROPERTIES)
	val CURSED_LASSO: DeferredItem<EntityLassoItem> =
		register("cursed_lasso", { EntityLassoItem(canHoldHostileMobs = true, it) }, EntityLassoItem.DEFAULT_PROPERTIES)
	val WOODEN_SICKLE: DeferredItem<SickleItem> =
		register("wooden_sickle", ::SickleItem) { SickleItem.getDefaultProperties(Tiers.WOOD) }
	val STONE_SICKLE: DeferredItem<SickleItem> =
		register("stone_sickle", ::SickleItem) { SickleItem.getDefaultProperties(Tiers.STONE) }
	val IRON_SICKLE: DeferredItem<SickleItem> =
		register("iron_sickle", ::SickleItem) { SickleItem.getDefaultProperties(Tiers.IRON) }
	val GOLDEN_SICKLE: DeferredItem<SickleItem> =
		register("golden_sickle", ::SickleItem) { SickleItem.getDefaultProperties(Tiers.GOLD) }
	val DIAMOND_SICKLE: DeferredItem<SickleItem> =
		register("diamond_sickle", ::SickleItem) { SickleItem.getDefaultProperties(Tiers.DIAMOND) }
	val NETHERITE_SICKLE: DeferredItem<SickleItem> =
		register("netherite_sickle", ::SickleItem) { SickleItem.getDefaultProperties(Tiers.NETHERITE) }
	val BUILDERS_WAND =
		basic("builders_wand")
	val CREATIVE_BUILDERS_WAND =
		basic("creative_builders_wand")
	val DESTRUCTION_WAND =
		basic("destruction_wand")
	val CREATIVE_DESTRUCTION_WAND =
		basic("creative_destruction_wand")
	val INDEXER_REMOTE =
		basic("indexer_remote")

	// Weapons

	val KIKOKU =
		basic("kikoku")
	val FIRE_AXE =
		basic("fire_axe")
	val LUX_SABER =
		basic("lux_saber")
	val COMPOUND_BOW =
		basic("compound_bow")
	val MAGICAL_BOOMERANG =
		basic("magical_boomerang")

	// Misc
	@JvmField
	val HEATING_COIL =
		register("heating_coil", ::HeatingCoilItem, HeatingCoilItem.DEFAULT_PROPERTIES)

	@JvmField
	val WIRELESS_RF_HEATING_COIL =
		basic("wireless_rf_heating_coil")
	val POWER_MANAGER =
		basic("power_manager")
	val SUN_CRYSTAL =
		basic("sun_crystal")
	val BIOME_MARKER =
		basic("biome_marker")
	val BAG_OF_HOLDING: DeferredItem<BagOfHoldingItem> =
		register("bag_of_holding", ::BagOfHoldingItem, BagOfHoldingItem.DEFAULT_PROPERTIES)
	val MAGICAL_APPLE: DeferredItem<MagicalAppleItem> =
		register("magical_apple", ::MagicalAppleItem, MagicalAppleItem.PROPERTIES)
	val PORTABLE_SCANNER: DeferredItem<PortableScanner> =
		register("portable_scanner", ::PortableScanner, PortableScanner.DEFAULT_PROPERTIES)
	val SONAR_GOGGLES =
		basic("sonar_goggles")
	val FLAT_TRANSFER_NODE_ITEMS: DeferredItem<FlatTransferNodeItem> =
		register(
			"flat_transfer_node_items",
			{ FlatTransferNodeItem(it, isItemNode = true) },
			FlatTransferNodeItem.DEFAULT_PROPERTIES
		)
	val FLAT_TRANSFER_NODE_FLUIDS: DeferredItem<FlatTransferNodeItem> =
		register(
			"flat_transfer_node_fluids",
			{ FlatTransferNodeItem(it, isItemNode = false) },
			FlatTransferNodeItem.DEFAULT_PROPERTIES
		)
	val ANGEL_BLOCK: DeferredItem<AngelBlockItem> =
		register("angel_block", ::AngelBlockItem)
	val MAGICAL_SNOW_GLOBE: DeferredItem<MagicalSnowGlobeBlockItem> =
		register("magical_snow_globe", ::MagicalSnowGlobeBlockItem, MagicalSnowGlobeBlockItem.DEFAULT_PROPERTIES)

	// Transfer Node Upgrades

	val SPEED_UPGRADE: DeferredItem<SpeedUpgradeItem> =
		register("speed_upgrade", ::SpeedUpgradeItem, SpeedUpgradeItem.BASIC_PROPERTIES)
	val SPEED_UPGRADE_MAGICAL: DeferredItem<SpeedUpgradeItem> =
		register("speed_upgrade_magical", ::SpeedUpgradeItem, SpeedUpgradeItem.MAGICAL_PROPERTIES)
	val SPEED_UPGRADE_ULTIMATE: DeferredItem<SpeedUpgradeItem> =
		register("speed_upgrade_ultimate", ::SpeedUpgradeItem, SpeedUpgradeItem.ULTIMATE_PROPERTIES)
	val STACK_UPGRADE =
		basic("stack_upgrade")
	val WORLD_INTERACTION_UPGRADE =
		basic("world_interaction_upgrade")
	val ITEM_FILTER =
		basic("item_filter")
	val ADVANCED_ITEM_FILTER =
		basic("advanced_item_filter")
	val FLUID_FILTER =
		basic("fluid_filter")
	val BREADTH_FIRST_SEARCH_UPGRADE =
		basic("breadth_first_search_upgrade")
	val DEPTH_FIRST_SEARCH_UPGRADE =
		basic("depth_first_search_upgrade")
	val PSEUDO_ROUND_ROBIN_UPGRADE =
		basic("pseudo_round_robin_upgrade")
	val ENDER_RECEIVER =
		basic("ender_receiver")
	val ENDER_TRANSMITTER =
		basic("ender_transmitter")
	val CREATIVE_UPGRADE =
		basic("creative_upgrade")

	// Rings

	val CHICKEN_WING_RING: DeferredItem<ChickenWingRingItem> =
		register("chicken_wing_ring", ::ChickenWingRingItem, ChickenWingRingItem.DEFAULT_PROPERTIES)
	val RING_OF_THE_FLYING_SQUID: DeferredItem<FlyingSquidRingItem> =
		register("ring_of_the_flying_squid", ::FlyingSquidRingItem, FlyingSquidRingItem.DEFAULT_PROPERTIES)
	val ANGEL_RING: DeferredItem<AngelRingItem> =
		register("angel_ring", ::AngelRingItem, AngelRingItem.DEFAULT_PROPERTIES)

	// Items not reimplemented:
	// - Contract (used only for Chunk Loading Ward)

}