package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.*
import dev.aaronhowser.mods.excessive_utilities.util.EnderQuarryUpgradeType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(ExcessiveUtilities.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	// Functional

	val COMPRESSED_BLOCK =
		basicBlock("compressed_block")
	val ANGEL_BLOCK: DeferredBlock<AngelBlock> =
		registerBlockWithoutItem("angel_block", ::AngelBlock)
	val BLACKOUT_CURTAIN: DeferredBlock<BlackoutCurtainBlock> =
		registerBlock("blackout_curtain", ::BlackoutCurtainBlock)
	val CONVEYOR_BELT: DeferredBlock<ConveyorBeltBlock> =
		registerBlock("conveyor_belt", ::ConveyorBeltBlock)
	val CURSED_EARTH: DeferredBlock<CursedEarthBlock> =
		registerBlock("cursed_earth", ::CursedEarthBlock)
	val ENDER_COLLECTOR =
		basicBlock("ender_collector")
	val MAGICAL_WOOD: DeferredBlock<MagicalWoodBlock> =
		registerBlock("magical_wood", ::MagicalWoodBlock)
	val ENDER_CORE: DeferredBlock<EnderCoreBlock> =
		registerBlock("ender_core", ::EnderCoreBlock)
	val MAGNUM_TORCH: DeferredBlock<MagnumTorchBlock> =
		registerBlock("magnum_torch", ::MagnumTorchBlock)
	val PEACEFUL_TABLE: DeferredBlock<PeacefulTableBlock> =
		registerBlock("peaceful_table", ::PeacefulTableBlock)
	val DEEP_DARK_PORTAL =
		basicBlock("deep_dark_portal")
	val LAST_MILLENNIUM_PORTAL =
		basicBlock("last_millennium_portal")
	val SOUND_MUFFLER: DeferredBlock<SoundMufflerBlock> =
		registerBlock("sound_muffler", ::SoundMufflerBlock)
	val TRADING_POST =
		basicBlock("trading_post")
	val CREATIVE_HARVEST: DeferredBlock<CreativeHarvestBlock> =
		registerBlock("creative_harvest", ::CreativeHarvestBlock)
	val ENDER_PORCUPINE =
		basicBlock("ender_porcupine")
	val INDEXER =
		basicBlock("indexer")
	val MAGICAL_SNOW_GLOBE: DeferredBlock<MagicalSnowGlobeBlock> =
		registerBlockWithoutItem("magical_snow_globe", ::MagicalSnowGlobeBlock)
	val RESTURBED_MOB_SPAWNER =
		basicBlock("resturbed_mob_spawner")
	val SCANNER =
		basicBlock("scanner")
	val MOON_STORE_ORE: DeferredBlock<MoonStoreOreBlock> =
		registerBlock("moon_store_ore") { MoonStoreOreBlock(Blocks.IRON_ORE) }
	val DEEPSLATE_MOON_STONE_ORE: DeferredBlock<MoonStoreOreBlock> =
		registerBlock("deepslate_moon_stone_ore") { MoonStoreOreBlock(Blocks.DEEPSLATE_IRON_ORE) }

	// Redstone stuff

	val REDSTONE_LANTERN: DeferredBlock<RedstoneLanternBlock> =
		registerBlock("redstone_lantern", ::RedstoneLanternBlock)
	val RED_ORCHID =
		basicBlock("red_orchid")
	val MECHANICAL_MINER =
		basicBlock("mechanical_miner")
	val MECHANICAL_USER =
		basicBlock("mechanical_user")
	val REDSTONE_CLOCK: DeferredBlock<RedstoneClockBlock> =
		registerBlock("redstone_clock", ::RedstoneClockBlock)

	// Other FE Stuff

	val WIRELESS_FE_BATTERY: DeferredBlock<WirelessFeBatteryBlock> =
		registerBlock("wireless_fe_battery", ::WirelessFeBatteryBlock)
	val WIRELESS_FE_TRANSMITTER: DeferredBlock<WirelessFeTransmitterBlock> =
		registerBlock("wireless_fe_transmitter", ::WirelessFeTransmitterBlock)

	// Machines
	val RESONATOR: DeferredBlock<ResonatorBlock> =
		registerBlock("resonator", ::ResonatorBlock)
	val ENDER_THERMIC_PUMP =
		basicBlock("ender_thermic_pump")
	val QED: DeferredBlock<QedBlock> =
		registerBlock("qed", ::QedBlock)
	val ENDER_FLUX_CRYSTAL: DeferredBlock<EnderFluxCrystalBlock> =
		registerBlock("ender_flux_crystal", ::EnderFluxCrystalBlock)
	val MACHINE_BLOCK =
		basicBlock("machine_block")
	val CRUSHER =
		basicBlock("crusher")
	val FURNACE: DeferredBlock<EUFurnaceBlock> =
		registerBlock("furnace", ::EUFurnaceBlock)
	val ENCHANTER =
		basicBlock("enchanter")
	val QUANTUM_QUARRY: DeferredBlock<QuantumQuarryBlock> =
		registerBlock("quantum_quarry", ::QuantumQuarryBlock)
	val QUANTUM_QUARRY_ACTUATOR: DeferredBlock<QuantumQuarryActuatorBlock> =
		registerBlock("quantum_quarry_actuator", ::QuantumQuarryActuatorBlock)
	val ENDER_QUARRY: DeferredBlock<EnderQuarryBlock> =
		registerBlock("ender_quarry", ::EnderQuarryBlock)
	val ENDER_MARKER: DeferredBlock<EnderMarkerBlock> =
		registerBlock("ender_marker", ::EnderMarkerBlock)

	// Upgrades

	val ENDER_QUARRY_UPGRADE_BASE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_upgrade_base") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.NONE) }
	val ENDER_QUARRY_FORTUNE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_fortune_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.FORTUNE_ONE) }
	val ENDER_QUARRY_FORTUNE_TWO_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_fortune_two_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.FORTUNE_TWO) }
	val ENDER_QUARRY_FORTUNE_THREE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_fortune_three_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.FORTUNE_THREE) }
	val ENDER_QUARRY_SILK_TOUCH_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_silk_touch_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SILK_TOUCH) }
	val ENDER_QUARRY_SPEED_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_speed_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SPEED_ONE) }
	val ENDER_QUARRY_SPEED_TWO_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_speed_two_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SPEED_TWO) }
	val ENDER_QUARRY_SPEED_THREE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_speed_three_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SPEED_THREE) }
	val ENDER_QUARRY_WORLD_HOLE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_world_hole_upgrade") { EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.WORLD_HOLE) }

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
				properties = BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)
			)
		}

	// Storage

	val SLIGHTLY_LARGER_CHEST: DeferredBlock<SlightlyLargerChestBlock> =
		registerBlock("slightly_larger_chest", ::SlightlyLargerChestBlock)
	val MINI_CHEST: DeferredBlock<MiniChestBlock> =
		registerBlock("mini_chest", ::MiniChestBlock)
	val PLAYER_CHEST =
		basicBlock("player_chest")
	val FILING_CABINET: DeferredBlock<FilingCabinetBlock> =
		registerBlock("filing_cabinet", ::FilingCabinetBlock)
	val ADVANCED_FILING_CABINET: DeferredBlock<FilingCabinetBlock> =
		registerBlock("advanced_filing_cabinet", ::FilingCabinetBlock)
	val STONE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("stone_drum", ::DrumBlock)
	val IRON_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("iron_drum", ::DrumBlock)
	val REINFORCED_LARGE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("reinforced_large_drum", ::DrumBlock)
	val DEMONICALLY_GARGANTUAN_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("demonically_gargantuan_drum", ::DrumBlock)
	val BEDROCKIUM_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("bedrockium_drum", ::DrumBlock)
	val CREATIVE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("creative_drum", ::DrumBlock)
	val CREATIVE_CHEST: DeferredBlock<CreativeChestBlock> =
		registerBlock("creative_chest", ::CreativeChestBlock)
	val TRASH_CAN: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can") { TrashCanBlock(TrashCanBlock.Type.ITEM) }
	val TRASH_CAN_ENERGY: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can_energy") { TrashCanBlock(TrashCanBlock.Type.ENERGY) }
	val TRASH_CAN_FLUID: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can_fluid") { TrashCanBlock(TrashCanBlock.Type.FLUID) }
	val TRASH_CAN_CHEST: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can_chest") { TrashCanBlock(TrashCanBlock.Type.CHEST) }

	// Transfer

	val TRANSFER_PIPE: DeferredBlock<TransferPipeBlock> =
		registerBlock("transfer_pipe", ::TransferPipeBlock)

	//TODO
