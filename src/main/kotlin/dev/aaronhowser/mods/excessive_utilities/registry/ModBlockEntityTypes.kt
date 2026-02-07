package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockEntityTypeRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlockEntityTypes : AaronBlockEntityTypeRegistry() {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,
			ExcessiveUtilities.MOD_ID
		)

	override fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>> = BLOCK_ENTITY_REGISTRY

	val MAGNUM_TORCH: DeferredHolder<BlockEntityType<*>, BlockEntityType<MagnumTorchBlockEntity>> =
		register("magnum_torch", ::MagnumTorchBlockEntity, ModBlocks.MAGNUM_TORCH)
	val CHANDELIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<ChandelierBlockEntity>> =
		register("chandelier", ::ChandelierBlockEntity, ModBlocks.CHANDELIER)
	val PEACEFUL_TABLE: DeferredHolder<BlockEntityType<*>, BlockEntityType<PeacefulTableBlockEntity>> =
		register("peaceful_table", ::PeacefulTableBlockEntity, ModBlocks.PEACEFUL_TABLE)
	val WIRELESS_FE_BATTERY: DeferredHolder<BlockEntityType<*>, BlockEntityType<WirelessFeBatteryBlockEntity>> =
		register("wireless_fe_battery", ::WirelessFeBatteryBlockEntity, ModBlocks.WIRELESS_FE_BATTERY)
	val WIRELESS_FE_TRANSMITTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<WirelessFeTransmitterBlockEntity>> =
		register("wireless_fe_transmitter", ::WirelessFeTransmitterBlockEntity, ModBlocks.WIRELESS_FE_TRANSMITTER)

	val MANUAL_MILL: DeferredHolder<BlockEntityType<*>, BlockEntityType<ManualMillBlockEntity>> =
		register("manual_mill", ::ManualMillBlockEntity, ModBlocks.MANUAL_MILL)
	val GP_PANEL: DeferredHolder<BlockEntityType<*>, BlockEntityType<GpPanelBlockEntity>> =
		register("gp_panel", ::GpPanelBlockEntity, ModBlocks.SOLAR_PANEL)
	val LAVA_MILL: DeferredHolder<BlockEntityType<*>, BlockEntityType<LavaMillBlockEntity>> =
		register("lava_mill", ::LavaMillBlockEntity, ModBlocks.LAVA_MILL)
	val FIRE_MILL: DeferredHolder<BlockEntityType<*>, BlockEntityType<FireMillBlockEntity>> =
		register("fire_mill", ::FireMillBlockEntity, ModBlocks.FIRE_MILL)
	val WATER_MILL: DeferredHolder<BlockEntityType<*>, BlockEntityType<WaterMillBlockEntity>> =
		register("water_mill", ::WaterMillBlockEntity, ModBlocks.WATER_MILL)
	val DRAGON_EGG_MILL: DeferredHolder<BlockEntityType<*>, BlockEntityType<DragonEggMillBlockEntity>> =
		register("dragon_egg_mill", ::DragonEggMillBlockEntity, ModBlocks.DRAGON_EGG_MILL)
	val CREATIVE_MILL: DeferredHolder<BlockEntityType<*>, BlockEntityType<CreativeMillBlockEntity>> =
		register("creative_mill", ::CreativeMillBlockEntity, ModBlocks.CREATIVE_MILL)

	val RESONATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<ResonatorBlockEntity>> =
		register("resonator", ::ResonatorBlockEntity, ModBlocks.RESONATOR)

	val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<DrumBlockEntity>> =
		register(
			"drum",
			::DrumBlockEntity,
			ModBlocks.STONE_DRUM, ModBlocks.IRON_DRUM, ModBlocks.REINFORCED_LARGE_DRUM, ModBlocks.DEMONICALLY_GARGANTUAN_DRUM, ModBlocks.CREATIVE_DRUM
		)

	val ENDER_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("ender_generator", DataDrivenGeneratorBlockEntity::ender, ModBlocks.ENDER_GENERATOR)
	val ENDER_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("ender_generator_eight", DataDrivenGeneratorBlockEntity::enderEight, ModBlocks.ENDER_GENERATOR_X8)
	val ENDER_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("ender_generator_sixty_four", DataDrivenGeneratorBlockEntity::enderSixtyFour, ModBlocks.ENDER_GENERATOR_X64)

	val EXPLOSIVE_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("explosive_generator", DataDrivenGeneratorBlockEntity::explosive, ModBlocks.EXPLOSIVE_GENERATOR)
	val EXPLOSIVE_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("explosive_generator_eight", DataDrivenGeneratorBlockEntity::explosiveEight, ModBlocks.EXPLOSIVE_GENERATOR_X8)
	val EXPLOSIVE_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("explosive_generator_sixty_four", DataDrivenGeneratorBlockEntity::explosiveSixtyFour, ModBlocks.EXPLOSIVE_GENERATOR_X64)

	val PINK_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("pink_generator", DataDrivenGeneratorBlockEntity::pink, ModBlocks.PINK_GENERATOR)
	val PINK_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("pink_generator_eight", DataDrivenGeneratorBlockEntity::pinkEight, ModBlocks.PINK_GENERATOR_X8)
	val PINK_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("pink_generator_sixty_four", DataDrivenGeneratorBlockEntity::pinkSixtyFour, ModBlocks.PINK_GENERATOR_X64)

	val NETHER_STAR_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("nether_star_generator", DataDrivenGeneratorBlockEntity::netherStar, ModBlocks.NETHER_STAR_GENERATOR)
	val NETHER_STAR_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("nether_star_generator_eight", DataDrivenGeneratorBlockEntity::netherStarEight, ModBlocks.NETHER_STAR_GENERATOR_X8)
	val NETHER_STAR_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("nether_star_generator_sixty_four", DataDrivenGeneratorBlockEntity::netherStarSixtyFour, ModBlocks.NETHER_STAR_GENERATOR_X64)

	val FROSTY_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("frosty_generator", DataDrivenGeneratorBlockEntity::frosty, ModBlocks.FROSTY_GENERATOR)
	val FROSTY_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("frosty_generator_eight", DataDrivenGeneratorBlockEntity::frostyEight, ModBlocks.FROSTY_GENERATOR_X8)
	val FROSTY_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("frosty_generator_sixty_four", DataDrivenGeneratorBlockEntity::frostySixtyFour, ModBlocks.FROSTY_GENERATOR_X64)

	val HALITOSIS_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("halitosis_generator", DataDrivenGeneratorBlockEntity::halitosis, ModBlocks.HALITOSIS_GENERATOR)
	val HALITOSIS_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("halitosis_generator_eight", DataDrivenGeneratorBlockEntity::halitosisEight, ModBlocks.HALITOSIS_GENERATOR_X8)
	val HALITOSIS_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("halitosis_generator_sixty_four", DataDrivenGeneratorBlockEntity::halitosisSixtyFour, ModBlocks.HALITOSIS_GENERATOR_X64)

	val DEATH_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("death_generator", DataDrivenGeneratorBlockEntity::death, ModBlocks.DEATH_GENERATOR)
	val DEATH_GENERATOR_EIGHT: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("death_generator_eight", DataDrivenGeneratorBlockEntity::deathEight, ModBlocks.DEATH_GENERATOR_X8)
	val DEATH_GENERATOR_SIXTY_FOUR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DataDrivenGeneratorBlockEntity>> =
		register("death_generator_sixty_four", DataDrivenGeneratorBlockEntity::deathSixtyFour, ModBlocks.DEATH_GENERATOR_X64)

}