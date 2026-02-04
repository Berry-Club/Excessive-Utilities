package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.DrumBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.MagnumTorchBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent

@EventBusSubscriber(
	modid = ExcessiveUtilities.MOD_ID
)
object CommonEvents {

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

	}

}