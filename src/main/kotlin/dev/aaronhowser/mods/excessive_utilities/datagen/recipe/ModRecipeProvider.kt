package dev.aaronhowser.mods.excessive_utilities.datagen.recipe

import dev.aaronhowser.mods.aaron.datagen.AaronRecipeProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.asIngredient
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronRecipeProvider(output, lookupProvider) {

	override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
		buildShapedRecipes(recipeOutput)
		buildShapelessRecipes(recipeOutput)
		buildResonatorRecipes(recipeOutput)
	}

	private fun buildShapedRecipes(recipeOutput: RecipeOutput) {
		val recipes = listOf(
			shapedRecipe(
				ModBlocks.MAGICAL_SNOW_GLOBE.toStack(),
				"GSN,DLA,EW ",
				mapOf(
					'G' to ing(Tags.Items.GLASS_BLOCKS),
					'S' to ing(ItemTags.SAPLINGS),
					'N' to ing(Items.SNOWBALL),
					'D' to ing(ItemTags.WOODEN_DOORS),
					'L' to ing(ItemTags.LOGS),
					'A' to ing(Items.GRASS_BLOCK),
					'E' to ing(Tags.Items.ENDER_PEARLS),
					'W' to ing(Items.NETHER_STAR)
				)
			),
			shapedRecipe(
				ModItems.BUILDERS_WAND.toStack(),
				"  G, W ,W  ",
				mapOf(
					'G' to ing(Tags.Items.INGOTS_GOLD),
					'W' to ing(ModBlocks.MAGICAL_WOOD)
				)
			),
			shapedRecipe(
				ModItems.DESTRUCTION_WAND.toStack(),
				" GG, WG,W  ",
				mapOf(
					'G' to ing(Tags.Items.INGOTS_GOLD),
					'W' to ing(ModBlocks.MAGICAL_WOOD)
				)
			),
			shapedRecipe(
				ModBlocks.TRASH_CAN.toStack(),
				"SSS,CHC,CCC",
				mapOf(
					'S' to ing(Tags.Items.STONES),
					'H' to ing(Tags.Items.CHESTS_WOODEN),
					'C' to ing(Tags.Items.COBBLESTONES_NORMAL)
				)
			),
			shapedRecipe(
				ModBlocks.TRASH_CAN_FLUID.toStack(),
				"SSS,CBC,CCC",
				mapOf(
					'S' to ing(Tags.Items.STONES),
					'B' to ing(Items.BUCKET),
					'C' to ing(Tags.Items.COBBLESTONES_NORMAL)
				)
			),
			shapedRecipe(
				ModBlocks.TRASH_CAN_ENERGY.toStack(),
				"SSS,CRC,CCC",
				mapOf(
					'S' to ing(Tags.Items.STONES),
					'R' to ing(Tags.Items.STORAGE_BLOCKS_REDSTONE),
					'C' to ing(Tags.Items.COBBLESTONES_NORMAL)
				)
			),
			shapedRecipe(
				ModBlocks.ANGEL_BLOCK.toStack(),
				" G ,FOF",
				mapOf(
					'G' to ing(Tags.Items.INGOTS_GOLD),
					'F' to ing(Tags.Items.FEATHERS),
					'O' to ing(Tags.Items.OBSIDIANS_NORMAL)
				)
			),
			shapedRecipe(
				ModBlocks.SOLAR_PANEL.toStack(3),
				"LLL,PRP",
				mapOf(
					'L' to ing(Tags.Items.GEMS_LAPIS),
					'P' to ing(ModBlocks.POLISHED_STONE),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL)
				)
			),
			shapedRecipe(
				ModBlocks.LUNAR_PANEL.toStack(3),
				"LLL,PRP",
				mapOf(
					'L' to ing(ModItems.LUNAR_REACTIVE_DUST),
					'P' to ing(ModBlocks.POLISHED_STONE),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL)
				)
			)
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}
	}

	private fun buildShapelessRecipes(recipeOutput: RecipeOutput) {
		val recipes = listOf(
			shapelessRecipe(
				ModBlocks.COOLER,
				listOf(
					ing(ModBlocks.CLIMOGRAPH_BASE_UNIT),
					ing(Items.SNOWBALL),
					ing(Items.SNOWBALL),
				)
			),
			shapelessRecipe(
				ModBlocks.SOUND_MUFFLER,
				listOf(
					ing(ItemTags.WOOL),
					ing(Blocks.NOTE_BLOCK)
				)
			),
			shapelessRecipe(
				ModBlocks.MAGICAL_PLANKS,
				4,
				listOf(
					ing(ModBlocks.MAGICAL_WOOD)
				)
			)
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}
	}

	private fun buildResonatorRecipes(recipeOutput: RecipeOutput) {
		val recipes = listOf(
			ResonatorRecipeBuilder(
				ModBlocks.POLISHED_STONE.asIngredient(),
				ModBlocks.STONEBURNT.toStack(),
				8.0
			),
			ResonatorRecipeBuilder(
				Blocks.QUARTZ_BLOCK.asIngredient(),
				ModBlocks.QUARTZBURNT.toStack(),
				8.0
			),
			ResonatorRecipeBuilder(
				ModBlocks.STONEBURNT.asIngredient(),
				ModBlocks.RAINBOW_STONE.toStack(),
				64.0
			),
			ResonatorRecipeBuilder(
				Tags.Items.GEMS_LAPIS.asIngredient(),
				ModItems.LUNAR_REACTIVE_DUST.toStack(),
				16.0
			),
			ResonatorRecipeBuilder(
				ItemTags.COALS.asIngredient(),
				ModItems.RED_COAL.toStack(),
				16.0
			),
			ResonatorRecipeBuilder(
				Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.asIngredient(),
				ModItems.UPGRADE_BASE.toStack(),
				8.0
			),
			ResonatorRecipeBuilder(
				Blocks.IRON_BARS.asIngredient(),
				ModItems.WIRELESS_RF_HEATING_COIL.toStack(),
				16.0
			),
			ResonatorRecipeBuilder(
				ModBlocks.THICKENED_GLASS.asIngredient(),
				ModBlocks.REDSTONE_GLASS.toStack(),
				1.0
			)
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}
	}

}