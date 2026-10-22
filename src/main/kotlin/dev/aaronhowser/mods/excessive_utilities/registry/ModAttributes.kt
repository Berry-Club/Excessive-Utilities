package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModAttributes {

	val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
		DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, ExcessiveUtilities.MOD_ID)

	val SOUL_RENDING: DeferredHolder<Attribute, RangedAttribute> =
		register("soul_rending", 0.0, 0.0, Double.MAX_VALUE)

	@Suppress("SameParameterValue")
	private fun register(
		name: String,
		default: Double,
		min: Double,
		max: Double
	): DeferredHolder<Attribute, RangedAttribute> {
		return ATTRIBUTE_REGISTRY.register(name, Supplier {
			RangedAttribute("attribute.name.excessive_utilities.$name", default, min, max)
		})
	}

}