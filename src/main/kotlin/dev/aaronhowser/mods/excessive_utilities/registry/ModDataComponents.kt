package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import net.minecraft.core.Holder
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.UUID

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
	val OPINIUM_CORE_CONTENTS: DeferredHolder<DataComponentType<*>, DataComponentType<OpiniumCoreContentsComponent>> =
		register("opinium_core_contents", OpiniumCoreContentsComponent.CODEC, OpiniumCoreContentsComponent.STREAM_CODEC)
	val MAGICAL_SNOW_GLOBE_PROGRESS: DeferredHolder<DataComponentType<*>, DataComponentType<MagicalSnowGlobeProgressComponent>> =
		register("magical_snow_globe_progress", MagicalSnowGlobeProgressComponent.CODEC, MagicalSnowGlobeProgressComponent.STREAM_CODEC)
	val TANK: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
		register("tank", SimpleFluidContent.CODEC, SimpleFluidContent.STREAM_CODEC)
	val IS_BROKEN: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
		unit("is_broken")
	val BAG_OF_HOLDING_ID: DeferredHolder<DataComponentType<*>, DataComponentType<UUID>> =
		register("bag", UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC)

}