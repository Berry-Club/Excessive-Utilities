package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.feature.DepthsPillarFeature
import dev.aaronhowser.mods.excessive_utilities.feature.DepthsSculkVeinFeature
import dev.aaronhowser.mods.excessive_utilities.feature.DepthsStalactiteFeature
import dev.aaronhowser.mods.excessive_utilities.feature.RedOrchidFeature
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.Feature
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModFeatures {

	val FEATURE_REGISTRY: DeferredRegister<Feature<*>> =
		DeferredRegister.create(BuiltInRegistries.FEATURE, ExcessiveUtilities.MOD_ID)

	val DEPTHS_PILLAR: DeferredHolder<Feature<*>, DepthsPillarFeature> =
		FEATURE_REGISTRY.register("depths_pillar", ::DepthsPillarFeature)

	val DEPTHS_STALACTITE: DeferredHolder<Feature<*>, DepthsStalactiteFeature> =
		FEATURE_REGISTRY.register("depths_stalactite", ::DepthsStalactiteFeature)

	val DEPTHS_SCULK_VEIN: DeferredHolder<Feature<*>, DepthsSculkVeinFeature> =
		FEATURE_REGISTRY.register("depths_sculk_vein", ::DepthsSculkVeinFeature)

	val RED_ORCHID: DeferredHolder<Feature<*>, RedOrchidFeature> =
		FEATURE_REGISTRY.register("red_orchid", ::RedOrchidFeature)

}
