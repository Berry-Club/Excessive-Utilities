package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModParticleTypes {

	val PARTICLE_TYPE_REGISTRY: DeferredRegister<ParticleType<*>> =
		DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ExcessiveUtilities.MOD_ID)

	val VOMIT: DeferredHolder<ParticleType<*>, SimpleParticleType> =
		PARTICLE_TYPE_REGISTRY.register("vomit", Supplier { SimpleParticleType(false) })

}
