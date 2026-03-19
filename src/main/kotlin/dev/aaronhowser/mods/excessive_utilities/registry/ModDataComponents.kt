package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import dev.aaronhowser.mods.excessive_utilities.item.component.FluidFilterFlagsComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.ItemFilterFlagsComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

object ModDataComponents : AaronDataComponentRegistry() {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ExcessiveUtilities.MOD_ID)

	override fun getDataComponentRegistry(): DeferredRegister.DataComponents = DATA_COMPONENT_REGISTRY

	val RADIUS: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("radius")
	val AMOUNT_BLOCKS: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("amount_blocks")
	val ENTITY_DATA: DeferredHolder<DataComponentType<*>, DataComponentType<CustomData>> =
		register("entity_data", CustomData.CODEC, CustomData.STREAM_CODEC)
	val ENTITY_TYPE: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<EntityType<*>>>> =
		registryHolder("entity_type", BuiltInRegistries.ENTITY_TYPE)
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
		uuid("bag")
	val CHARGE: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("charge")
	val OWNER: DeferredHolder<DataComponentType<*>, DataComponentType<UUID>> =
		uuid("owner")
	val COUNTDOWN: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("countdown")
	val THROWN_BOOMERANG: DeferredHolder<DataComponentType<*>, DataComponentType<UUID>> =
		uuid("thrown_boomerang")
	val BIOME: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<Biome>>> =
		register("biome", Biome.CODEC, ByteBufCodecs.holderRegistry(Registries.BIOME))
	val ITEM_FILTER_FLAGS: DeferredHolder<DataComponentType<*>, DataComponentType<ItemFilterFlagsComponent>> =
		register("item_filter_flags", ItemFilterFlagsComponent.CODEC, ItemFilterFlagsComponent.STREAM_CODEC)
	val FLUID_FILTER_FLAGS: DeferredHolder<DataComponentType<*>, DataComponentType<FluidFilterFlagsComponent>> =
		register("fluid_filter_flags", FluidFilterFlagsComponent.CODEC, FluidFilterFlagsComponent.STREAM_CODEC)
	val FREQUENCY: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("frequency")
	val COLOR: DeferredHolder<DataComponentType<*>, DataComponentType<DyeColor>> =
		register("color", DyeColor.CODEC, DyeColor.STREAM_CODEC)
	val ANGEL_RING_TYPE: DeferredHolder<DataComponentType<*>, DataComponentType<AngelRingItem.Type>> =
		register("angel_ring_type", AngelRingItem.Type.CODEC, AngelRingItem.Type.STREAM_CODEC)
	val REMAINING_USES: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		int("remaining_uses")
	val CRAFTED_IN_MENU: DeferredHolder<DataComponentType<*>, DataComponentType<ResourceLocation>> =
		register("crafted_in_menu", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC)

}