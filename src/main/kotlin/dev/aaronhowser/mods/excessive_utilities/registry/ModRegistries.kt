package dev.aaronhowser.mods.excessive_utilities.registry

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

	fun register(modBus: IEventBus) {
		val registries: List<DeferredRegister<*>> = listOf(
			ModItems.ITEM_REGISTRY,
			ModBlocks.BLOCK_REGISTRY,
			ModCreativeModeTabs.TABS_REGISTRY,
			ModDataComponents.DATA_COMPONENT_REGISTRY
		)

		for (registry in registries) {
			registry.register(modBus)
		}
	}

}