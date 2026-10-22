package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.*
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.*
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.MagmaticFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleItemFuelRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

	val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ExcessiveUtilities.MOD_ID)

	val RESONATOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<ResonatorRecipe>> =
		registerRecipeSerializer("resonator", { RecipeSerializer(ResonatorRecipe.Serializer.CODEC, ResonatorRecipe.Serializer.STREAM_CODEC) })
	val ENCHANTER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<EnchanterRecipe>> =
		registerRecipeSerializer("enchanter", { RecipeSerializer(EnchanterRecipe.Serializer.CODEC, EnchanterRecipe.Serializer.STREAM_CODEC) })
	val QED: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<QedRecipe>> =
		registerRecipeSerializer("qed", { RecipeSerializer(QedRecipe.Serializer.CODEC, QedRecipe.Serializer.STREAM_CODEC) })
	val CRUSHER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<CrusherRecipe>> =
		registerRecipeSerializer("crusher", { RecipeSerializer(CrusherRecipe.Serializer.CODEC, CrusherRecipe.Serializer.STREAM_CODEC) })
	val SHAPED_UNSTABLE: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<ShapedUnstableRecipe>> =
		registerRecipeSerializer("shaped_unstable", { RecipeSerializer(ShapedUnstableRecipe.Serializer.CODEC, ShapedUnstableRecipe.Serializer.STREAM_CODEC) })
	val SHAPED_DIVISION: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<ShapedDivisionRecipe>> =
		registerRecipeSerializer("shaped_division", { RecipeSerializer(ShapedDivisionRecipe.Serializer.CODEC, ShapedDivisionRecipe.Serializer.STREAM_CODEC) })
	val DAMAGE_GLASS_CUTTER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<DamageGlassCutterRecipe>> =
		registerRecipeSerializer("damage_glass_cutter", { RecipeSerializer(DamageGlassCutterRecipe.Serializer.CODEC, DamageGlassCutterRecipe.Serializer.STREAM_CODEC) })
	val KEEP_PAINTBRUSH: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<KeepPaintbrushRecipe>> =
		registerRecipeSerializer("keep_paintbrush", { RecipeSerializer(KeepPaintbrushRecipe.Serializer.CODEC, KeepPaintbrushRecipe.Serializer.STREAM_CODEC) })
	val SNOW_GLOBE: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<SnowGlobeRecipe>> =
		registerRecipeSerializer("snow_globe", { RecipeSerializer(SnowGlobeRecipe.Serializer.CODEC, SnowGlobeRecipe.Serializer.STREAM_CODEC) })

	val WORLD_INTERACTION_ITEM: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<WorldInteractionItemRecipe>> =
		registerRecipeSerializer("world_interaction_item", { RecipeSerializer(WorldInteractionItemRecipe.Serializer.CODEC, WorldInteractionItemRecipe.Serializer.STREAM_CODEC) })
	val WORLD_INTERACTION_FLUID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<WorldInteractionFluidRecipe>> =
		registerRecipeSerializer("world_interaction_fluid", { RecipeSerializer(WorldInteractionFluidRecipe.Serializer.CODEC, WorldInteractionFluidRecipe.Serializer.STREAM_CODEC) })

	// Generator fuels
	val SINGLE_ITEM_FUEL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<SingleItemFuelRecipe>> =
		registerRecipeSerializer("single_item_fuel", { RecipeSerializer(SingleItemFuelRecipe.Serializer.CODEC, SingleItemFuelRecipe.Serializer.STREAM_CODEC) })
	val MAGMATIC_FUEL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<MagmaticFuelRecipe>> =
		registerRecipeSerializer("magmatic_fuel", { RecipeSerializer(MagmaticFuelRecipe.Serializer.CODEC, MagmaticFuelRecipe.Serializer.STREAM_CODEC) })
	val ITEM_AND_FLUID_FUEL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<ItemAndFluidFuelRecipe>> =
		registerRecipeSerializer("item_and_fluid_fuel", { RecipeSerializer(ItemAndFluidFuelRecipe.Serializer.CODEC, ItemAndFluidFuelRecipe.Serializer.STREAM_CODEC) })

	private fun <T : Recipe<*>> registerRecipeSerializer(
		name: String,
		factory: () -> RecipeSerializer<T>
	): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<T>> {
		return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
	}

}