package dev.aaronhowser.mods.excessive_utilities.datagen.recipe

import dev.aaronhowser.mods.aaron.datagen.AaronRecipeProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.asIngredient
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
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
			),
			shapedRecipe(
				ModBlocks.LAVA_MILL.toStack(),
				"SSS,SRS,SGS",
				mapOf(
					'S' to ing(ModBlocks.STONEBURNT),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL),
					'G' to ing(Tags.Items.INGOTS_GOLD)
				)
			),
			shapedRecipe(
				ModBlocks.WATER_MILL.toStack(),
				"SSS,GRG,SSS",
				mapOf(
					'S' to ing(ModBlocks.STONEBURNT),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL),
					'G' to ing(ModItems.REDSTONE_GEAR)
				)
			),
			shapedRecipe(
				ModBlocks.WIND_MILL.toStack(),
				"SSS, GR,SSS",
				mapOf(
					'S' to ing(ModBlocks.STONEBURNT),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL),
					'G' to ing(ModItems.REDSTONE_GEAR)
				)
			),
			shapedRecipe(
				ModBlocks.FIRE_MILL.toStack(),
				"SRS,SGS,SFS",
				mapOf(
					'S' to ing(ModBlocks.STONEBURNT),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL),
					'G' to ing(ModItems.REDSTONE_GEAR),
					'F' to ing(Tags.Items.FENCES_NETHER_BRICK)
				)
			),
			shapedRecipe(
				ModBlocks.MANUAL_MILL.toStack(),
				" G ,SRS",
				mapOf(
					'G' to ing(ModItems.REDSTONE_GEAR),
					'S' to ing(ModBlocks.POLISHED_STONE),
					'R' to ing(ModItems.RESONATING_REDSTONE_CRYSTAL)
				)
			),
			shapedRecipe(
				ModBlocks.DRAGON_EGG_MILL.toStack(),
				"SGS,NGN,SIS",
				mapOf(
					'S' to ing(ModBlocks.STONEBURNT),
					'G' to ing(ModItems.REDSTONE_GEAR),
					'N' to ing(Items.NETHER_STAR),
					'I' to ing(ModItems.EYE_OF_REDSTONE)
				)
			),
			shapedRecipe(
				ModBlocks.REDSTONE_CLOCK.toStack(),
				"SRS,RTR,SRS",
				mapOf(
					'S' to ing(Tags.Items.STONES),
					'R' to ing(Tags.Items.DUSTS_REDSTONE),
					'T' to ing(Blocks.REDSTONE_TORCH)
				)
			),
			shapedRecipe(
				ModItems.GLASS_CUTTER.toStack(),
				"  I, SI,I  ",
				mapOf(
					'I' to ing(Tags.Items.INGOTS_IRON),
					'S' to ing(Tags.Items.RODS_WOODEN)
				)
			)
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}

		fun sickle(output: ItemLike, material: TagKey<Item>): ShapedRecipeBuilder {
			return shapedRecipe(
				output,
				" MM,  M,SMM",
				mapOf(
					'M' to ing(material),
					'S' to ing(Tags.Items.RODS_WOODEN)
				)
			)
		}

		val sickles = listOf(
			sickle(ModItems.WOODEN_SICKLE, ItemTags.PLANKS),
			sickle(ModItems.STONE_SICKLE, Tags.Items.STONES),
			sickle(ModItems.IRON_SICKLE, Tags.Items.INGOTS_IRON),
			sickle(ModItems.GOLDEN_SICKLE, Tags.Items.INGOTS_GOLD),
			sickle(ModItems.DIAMOND_SICKLE, Tags.Items.GEMS_DIAMOND),
			sickle(ModItems.NETHERITE_SICKLE, Tags.Items.INGOTS_NETHERITE)
		)

		for (recipe in sickles) {
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
			),
			shapelessRecipe(
				ModItems.ENDER_SHARD,
				8,
				listOf(
					ing(Tags.Items.ENDER_PEARLS),
					ing(ModItems.GLASS_CUTTER)    // TODO: Damage the glass cutter
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