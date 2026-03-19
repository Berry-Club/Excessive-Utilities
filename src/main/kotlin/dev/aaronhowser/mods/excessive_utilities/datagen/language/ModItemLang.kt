package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModPotions
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.registries.DeferredHolder

object ModItemLang {

	const val CREATIVE_TAB = "itemGroup.excessive_utilities"

	const val OPINIUM_CORE_PATHETIC = "item.excessive_utilities.opinium_core.pathetic"
	const val OPINIUM_CORE_MEDIOCRE = "item.excessive_utilities.opinium_core.mediocre"
	const val OPINIUM_CORE_PASSABLE = "item.excessive_utilities.opinium_core.passable"
	const val OPINIUM_CORE_DECENT = "item.excessive_utilities.opinium_core.decent"
	const val OPINIUM_CORE_GOOD = "item.excessive_utilities.opinium_core.good"
	const val OPINIUM_CORE_DAMN_GOOD = "item.excessive_utilities.opinium_core.damn_good"
	const val OPINIUM_CORE_AMAZING = "item.excessive_utilities.opinium_core.amazing"
	const val OPINIUM_CORE_INSPIRING = "item.excessive_utilities.opinium_core.inspiring"
	const val OPINIUM_CORE_PERFECTED = "item.excessive_utilities.opinium_core.perfected"

	const val MOBIUS_INGOT = "item.excessive_utilities.mobius_ingot"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(CREATIVE_TAB, "Excessive Utilities")

			addItem(ModItems.BEDROCKIUM_INGOT, "Bedrockium Ingot")
			addItem(ModItems.SOUL_FRAGMENT, "Soul Fragment")
			addItem(ModItems.ENDER_SHARD, "Ender Shard")
			addItem(ModItems.DEMON_INGOT, "Demon Ingot")
			addItem(ModItems.DROP_OF_EVIL, "Drop of Evil")
			addItem(ModItems.ENCHANTED_INGOT, "Enchanted Ingot")
			addItem(ModItems.EVIL_INFUSED_IRON_INGOT, "Evil Infused Iron Ingot")
			addItem(ModItems.EYE_OF_REDSTONE, "Eye of Redstone")
			addItem(ModItems.LUNAR_REACTIVE_DUST, "Lunar Reactive Dust")
			addItem(ModItems.MOON_STONE, "Moon Stone")
			addItem(ModItems.RED_COAL, "Red Coal")
			addItem(ModItems.REDSTONE_GEAR, "Redstone Gear")
			addItem(ModItems.RESONATING_REDSTONE_CRYSTAL, "Resonating Redstone Crystal")
			addItem(ModItems.UPGRADE_BASE, "Upgrade Base")
			addItem(ModItems.OPINIUM_CORE, "Opinium Core")
			addItem(ModItems.UNSTABLE_INGOT, "Unstable Ingot")
			addItem(ModItems.SEMI_UNSTABLE_NUGGET, "Semi-Unstable Nugget")
			add(MOBIUS_INGOT, "Mobius \"Unstable/Stable\" Ingot")
			addItem(ModItems.KLEIN_BOTTLE, "Klein Bottle")

			addItem(ModItems.DESTRUCTION_PICKAXE, "Destruction Pickaxe")
			addItem(ModItems.EROSION_SHOVEL, "Erosion Shovel")
			addItem(ModItems.ETHERIC_SWORD, "Etheric Sword")
			addItem(ModItems.HEALING_AXE, "Healing Axe")
			addItem(ModItems.REVERSING_HOE, "Reversing Hoe")
			addItem(ModItems.PRECISION_SHEARS, "Precision Shears")
			addItem(ModItems.PAINTBRUSH, "Paintbrush")
			addItem(ModItems.GLASS_CUTTER, "Glass Cutter")
			addItem(ModItems.TROWEL, "Trowel")
			addItem(ModItems.PIPE_WRENCH, "Pipe Wrench")
			addItem(ModItems.WATERING_CAN, "Watering Can")
			addItem(ModItems.REINFORCED_WATERING_CAN, "Reinforced Watering Can")
			addItem(ModItems.GOLDEN_LASSO, "Golden Lasso")
			addItem(ModItems.CURSED_LASSO, "Cursed Lasso")
			addItem(ModItems.WOODEN_SICKLE, "Wooden Sickle")
			addItem(ModItems.STONE_SICKLE, "Stone Sickle")
			addItem(ModItems.IRON_SICKLE, "Iron Sickle")
			addItem(ModItems.GOLDEN_SICKLE, "Golden Sickle")
			addItem(ModItems.DIAMOND_SICKLE, "Diamond Sickle")
			addItem(ModItems.NETHERITE_SICKLE, "Netherite Sickle")
			addItem(ModItems.BUILDERS_WAND, "Builder's Wand")
			addItem(ModItems.CREATIVE_BUILDERS_WAND, "Creative Builder's Wand")
			addItem(ModItems.DESTRUCTION_WAND, "Destruction Wand")
			addItem(ModItems.CREATIVE_DESTRUCTION_WAND, "Creative Destruction Wand")

