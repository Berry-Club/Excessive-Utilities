package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.*
import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorItemFuel
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.packet.ModPacketHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent


@EventBusSubscriber(
	modid = ExcessiveUtilities.MOD_ID
)
object CommonEvents {

	@SubscribeEvent
	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		ModPacketHandler.registerPayloads(event)
	}

	@SubscribeEvent
	fun onMobSpawn(event: MobSpawnEvent.SpawnPlacementCheck) {
		MagnumTorchBlockEntity.handleSpawnEvent(event)
	}

	@SubscribeEvent
	fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {

		event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			ModBlockEntityTypes.DRUM.get(),
			DrumBlockEntity::getFluidCapability
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.RESONATOR.get(),
			ResonatorBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.ENCHANTER.get(),
			EnchanterBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(),
			WirelessFeBatteryBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.WIRELESS_FE_TRANSMITTER.get(),
			WirelessFeTransmitterBlockEntity::getEnergyCapability
		)

		val generators = listOf(
			ModBlockEntityTypes.ENDER_GENERATOR.get(),
			ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get(),
			ModBlockEntityTypes.PINK_GENERATOR.get(),
			ModBlockEntityTypes.NETHER_STAR_GENERATOR.get(),
			ModBlockEntityTypes.FROSTY_GENERATOR.get(),
			ModBlockEntityTypes.HALITOSIS_GENERATOR.get(),
			ModBlockEntityTypes.DEATH_GENERATOR.get(),
			ModBlockEntityTypes.CULINARY_GENERATOR.get()
		)

		for (beType in generators) {
			event.registerBlockEntity(
				Capabilities.EnergyStorage.BLOCK,
				beType,
				GeneratorBlockEntity::getEnergyCapability
			)

			event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				beType,
				GeneratorBlockEntity::getItemHandler
			)
		}

		event.registerItem(
			Capabilities.EnergyStorage.ITEM,
			HeatingCoilItem::getEnergyCapability,
			ModItems.HEATING_COIL.get()
		)

	}

	@SubscribeEvent
	fun afterServerTick(event: ServerTickEvent.Post) {
		val overworld = event.server.overworld()
		GridPowerHandler.get(overworld).tick(overworld)
	}

	@SubscribeEvent
	fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
		event.register(GeneratorItemFuel.ENDER)
		event.register(GeneratorItemFuel.EXPLOSIVE)
		event.register(GeneratorItemFuel.PINK)
		event.register(GeneratorItemFuel.NETHER_STAR)
		event.register(GeneratorItemFuel.FROSTY)
		event.register(GeneratorItemFuel.HALITOSIS)
		event.register(GeneratorItemFuel.DEATH)
	}

}