//	val RATIONING_PIPE: DeferredBlock<TransferPipeBlock> =
//		registerBlock("rationing_pipe", ::TransferPipeBlock)
//	val HYPER_RATIONING_PIPE: DeferredBlock<TransferPipeBlock> =
//		registerBlock("hyper_rationing_pipe", ::TransferPipeBlock)
	val ITEM_TRANSFER_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("item_transfer_node") { TransferNodeBlock(TransferNodeBlock.Type.ITEM, isRetrieval = false) }
	val FLUID_TRANSFER_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("fluid_transfer_node") { TransferNodeBlock(TransferNodeBlock.Type.FLUID, isRetrieval = false) }
	val ENERGY_TRANSFER_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("energy_transfer_node") { TransferNodeBlock(TransferNodeBlock.Type.ENERGY, isRetrieval = false) }
	val ITEM_RETRIEVAL_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("item_retrieval_node") { TransferNodeBlock(TransferNodeBlock.Type.ITEM, isRetrieval = true) }
	val FLUID_RETRIEVAL_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("fluid_retrieval_node") { TransferNodeBlock(TransferNodeBlock.Type.FLUID, isRetrieval = true) }
	val ENERGY_RETRIEVAL_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("energy_retrieval_node") { TransferNodeBlock(TransferNodeBlock.Type.ENERGY, isRetrieval = true) }
	val TRANSFER_FILTER =
		basicBlock("transfer_filter")
	val TRANSFER_PIPE_FILTER =
		basicBlock("transfer_pipe_filter")

	// FE Generators

	val CREATIVE_ENERGY_SOURCE: DeferredBlock<CreativeEnergySourceBlock> =
		registerBlock("creative_energy_source", ::CreativeEnergySourceBlock)
	val SURVIVAL_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("survival_generator") { GeneratorBlock { ModBlockEntityTypes.SURVIVAL_GENERATOR.get() } }
	val FURNACE_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("furnace_generator") { GeneratorBlock { ModBlockEntityTypes.FURNACE_GENERATOR.get() } }
	val MAGMATIC_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("magmatic_generator") { GeneratorBlock { ModBlockEntityTypes.MAGMATIC_GENERATOR.get() } }
	val ENDER_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("ender_generator") { GeneratorBlock { ModBlockEntityTypes.ENDER_GENERATOR.get() } }
	val HEATED_REDSTONE_GENERATOR =
		basicBlock("heated_redstone_generator")
	val CULINARY_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("culinary_generator") { GeneratorBlock { ModBlockEntityTypes.CULINARY_GENERATOR.get() } }
	val POTION_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("potion_generator") { GeneratorBlock { ModBlockEntityTypes.POTION_GENERATOR.get() } }
	val EXPLOSIVE_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("explosive_generator") { GeneratorBlock { ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get() } }
	val PINK_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("pink_generator") { GeneratorBlock { ModBlockEntityTypes.PINK_GENERATOR.get() } }
	val HIGH_TEMPERATURE_FURNACE_GENERATOR =
		basicBlock("high_temperature_furnace_generator")
	val NETHER_STAR_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("nether_star_generator") { GeneratorBlock { ModBlockEntityTypes.NETHER_STAR_GENERATOR.get() } }
	val DISENCHANTMENT_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("disenchantment_generator") { GeneratorBlock { ModBlockEntityTypes.DISENCHANTMENT_GENERATOR.get() } }
	val FROSTY_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("frosty_generator") { GeneratorBlock { ModBlockEntityTypes.FROSTY_GENERATOR.get() } }
	val HALITOSIS_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("halitosis_generator") { GeneratorBlock { ModBlockEntityTypes.HALITOSIS_GENERATOR.get() } }
	val SLIMY_GENERATOR =
		basicBlock("slimy_generator")
	val DEATH_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("death_generator") { GeneratorBlock { ModBlockEntityTypes.DEATH_GENERATOR.get() } }
	val RAINBOW_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("rainbow_generator") { GeneratorBlock { ModBlockEntityTypes.RAINBOW_GENERATOR.get() } }

	// GP Generators

	val MANUAL_MILL: DeferredBlock<ManualMillBlock> =
		registerBlock("manual_mill", ::ManualMillBlock)
	val WATER_MILL: DeferredBlock<WaterMillBlock> =
		registerBlock("water_mill", ::WaterMillBlock)
	val WIND_MILL: DeferredBlock<WindMillBlock> =
		registerBlock("wind_mill", ::WindMillBlock)
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

	val BLOCK_OF_BEDROCKIUM: DeferredBlock<Block> =
		basicCopiedBlock("block_of_bedrockium", Blocks.OBSIDIAN)
	val BLOCK_OF_UNSTABLE_INGOT: DeferredBlock<Block> =
		basicCopiedBlock("block_of_unstable_ingot", Blocks.IRON_BLOCK)
	val BLOCK_OF_EVIL_INFUSED_IRON: DeferredBlock<Block> =
		basicCopiedBlock("block_of_evil_infused_iron", Blocks.IRON_BLOCK)
	val BLOCK_OF_DEMON_METAL: DeferredBlock<Block> =
		registerBlockWithoutItem("block_of_demon_metal") { Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)) }
	val BLOCK_OF_ENCHANTED_METAL: DeferredBlock<Block> =
		basicCopiedBlock("block_of_enchanted_metal", Blocks.IRON_BLOCK)
	val ENDER_INFUSED_OBSIDIAN: DeferredBlock<Block> =
		basicCopiedBlock("ender_infused_obsidian", Blocks.OBSIDIAN)
	val POLISHED_STONE: DeferredBlock<Block> =
		basicStoneBlock("polished_stone")
	val BORDER_STONE: DeferredBlock<Block> =
		basicStoneBlock("border_stone")
	val CROSSED_STONE: DeferredBlock<Block> =
		basicStoneBlock("crossed_stone")
	val EMINENCE_STONE: DeferredBlock<Block> =
		basicStoneBlock("eminence_stone")
	val DIAMOND_ETCHED_COMPUTATIONAL_MATRIX: DeferredBlock<Block> =
		basicCopiedBlock("diamond_etched_computational_matrix", Blocks.DIAMOND_BLOCK)
	val EDGED_STONE_BRICKS: DeferredBlock<Block> =
		basicStoneBlock("edged_stone_bricks")
	val ENDER_SAND_ALLOY: DeferredBlock<Block> =
		basicCopiedBlock("ender_sand_alloy", Blocks.SANDSTONE)
	val FROSTED_STONE: DeferredBlock<Block> =
		basicStoneBlock("frosted_stone")
	val GLASS_BRICKS: DeferredBlock<TransparentBlock> =
		basicGlassBlock("glass_bricks")
	val GRAVEL_BRICKS: DeferredBlock<Block> =
		basicCopiedBlock("gravel_bricks", Blocks.GRAVEL)
	val GRAVEL_ROAD: DeferredBlock<Block> =
		basicCopiedBlock("gravel_road", Blocks.GRAVEL)
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
	val STONEBURNT: DeferredBlock<Block> =
		basicStoneBlock("stoneburnt")
	val TRUCHET: DeferredBlock<Block> =
		basicStoneBlock("truchet")
	val QUARTZBURNT: DeferredBlock<Block> =
		basicCopiedBlock("quartzburnt", Blocks.QUARTZ_BLOCK)
	val RAINBOW_STONE: DeferredBlock<Block> =
		basicStoneBlock("rainbow_stone")
	val MAGICAL_PLANKS: DeferredBlock<Block> =
		basicCopiedBlock("magical_planks", Blocks.OAK_PLANKS)
	val DIAGONAL_WOOD: DeferredBlock<Block> =
		basicCopiedBlock("diagonal_wood", Blocks.OAK_PLANKS)

	// Glass

	val CARVED_GLASS =
		basicGlassBlock("carved_glass")
	val EDGED_GLASS =
		basicGlassBlock("edged_glass")
	val GOLDEN_EDGED_GLASS =
		basicGlassBlock("golden_edged_glass")
	val GLOWING_GLASS =
		basicGlassBlock("glowing_glass")
	val HEART_GLASS =
		basicGlassBlock("heart_glass")
	val OBSIDIAN_GLASS =
		basicGlassBlock("obsidian_glass")
	val REINFORCED_DARK_GLASS =
		basicGlassBlock("reinforced_dark_glass")
	val SANDY_GLASS =
		basicBlock("sandy_glass")
	val SQUARE_GLASS =
		basicGlassBlock("square_glass")
	val SWIRLING_GLASS =
		basicGlassBlock("swirling_glass")
	val THICKENED_GLASS =
		basicGlassBlock("thickened_glass")
	val THICKENED_GLASS_BORDERED =
		basicGlassBlock("thickened_glass_bordered")
	val THICKENED_GLASS_PATTERNED =
		basicGlassBlock("thickened_glass_patterned")
	val REDSTONE_GLASS =
		basicGlassBlock("redstone_glass")
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

	val COLORED_STONES: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_stone", Blocks.STONE)
	val COLORED_COBBLESTONES: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_cobblestone", Blocks.COBBLESTONE)
	val COLORED_STONE_BRICKS: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_stone_bricks", Blocks.STONE_BRICKS)
	val COLORED_BRICKS: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_bricks", Blocks.BRICKS)
	val COLORED_COAL_BLOCKS: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_coal_block", Blocks.COAL_BLOCK)
	val COLORED_LAPIS_BLOCKS: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_lapis_block", Blocks.LAPIS_BLOCK)
	val COLORED_PLANKS: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_planks", Blocks.OAK_PLANKS)
	val COLORED_OBSIDIANS: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_obsidian", Blocks.OBSIDIAN)
	val COLORED_QUARTZES: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_quartz_block", Blocks.QUARTZ_BLOCK)
	val COLORED_GLOWSTONES: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("colored_glowstone", Blocks.GLOWSTONE)
	val COLORED_REDSTONE_BLOCKS: Map<DyeColor, DeferredBlock<PoweredBlock>> =
		getColorBlockMap("colored_redstone_block", Blocks.REDSTONE_BLOCK, ::PoweredBlock)
	val COLORED_REDSTONE_LAMPS: Map<DyeColor, DeferredBlock<RedstoneLampBlock>> =
		getColorBlockMap("colored_redstone_lamp", Blocks.REDSTONE_LAMP, ::RedstoneLampBlock)
	val COLORED_SOUL_SANDS: Map<DyeColor, DeferredBlock<SoulSandBlock>> =
		getColorBlockMap("colored_soul_sand", Blocks.SOUL_SAND, ::SoulSandBlock)

	fun getColoredStone(color: DyeColor): DeferredBlock<out Block> = COLORED_STONES.getValue(color)
	fun getColoredCobblestone(color: DyeColor): DeferredBlock<out Block> = COLORED_COBBLESTONES.getValue(color)
	fun getColoredStoneBricks(color: DyeColor): DeferredBlock<out Block> = COLORED_STONE_BRICKS.getValue(color)
	fun getColoredBricks(color: DyeColor): DeferredBlock<out Block> = COLORED_BRICKS.getValue(color)
	fun getColoredPlanks(color: DyeColor): DeferredBlock<out Block> = COLORED_PLANKS.getValue(color)
	fun getColoredCoalBlock(color: DyeColor): DeferredBlock<out Block> = COLORED_COAL_BLOCKS.getValue(color)
	fun getColoredRedstoneBlock(color: DyeColor): DeferredBlock<out Block> = COLORED_REDSTONE_BLOCKS.getValue(color)
	fun getColoredLapisBlock(color: DyeColor): DeferredBlock<out Block> = COLORED_LAPIS_BLOCKS.getValue(color)
	fun getColoredObsidian(color: DyeColor): DeferredBlock<out Block> = COLORED_OBSIDIANS.getValue(color)
	fun getColoredQuartz(color: DyeColor): DeferredBlock<out Block> = COLORED_QUARTZES.getValue(color)
	fun getColoredRedstoneLamp(color: DyeColor): DeferredBlock<out Block> = COLORED_REDSTONE_LAMPS.getValue(color)
	fun getColoredSoulSand(color: DyeColor): DeferredBlock<out Block> = COLORED_SOUL_SANDS.getValue(color)
	fun getColoredGlowstone(color: DyeColor): DeferredBlock<out Block> = COLORED_GLOWSTONES.getValue(color)

	private fun getColorBlockMap(
		name: String,
		blockToCopy: Block
	): Map<DyeColor, DeferredBlock<Block>> {
		return getColorBlockMap(name, blockToCopy) { properties -> Block(properties) }
	}

	private fun <T : Block> getColorBlockMap(
		name: String,
		blockToCopy: Block,
		blockConstructor: ((BlockBehaviour.Properties) -> T)
	): Map<DyeColor, DeferredBlock<T>> {
		return buildMap {
			for (color in DyeColor.entries) {
				val properties = BlockBehaviour.Properties
					.ofFullCopy(blockToCopy)
					.mapColor(color)

				put(
					color,
					registerBlock(
						"${name}_${color.serializedName}"
					) { blockConstructor(properties) }
				)
			}
		}
	}

	fun getColorFromColoredBlock(blockState: BlockState): DyeColor? {
		val allMaps = listOf(
			COLORED_STONES,
			COLORED_COBBLESTONES,
			COLORED_STONE_BRICKS,
			COLORED_BRICKS,
			COLORED_COAL_BLOCKS,
			COLORED_LAPIS_BLOCKS,
			COLORED_PLANKS,
			COLORED_OBSIDIANS,
			COLORED_QUARTZES,
			COLORED_REDSTONE_BLOCKS,
//			COLORED_REDSTONE_LAMPS,
			COLORED_SOUL_SANDS,
			COLORED_GLOWSTONES
		)

		for (map in allMaps) {
			for ((color, block) in map) {
				if (blockState.isBlock(block)) {
					return color
				}
			}
		}

		return null
	}

	// Blocks not reimplemented:
	// - Screen (Hard)
	// - Chunk Loading Ward (Use FTB Chunks or something)
	// - Ender Quarry Pump Upgrade (Use the Ender-Thermic Pump)
	// - Rain Muffler (Just mute it in your sound settings)
	// - Analog Crafter (Added to vanilla)

}