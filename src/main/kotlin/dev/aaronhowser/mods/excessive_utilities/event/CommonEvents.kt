package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.*
import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorFuelDataMap
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.packet.ModPacketHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
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
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(),
			WirelessFeBatteryBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.WIRELESS_FE_TRANSMITTER.get(),
			WirelessFeTransmitterBlockEntity::getEnergyCapability
		)

	}

	@SubscribeEvent
	fun afterServerTick(event: ServerTickEvent.Post) {
		val overworld = event.server.overworld()
		GridPowerHandler.get(overworld).tick(overworld)
	}

	@SubscribeEvent
	fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
		event.register(GeneratorFuelDataMap.ENDER)
		event.register(GeneratorFuelDataMap.EXPLOSIVE)
		event.register(GeneratorFuelDataMap.PINK)
		event.register(GeneratorFuelDataMap.NETHER_STAR)
		event.register(GeneratorFuelDataMap.FROSTY)
		event.register(GeneratorFuelDataMap.HALITOSIS)
		event.register(GeneratorFuelDataMap.DEATH)
	}

}