package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.*
import dev.aaronhowser.mods.excessive_utilities.block_entity.MechanicalMinerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.MechanicalUserBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.EnderQuarryUpgradeType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(ExcessiveUtilities.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	// Functional

	val ANGEL_BLOCK: DeferredBlock<AngelBlock> =
	registerBlockWithoutItem("angel_block", ::AngelBlock, properties = { BlockBehaviour.Properties.of().strength(0f, 1200f).mapColor(MapColor.COLOR_BLACK) })
	val BLACKOUT_CURTAIN: DeferredBlock<BlackoutCurtainBlock> =
	registerBlock("blackout_curtain", ::BlackoutCurtainBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL).noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never) })
	val CONVEYOR_BELT: DeferredBlock<ConveyorBeltBlock> =
	registerBlock("conveyor_belt", ::ConveyorBeltBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val MAGICAL_WOOD: DeferredBlock<MagicalWoodBlock> =
	registerBlock("magical_wood", ::MagicalWoodBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val ENDER_CORE: DeferredBlock<EnderCoreBlock> =
	registerBlock("ender_core", ::EnderCoreBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val DEPTHS_PORTAL: DeferredBlock<DepthsPortalBLock> =
	registerBlock("depths_portal", ::DepthsPortalBLock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val LAST_MILLENNIUM_PORTAL: DeferredBlock<LastMillenniumPortalBlock> =
	registerBlock("last_millennium_portal", ::LastMillenniumPortalBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val MOON_STONE_ORE: DeferredBlock<MoonStoneOreBlock> =
		registerBlock("moon_stone_ore", ::MoonStoneOreBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE) })
	val DEEPSLATE_MOON_STONE_ORE: DeferredBlock<MoonStoneOreBlock> =
		registerBlock("deepslate_moon_stone_ore", ::MoonStoneOreBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE) })

	// Misc block entities

	val SOUND_MUFFLER: DeferredBlock<SoundMufflerBlock> =
	registerBlock("sound_muffler", ::SoundMufflerBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL) })
	val PEACEFUL_TABLE: DeferredBlock<PeacefulTableBlock> =
	registerBlock("peaceful_table", ::PeacefulTableBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE) })
	val CHANDELIER: DeferredBlock<ChandelierBlock> =
	registerBlock("chandelier", ::ChandelierBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH) })
	val MAGNUM_TORCH: DeferredBlock<MagnumTorchBlock> =
	registerBlock("magnum_torch", ::MagnumTorchBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).lightLevel { 14 } })
	val CURSED_EARTH: DeferredBlock<CursedEarthBlock> =
	registerBlock("cursed_earth", ::CursedEarthBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK) })
	val ENDER_COLLECTOR: DeferredBlock<EnderCollectorBlock> =
	registerBlock("ender_collector", ::EnderCollectorBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val TRADING_POST =
		basicBlock("trading_post")
	val ENDER_PORCUPINE: DeferredBlock<EnderPorcupineBlock> =
	registerBlock("ender_porcupine", ::EnderPorcupineBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val MAGICAL_SNOW_GLOBE: DeferredBlock<MagicalSnowGlobeBlock> =
	registerBlockWithoutItem("magical_snow_globe", ::MagicalSnowGlobeBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS) })
	val RESTURBED_MOB_SPAWNER: DeferredBlock<ResturbedMobSpawnerBlock> =
	registerBlock("resturbed_mob_spawner", ::ResturbedMobSpawnerBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER) })
	val CREATIVE_HARVEST: DeferredBlock<CreativeHarvestBlock> =
	registerBlock("creative_harvest", ::CreativeHarvestBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })

	// Crops

	//TODO: Spawn both in loot
	val RED_ORCHID: DeferredBlock<RedOrchidBlock> =
	registerBlockWithoutItem("red_orchid", ::RedOrchidBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT) })
	val ENDER_LILY: DeferredBlock<EnderLilyBlock> =
	registerBlockWithoutItem("ender_lily", ::EnderLilyBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT) })

	// Redstone stuff

	val REDSTONE_LANTERN: DeferredBlock<RedstoneLanternBlock> =
		registerBlock("redstone_lantern", ::RedstoneLanternBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val MECHANICAL_MINER: DeferredBlock<MechanicalInteractorBlock> =
		registerBlock("mechanical_miner", { properties ->
			MechanicalInteractorBlock(::MechanicalMinerBlockEntity, ModBlockEntityTypes.MECHANICAL_MINER::get, properties)
		}, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val MECHANICAL_USER: DeferredBlock<MechanicalInteractorBlock> =
		registerBlock("mechanical_user", { properties ->
			MechanicalInteractorBlock(::MechanicalUserBlockEntity, ModBlockEntityTypes.MECHANICAL_USER::get, properties)
		}, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val REDSTONE_CLOCK: DeferredBlock<RedstoneClockBlock> =
	registerBlock("redstone_clock", ::RedstoneClockBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val SCANNER =
		basicBlock("scanner")

	// Other FE Stuff

	val WIRELESS_FE_BATTERY: DeferredBlock<WirelessFeBatteryBlock> =
	registerBlock("wireless_fe_battery", ::WirelessFeBatteryBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK) })
	val WIRELESS_FE_TRANSMITTER: DeferredBlock<WirelessFeTransmitterBlock> =
	registerBlock("wireless_fe_transmitter", ::WirelessFeTransmitterBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK) })

	// Machines
	val RESONATOR: DeferredBlock<ResonatorBlock> =
	registerBlock("resonator", ::ResonatorBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK) })
	val ENDER_THERMIC_PUMP =
		basicBlock("ender_thermic_pump")
	val QED: DeferredBlock<QedBlock> =
	registerBlock("qed", ::QedBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val ENDER_FLUX_CRYSTAL: DeferredBlock<EnderFluxCrystalBlock> =
	registerBlock("ender_flux_crystal", ::EnderFluxCrystalBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val MACHINE_BLOCK: DeferredBlock<Block> =
		basicCopiedBlock("machine_block", Blocks.STONE)
	val CRUSHER: DeferredBlock<CrusherBlock> =
	registerBlock("crusher", ::CrusherBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val FURNACE: DeferredBlock<EUFurnaceBlock> =
	registerBlock("furnace", ::EUFurnaceBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val ENCHANTER: DeferredBlock<EnchanterBlock> =
	registerBlock("enchanter", ::EnchanterBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val QUANTUM_QUARRY: DeferredBlock<QuantumQuarryBlock> =
		registerBlock("quantum_quarry", ::QuantumQuarryBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val QUANTUM_QUARRY_ACTUATOR: DeferredBlock<QuantumQuarryActuatorBlock> =
		registerBlock("quantum_quarry_actuator", ::QuantumQuarryActuatorBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val ENDER_QUARRY: DeferredBlock<EnderQuarryBlock> =
		registerBlock("ender_quarry", ::EnderQuarryBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })
	val ENDER_MARKER: DeferredBlock<EnderMarkerBlock> =
	registerBlock("ender_marker", ::EnderMarkerBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })

	// Upgrades

	val ENDER_QUARRY_UPGRADE_BASE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_upgrade_base", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.NONE, properties) })
	val ENDER_QUARRY_FORTUNE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_fortune_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.FORTUNE_ONE, properties) })
	val ENDER_QUARRY_FORTUNE_TWO_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_fortune_two_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.FORTUNE_TWO, properties) })
	val ENDER_QUARRY_FORTUNE_THREE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_fortune_three_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.FORTUNE_THREE, properties) })
	val ENDER_QUARRY_SILK_TOUCH_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_silk_touch_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SILK_TOUCH, properties) })
	val ENDER_QUARRY_SPEED_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_speed_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SPEED_ONE, properties) })
	val ENDER_QUARRY_SPEED_TWO_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_speed_two_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SPEED_TWO, properties) })
	val ENDER_QUARRY_SPEED_THREE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_speed_three_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.SPEED_THREE, properties) })
	val ENDER_QUARRY_WORLD_HOLE_UPGRADE: DeferredBlock<EnderQuarryUpgradeBlock> =
		registerBlock("ender_quarry_world_hole_upgrade", { properties -> EnderQuarryUpgradeBlock(EnderQuarryUpgradeType.WORLD_HOLE, properties) })

	// Spikes

	val WOODEN_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("wooden_spike", { properties ->
			SpikeBlock(
				damagePerHit = 1f,
				canKill = false,
				properties = properties
			)
		})

	val STONE_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("stone_spike", { properties ->
			SpikeBlock(
				damagePerHit = 2f,
				properties = properties
			)
		})

	val IRON_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("iron_spike", { properties ->
			SpikeBlock(
				damagePerHit = 4f,
				properties = properties
			)
		})

	val GOLDEN_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("golden_spike", { properties ->
			SpikeBlock(
				damagePerHit = 2f,
				dropsExperience = true,
				properties = properties
			)
		})

	val DIAMOND_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("diamond_spike", { properties ->
			SpikeBlock(
				damagePerHit = 8f,
				dropsExperience = true,
				killsAsPlayer = true,
				properties = properties
			)
		})

	val NETHERITE_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("netherite_spike", { properties ->
			SpikeBlock(
				damagePerHit = 10f,
				dropsExperience = true,
				killsAsPlayer = true,
				properties = properties
			)
		})

	val CREATIVE_SPIKE: DeferredBlock<SpikeBlock> =
		registerBlock("creative_spike", { properties ->
			SpikeBlock(
				damagePerHit = Float.MAX_VALUE,
				dropsExperience = true,
				killsAsPlayer = true,
				properties = properties
			)
		})

	// Storage

	val SLIGHTLY_LARGER_CHEST: DeferredBlock<SlightlyLargerChestBlock> =
	registerBlock("slightly_larger_chest", ::SlightlyLargerChestBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST) })
	val MINI_CHEST: DeferredBlock<MiniChestBlock> =
	registerBlock("mini_chest", ::MiniChestBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST) })
	val PLAYER_CHEST =
		basicBlock("player_chest")
	val FILING_CABINET: DeferredBlock<FilingCabinetBlock> =
	registerBlock("filing_cabinet", ::FilingCabinetBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val ADVANCED_FILING_CABINET: DeferredBlock<FilingCabinetBlock> =
	registerBlock("advanced_filing_cabinet", ::FilingCabinetBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val STONE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("stone_drum", ::DrumBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val IRON_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("iron_drum", ::DrumBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val REINFORCED_LARGE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("reinforced_large_drum", ::DrumBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val DEMONICALLY_GARGANTUAN_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("demonically_gargantuan_drum", ::DrumBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val BEDROCKIUM_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("bedrockium_drum", ::DrumBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val CREATIVE_DRUM: DeferredBlock<DrumBlock> =
		registerBlock("creative_drum", ::DrumBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
	val CREATIVE_CHEST: DeferredBlock<CreativeChestBlock> =
	registerBlock("creative_chest", ::CreativeChestBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val TRASH_CAN: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can", { properties -> TrashCanBlock(TrashCanBlock.Type.ITEM, properties) })
	val TRASH_CAN_ENERGY: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can_energy", { properties -> TrashCanBlock(TrashCanBlock.Type.ENERGY, properties) })
	val TRASH_CAN_FLUID: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can_fluid", { properties -> TrashCanBlock(TrashCanBlock.Type.FLUID, properties) })
	val TRASH_CAN_CHEST: DeferredBlock<TrashCanBlock> =
		registerBlock("trash_can_chest", { properties -> TrashCanBlock(TrashCanBlock.Type.CHEST, properties) })

	// Transfer

	val TRANSFER_PIPE: DeferredBlock<TransferPipeBlock> =
	registerBlock("transfer_pipe", ::TransferPipeBlock, properties = { BlockBehaviour.Properties.of().strength(0.5f).noOcclusion() })

	//TODO
//	val RATIONING_PIPE: DeferredBlock<TransferPipeBlock> =
//		registerBlock("rationing_pipe", ::TransferPipeBlock)
//	val HYPER_RATIONING_PIPE: DeferredBlock<TransferPipeBlock> =
//		registerBlock("hyper_rationing_pipe", ::TransferPipeBlock)
	val ITEM_TRANSFER_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("item_transfer_node", { properties -> TransferNodeBlock(TransferNodeBlock.Type.ITEM, isRetrieval = false, properties = properties) })
	val FLUID_TRANSFER_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("fluid_transfer_node", { properties -> TransferNodeBlock(TransferNodeBlock.Type.FLUID, isRetrieval = false, properties = properties) })
	val ENERGY_TRANSFER_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("energy_transfer_node", { properties -> TransferNodeBlock(TransferNodeBlock.Type.ENERGY, isRetrieval = false, properties = properties) })
	val ITEM_RETRIEVAL_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("item_retrieval_node", { properties -> TransferNodeBlock(TransferNodeBlock.Type.ITEM, isRetrieval = true, properties = properties) })
	val FLUID_RETRIEVAL_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("fluid_retrieval_node", { properties -> TransferNodeBlock(TransferNodeBlock.Type.FLUID, isRetrieval = true, properties = properties) })
	val ENERGY_RETRIEVAL_NODE: DeferredBlock<TransferNodeBlock> =
		registerBlock("energy_retrieval_node", { properties -> TransferNodeBlock(TransferNodeBlock.Type.ENERGY, isRetrieval = true, properties = properties) })
	val TRANSFER_FILTER =
		basicBlock("transfer_filter")
	val TRANSFER_PIPE_FILTER =
		basicBlock("transfer_pipe_filter")
	val INDEXER =
		basicBlock("indexer")

	// FE Generators

	val CREATIVE_ENERGY_SOURCE: DeferredBlock<CreativeEnergySourceBlock> =
	registerBlock("creative_energy_source", ::CreativeEnergySourceBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val SURVIVALIST_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("survivalist_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.SURVIVALIST_GENERATOR.get() }, properties) })
	val FURNACE_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("furnace_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.FURNACE_GENERATOR.get() }, properties) })
	val MAGMATIC_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("magmatic_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.MAGMATIC_GENERATOR.get() }, properties) })
	val ENDER_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("ender_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.ENDER_GENERATOR.get() }, properties) })
	val HEATED_REDSTONE_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("heated_redstone_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get() }, properties) })
	val CULINARY_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("culinary_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.CULINARY_GENERATOR.get() }, properties) })
	val POTION_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("potion_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.POTION_GENERATOR.get() }, properties) })
	val EXPLOSIVE_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("explosive_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get() }, properties) })
	val PINK_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("pink_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.PINK_GENERATOR.get() }, properties) })
	val OVERCLOCKED_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("overclocked_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.OVERCLOCKED_GENERATOR.get() }, properties) })
	val NETHER_STAR_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("nether_star_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.NETHER_STAR_GENERATOR.get() }, properties) })
	val DISENCHANTMENT_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("disenchantment_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.DISENCHANTMENT_GENERATOR.get() }, properties) })
	val FROSTY_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("frosty_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.FROSTY_GENERATOR.get() }, properties) })
	val HALITOSIS_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("halitosis_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.HALITOSIS_GENERATOR.get() }, properties) })
	val SLIMY_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("slimy_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.SLIMY_GENERATOR.get() }, properties) })
	val DEATH_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("death_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.DEATH_GENERATOR.get() }, properties) })
	val RAINBOW_GENERATOR: DeferredBlock<GeneratorBlock> =
		registerBlock("rainbow_generator", { properties -> GeneratorBlock({ ModBlockEntityTypes.RAINBOW_GENERATOR.get() }, properties) })
	val RAINBOW_GENERATOR_TOP_SLAB: DeferredBlock<RainbowGeneratorSlabBlock> =
		registerBlock("rainbow_generator_top_slab", { properties -> RainbowGeneratorSlabBlock(isTop = true, properties = properties) })
	val RAINBOW_GENERATOR_BOTTOM_SLAB: DeferredBlock<RainbowGeneratorSlabBlock> =
		registerBlock("rainbow_generator_bottom_slab", { properties -> RainbowGeneratorSlabBlock(isTop = false, properties = properties) })

	// GP Generators

	val MANUAL_MILL: DeferredBlock<ManualMillBlock> =
	registerBlock("manual_mill", ::ManualMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion() })
	val WATER_MILL: DeferredBlock<WaterMillBlock> =
	registerBlock("water_mill", ::WaterMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val WIND_MILL: DeferredBlock<WindMillBlock> =
	registerBlock("wind_mill", ::WindMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion() })
	val FIRE_MILL: DeferredBlock<FireMillBlock> =
	registerBlock("fire_mill", ::FireMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion() })
	val LAVA_MILL: DeferredBlock<LavaMillBlock> =
	registerBlock("lava_mill", ::LavaMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val SOLAR_PANEL: DeferredBlock<GpPanelBlock> =
		registerBlock("solar_panel", { properties -> GpPanelBlock(requiresDay = true, properties = properties) })
	val LUNAR_PANEL: DeferredBlock<GpPanelBlock> =
		registerBlock("lunar_panel", { properties -> GpPanelBlock(requiresDay = false, properties = properties) })
	val DRAGON_EGG_MILL: DeferredBlock<DragonEggMillBlock> =
	registerBlock("dragon_egg_mill", ::DragonEggMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.STONE) })
	val CREATIVE_MILL: DeferredBlock<CreativeMillBlock> =
	registerBlock("creative_mill", ::CreativeMillBlock, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN) })

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
		registerBlockWithoutItem("block_of_demon_metal", { properties -> Block(properties) }, properties = { BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK) })
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
	val STONEBURNT: DeferredBlock<Block> =
		basicStoneBlock("stoneburnt")
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
	val PATTERNED_GLASS =
		basicGlassBlock("patterned_glass")
	val REDSTONE_GLASS =
		basicGlassBlock("redstone_glass")
	val ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("ethereal_glass", { properties -> SemiPermeableGlassBlock(isInverted = false, properties = properties) })
	val INVERTED_ETHEREAL_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("inverted_ethereal_glass", { properties -> SemiPermeableGlassBlock(isInverted = true, properties = properties) })
	val INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("ineffable_glass", { properties -> SemiPermeableGlassBlock(isInverted = false, properties = properties) })
	val INVERTED_INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("inverted_ineffable_glass", { properties -> SemiPermeableGlassBlock(isInverted = true, properties = properties) })
	val DARK_INEFFABLE_GLASS: DeferredBlock<SemiPermeableGlassBlock> =
		registerBlock("dark_ineffable_glass", { properties -> SemiPermeableGlassBlock.Dark(isInverted = false, properties = properties) })

	// Compressed Blocks

	val COMPRESSED_COBBLESTONES: List<DeferredBlock<CompressedBlock>> =
		compressedBlocks("cobblestone", Blocks.COBBLESTONE, baseExplosionResistance = 6f)
	val COMPRESSED_COBBLED_DEEPSLATES: List<DeferredBlock<CompressedBlock>> =
		compressedBlocks("cobbled_deepslate", Blocks.COBBLED_DEEPSLATE, baseExplosionResistance = 6f)
	val COMPRESSED_DIRTS: List<DeferredBlock<CompressedBlock>> =
		compressedBlocks("dirt", Blocks.DIRT, baseExplosionResistance = 0.5f)
	val COMPRESSED_SANDS: List<DeferredBlock<CompressedBlock>> =
		compressedBlocks("sand", Blocks.SAND, baseExplosionResistance = 0.5f)
	val COMPRESSED_GRAVELS: List<DeferredBlock<CompressedBlock>> =
		compressedBlocks("gravel", Blocks.GRAVEL, baseExplosionResistance = 0.6f)

	fun getCompressedCobblestone(level: Int): DeferredBlock<out CompressedBlock> = COMPRESSED_COBBLESTONES[level - 1]
	fun getCompressedCobbledDeepslate(level: Int): DeferredBlock<out CompressedBlock> = COMPRESSED_COBBLED_DEEPSLATES[level - 1]
	fun getCompressedDirt(level: Int): DeferredBlock<out CompressedBlock> = COMPRESSED_DIRTS[level - 1]
	fun getCompressedSand(level: Int): DeferredBlock<out CompressedBlock> = COMPRESSED_SANDS[level - 1]
	fun getCompressedGravel(level: Int): DeferredBlock<out CompressedBlock> = COMPRESSED_GRAVELS[level - 1]

	private fun compressedBlocks(baseName: String, baseBlock: Block, baseExplosionResistance: Float): List<DeferredBlock<CompressedBlock>> {
		return List(16) { i ->
			val level = i + 1

			val baseDestroyTime = baseBlock.defaultDestroyTime()

			val properties = BlockBehaviour.Properties
				.ofFullCopy(baseBlock)
				.destroyTime(baseDestroyTime * (level))
				.explosionResistance(baseExplosionResistance * (level))

			val name = "compressed_${baseName}_${level}"

			registerBlock(name, { blockProperties -> CompressedBlock(level, blockProperties) }, properties = { properties })
		}
	}

	// Colored blocks

	val LAPIS_CAELESTI: Map<DyeColor, DeferredBlock<Block>> =
		getColorBlockMap("lapis_caelestis", Blocks.LAPIS_BLOCK)
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

	fun getLapisCaelestis(color: DyeColor): DeferredBlock<out Block> = LAPIS_CAELESTI.getValue(color)
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
						"${name}_${color.serializedName}",
						blockConstructor,
						properties = { properties }
					)
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







