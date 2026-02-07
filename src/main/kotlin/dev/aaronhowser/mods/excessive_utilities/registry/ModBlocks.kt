package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.*
import dev.aaronhowser.mods.excessive_utilities.block.base.CompressibleFeGeneratorBlock
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
	val RESONATOR: DeferredBlock<ResonatorBlock> =
		registerBlock("resonator", ::ResonatorBlock)
	val RESTURBED_MOB_SPAWNER =
		basicBlock("resturbed_mob_spawner")
	val SCANNER =
		basicBlock("scanner")
	val WIRELESS_FE_BATTERY: DeferredBlock<WirelessFeBatteryBlock> =
		registerBlock("wireless_fe_battery", ::WirelessFeBatteryBlock)
	val WIRELESS_FE_TRANSMITTER: DeferredBlock<WirelessFeTransmitterBlock> =
		registerBlock("wireless_fe_transmitter", ::WirelessFeTransmitterBlock)

	// Machines
	val ENDER_QUARRY =
		basicBlock("ender_quarry")
	val ENDER_MARKER =
		basicBlock("ender_marker")
	val ENDER_THERMIC_PUMP =
		basicBlock("ender_thermic_pump")
	val QED =
		basicBlock("qed")
	val MACHINE_BLOCK =
		basicBlock("machine_block")
	val CRUSHER =
		basicBlock("crusher")
	val FURNACE =
		basicBlock("furnace")
	val ENCHANTER =
		basicBlock("enchanter")
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

	// Transfer

	val TRANSFER_PIPE =
		basicBlock("transfer_pipe")
	val ITEM_TRANSFER_NODE =
		basicBlock("item_transfer_node")
	val FLUID_TRANSFER_NODE =
		basicBlock("fluid_transfer_node")
	val ENERGY_TRANSFER_NODE =
		basicBlock("energy_transfer_node")
	val ITEM_RETRIEVAL_NODE =
		basicBlock("item_retrieval_node")
	val FLUID_RETRIEVAL_NODE =
		basicBlock("fluid_retrieval_node")
	val ENERGY_RETRIEVAL_NODE =
		basicBlock("energy_retrieval_node")
	val TRANSFER_FILTER =
		basicBlock("transfer_filter")
	val TRANSFER_PIPE_FILTER =
		basicBlock("transfer_pipe_filter")

	// FE Generators

	val CREATIVE_ENERGY_SOURCE =
		basicBlock("creative_energy_source")
	val SURVIVALIST_GENERATOR =
		basicBlock("survivalist_generator")
	val FURNACE_GENERATOR =
		basicBlock("furnace_generator")
	val MAGMATIC_GENERATOR =
		basicBlock("magmatic_generator")
	val ENDER_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("ender_generator") { CompressibleFeGeneratorBlock.ender(tier = 1) }
	val HEATED_REDSTONE_GENERATOR =
		basicBlock("heated_redstone_generator")
	val CULINARY_GENERATOR =
		basicBlock("culinary_generator")
	val POTIONS_GENERATOR =
		basicBlock("potions_generator")
	val SOLAR_GENERATOR =
		basicBlock("solar_generator")
	val EXPLOSIVE_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("explosive_generator") { CompressibleFeGeneratorBlock.explosive(tier = 1) }
	val PINK_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("pink_generator") { CompressibleFeGeneratorBlock.pink(tier = 1) }
	val HIGH_TEMPERATURE_FURNACE_GENERATOR =
		basicBlock("high_temperature_furnace_generator")
	val NETHER_STAR_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("nether_star_generator") { CompressibleFeGeneratorBlock.netherStar(tier = 1) }
	val DISENCHANTMENT_GENERATOR =
		basicBlock("disenchantment_generator")
	val FROSTY_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("frosty_generator") { CompressibleFeGeneratorBlock.frosty(tier = 1) }
	val HALITOSIS_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("halitosis_generator") { CompressibleFeGeneratorBlock.halitosis(tier = 1) }
	val SLIMY_GENERATOR =
		basicBlock("slimy_generator")
	val DEATH_GENERATOR: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("death_generator") { CompressibleFeGeneratorBlock.death(tier = 1) }
	val RAINBOW_GENERATOR =
		basicBlock("rainbow_generator")

	val SURVIVALIST_GENERATOR_MK2 =
		basicBlock("survivalist_generator_mk2")
	val FURNACE_GENERATOR_MK2 =
		basicBlock("furnace_generator_mk2")
	val MAGMATIC_GENERATOR_MK2 =
		basicBlock("magmatic_generator_mk2")
	val ENDER_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("ender_generator_mk2") { CompressibleFeGeneratorBlock.ender(tier = 2) }
	val HEATED_REDSTONE_GENERATOR_MK2 =
		basicBlock("heated_redstone_generator_mk2")
	val CULINARY_GENERATOR_MK2 =
		basicBlock("culinary_generator_mk2")
	val POTIONS_GENERATOR_MK2 =
		basicBlock("potions_generator_mk2")
	val SOLAR_GENERATOR_MK2 =
		basicBlock("solar_generator_mk2")
	val EXPLOSIVE_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("explosive_generator_mk2") { CompressibleFeGeneratorBlock.explosive(tier = 2) }
	val PINK_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("pink_generator_mk2") { CompressibleFeGeneratorBlock.pink(tier = 2) }
	val HIGH_TEMPERATURE_FURNACE_GENERATOR_MK2 =
		basicBlock("high_temperature_furnace_generator_mk2")
	val NETHER_STAR_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("nether_star_generator_mk2") { CompressibleFeGeneratorBlock.netherStar(tier = 2) }
	val DISENCHANTMENT_GENERATOR_MK2 =
		basicBlock("disenchantment_generator_mk2")
	val FROSTY_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("frosty_generator_mk2") { CompressibleFeGeneratorBlock.frosty(tier = 2) }
	val HALITOSIS_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("halitosis_generator_mk2") { CompressibleFeGeneratorBlock.halitosis(tier = 2) }
	val SLIMY_GENERATOR_MK2 =
		basicBlock("slimy_generator_mk2")
	val DEATH_GENERATOR_MK2: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("death_generator_mk2") { CompressibleFeGeneratorBlock.death(tier = 2) }
	val RAINBOW_GENERATOR_MK2 =
		basicBlock("rainbow_generator_mk2")

	val SURVIVALIST_GENERATOR_MK3 =
		basicBlock("survivalist_generator_mk3")
	val FURNACE_GENERATOR_MK3 =
		basicBlock("furnace_generator_mk3")
	val MAGMATIC_GENERATOR_MK3 =
		basicBlock("magmatic_generator_mk3")
	val ENDER_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("ender_generator_mk3") { CompressibleFeGeneratorBlock.ender(tier = 3) }
	val HEATED_REDSTONE_GENERATOR_MK3 =
		basicBlock("heated_redstone_generator_mk3")
	val CULINARY_GENERATOR_MK3 =
		basicBlock("culinary_generator_mk3")
	val POTIONS_GENERATOR_MK3 =
		basicBlock("potions_generator_mk3")
	val SOLAR_GENERATOR_MK3 =
		basicBlock("solar_generator_mk3")
	val EXPLOSIVE_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("explosive_generator_mk3") { CompressibleFeGeneratorBlock.explosive(tier = 3) }
	val PINK_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("pink_generator_mk3") { CompressibleFeGeneratorBlock.pink(tier = 3) }
	val HIGH_TEMPERATURE_FURNACE_GENERATOR_MK3 =
		basicBlock("high_temperature_furnace_generator_mk3")
	val NETHER_STAR_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("nether_star_generator_mk3") { CompressibleFeGeneratorBlock.netherStar(tier = 3) }
	val DISENCHANTMENT_GENERATOR_MK3 =
		basicBlock("disenchantment_generator_mk3")
	val FROSTY_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("frosty_generator_mk3") { CompressibleFeGeneratorBlock.frosty(tier = 3) }
	val HALITOSIS_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("halitosis_generator_mk3") { CompressibleFeGeneratorBlock.halitosis(tier = 3) }
	val SLIMY_GENERATOR_MK3 =
		basicBlock("slimy_generator_mk3")
	val DEATH_GENERATOR_MK3: DeferredBlock<CompressibleFeGeneratorBlock> =
		registerBlock("death_generator_mk3") { CompressibleFeGeneratorBlock.death(tier = 3) }
	val RAINBOW_GENERATOR_MK3 =
		basicBlock("rainbow_generator_mk3")

	// GP Generators

	val MANUAL_MILL: DeferredBlock<ManualMillBlock> =
		registerBlock("manual_mill", ::ManualMillBlock)
	val WATER_MILL: DeferredBlock<WaterMillBlock> =
		registerBlock("water_mill", ::WaterMillBlock)
	val WIND_MILL =
		basicBlock("wind_mill")
	val FIRE_MILL: DeferredBlock<FireMillBlock> =
		registerBlock("fire_mill", ::FireMillBlock)
	val LAVA_MILL: DeferredBlock<LavaMillBlock> =
		registerBlock("lava_mill", ::LavaMillBlock)
	val SOLAR_PANEL: DeferredBlock<GpPanelBlock> =
		registerBlock("solar_panel") { GpPanelBlock(requiresDay = true) }
	val LUNAR_PANEL: DeferredBlock<GpPanelBlock> =
		registerBlock("lunar_panel") { GpPanelBlock(requiresDay = false) }
	val DRAGON_EGG_MILL: DeferredBlock<DragonEggMillBlock> =
		registerBlock("dragon_egg_mill", ::DragonEggMillBlock)
	val CREATIVE_MILL: DeferredBlock<CreativeMillBlock> =
		registerBlock("creative_mill", ::CreativeMillBlock)

	// Terraformer

	val TERRAFORMER =
		basicBlock("terraformer")
	val CLIMOGRAPH_BASE_UNIT =
		basicBlock("climograph_base_unit")
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
	val BLOCK_OF_UNSTABLE_INGOT =
		basicBlock("block_of_unstable_ingot")
	val BLOCK_OF_EVIL_INFUSED_IRON =
		basicBlock("block_of_evil_infused_iron")
	val BLOCK_OF_DEMON_METAL =
		basicBlock("block_of_demon_metal")
	val BLOCK_OF_ENCHANTED_METAL =
		basicBlock("block_of_enchanted_metal")
	val POLISHED_STONE =
		basicBlock("polished_stone")
	val BORDER_STONE =
		basicBlock("border_stone")
	val CROSSED_STONE =
		basicBlock("crossed_stone")
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

	// Glass

	val CARVED_GLASS =
		basicBlock("carved_glass")
	val EDGED_GLASS =
		basicBlock("edged_glass")
	val GOLDEN_EDGED_GLASS =
		basicBlock("golden_edged_glass")
	val GLOWING_GLASS =
		basicBlock("glowing_glass")
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
	val THICKENED_GLASS_BORDERED =
		basicBlock("thickened_glass_bordered")
	val THICKENED_GLASS_PATTERNED =
		basicBlock("thickened_glass_patterned")
	val REDSTONE_GLASS =
		basicBlock("redstone_glass")
	val ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("ethereal_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = false) }
	val INVERTED_ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("inverted_ethereal_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = true) }
	val INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("ineffable_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = false) }
	val INVERTED_INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("inverted_ineffable_glass") { SemiPermeableGlassBlock(isSolidForMobsOnly = true) }
	val DARK_INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("dark_ineffable_glass") { SemiPermeableGlassBlock.Dark(isSolidForMobsOnly = false) }

	// Blocks not reimplemented:
	// - Screen
	// - Chunk Loading Ward

}