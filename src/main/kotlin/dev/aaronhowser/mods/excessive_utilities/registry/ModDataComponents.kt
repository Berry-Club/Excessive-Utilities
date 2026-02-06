package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents : AaronDataComponentRegistry() {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExcessiveUtilities.MOD_ID)

	override fun getDataComponentRegistry(): DeferredRegister.DataComponents = DATA_COMPONENT_REGISTRY

	val RADIUS: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("radius")
	val ENTITY: DeferredHolder<DataComponentType<*>, DataComponentType<CustomData>> =
		register("entity", CustomData.CODEC, CustomData.STREAM_CODEC)
	val ENTITY_TYPE: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<EntityType<*>>>> =
		register(
			"entity_type",
			BuiltInRegistries.ENTITY_TYPE.holderByNameCodec(),
			ByteBufCodecs.holderRegistry(Registries.ENTITY_TYPE)
		)
	val ENERGY: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("energy")

}