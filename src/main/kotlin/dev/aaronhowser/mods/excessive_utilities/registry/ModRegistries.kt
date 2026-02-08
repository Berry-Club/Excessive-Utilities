package dev.aaronhowser.mods.excessive_utilities.registry

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {

	fun register(modBus: IEventBus) {
		val registries: List<DeferredRegister<*>> = listOf(
			ModItems.ITEM_REGISTRY,
			ModBlocks.BLOCK_REGISTRY,
			ModBlockEntityTypes.BLOCK_ENTITY_REGISTRY,
			ModCreativeModeTabs.TABS_REGISTRY,
			ModDataComponents.DATA_COMPONENT_REGISTRY,
			ModMobEffects.EFFECT_REGISTRY,
			ModRecipeTypes.RECIPE_TYPES_REGISTRY,
			ModRecipeSerializers.RECIPE_SERIALIZERS_REGISTRY
		)

		for (registry in registries) {
			registry.register(modBus)
		}
	}

}