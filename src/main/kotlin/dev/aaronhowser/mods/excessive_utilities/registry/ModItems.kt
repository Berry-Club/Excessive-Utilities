package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.BedrockiumIngotItem
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
	val ENDER_SHARD =
		basic("ender_shard")
	val DEMON_INGOT =
		basic("demon_ingot")
	val DROP_OF_EVIL =
		basic("drop_of_evil")
	val ENCHANTED_INGOT =
		basic("enchanted_ingot")
	val EVIL_INFUSED_IRON_INGOT =
		basic("evil_infused_iron_ingot")
	val EYE_OF_REDSTONE =
		basic("eye_of_redstone")
	val LUNAR_REACTIVE_DUST =
		basic("lunar_reactive_dust")
	val MOON_STONE =
		basic("moon_stone")
	val RED_COAL =
		basic("red_coal")
	val REDSTONE_GEAR =
		basic("redstone_gear")
	val RESONATING_REDSTONE_CRYSTAL =
		basic("resonating_redstone_crystal")
	val UPGRADE_BASE =
		basic("upgrade_base")
	val OPINIUM_CORE =
		basic("opinium_core")
	val UNSTABLE_INGOT =
		basic("unstable_ingot")
	val SEMI_UNSTABLE_NUGGET =
		basic("semi_unstable_nugget")
	val MOBIUS_INGOT =
		basic("mobius_ingot")
	val KLEIN_FLASK =
		basic("klein_flask")

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
	val REVERSING_HOE =
		basic("reversing_hoe")
	val PRECISION_SHEARS =
		basic("precision_shears")
	val PAINTBRUSH =
		basic("paintbrush")
	val GLASS_CUTTER =
		basic("glass_cutter")
	val TROWEL =
		basic("trowel")
	val WRENCH =
		basic("wrench")
	val WATERING_CAN =
		basic("watering_can")
	val REINFORCED_WATERING_CAN =
		basic("reinforced_watering_can")
	val GOLDEN_LASSO =
		basic("golden_lasso")
	val CURSED_LASSO =
		basic("cursed_lasso")
	val WOODEN_SICKLE =
		basic("wooden_sickle")
	val STONE_SICKLE =
		basic("stone_sickle")
	val IRON_SICKLE =
		basic("iron_sickle")
	val GOLDEN_SICKLE =
		basic("golden_sickle")
	val DIAMOND_SICKLE =
		basic("diamond_sickle")
	val NETHERITE_SICKLE =
		basic("netherite_sickle")
	val BUILDERS_WAND =
		basic("builders_wand")
	val CREATIVE_BUILDERS_WAND =
		basic("creative_builders_wand")
	val DESTRUCTION_WAND =
		basic("destruction_wand")
	val CREATIVE_DESTRUCTION_WAND =
		basic("creative_destruction_wand")

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
	val HEATING_COIL =
		basic("heating_coil")
	val WIRELESS_RF_HEATING_COIL =
		basic("wireless_rf_heating_coil")
	val POWER_MANAGER =
		basic("power_manager")
	val SUN_CRYSTAL =
		basic("sun_crystal")
	val BIOME_MARKER =
		basic("biome_marker")
	val BAG_OF_HOLDING =
		basic("bag_of_holding")
	val MAGICAL_APPLE =
		basic("magical_apple")
	val PORTABLE_SCANNER =
		basic("portable_scanner")
	val SONAR_GOGGLES =
		basic("sonar_goggles")
	val FLAT_TRANSFER_NODE_FLUIDS =
		basic("flat_transfer_node_fluids")
	val FLAT_TRANSFER_NODE_ITEMS =
		basic("flat_transfer_node_items")

	// Transfer Node Upgrades

	val SPEED_UPGRADE =
		basic("speed_upgrade")
	val STACK_UPGRADE =
		basic("stack_upgrade")
	val WORLD_INTERACTION_UPGRADE =
		basic("world_interaction_upgrade")
	val ITEM_FILTER_UPGRADE =
		basic("item_filter_upgrade")
	val ADVANCED_ITEM_FILTER_UPGRADE =
		basic("advanced_item_filter_upgrade")
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

	val CHICKEN_WING_RING =
		basic("chicken_wing_ring")
	val RING_OF_THE_FLYING_SQUID =
		basic("ring_of_the_flying_squid")
	val ANGEL_RING =
		basic("angel_ring")

}