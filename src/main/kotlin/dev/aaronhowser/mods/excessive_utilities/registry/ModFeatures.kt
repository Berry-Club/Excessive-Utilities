package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.feature.DeepDarkPillarFeature
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.Feature
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModFeatures {

	val FEATURE_REGISTRY: DeferredRegister<Feature<*>> =
		DeferredRegister.create(BuiltInRegistries.FEATURE, ExcessiveUtilities.MOD_ID)

	val DEEP_DARK_PILLAR: DeferredHolder<Feature<*>, DeepDarkPillarFeature> =
		FEATURE_REGISTRY.register("deep_dark_pillar", ::DeepDarkPillarFeature)

}