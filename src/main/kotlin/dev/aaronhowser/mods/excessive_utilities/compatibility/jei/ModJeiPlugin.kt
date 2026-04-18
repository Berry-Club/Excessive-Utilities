package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.cast
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.CrusherJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.EnchanterJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.QedJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.ResonatorJeiCategory
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel.*
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension.ShapedRecipeExtension
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.AngelRingSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.DivisionCoreSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.LassoSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype.OpiniumCoreSubtypeInterpreter
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.DamageGlassCutterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.KeepPaintbrushRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedDivisionRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedUnstableRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.SnowGlobeRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.MagmaticFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleItemFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.neoforged.fml.ModList

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun getPluginUid(): ResourceLocation = ID

	override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
		registration.addRecipeCatalyst(ModBlocks.FURNACE, RecipeTypes.SMELTING)
		registration.addRecipeCatalyst(ModBlocks.CRUSHER, CRUSHER)
		registration.addRecipeCatalyst(ModBlocks.ENCHANTER, ENCHANTER)
		registration.addRecipeCatalyst(ModBlocks.QED, QED)
		registration.addRecipeCatalyst(ModBlocks.RESONATOR, RESONATOR)

		SingleItemFuelJeiCategory.registerCatalysts(registration)
		ItemAndFluidFuelJeiCategory.registerCatalysts(registration)
		registration.addRecipeCatalyst(ModBlocks.MAGMATIC_GENERATOR, MAGMATIC_FUELS)
		registration.addRecipeCatalyst(ModBlocks.CULINARY_GENERATOR, CULINARY_FUELS)
		registration.addRecipeCatalyst(ModBlocks.FURNACE_GENERATOR, FURNACE_FUELS)
		registration.addRecipeCatalyst(ModBlocks.SURVIVALIST_GENERATOR, SURVIVALIST_FUELS)
		registration.addRecipeCatalyst(ModBlocks.OVERCLOCKED_GENERATOR, OVERCLOCKED_FUELS)
		registration.addRecipeCatalyst(ModBlocks.POTION_GENERATOR, POTION_FUELS)
		registration.addRecipeCatalyst(ModBlocks.DISENCHANTMENT_GENERATOR, DISENCHANTMENT_FUELS)
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

		val enchanterRecipes = EnchanterRecipe.getAllRecipes(level.recipeManager)
		registration.addRecipes(ENCHANTER, enchanterRecipes)

		val crusherRecipes = CrusherRecipe
			.getAllRecipes(level.recipeManager)
		registration.addRecipes(CRUSHER, crusherRecipes)

		val qedRecipes = QedRecipe
			.getAllRecipes(level.recipeManager)
		registration.addRecipes(QED, qedRecipes)

		val resonatorRecipes = ResonatorRecipe.getAllRecipes(level.recipeManager)
		registration.addRecipes(RESONATOR, resonatorRecipes)

		SingleItemFuelJeiCategory.registerRecipes(level, registration)
		ItemAndFluidFuelJeiCategory.registerRecipes(level, registration)
		DynamicItemFuelJeiCategory.registerRecipes(level, registration)
		OverclockedFuelJeiCategory.registerRecipes(level, registration)

		val magmaticRecipes = MagmaticFuelRecipe.getAllRecipes(level.recipeManager)
		registration.addRecipes(MAGMATIC_FUELS, magmaticRecipes)

		val potionRecipes = PotionFuelJeiCategory.getAllRecipes(level, registration)
		registration.addRecipes(POTION_FUELS, potionRecipes)

		val disenchantmentRecipes = DisenchantmentFuelJeiCategory.getAllRecipes(level, registration)
		registration.addRecipes(DISENCHANTMENT_FUELS, disenchantmentRecipes)

		JeiInfoRecipes.addRecipes(registration)
	}

	override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
		val craftingCategory = registration.craftingCategory

		craftingCategory.addExtension(KeepPaintbrushRecipe::class.java, ShapedRecipeExtension())
		craftingCategory.addExtension(ShapedDivisionRecipe::class.java, ShapedRecipeExtension())
		craftingCategory.addExtension(ShapedUnstableRecipe::class.java, ShapedRecipeExtension())
		craftingCategory.addExtension(DamageGlassCutterRecipe::class.java, ShapedRecipeExtension())
		craftingCategory.addExtension(SnowGlobeRecipe::class.java, ShapedRecipeExtension())
	}

	//FIXME: For some reason I can't see higher-tier Opinium Core recipes?
	override fun registerItemSubtypes(registration: ISubtypeRegistration) {
		registration.registerSubtypeInterpreter(ModItems.OPINIUM_CORE.get(), OpiniumCoreSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.DIVISION_SIGIL.get(), DivisionCoreSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.ANGEL_RING.get(), AngelRingSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.GOLDEN_LASSO.get(), LassoSubtypeInterpreter)
		registration.registerSubtypeInterpreter(ModItems.CURSED_LASSO.get(), LassoSubtypeInterpreter)
	}

	companion object {
		val ID: ResourceLocation = ResourceLocation.fromNamespaceAndPath(ExcessiveUtilities.MOD_ID, "jei_plugin")

		val CRUSHER: RecipeType<RecipeHolder<CrusherRecipe>> = makeRecipeType("crusher")
		val ENCHANTER: RecipeType<RecipeHolder<EnchanterRecipe>> = makeRecipeType("enchanter")
		val QED: RecipeType<RecipeHolder<QedRecipe>> = makeRecipeType("qed")
		val RESONATOR: RecipeType<RecipeHolder<ResonatorRecipe>> = makeRecipeType("resonator")

		val MAGMATIC_FUELS: RecipeType<RecipeHolder<MagmaticFuelRecipe>> = makeRecipeType("generator_fuel/magmatic")
		val CULINARY_FUELS: RecipeType<DynamicItemFuelJeiCategory.Recipe> =
			dynamicItemFuel("culinary")
		val FURNACE_FUELS: RecipeType<DynamicItemFuelJeiCategory.Recipe> =
			dynamicItemFuel("furnace")
		val SURVIVALIST_FUELS: RecipeType<DynamicItemFuelJeiCategory.Recipe> =
			dynamicItemFuel("survivalist")
		val OVERCLOCKED_FUELS: RecipeType<OverclockedFuelJeiCategory.Recipe> =
			makeRecipeType("generator_fuel/overclocked", OverclockedFuelJeiCategory.Recipe::class.java)
		val POTION_FUELS: RecipeType<PotionFuelJeiCategory.Recipe> =
			makeRecipeType("generator_fuel/potion", PotionFuelJeiCategory.Recipe::class.java)
		val DISENCHANTMENT_FUELS: RecipeType<DisenchantmentFuelJeiCategory.Recipe> =
			makeRecipeType("generator_fuel/disenchantment", DisenchantmentFuelJeiCategory.Recipe::class.java)

		val SINGLE_ITEM_FUELS: Map<SingleItemFuelRecipe.GeneratorType, RecipeType<RecipeHolder<SingleItemFuelRecipe>>> =
			buildMap {
				for (type in SingleItemFuelRecipe.GeneratorType.entries) {
					val name = "generator_fuel/single_item_fuel/${type.id}"
					set(type, makeRecipeType(name))
				}
			}
		val ITEM_AND_FLUID_FUELS: Map<ItemAndFluidFuelRecipe.GeneratorType, RecipeType<RecipeHolder<ItemAndFluidFuelRecipe>>> =
			buildMap {
				for (type in ItemAndFluidFuelRecipe.GeneratorType.entries) {
					val name = "generator_fuel/item_and_fluid_fuel/${type.id}"
					set(type, makeRecipeType(name))
				}
			}

		private fun <T : Recipe<*>> makeRecipeType(id: String): RecipeType<RecipeHolder<T>> {
			return RecipeType.create(ExcessiveUtilities.MOD_ID, id, RecipeHolder::class.java).cast()
		}

		private fun <T> makeRecipeType(id: String, recipeClass: Class<T>): RecipeType<T> {
			return RecipeType.create(ExcessiveUtilities.MOD_ID, id, recipeClass)
		}

		private fun dynamicItemFuel(id: String): RecipeType<DynamicItemFuelJeiCategory.Recipe> {
			return makeRecipeType("generator_fuel/dynamic_item_fuel/$id", DynamicItemFuelJeiCategory.Recipe::class.java)
		}

		private fun shouldLoad(): Boolean = !ModList.get().isLoaded("emi")
	}

}