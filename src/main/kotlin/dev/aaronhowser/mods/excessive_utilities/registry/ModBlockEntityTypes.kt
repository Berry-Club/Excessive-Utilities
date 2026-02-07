package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockEntityTypeRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.*
import dev.aaronhowser.mods.excessive_utilities.block.base.generator.DataDrivenGeneratorBlockEntity
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

	val ENDER_GENERATOR =
		register("ender_generator", DataDrivenGeneratorBlockEntity::Ender, ModBlocks.ENDER_GENERATOR)

}