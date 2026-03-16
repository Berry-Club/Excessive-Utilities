package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronItemRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.*
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.Tiers
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems : AaronItemRegistry() {

	val ITEM_REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(ExcessiveUtilities.MOD_ID)
	override fun getItemRegistry(): DeferredRegister.Items = ITEM_REGISTRY

	// Ingredients

	val BEDROCKIUM_INGOT: DeferredItem<BedrockiumIngotItem> =
		register("bedrockium_ingot", ::BedrockiumIngotItem)
	val SOUL_FRAGMENT: DeferredItem<SoulFragmentItem> =
		register("soul_fragment", ::SoulFragmentItem, SoulFragmentItem.DEFAULT_PROPERTIES)
	val ENDER_SHARD: DeferredItem<Item> =
		basic("ender_shard")
	val DEMON_INGOT: DeferredItem<Item> =
		basic("demon_ingot", Item.Properties().fireResistant())
	val DROP_OF_EVIL: DeferredItem<DropOfEvilItem> =
		register("drop_of_evil", ::DropOfEvilItem)
	val ENCHANTED_INGOT: DeferredItem<Item> =
		basic("enchanted_ingot")
	val EVIL_INFUSED_IRON_INGOT: DeferredItem<Item> =
		basic("evil_infused_iron_ingot")
	val EYE_OF_REDSTONE: DeferredItem<Item> =
		basic("eye_of_redstone")
	val LUNAR_REACTIVE_DUST: DeferredItem<Item> =
		basic("lunar_reactive_dust")
	val MOON_STONE: DeferredItem<Item> =
		basic("moon_stone")

	@JvmField
	val RED_COAL: DeferredItem<RedCoalItem> =
		register("red_coal", ::RedCoalItem, RedCoalItem.DEFAULT_PROPERTIES)
	val REDSTONE_GEAR: DeferredItem<Item> =
		basic("redstone_gear")
	val RESONATING_REDSTONE_CRYSTAL: DeferredItem<Item> =
		basic("resonating_redstone_crystal")
	val UPGRADE_BASE: DeferredItem<Item> =
		basic("upgrade_base")
	val OPINIUM_CORE: DeferredItem<OpiniumCoreItem> =
		register("opinium_core", ::OpiniumCoreItem, OpiniumCoreItem.DEFAULT_PROPERTIES)
	val DIVISION_SIGIL =
		basic("division_sigil")
	val UNSTABLE_INGOT: DeferredItem<UnstableIngotItem> =
		register("unstable_ingot", ::UnstableIngotItem, PROPERTIES_SINGLE_STACK)
	val SEMI_UNSTABLE_NUGGET: DeferredItem<Item> =
		basic("semi_unstable_nugget")
	val KLEIN_BOTTLE: DeferredItem<Item> =
		basic("klein_bottle")

	// Tools

	val DESTRUCTION_PICKAXE: DeferredItem<DestructionPickaxeItem> =
		register("destruction_pickaxe", ::DestructionPickaxeItem, DestructionPickaxeItem.DEFAULT_PROPERTIES)
	val EROSION_SHOVEL: DeferredItem<ErosionShovelItem> =
		register("erosion_shovel", ::ErosionShovelItem, ErosionShovelItem.DEFAULT_PROPERTIES)
	val ETHERIC_SWORD: DeferredItem<EthericSwordItem> =
		register("etheric_sword", ::EthericSwordItem, EthericSwordItem.DEFAULT_PROPERTIES)
	val HEALING_AXE: DeferredItem<HealingAxeItem> =
		register("healing_axe", ::HealingAxeItem, HealingAxeItem.DEFAULT_PROPERTIES)
	val REVERSING_HOE: DeferredItem<ReversingHoeItem> =
		register("reversing_hoe", ::ReversingHoeItem, ReversingHoeItem.DEFAULT_PROPERTIES)
	val PRECISION_SHEARS =
		basic("precision_shears")
	val PAINTBRUSH =
		basic("paintbrush")
	val GLASS_CUTTER: DeferredItem<GlassCutterItem> =
		register("glass_cutter", ::GlassCutterItem, GlassCutterItem.DEFAULT_PROPERTIES)
	val TROWEL: DeferredItem<TrowelItem> =
		register("trowel", ::TrowelItem, TrowelItem.DEFAULT_PROPERTIES)
	val WRENCH =
		basic("wrench")
	val WATERING_CAN: DeferredItem<WateringCanItem> =
		register("watering_can", { WateringCanItem(isReinforced = false, it) }, WateringCanItem.DEFAULT_PROPERTIES)
	val REINFORCED_WATERING_CAN: DeferredItem<WateringCanItem> =
		register("reinforced_watering_can", { WateringCanItem(isReinforced = true, it) }, WateringCanItem.DEFAULT_REINFORCED_PROPERTIES)
	val GOLDEN_LASSO: DeferredItem<EntityLassoItem> =
		register("golden_lasso", { EntityLassoItem(canHoldHostileMobs = false, it) }, PROPERTIES_SINGLE_STACK)
	val CURSED_LASSO: DeferredItem<EntityLassoItem> =
		register("cursed_lasso", { EntityLassoItem(canHoldHostileMobs = true, it) }, PROPERTIES_SINGLE_STACK)
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
	val BUILDERS_WAND: DeferredItem<BuildersWandItem> =
		register("builders_wand", ::BuildersWandItem) { BuildersWandItem.propertiesWithVolume(9) }
	val CREATIVE_BUILDERS_WAND: DeferredItem<BuildersWandItem> =
		register("creative_builders_wand", ::BuildersWandItem) { BuildersWandItem.propertiesWithVolume(47) }
	val DESTRUCTION_WAND: DeferredItem<DestructionWandItem> =
		register("destruction_wand", ::DestructionWandItem) { DestructionWandItem.propertiesWithVolume(9) }
	val CREATIVE_DESTRUCTION_WAND: DeferredItem<DestructionWandItem> =
		register("creative_destruction_wand", ::DestructionWandItem) { DestructionWandItem.propertiesWithVolume(47) }
	val INDEXER_REMOTE =
		basic("indexer_remote")

	// Weapons

	val KIKOKU =
		basic("kikoku")
	val FIRE_AXE: DeferredItem<FireAxeItem> =
		register("fire_axe", ::FireAxeItem, FireAxeItem.DEFAULT_PROPERTIES)
	val LUX_SABER =
		basic("lux_saber")
	val COMPOUND_BOW: DeferredItem<CompoundBowItem> =
		register("compound_bow", ::CompoundBowItem)
	val MAGICAL_BOOMERANG: DeferredItem<MagicalBoomerangItem> =
		register("magical_boomerang", ::MagicalBoomerangItem, PROPERTIES_SINGLE_STACK)

	// Misc
	@JvmField
	val HEATING_COIL: DeferredItem<HeatingCoilItem> =
		register("heating_coil", ::HeatingCoilItem, HeatingCoilItem.DEFAULT_PROPERTIES)
	val POWER_MANAGER: DeferredItem<PowerManagerItem> =
		register("power_manager", ::PowerManagerItem, PROPERTIES_SINGLE_STACK)
	val SUN_CRYSTAL: DeferredItem<SunCrystalItem> =
		register("sun_crystal", ::SunCrystalItem, SunCrystalItem.DEFAULT_PROPERTIES)
	val BIOME_MARKER: DeferredItem<BiomeMarkerItem> =
		register("biome_marker", ::BiomeMarkerItem)
	val BAG_OF_HOLDING: DeferredItem<BagOfHoldingItem> =
		register("bag_of_holding", ::BagOfHoldingItem, BagOfHoldingItem.DEFAULT_PROPERTIES)
	val MAGICAL_APPLE: DeferredItem<MagicalAppleItem> =
		register("magical_apple", ::MagicalAppleItem, MagicalAppleItem.PROPERTIES)
	val PORTABLE_SCANNER: DeferredItem<PortableScanner> =
		register("portable_scanner", ::PortableScanner, PROPERTIES_SINGLE_STACK)
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

	// Block Items

	val ENDER_LILY: DeferredItem<BlockItem> =
		register("ender_lily", { BlockItem(ModBlocks.ENDER_LILY.get(), it) })
	val ANGEL_BLOCK: DeferredItem<AngelBlockItem> =
		register("angel_block", ::AngelBlockItem)
	val MAGICAL_SNOW_GLOBE: DeferredItem<MagicalSnowGlobeBlockItem> =
		register("magical_snow_globe", ::MagicalSnowGlobeBlockItem, MagicalSnowGlobeBlockItem.DEFAULT_PROPERTIES)
	val BLOCK_OF_DEMON_METAL: DeferredItem<Item> =
		register(
			"block_of_demon_metal",
			{ BlockItem(ModBlocks.BLOCK_OF_DEMON_METAL.get(), it) },
			Item.Properties().fireResistant()
		)

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
	val ITEM_FILTER: DeferredItem<ItemFilterItem> =
		register("item_filter", ::ItemFilterItem, PROPERTIES_SINGLE_STACK)
	val FLUID_FILTER: DeferredItem<FluidFilterItem> =
		register("fluid_filter", ::FluidFilterItem, PROPERTIES_SINGLE_STACK)
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
		register("angel_ring", ::AngelRingItem, PROPERTIES_SINGLE_STACK)

	// Items not reimplemented:
	// - Contract (used only for Chunk Loading Ward)
	// - Wireless Heating Coil (annoying to implement)

}