package dev.aaronhowser.mods.excessive_utilities.registry

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry.QuantumQuarryBiomeSource
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.biome.BiomeSource
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBiomeSources {

	val BIOME_SOURCE_REGISTRY: DeferredRegister<MapCodec<out BiomeSource>> =
		DeferredRegister.create(BuiltInRegistries.BIOME_SOURCE, ExcessiveUtilities.MOD_ID)

	val QUANTUM_QUARRY: DeferredHolder<MapCodec<out BiomeSource>, MapCodec<QuantumQuarryBiomeSource>> =
		BIOME_SOURCE_REGISTRY.register("quantum_quarry", Supplier { QuantumQuarryBiomeSource.CODEC })

}