			addItem(ModItems.KIKOKU, "Kikoku")
			addItem(ModItems.FIRE_AXE, "Fire Axe")
			addItem(ModItems.LUX_SABER, "Lux Saber")
			addItem(ModItems.COMPOUND_BOW, "Compound Bow")
			addItem(ModItems.MAGICAL_BOOMERANG, "Magical Boomerang")

			addItem(ModItems.HEATING_COIL, "Heating Coil")
			addItem(ModItems.POWER_MANAGER, "Power Manager")
			addItem(ModItems.SUN_CRYSTAL, "Sun Crystal")
			addItem(ModItems.BIOME_MARKER, "Biome Marker")
			addItem(ModItems.BAG_OF_HOLDING, "Bag of Holding")
			addItem(ModItems.MAGICAL_APPLE, "Magical Apple")
			addItem(ModItems.FLAT_TRANSFER_NODE_FLUIDS, "Flat Transfer Node (Fluids)")
			addItem(ModItems.FLAT_TRANSFER_NODE_ITEMS, "Flat Transfer Node (Items)")
			addItem(ModItems.PORTABLE_SCANNER, "Portable Scanner")
			addItem(ModItems.SONAR_GOGGLES, "Sonar Goggles")
			addItem(ModItems.INDEXER_REMOTE, "Indexer Remote")

			addItem(ModItems.CHICKEN_WING_RING, "Chicken Wing Ring")
			addItem(ModItems.RING_OF_THE_FLYING_SQUID, "Ring of the Flying Squid")
			addItem(ModItems.ANGEL_RING, "Angel Ring")

			addItem(ModItems.SPEED_UPGRADE, "Speed Upgrade")
			addItem(ModItems.SPEED_UPGRADE_MAGICAL, "Speed Upgrade (Magical)")
			addItem(ModItems.SPEED_UPGRADE_ULTIMATE, "Speed Upgrade (Ultimate)")
			addItem(ModItems.STACK_UPGRADE, "Stack Upgrade")
			addItem(ModItems.WORLD_INTERACTION_UPGRADE, "World Interaction Upgrade")
			addItem(ModItems.ITEM_FILTER, "Item Filter")
			addItem(ModItems.FLUID_FILTER, "Fluid Filter")
			addItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE, "Breadth-First Search Upgrade")
			addItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE, "Depth-First Search Upgrade")
			addItem(ModItems.PSEUDO_ROUND_ROBIN_UPGRADE, "Pseudo Round Robin Upgrade")
			addItem(ModItems.ENDER_RECEIVER, "Ender Receiver")
			addItem(ModItems.ENDER_TRANSMITTER, "Ender Transmitter")
			addItem(ModItems.CREATIVE_UPGRADE, "Creative Upgrade")

			add(OPINIUM_CORE_PATHETIC, "Opinium Core (Pathetic)")
			add(OPINIUM_CORE_MEDIOCRE, "Opinium Core (Mediocre)")
			add(OPINIUM_CORE_PASSABLE, "Opinium Core (Passable)")
			add(OPINIUM_CORE_DECENT, "Opinium Core (Decent)")
			add(OPINIUM_CORE_GOOD, "Opinium Core (Good)")
			add(OPINIUM_CORE_DAMN_GOOD, "Opinium Core (Damn Good)")
			add(OPINIUM_CORE_AMAZING, "Opinium Core (Amazing)")
			add(OPINIUM_CORE_INSPIRING, "Opinium Core (Inspiring)")
			add(OPINIUM_CORE_PERFECTED, "Opinium Core (Perfected)")

			fun potion(
				potionName: String,
				potion: DeferredHolder<Potion, Potion>
			) {
				val effectName = ExcessiveUtilities.MOD_ID + "." + potion.unwrapKey().get().location().path

				add("item.minecraft.tipped_arrow.effect.$effectName", "Arrow of $potionName")
				add("item.minecraft.potion.effect.$effectName", "Potion of $potionName")
				add("item.minecraft.splash_potion.effect.$effectName", "Splash Potion of $potionName")
				add("item.minecraft.lingering_potion.effect.$effectName", "Lingering Potion of $potionName")
			}

			potion("Gravity", ModPotions.GRAVITY)
			potion("Oily", ModPotions.OILY)
			potion("Greek Fire", ModPotions.GREEK_FIRE)
			potion("Love", ModPotions.LOVE)
			potion("Relapse", ModPotions.RELAPSE)
			potion("Second Chance", ModPotions.SECOND_CHANCE)
			potion("Doom", ModPotions.DOOM)
			potion("Purging", ModPotions.PURGING)
		}
	}

}