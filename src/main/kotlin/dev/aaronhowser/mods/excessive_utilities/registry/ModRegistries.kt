package dev.aaronhowser.mods.excessive_utilities.registry

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

	fun register(modBus: IEventBus) {
		val registries: List<DeferredRegister<*>> = listOf(
			ModItems.ITEM_REGISTRY,
		)

		registries.forEach { it.register(modBus) }
	}

}