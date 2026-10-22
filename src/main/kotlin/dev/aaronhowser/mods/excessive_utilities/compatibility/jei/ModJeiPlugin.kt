package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.cast
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.CrusherJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.EnchanterJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.QedJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.ResonatorJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel.*
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension.ShapedDivisionRecipeExtension
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension.ShapedRecipeExtension
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension.ShapedUnstableRecipeExtension
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.AngelRingSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.DivisionSigilSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.LassoSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.OpiniumCoreSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.*
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.MagmaticFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleItemFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.*
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.fml.ModList

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun getPluginUid(): Identifier = ID

	override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
		registration.addCraftingStation(RecipeTypes.SMELTING, ModBlocks.FURNACE)
		registration.addCraftingStation(CRUSHER, ModBlocks.CRUSHER)
		registration.addCraftingStation(ENCHANTER, ModBlocks.ENCHANTER)
		registration.addCraftingStation(QED, ModBlocks.QED)
		registration.addCraftingStation(RESONATOR, ModBlocks.RESONATOR)

		SingleItemFuelJeiCategory.registerCatalysts(registration)
		ItemAndFluidFuelJeiCategory.registerCatalysts(registration)
		registration.addCraftingStation(MAGMATIC_FUELS, ModBlocks.MAGMATIC_GENERATOR)
		registration.addCraftingStation(CULINARY_FUELS, ModBlocks.CULINARY_GENERATOR)
		registration.addCraftingStation(FURNACE_FUELS, ModBlocks.FURNACE_GENERATOR)
		registration.addCraftingStation(SURVIVALIST_FUELS, ModBlocks.SURVIVALIST_GENERATOR)
		registration.addCraftingStation(OVERCLOCKED_FUELS, ModBlocks.OVERCLOCKED_GENERATOR)
		registration.addCraftingStation(POTION_FUELS, ModBlocks.POTION_GENERATOR)
		registration.addCraftingStation(DISENCHANTMENT_FUELS, ModBlocks.DISENCHANTMENT_GENERATOR)
	}

	override fun registerCategories(registration: IRecipeCategoryRegistration) {
		val guiHelper = registration.jeiHelpers.guiHelper

		registration.addRecipeCategories(EnchanterJeiCategory(ENCHANTER, guiHelper))
		registration.addRecipeCategories(ResonatorJeiCategory(RESONATOR, guiHelper))
		registration.addRecipeCategories(QedJeiCategory(QED, guiHelper))
		registration.addRecipeCategories(CrusherJeiCategory(CRUSHER, guiHelper))

		SingleItemFuelJeiCategory.registerCategory(registration)
		ItemAndFluidFuelJeiCategory.registerCategories(registration)
		DynamicItemFuelJeiCategory.registerCategories(registration)
		registration.addRecipeCategories(MagmaticFuelJeiCategory(MAGMATIC_FUELS, guiHelper))
		registration.addRecipeCategories(OverclockedFuelJeiCategory(OVERCLOCKED_FUELS, guiHelper))
		registration.addRecipeCategories(PotionFuelJeiCategory(POTION_FUELS, guiHelper))
		registration.addRecipeCategories(DisenchantmentFuelJeiCategory(DISENCHANTMENT_FUELS, guiHelper))
	}

	override fun registerRecipes(registration: IRecipeRegistration) {
		val level = AaronClientUtil.localLevel ?: return
		val recipes = ModRecipes.recipes(level)

		val enchanterRecipes = EnchanterRecipe.getAllRecipes(recipes)
		registration.addRecipes(ENCHANTER, enchanterRecipes)

		val crusherRecipes = CrusherRecipe
			.getAllRecipes(recipes)
		registration.addRecipes(CRUSHER, crusherRecipes)

		val qedRecipes = QedRecipe
			.getAllRecipes(recipes)
		registration.addRecipes(QED, qedRecipes)

		val resonatorRecipes = ResonatorRecipe.getAllRecipes(recipes)
		registration.addRecipes(RESONATOR, resonatorRecipes)

		SingleItemFuelJeiCategory.registerRecipes(level, registration)
		ItemAndFluidFuelJeiCategory.registerRecipes(level, registration)
		DynamicItemFuelJeiCategory.registerRecipes(level, registration)
		OverclockedFuelJeiCategory.registerRecipes(level, registration)

		val magmaticRecipes = MagmaticFuelRecipe.getAllRecipes(recipes)
		registration.addRecipes(MAGMATIC_FUELS, magmaticRecipes)

		val potionRecipes = PotionFuelJeiCategory.getAllRecipes(level, registration)
		registration.addRecipes(POTION_FUELS, potionRecipes)

		val disenchantmentRecipes = DisenchantmentFuelJeiCategory.getAllRecipes(level, registration)
		registration.addRecipes(DISENCHANTMENT_FUELS, disenchantmentRecipes)

		JeiInfoRecipes.addRecipes(registration)
	}

	override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
		val craftingCategory = registration.craftingCategory

		craftingCategory.addExtension(KeepPaintbrushRecipe::class.java, ShapedRecipeExtension<KeepPaintbrushRecipe>())
		craftingCategory.addExtension(DamageGlassCutterRecipe::class.java, ShapedRecipeExtension<DamageGlassCutterRecipe>())
		craftingCategory.addExtension(SnowGlobeRecipe::class.java, ShapedRecipeExtension<SnowGlobeRecipe>())
		craftingCategory.addExtension(ShapedUnstableRecipe::class.java, ShapedUnstableRecipeExtension())
		craftingCategory.addExtension(ShapedDivisionRecipe::class.java, ShapedDivisionRecipeExtension())
	}

	override fun registerItemSubtypes(registration: ISubtypeRegistration) {
		registration.registerSubtypeInterpreter(ModItems.OPINIUM_CORE.get(), OpiniumCoreSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.DIVISION_SIGIL.get(), DivisionSigilSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.ANGEL_RING.get(), AngelRingSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.GOLDEN_LASSO.get(), LassoSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.CURSED_LASSO.get(), LassoSubtypeInterpreter)
	}

	companion object {
		val ID: Identifier = Identifier.fromNamespaceAndPath(ExcessiveUtilities.MOD_ID, "jei_plugin")

		val CRUSHER: IRecipeType<RecipeHolder<CrusherRecipe>> = makeRecipeType("crusher")
		val ENCHANTER: IRecipeType<RecipeHolder<EnchanterRecipe>> = makeRecipeType("enchanter")
		val QED: IRecipeType<RecipeHolder<QedRecipe>> = makeRecipeType("qed")
		val RESONATOR: IRecipeType<RecipeHolder<ResonatorRecipe>> = makeRecipeType("resonator")

		val MAGMATIC_FUELS: IRecipeType<RecipeHolder<MagmaticFuelRecipe>> = makeRecipeType("generator_fuel/magmatic")
		val CULINARY_FUELS: IRecipeType<DynamicItemFuelJeiCategory.Recipe> =
			dynamicItemFuel("culinary")
		val FURNACE_FUELS: IRecipeType<DynamicItemFuelJeiCategory.Recipe> =
			dynamicItemFuel("furnace")
		val SURVIVALIST_FUELS: IRecipeType<DynamicItemFuelJeiCategory.Recipe> =
			dynamicItemFuel("survivalist")
		val OVERCLOCKED_FUELS: IRecipeType<OverclockedFuelJeiCategory.Recipe> =
			IRecipeType.create(ExcessiveUtilities.MOD_ID, "generator_fuel/overclocked", OverclockedFuelJeiCategory.Recipe::class.java)
		val POTION_FUELS: IRecipeType<PotionFuelJeiCategory.Recipe> =
			IRecipeType.create(ExcessiveUtilities.MOD_ID, "generator_fuel/potion", PotionFuelJeiCategory.Recipe::class.java)
		val DISENCHANTMENT_FUELS: IRecipeType<DisenchantmentFuelJeiCategory.Recipe> =
			IRecipeType.create(ExcessiveUtilities.MOD_ID, "generator_fuel/disenchantment", DisenchantmentFuelJeiCategory.Recipe::class.java)

		val SINGLE_ITEM_FUELS: Map<SingleItemFuelRecipe.GeneratorType, IRecipeType<RecipeHolder<SingleItemFuelRecipe>>> by lazy {
			buildMap {
				for (type in SingleItemFuelRecipe.GeneratorType.entries) {
					val name = "generator_fuel/single_item_fuel/${type.id}"
					set(type, makeRecipeType(name))
				}
			}
		}
		val ITEM_AND_FLUID_FUELS: Map<ItemAndFluidFuelRecipe.GeneratorType, IRecipeType<RecipeHolder<ItemAndFluidFuelRecipe>>> by lazy {
			buildMap {
				for (type in ItemAndFluidFuelRecipe.GeneratorType.entries) {
					val name = "generator_fuel/item_and_fluid_fuel/${type.id}"
					set(type, makeRecipeType(name))
				}
			}
		}

		private fun <T : Recipe<*>> makeRecipeType(id: String): IRecipeType<RecipeHolder<T>> {
			return IRecipeType.create(ExcessiveUtilities.MOD_ID, id, RecipeHolder::class.java).cast()
		}

		private fun dynamicItemFuel(id: String): IRecipeType<DynamicItemFuelJeiCategory.Recipe> {
			return IRecipeType.create(ExcessiveUtilities.MOD_ID, "generator_fuel/dynamic_item_fuel/$id", DynamicItemFuelJeiCategory.Recipe::class.java)
		}

		private fun shouldLoad(): Boolean = !ModList.get().isLoaded("emi")
	}

}
