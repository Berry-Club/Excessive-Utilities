package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.*
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(ExcessiveUtilities.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	// Functional

	val COMPRESSED_BLOCK =
		basicBlock("compressed_block")
	val ANGEL_BLOCK =
		registerBlockWithoutItem("angel_block") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)) }
	val BLACKOUT_CURTAIN: DeferredBlock<BlackoutCurtainBlock> =
		registerBlock("blackout_curtain", ::BlackoutCurtainBlock)
	val ADVANCED_OBSERVER =
		basicBlock("advanced_observer")
	val CONVEYOR_BELT =
		basicBlock("conveyor_belt")
	val CURSED_EARTH =
		basicBlock("cursed_earth")
	val ENDER_COLLECTOR =
		basicBlock("ender_collector")
	val ENDER_CORE =
		registerBlock("ender_core", ::EnderCoreBlock)
	val ENDER_FLUX_CRYSTAL =
		basicBlock("ender_flux_crystal")
	val ENDER_INFUSED_OBSIDIAN =
		basicBlock("ender_infused_obsidian")
	val MAGICAL_WOOD =
		basicBlock("magical_wood")
	val MAGNUM_TORCH: DeferredBlock<MagnumTorchBlock> =
		registerBlock("magnum_torch", ::MagnumTorchBlock)
	val PEACEFUL_TABLE: DeferredBlock<PeacefulTableBlock> =
		registerBlock("peaceful_table", ::PeacefulTableBlock)
	val DEEP_DARK_PORTAL =
		basicBlock("deep_dark_portal")
	val LAST_MILLENNIUM_PORTAL =
		basicBlock("last_millennium_portal")
	val RAIN_MUFFLER =
		basicBlock("rain_muffler")
	val REDSTONE_CLOCK =
		basicBlock("redstone_clock")
	val SOUND_MUFFLER =
		basicBlock("sound_muffler")
	val TRADING_POST =
		basicBlock("trading_post")
	val CREATIVE_HARVEST =
		basicBlock("creative_harvest")
	val ENDER_PORCUPINE =
		basicBlock("ender_porcupine")
	val INDEXER =
		basicBlock("indexer")
	val MAGICAL_SNOW_GLOBE =
		basicBlock("magical_snow_globe")
	val MECHANICAL_MINER =
		basicBlock("mechanical_miner")
	val MECHANICAL_USER =
		basicBlock("mechanical_user")
	val POWER_OVERLOAD =
		basicBlock("power_overload")
	val RED_ORCHID =
		basicBlock("red_orchid")
	val REDSTONE_LANTERN =
		basicBlock("redstone_lantern")
	val RESONATOR =
		basicBlock("resonator")
	val RESTURBED_MOB_SPAWNER =
		basicBlock("resturbed_mob_spawner")
	val SCANNER =
		basicBlock("scanner")
	val SCREEN =
		basicBlock("screen")
	val WIRELESS_RF_BATTERY =
		basicBlock("wireless_rf_battery")
	val WIRELESS_RF_TRANSMITTER =
		basicBlock("wireless_rf_transmitter")

	// Machines
	val ENDER_QUARRY =
		basicBlock("ender_quarry")
	val ENDER_MARKER =
		basicBlock("ender_marker")
	val ENDER_THERMIC_PUMP =
		basicBlock("ender_thermic_pump")
	val QED =
		basicBlock("qed")
	val CRUSHER =
		basicBlock("crusher")
	val FURNACE =
		basicBlock("furnace")
	val MACHINE_BLOCK =
		basicBlock("machine_block")
	val QUANTUM_QUARRY =
		basicBlock("quantum_quarry")
	val QUANTUM_QUARRY_ACTUATOR =
		basicBlock("quantum_quarry_actuator")

	// Spikes

	val WOODEN_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("wooden_spike") {
			SpikeBlock(
				damagePerHit = 1f,
				canKill = false,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
			)
		}
	val STONE_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("stone_spike") {
			SpikeBlock(
				damagePerHit = 2f,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)
			)
		}
	val IRON_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("iron_spike") {
			SpikeBlock(
				damagePerHit = 4f,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
			)
		}
	val GOLDEN_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("golden_spike") {
			SpikeBlock(
				damagePerHit = 2f,
				dropsExperience = true,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)
			)
		}
	val DIAMOND_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("diamond_spike") {
			SpikeBlock(
				damagePerHit = 8f,
				dropsExperience = true,
				killsAsPlayer = true,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)
			)
		}
	val NETHERITE_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("netherite_spike") {
			SpikeBlock(
				damagePerHit = 10f,
				dropsExperience = true,
				killsAsPlayer = true,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)
			)
		}
	val CREATIVE_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("creative_spike") {
			SpikeBlock(
				damagePerHit = Float.MAX_VALUE,
				dropsExperience = true,
				killsAsPlayer = true,
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)
			)
		}

	// Upgrades

	val ENDER_QUARRY_UPGRADE_BASE =
		basicBlock("ender_quarry_upgrade_base")
	val ENDER_QUARRY_FORTUNE_UPGRADE =
		basicBlock("ender_quarry_fortune_upgrade")
	val ENDER_QUARRY_FORTUNE_TWO_UPGRADE =
		basicBlock("ender_quarry_fortune_two_upgrade")
	val ENDER_QUARRY_FORTUNE_THREE_UPGRADE =
		basicBlock("ender_quarry_fortune_three_upgrade")
	val ENDER_QUARRY_PUMP_UPGRADE =
		basicBlock("ender_quarry_pump_upgrade")
	val ENDER_QUARRY_SILK_TOUCH_UPGRADE =
		basicBlock("ender_quarry_silk_touch_upgrade")
	val ENDER_QUARRY_SPEED_UPGRADE =
		basicBlock("ender_quarry_speed_upgrade")
	val ENDER_QUARRY_SPEED_TWO_UPGRADE =
		basicBlock("ender_quarry_speed_two_upgrade")
	val ENDER_QUARRY_SPEED_THREE_UPGRADE =
		basicBlock("ender_quarry_speed_three_upgrade")
	val ENDER_QUARRY_WORLD_HOLE_UPGRADE =
		basicBlock("ender_quarry_world_hole_upgrade")

	// Storage

	val SLIGHTLY_LARGER_CHEST =
		basicBlock("slightly_larger_chest")
	val MINI_CHEST =
		basicBlock("mini_chest")
	val PLAYER_CHEST =
		basicBlock("player_chest")
	val FILING_CABINET =
		basicBlock("filing_cabinet")
	val ADVANCED_FILING_CABINET =
		basicBlock("advanced_filing_cabinet")
	val STONE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("stone_drum", ::DrumBlock)
	val IRON_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("iron_drum", ::DrumBlock)
	val REINFORCED_LARGE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("reinforced_large_drum", ::DrumBlock)
	val DEMONICALLY_GARGANTUAN_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("demonically_gargantuan_drum", ::DrumBlock)
	val CREATIVE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("creative_drum", ::DrumBlock)
	val CREATIVE_CHEST =
		basicBlock("creative_chest")
	val TRASH_CAN =
		basicBlock("trash_can")
	val TRASH_CAN_CHEST =
		basicBlock("trash_can_chest")
	val TRASH_CAN_ENERGY =
		basicBlock("trash_can_energy")
	val TRASH_CAN_FLUID =
		basicBlock("trash_can_fluid")

	// FE Generators

	val CREATIVE_ENERGY_SOURCE =
		basicBlock("creative_energy_source")
	val SURVIVALIST_GENERATOR =
		basicBlock("survivalist_generator")
	val FURNACE_GENERATOR =
		basicBlock("furnace_generator")
	val MAGMATIC_GENERATOR =
		basicBlock("magmatic_generator")
	val ENDER_GENERATOR =
		basicBlock("ender_generator")
	val HEATED_REDSTONE_GENERATOR =
		basicBlock("heated_redstone_generator")
	val CULINARY_GENERATOR =
		basicBlock("culinary_generator")
	val POTIONS_GENERATOR =
		basicBlock("potions_generator")
	val SOLAR_GENERATOR =
		basicBlock("solar_generator")
	val EXPLOSIVE_GENERATOR =
		basicBlock("explosive_generator")
	val PINK_GENERATOR =
		basicBlock("pink_generator")
	val HIGH_TEMPERATURE_FURNACE_GENERATOR =
		basicBlock("high_temperature_furnace_generator")
	val NETHER_STAR_GENERATOR =
		basicBlock("nether_star_generator")
	val DISENCHANTMENT_GENERATOR =
		basicBlock("disenchantment_generator")
	val FROSTY_GENERATOR =
		basicBlock("frosty_generator")
	val HALITOSIS_GENERATOR =
		basicBlock("halitosis_generator")
	val SLIMEY_GENERATOR =
		basicBlock("slimey_generator")
	val DEATH_GENERATOR =
		basicBlock("death_generator")
	val RAINBOW_GENERATOR =
		basicBlock("rainbow_generator")

	// x8

	val SURVIVALIST_GENERATOR_X8 =
		basicBlock("survivalist_generator_x8")
	val FURNACE_GENERATOR_X8 =
		basicBlock("furnace_generator_x8")
	val MAGMATIC_GENERATOR_X8 =
		basicBlock("magmatic_generator_x8")
	val ENDER_GENERATOR_X8 =
		basicBlock("ender_generator_x8")
	val HEATED_REDSTONE_GENERATOR_X8 =
		basicBlock("heated_redstone_generator_x8")
	val CULINARY_GENERATOR_X8 =
		basicBlock("culinary_generator_x8")
	val POTIONS_GENERATOR_X8 =
		basicBlock("potions_generator_x8")
	val SOLAR_GENERATOR_X8 =
		basicBlock("solar_generator_x8")
	val EXPLOSIVE_GENERATOR_X8 =
		basicBlock("explosive_generator_x8")
	val PINK_GENERATOR_X8 =
		basicBlock("pink_generator_x8")
	val HIGH_TEMPERATURE_FURNACE_GENERATOR_X8 =
		basicBlock("high_temperature_furnace_generator_x8")
	val NETHER_STAR_GENERATOR_X8 =
		basicBlock("nether_star_generator_x8")
	val DISENCHANTMENT_GENERATOR_X8 =
		basicBlock("disenchantment_generator_x8")
	val FROSTY_GENERATOR_X8 =
		basicBlock("frosty_generator_x8")
	val HALITOSIS_GENERATOR_X8 =
		basicBlock("halitosis_generator_x8")
	val SLIMEY_GENERATOR_X8 =
		basicBlock("slimey_generator_x8")
	val DEATH_GENERATOR_X8 =
		basicBlock("death_generator_x8")
	val RAINBOW_GENERATOR_X8 =
		basicBlock("rainbow_generator_x8")

	// x64

	val SURVIVALIST_GENERATOR_X64 =
		basicBlock("survivalist_generator_x64")
	val FURNACE_GENERATOR_X64 =
		basicBlock("furnace_generator_x64")
	val MAGMATIC_GENERATOR_X64 =
		basicBlock("magmatic_generator_x64")
	val ENDER_GENERATOR_X64 =
		basicBlock("ender_generator_x64")
	val HEATED_REDSTONE_GENERATOR_X64 =
		basicBlock("heated_redstone_generator_x64")
	val CULINARY_GENERATOR_X64 =
		basicBlock("culinary_generator_x64")
	val POTIONS_GENERATOR_X64 =
		basicBlock("potions_generator_x64")
	val SOLAR_GENERATOR_X64 =
		basicBlock("solar_generator_x64")
	val EXPLOSIVE_GENERATOR_X64 =
		basicBlock("explosive_generator_x64")
	val PINK_GENERATOR_X64 =
		basicBlock("pink_generator_x64")
	val HIGH_TEMPERATURE_FURNACE_GENERATOR_X64 =
		basicBlock("high_temperature_furnace_generator_x64")
	val NETHER_STAR_GENERATOR_X64 =
		basicBlock("nether_star_generator_x64")
	val DISENCHANTMENT_GENERATOR_X64 =
		basicBlock("disenchantment_generator_x64")
	val FROSTY_GENERATOR_X64 =
		basicBlock("frosty_generator_x64")
	val HALITOSIS_GENERATOR_X64 =
		basicBlock("halitosis_generator_x64")
	val SLIMEY_GENERATOR_X64 =
		basicBlock("slimey_generator_x64")
	val DEATH_GENERATOR_X64 =
		basicBlock("death_generator_x64")
	val RAINBOW_GENERATOR_X64 =
		basicBlock("rainbow_generator_x64")

	// GP Generators

	val MANUAL_MILL =
		basicBlock("manual_mill")
	val WATER_MILL =
		basicBlock("water_mill")
	val WIND_MILL =
		basicBlock("wind_mill")
	val FIRE_MILL =
		basicBlock("fire_mill")
	val LAVA_MILL =
		basicBlock("lava_mill")
	val SOLAR_PANEL: DeferredBlock<GpPanelBlock> =
		registerBlock("solar_panel") { GpPanelBlock(isDay = true, beType = ModBlockEntityTypes.GP_PANEL) }
	val LUNAR_PANEL: DeferredBlock<GpPanelBlock> =
		registerBlock("lunar_panel") { GpPanelBlock(isDay = false, beType = ModBlockEntityTypes.GP_PANEL) }
	val DRAGON_EGG_MILL =
		basicBlock("dragon_egg_mill")
	val CREATIVE_MILL =
		basicBlock("creative_mill")

	// Terraformer

	val TERRAFORMER =
		basicBlock("terraformer")
	val ANTENNA =
		basicBlock("antenna")
	val COOLER =
		basicBlock("cooler")
	val DEHOSTILIFIER =
		basicBlock("dehostilifier")
	val DEHUMIDIFIER =
		basicBlock("dehumidifier")
	val HEATER =
		basicBlock("heater")
	val HUMIDIFIER =
		basicBlock("humidifier")
	val MAGIC_ABSORPTION =
		basicBlock("magic_absorption")
	val MAGIC_INFUSER =
		basicBlock("magic_infuser")

	// Decor

	val BLOCK_OF_BEDROCKIUM =
		basicBlock("block_of_bedrockium")
	val UNSTABLE_INGOT_BLOCK =
		basicBlock("unstable_ingot_block")
	val BORDER_STONE =
		basicBlock("border_stone")
	val BORDER_STONE_ALT =
		basicBlock("border_stone_alt")
	val BURNT_QUARTZ =
		basicBlock("burnt_quartz")
	val EMINENCE_STONE =
		basicBlock("eminence_stone")
	val DIAMOND_ETCHED_COMPUTATIONAL_MATRIX =
		basicBlock("diamond_etched_computational_matrix")
	val EDGED_STONE_BRICKS =
		basicBlock("edged_stone_bricks")
	val ENDER_SAND_ALLOY =
		basicBlock("ender_sand_alloy")
	val FROSTED_STONE =
		basicBlock("frosted_stone")
	val GLASS_BRICKS =
		basicBlock("glass_bricks")
	val GRAVEL_BRICKS =
		basicBlock("gravel_bricks")
	val GRAVEL_ROAD =
		basicBlock("gravel_road")
	val LAPIS_CAELESTIS =
		basicBlock("lapis_caelestis")
	val CHANDELIER: DeferredBlock<ChandelierBlock> =
		registerBlock("chandelier", ::ChandelierBlock)
	val BEDROCK_BRICKS =
		basicBlock("bedrock_bricks")
	val BEDROCK_COBBLESTONE =
		basicBlock("bedrock_cobblestone")
	val BEDROCK_SLABS =
		basicBlock("bedrock_slabs")
	val BLUE_QUARTZ =
		basicBlock("blue_quartz")
	val BLOCK_OF_EVIL_INFUSED_IRON =
		basicBlock("block_of_evil_infused_iron")
	val STONEBURNT =
		basicBlock("stoneburnt")
	val TRUCHET =
		basicBlock("truchet")
	val QUARTZBURNT =
		basicBlock("quartzburnt")
	val RAINBOW_STONE =
		basicBlock("rainbow_stone")
	val MAGICAL_PLANKS =
		basicBlock("magical_planks")
	val DIAGONAL_WOOD =
		basicBlock("diagonal_wood")
	val BLOCK_OF_DEMON_METAL =
		basicBlock("block_of_demon_metal")
	val BLOCK_OF_ENCHANTED_METAL =
		basicBlock("block_of_enchanted_metal")

	// Glass

	val CARVED_GLASS =
		basicBlock("carved_glass")
	val EDGED_GLASS =
		basicBlock("edged_glass")
	val GOLDEN_EDGED_GLASS =
		basicBlock("golden_edged_glass")
	val GLOWSTONE_GLASS =
		basicBlock("glowstone_glass")
	val HEART_GLASS =
		basicBlock("heart_glass")
	val OBSIDIAN_GLASS =
		basicBlock("obsidian_glass")
	val REINFORCED_DARK_GLASS =
		basicBlock("reinforced_dark_glass")
	val SANDY_GLASS =
		basicBlock("sandy_glass")
	val SQUARE_GLASS =
		basicBlock("square_glass")
	val SWIRLING_GLASS =
		basicBlock("swirling_glass")
	val THICKENED_GLASS =
		basicBlock("thickened_glass")
	val ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("ethereal_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = false) }
	val INVERTED_ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("inverted_ethereal_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = true) }
	val DARK_ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock.Dark> =
		registerBlock("dark_ethereal_glass") { SemiPermeableGlassBlock.Dark(isSolidForMobsOnly = false) }
	val INVERTED_DARK_ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock.Dark> =
		registerBlock("inverted_dark_ethereal_glass") { SemiPermeableGlassBlock.Dark(isSolidForMobsOnly = true) }
	val INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("ineffable_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = false) }
	val INVERTED_INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("inverted_ineffable_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = true) }

}