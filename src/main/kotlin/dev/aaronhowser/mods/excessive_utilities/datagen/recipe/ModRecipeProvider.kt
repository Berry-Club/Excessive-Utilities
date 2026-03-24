package dev.aaronhowser.mods.excessive_utilities.datagen.recipe

import dev.aaronhowser.mods.aaron.datagen.AaronRecipeProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.asIngredient
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withCount
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.crafting.SpecialShapedRecipeBuilder
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine.*
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine.generator_fuel.ItemAndFluidFuelRecipeBuilder
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine.generator_fuel.SingleFluidFuelRecipeBuilder
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine.generator_fuel.SingleItemFuelRecipeBuilder
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import dev.aaronhowser.mods.excessive_utilities.item.SunCrystalItem
import dev.aaronhowser.mods.excessive_utilities.item.UnstableIngotItem
import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import dev.aaronhowser.mods.excessive_utilities.recipe.base.BlockStateIngredient
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.DamageGlassCutterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.KeepPaintbrushRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedDivisionRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedUnstableRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleItemFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronRecipeProvider(output, lookupProvider) {

	private fun modLoc(name: String) = ExcessiveUtilities.modResource(name)

	override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
		buildGeneratorFuelRecipes(recipeOutput)
		buildShapedRecipes(recipeOutput, holderLookup)
		buildShapelessRecipes(recipeOutput)
		buildResonatorRecipes(recipeOutput)
		buildEnchanterRecipes(recipeOutput)
		buildNamedRecipes(recipeOutput)
		buildSmeltingRecipes(recipeOutput)
		buildQedRecipes(recipeOutput)
		buildWorldInteractionRecipes(recipeOutput)
		buildCrusherRecipes(recipeOutput)
		buildUnstableRecipes(recipeOutput)
		buildCompressedBlockRecipes(recipeOutput)
	}

	private fun buildShapedRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
		val perfectOpiniumCore = OpiniumCoreContentsComponent.getDefaultTiers().last().getStack()

		shapedRecipe(
			ModBlocks.SWIRLING_GLASS.toStack(5),
			" G ,GGG, G ",
			mapOf(
				'G' to ModBlocks.THICKENED_GLASS.asIngredient()
			)
		)

		shapedRecipe(
			ModItems.PAINTBRUSH,
			"S  , T ,  T",
			mapOf(
				'S' to Tags.Items.STRINGS.asIngredient(),
				'T' to Tags.Items.RODS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.WATERING_CAN,
			"IM ,IBI, I ",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'M' to Items.BONE_MEAL.asIngredient(),
				'B' to Items.BOWL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.REINFORCED_WATERING_CAN,
			"IS ,IBI, I ",
			mapOf(
				'I' to ModItems.BEDROCKIUM_INGOT.asIngredient(),
				'S' to ModItems.SOUL_FRAGMENT.asIngredient(),
				'B' to Items.BOWL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.PORTABLE_SCANNER,
			"III,ERI,III",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'E' to Items.ENDER_EYE.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.DEPTH_FIRST_SEARCH_UPGRADE,
			"SRR,SGS,GRR",
			mapOf(
				'S' to ModItemTagsProvider.SPEED_UPGRADES.asIngredient(),
				'R' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
				'G' to Tags.Items.INGOTS_GOLD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.BREADTH_FIRST_SEARCH_UPGRADE,
			"RRR,SDS,RRR",
			mapOf(
				'S' to ModItemTagsProvider.SPEED_UPGRADES.asIngredient(),
				'R' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
				'D' to ModItems.DEPTH_FIRST_SEARCH_UPGRADE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.BLACKOUT_CURTAIN.toStack(12),
			"WW,WW,WW",
			mapOf(
				'W' to ItemTags.WOOL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_CORE.toStack(),
			"WEW,EIE,WEW",
			mapOf(
				'W' to ModBlocks.MAGICAL_WOOD.asIngredient(),
				'E' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient(),
				'I' to Items.ENDER_EYE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_COLLECTOR,
			"EIE, I ,OOO",
			mapOf(
				'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
				'I' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient(),
				'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_THERMIC_PUMP,
			"ODO,LEW,OPO",
			mapOf(
				'O' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'L' to Items.LAVA_BUCKET.asIngredient(),
				'W' to Items.WATER_BUCKET.asIngredient(),
				'E' to Items.ENDER_EYE.asIngredient(),
				'P' to Items.IRON_PICKAXE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.QED,
			"ICI,EDE,EEE",
			mapOf(
				'I' to Items.ENDER_EYE.asIngredient(),
				'C' to Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES.asIngredient(),
				'E' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient(),
				'D' to ModBlocks.DIAMOND_ETCHED_COMPUTATIONAL_MATRIX.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.EDGED_STONE_BRICKS.toStack(9),
			"SBS,BBB,SBS",
			mapOf(
				'S' to Tags.Items.STONES.asIngredient(),
				'B' to Items.STONE_BRICKS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_SAND_ALLOY.toStack(4),
			"ES,SE",
			mapOf(
				'E' to Items.END_STONE.asIngredient(),
				'S' to Tags.Items.SANDSTONE_BLOCKS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FROSTED_STONE.toStack(5),
			" I ,ISI, I ",
			mapOf(
				'I' to Items.ICE.asIngredient(),
				'S' to Tags.Items.STONES.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.GLASS_BRICKS.toStack(4),
			"GG,GG",
			mapOf(
				'G' to ModBlocks.THICKENED_GLASS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.GRAVEL_BRICKS,
			"GG,GG",
			mapOf(
				'G' to Tags.Items.GRAVELS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.GRAVEL_ROAD.toStack(8),
			"SGS,GGG,SGS",
			mapOf(
				'S' to Items.STONE_BRICK_SLAB.asIngredient(),
				'G' to ModBlocks.GRAVEL_BRICKS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DIAGONAL_WOOD.toStack(5),
			"SP,PS",
			mapOf(
				'S' to ItemTags.WOODEN_STAIRS.asIngredient(),
				'P' to ItemTags.PLANKS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.GOLDEN_EDGED_GLASS,
			"NNN,NGN,NNN",
			mapOf(
				'N' to Tags.Items.NUGGETS_GOLD.asIngredient(),
				'G' to Tags.Items.GLASS_BLOCKS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.HEART_GLASS.toStack(6),
			"GPG,GGG, G ",
			mapOf(
				'G' to ModBlocks.THICKENED_GLASS.asIngredient(),
				'P' to Tags.Items.DYES_PINK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.OBSIDIAN_GLASS.toStack(4),
			"GOG,O O,GOG",
			mapOf(
				'G' to ModBlocks.THICKENED_GLASS.asIngredient(),
				'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.REINFORCED_DARK_GLASS.toStack(4),
			"GOG,O O,GOG",
			mapOf(
				'G' to Blocks.TINTED_GLASS.asIngredient(),
				'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.GOLDEN_LASSO,
			"GSG,S S,GSG",
			mapOf(
				'G' to Tags.Items.NUGGETS_GOLD.asIngredient(),
				'S' to Tags.Items.STRINGS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.RAINBOW_GENERATOR_TOP_SLAB,
			"123,456,789",
			mapOf(
				'1' to ModBlocks.CULINARY_GENERATOR.asIngredient(),
				'2' to ModBlocks.DEATH_GENERATOR.asIngredient(),
				'3' to ModBlocks.HALITOSIS_GENERATOR.asIngredient(),
				'4' to ModBlocks.DISENCHANTMENT_GENERATOR.asIngredient(),
				'5' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'6' to ModBlocks.ENDER_GENERATOR.asIngredient(),
				'7' to ModBlocks.FURNACE_GENERATOR.asIngredient(),
				'8' to ModBlocks.FROSTY_GENERATOR.asIngredient(),
				'9' to ModBlocks.MAGMATIC_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.RAINBOW_GENERATOR_BOTTOM_SLAB,
			"123,456,789",
			mapOf(
				'1' to ModBlocks.NETHER_STAR_GENERATOR.asIngredient(),
				'2' to ModBlocks.OVERCLOCKED_GENERATOR.asIngredient(),
				'3' to ModBlocks.PINK_GENERATOR.asIngredient(),
				'4' to ModBlocks.POTION_GENERATOR.asIngredient(),
				'5' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'6' to ModBlocks.HEATED_REDSTONE_GENERATOR.asIngredient(),
				'7' to ModBlocks.SLIMY_GENERATOR.asIngredient(),
				'8' to ModBlocks.SURVIVALIST_GENERATOR.asIngredient(),
				'9' to ModBlocks.EXPLOSIVE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.RAINBOW_GENERATOR,
			"T,B",
			mapOf(
				'T' to ModBlocks.RAINBOW_GENERATOR_TOP_SLAB.asIngredient(),
				'B' to ModBlocks.RAINBOW_GENERATOR_BOTTOM_SLAB.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_INFUSED_OBSIDIAN.toStack(4),
			" O ,OEO, O ",
			mapOf(
				'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient(),
				'E' to Items.ENDER_EYE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.QED.toStack(),
			"ECE,ODO,OOO",
			mapOf(
				'E' to Items.ENDER_EYE.asIngredient(),
				'C' to Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES.asIngredient(),
				'O' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient(),
				'D' to ModBlocks.DIAMOND_ETCHED_COMPUTATIONAL_MATRIX.asIngredient()
			)
		)

		shapedRecipe(
			ModBlocks.ENDER_FLUX_CRYSTAL.toStack(),
			" E , O ,OOO",
			mapOf(
				'E' to Items.ENDER_EYE.asIngredient(),
				'O' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.CONVEYOR_BELT.toStack(8),
			"RRR,IDI,RRR",
			mapOf(
				'R' to Items.RAIL.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'D' to Tags.Items.DUSTS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DIAMOND_ETCHED_COMPUTATIONAL_MATRIX.toStack(),
			"QDQ,DOD,QDQ",
			mapOf(
				'Q' to ModBlocks.QUARTZBURNT.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'O' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.CHANDELIER.toStack(),
			"GDG,TTT, T ",
			mapOf(
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'T' to Items.TORCH.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.MAGICAL_SNOW_GLOBE.toStack(),
			"GSN,DLA,EW ",
			mapOf(
				'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
				'S' to ItemTags.SAPLINGS.asIngredient(),
				'N' to Items.SNOWBALL.asIngredient(),
				'D' to ItemTags.WOODEN_DOORS.asIngredient(),
				'L' to ItemTags.LOGS.asIngredient(),
				'A' to Items.GRASS_BLOCK.asIngredient(),
				'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
				'W' to Items.NETHER_STAR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.BUILDERS_WAND.toStack(),
			"  G, W ,W  ",
			mapOf(
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'W' to ModBlocks.MAGICAL_WOOD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.DESTRUCTION_WAND.toStack(),
			" GG, WG,W  ",
			mapOf(
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'W' to ModBlocks.MAGICAL_WOOD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.TRASH_CAN.toStack(),
			"SSS,CHC,CCC",
			mapOf(
				'S' to Tags.Items.STONES.asIngredient(),
				'H' to Tags.Items.CHESTS_WOODEN.asIngredient(),
				'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.TRASH_CAN_FLUID.toStack(),
			"SSS,CBC,CCC",
			mapOf(
				'S' to Tags.Items.STONES.asIngredient(),
				'B' to Items.BUCKET.asIngredient(),
				'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.TRASH_CAN_ENERGY.toStack(),
			"SSS,CRC,CCC",
			mapOf(
				'S' to Tags.Items.STONES.asIngredient(),
				'R' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
				'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.ANGEL_BLOCK.toStack(),
			" G ,FOF",
			mapOf(
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'F' to Tags.Items.FEATHERS.asIngredient(),
				'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.SOLAR_PANEL.toStack(3),
			"LLL,PRP",
			mapOf(
				'L' to Tags.Items.GEMS_LAPIS.asIngredient(),
				'P' to ModBlocks.POLISHED_STONE.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.LUNAR_PANEL.toStack(3),
			"LLL,PRP",
			mapOf(
				'L' to ModItems.LUNAR_REACTIVE_DUST.asIngredient(),
				'P' to ModBlocks.POLISHED_STONE.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.LAVA_MILL.toStack(),
			"SSS,SRS,SGS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'G' to Tags.Items.INGOTS_GOLD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.WATER_MILL.toStack(),
			"SSS,GRG,SSS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'G' to ModItems.REDSTONE_GEAR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.WIND_MILL.toStack(),
			"SSS, GR,SSS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'G' to ModItems.REDSTONE_GEAR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FIRE_MILL.toStack(),
			"SRS,SGS,SFS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'G' to ModItems.REDSTONE_GEAR.asIngredient(),
				'F' to Tags.Items.FENCES_NETHER_BRICK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.MANUAL_MILL.toStack(),
			" G ,SRS",
			mapOf(
				'G' to ModItems.REDSTONE_GEAR.asIngredient(),
				'S' to ModBlocks.POLISHED_STONE.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DRAGON_EGG_MILL.toStack(),
			"SGS,NGN,SIS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'G' to ModItems.REDSTONE_GEAR.asIngredient(),
				'N' to Items.NETHER_STAR.asIngredient(),
				'I' to ModItems.EYE_OF_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.REDSTONE_CLOCK.toStack(),
			"SRS,RTR,SRS",
			mapOf(
				'S' to Tags.Items.STONES.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'T' to Blocks.REDSTONE_TORCH.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.GLASS_CUTTER.toStack(),
			"  I, SI,I  ",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'S' to Tags.Items.RODS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.RESONATOR.toStack(),
			"DCD,IRI,III",
			mapOf(
				'D' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'C' to Tags.Items.STORAGE_BLOCKS_COAL.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.REDSTONE_GEAR.toStack(),
			" T ,TPT, T ",
			mapOf(
				'T' to Items.REDSTONE_TORCH.asIngredient(),
				'P' to ItemTags.PLANKS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.MOON_STONE,
			"LLL,LDL,LLL",
			mapOf(
				'L' to ModItems.LUNAR_REACTIVE_DUST.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.SPEED_UPGRADE_MAGICAL,
			"AIA,IUI,AIA",
			mapOf(
				'A' to ModItems.MAGICAL_APPLE.asIngredient(),
				'I' to ModItems.ENCHANTED_INGOT.asIngredient(),
				'U' to ModItems.SPEED_UPGRADE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.SPEED_UPGRADE_ULTIMATE,
			"EIE,IUI,EIE",
			mapOf(
				'E' to ModItems.DROP_OF_EVIL.asIngredient(),
				'I' to ModItems.EVIL_INFUSED_IRON_INGOT.asIngredient(),
				'U' to ModItems.SPEED_UPGRADE_MAGICAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.CHICKEN_WING_RING,
			"FIF,ILI,RIR",
			mapOf(
				'F' to Tags.Items.FEATHERS.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'L' to ModItems.GOLDEN_LASSO.withComponent(
					ModDataComponents.ENTITY_TYPE.get(),
					EntityType.CHICKEN.builtInRegistryHolder()
				).asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.RING_OF_THE_FLYING_SQUID,
			"IDI,LCE,IDI",
			mapOf(
				'I' to Items.INK_SAC.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'L' to ModItems.GOLDEN_LASSO.withComponent(
					ModDataComponents.ENTITY_TYPE.get(),
					EntityType.SQUID.builtInRegistryHolder()
				).asIngredient(),
				'C' to ModItems.CHICKEN_WING_RING.asIngredient(),
				'E' to Tags.Items.ENDER_PEARLS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.POWER_MANAGER,
			" R,SS,SS",
			mapOf(
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'S' to Tags.Items.STONES.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.BAG_OF_HOLDING,
			"GGG,CMC,GGG",
			mapOf(
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'C' to Tags.Items.CHESTS_WOODEN.asIngredient(),
				'M' to ModBlocks.MAGICAL_WOOD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.EDGED_GLASS,
			4,
			"GG,GG",
			mapOf(
				'G' to ModBlocks.THICKENED_GLASS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.PATTERNED_GLASS,
			4,
			"GG,GG",
			mapOf(
				'G' to ModBlocks.EDGED_GLASS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ETHEREAL_GLASS,
			8,
			"GGG,GMG,GGG",
			mapOf(
				'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
				'M' to ModItems.MOON_STONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.INEFFABLE_GLASS,
			8,
			"GGG,GMG,GGG",
			mapOf(
				'G' to ModBlocks.THICKENED_GLASS.asIngredient(),
				'M' to ModItems.MOON_STONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DARK_INEFFABLE_GLASS,
			8,
			"GGG,GMG,GGG",
			mapOf(
				'G' to Blocks.TINTED_GLASS.asIngredient(),
				'M' to ModItems.MOON_STONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.TRANSFER_PIPE,
			64,
			"SSS,GRG,SSS",
			mapOf(
				'S' to Items.STONE_SLAB.asIngredient(),
				'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.PLAYER_CHEST,
			"SSS,SES,SRS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'E' to Tags.Items.CHESTS_ENDER.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.PIPE_WRENCH,
			" DI, IR,I  ",
			mapOf(
				'D' to Tags.Items.DYES_RED.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.MAGICAL_BOOMERANG,
			" M ,M M",
			mapOf(
				'M' to ModBlocks.MAGICAL_WOOD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.ITEM_FILTER,
			"RSR,STS,RSR",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'S' to Tags.Items.RODS_WOODEN.asIngredient(),
				'T' to Tags.Items.STRINGS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.FLUID_FILTER,
			"LSL,STS,LSL",
			mapOf(
				'L' to Tags.Items.GEMS_LAPIS.asIngredient(),
				'S' to Tags.Items.RODS_WOODEN.asIngredient(),
				'T' to Tags.Items.STRINGS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ITEM_TRANSFER_NODE,
			4,
			"RPR,SCS",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'P' to ModBlocks.TRANSFER_PIPE.asIngredient(),
				'S' to Tags.Items.STONES.asIngredient(),
				'C' to Tags.Items.CHESTS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.TRANSFER_FILTER,
			4,
			"RFR,SPS",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'F' to ModItems.ITEM_FILTER.asIngredient(),
				'S' to Tags.Items.STONES.asIngredient(),
				'P' to ModBlocks.TRANSFER_PIPE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FLUID_TRANSFER_NODE,
			4,
			"RPR,SCS",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'P' to ModBlocks.TRANSFER_PIPE.asIngredient(),
				'S' to Tags.Items.STONES.asIngredient(),
				'C' to Items.BUCKET.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENERGY_TRANSFER_NODE,
			4,
			"RPR,GBG",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'P' to ModBlocks.TRANSFER_PIPE.asIngredient(),
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'B' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.INDEXER,
			"SRS,SFS,SRS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'F' to Items.ITEM_FRAME.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.INDEXER_REMOTE,
			"SES,SFS,SES",
			mapOf(
				'S' to Tags.Items.STONES.asIngredient(),
				'E' to ModItems.EYE_OF_REDSTONE.asIngredient(),
				'F' to Items.ITEM_FRAME.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.STONE_DRUM,
			"CSC,CBC,CSC",
			mapOf(
				'S' to Tags.Items.COBBLESTONES_NORMAL.asIngredient(),
				'C' to Blocks.COBBLESTONE_SLAB.asIngredient(),
				'B' to Items.BOWL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.IRON_DRUM,
			"IPI,ICI,IPI",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'P' to Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE.asIngredient(),
				'C' to Blocks.CAULDRON.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.REINFORCED_LARGE_DRUM,
			"DPD,DID,DPD",
			mapOf(
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'P' to Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.asIngredient(),
				'I' to ModBlocks.IRON_DRUM.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM,
			"DKD,DRD,DKD",
			mapOf(
				'D' to ModItems.DEMON_INGOT.asIngredient(),
				'K' to ModItems.KLEIN_BOTTLE.asIngredient(),
				'R' to ModBlocks.REINFORCED_LARGE_DRUM.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.BEDROCKIUM_DRUM,
			"DBD,DRD,DBD",
			mapOf(
				'D' to ModItems.BEDROCKIUM_INGOT.asIngredient(),
				'B' to ItemTags.STONE_BUTTONS.asIngredient(),
				'R' to ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.MACHINE_BLOCK,
			4,
			"IRI,RCR,IRI",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'C' to Tags.Items.CHESTS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FURNACE,
			"BBB,BMB,BBB",
			mapOf(
				'B' to Tags.Items.BRICKS.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.CRUSHER,
			"IPI,IMI,IPI",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'P' to ModItemTagsProvider.PISTONS.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENCHANTER,
			" E ,DMD,III",
			mapOf(
				'E' to Items.ENCHANTED_BOOK.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.SURVIVALIST_GENERATOR,
			"CCC,CIC,RFR",
			mapOf(
				'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'F' to Items.FURNACE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FURNACE_GENERATOR,
			"III,IMI,RFR",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'F' to Items.FURNACE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.CULINARY_GENERATOR,
			"PPP,PFP,RMR",
			mapOf(
				'P' to Tags.Items.CROPS.asIngredient(),
				'F' to Tags.Items.FOODS_COOKED_MEAT.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.MAGMATIC_GENERATOR,
			"GGG,GLG,RMR",
			mapOf(
				'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
				'L' to Items.LAVA_BUCKET.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.HEATED_REDSTONE_GENERATOR,
			"RRR,RBR,RMR",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'B' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
				'M' to ModBlocks.MAGMATIC_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_GENERATOR,
			"EEE,EOE,RMR",
			mapOf(
				'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
				'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.POTION_GENERATOR,
			"BBB,BSB,RMR",
			mapOf(
				'B' to Tags.Items.RODS_BLAZE.asIngredient(),
				'S' to Blocks.BREWING_STAND.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.PINK_GENERATOR,
			"DDD,DWD,RMR",
			mapOf(
				'D' to Tags.Items.DYES_PINK.asIngredient(),
				'W' to Items.PINK_WOOL.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.OVERCLOCKED_GENERATOR,
			"LLL,LGL,RFR",
			mapOf(
				'L' to Tags.Items.GEMS_LAPIS.asIngredient(),
				'G' to Tags.Items.STORAGE_BLOCKS_GOLD.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'F' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.EXPLOSIVE_GENERATOR,
			"GGG,GTG,RMR",
			mapOf(
				'G' to Tags.Items.GUNPOWDERS.asIngredient(),
				'T' to Blocks.TNT.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.NETHER_STAR_GENERATOR,
			"WWW,WSW,RMR",
			mapOf(
				'W' to Items.WITHER_SKELETON_SKULL.asIngredient(),
				'S' to Items.NETHER_STAR.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.HALITOSIS_GENERATOR,
			"PPP,PEP,RMR",
			mapOf(
				'P' to Blocks.PURPUR_BLOCK.asIngredient(),
				'E' to Blocks.END_ROD.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FROSTY_GENERATOR,
			"SSS,SIS,RMR",
			mapOf(
				'S' to Items.SNOWBALL.asIngredient(),
				'I' to Items.ICE.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DEATH_GENERATOR,
			"CCC,CEC,RMR",
			mapOf(
				'C' to ModItemTagsProvider.CORPSE_PARTS.asIngredient(),
				'E' to Items.SPIDER_EYE.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.DISENCHANTMENT_GENERATOR,
			"WWW,WEW,RMR",
			mapOf(
				'W' to ModBlocks.MAGICAL_WOOD.asIngredient(),
				'E' to Blocks.ENCHANTING_TABLE.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.SLIMY_GENERATOR,
			"sss,sSs,RMR",
			mapOf(
				's' to Tags.Items.SLIME_BALLS.asIngredient(),
				'S' to Blocks.SLIME_BLOCK.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.WIRELESS_FE_TRANSMITTER,
			4,
			"R,S",
			mapOf(
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				'S' to ModBlocks.STONEBURNT.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.WIRELESS_FE_BATTERY,
			"SSS,BRB,SSS",
			mapOf(
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'B' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.QUANTUM_QUARRY_ACTUATOR,
			"ERE,EDE,SSS",
			mapOf(
				'E' to Tags.Items.END_STONES.asIngredient(),
				'R' to Blocks.END_ROD.asIngredient(),
				'D' to Items.DIAMOND_PICKAXE.asIngredient(),
				'S' to ModBlocks.STONEBURNT.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.QUANTUM_QUARRY,
			"ESE,SGS,ESE",
			mapOf(
				'E' to Tags.Items.END_STONES.asIngredient(),
				'S' to ModBlocks.STONEBURNT.asIngredient(),
				'G' to ModBlocks.MAGICAL_SNOW_GLOBE
					.withComponent(
						ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS.get(),
						MagicalSnowGlobeProgressComponent.DEFAULT_COMPLETED
					)
					.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.REDSTONE_LANTERN,
			"RSR,SCS,RPR",
			mapOf(
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'S' to ModBlocks.POLISHED_STONE.asIngredient(),
				'C' to ItemTags.COALS.asIngredient(),
				'P' to Items.REPEATER.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.SLIGHTLY_LARGER_CHEST,
			"SSS,SCS,SSS",
			mapOf(
				'S' to Tags.Items.RODS_WOODEN.asIngredient(),
				'C' to Tags.Items.CHESTS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.MAGICAL_APPLE,
			"AAA,AWA,AAA",
			mapOf(
				'A' to Items.APPLE.asIngredient(),
				'W' to ModBlocks.MAGICAL_WOOD.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.TROWEL,
			"  I, B ,S  ",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'B' to Items.STONE_BUTTON.asIngredient(),
				'S' to Tags.Items.RODS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.BIOME_MARKER,
			"PIP,ISI,PIP",
			mapOf(
				'P' to Tags.Items.DYES_PURPLE.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'S' to ItemTags.SAPLINGS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.TERRAFORMER,
			"ECE,RPR",
			mapOf(
				'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
				'C' to ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'P' to Items.REPEATER.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ANTENNA,
			"E E,ISI, I ",
			mapOf(
				'E' to Blocks.END_ROD.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'S' to ItemTags.SAPLINGS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.CLIMOGRAPH_BASE_UNIT,
			"IDI,SMS,IDI",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
				'S' to ItemTags.SAPLINGS.asIngredient(),
				'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.KIKOKU,
			"O,O,S",
			mapOf(
				'O' to perfectOpiniumCore.asIngredient(),
				'S' to Tags.Items.RODS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.FLAT_TRANSFER_NODE_ITEMS,
			8,
			"A, ,N",
			mapOf(
				'A' to ItemTags.ANVIL.asIngredient(),
				'N' to ModBlocks.ITEM_TRANSFER_NODE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.FLAT_TRANSFER_NODE_FLUIDS,
			8,
			"A, ,N",
			mapOf(
				'A' to ItemTags.ANVIL.asIngredient(),
				'N' to ModBlocks.FLUID_TRANSFER_NODE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.COMPOUND_BOW,
			" OS,I S, OS",
			mapOf(
				'O' to perfectOpiniumCore.asIngredient(),
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'S' to Tags.Items.STRINGS.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.FIRE_AXE,
			"OO,OS, S",
			mapOf(
				'O' to perfectOpiniumCore.asIngredient(),
				'S' to Tags.Items.RODS_WOODEN.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_PORCUPINE,
			"SES,EME,SES",
			mapOf(
				'S' to Tags.Items.STRINGS.asIngredient(),
				'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
				'M' to ModItems.POWER_MANAGER.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModItems.LUX_SABER,
			"EGE,ECE,ERE",
			mapOf(
				'E' to ModItems.EVIL_INFUSED_IRON_INGOT.asIngredient(),
				'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
				'C' to ModItems.SUN_CRYSTAL
					.withComponent(
						ModDataComponents.CHARGE.get(),
						SunCrystalItem.MAX_CHARGE
					)
					.asIngredient(),
				'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY,
			"OSO,CDC,PXP",
			mapOf(
				'O' to ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient(),
				'S' to ItemTags.SAPLINGS.asIngredient(),
				'C' to ModBlocks.ENDER_CORE.asIngredient(),
				'D' to ModBlocks.DIAMOND_ETCHED_COMPUTATIONAL_MATRIX.asIngredient(),
				'P' to ModBlocks.ENDER_THERMIC_PUMP.asIngredient(),
				'X' to Items.DIAMOND_PICKAXE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.FILING_CABINET,
			"ICI,ICI,ICI",
			mapOf(
				'I' to Tags.Items.INGOTS_IRON.asIngredient(),
				'C' to Tags.Items.CHESTS_WOODEN.asIngredient(
				)
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ADVANCED_FILING_CABINET,
			"WCW,WCW,WCW",
			mapOf(
				'W' to ModBlocks.MAGICAL_WOOD.asIngredient(),
				'C' to ModBlocks.FILING_CABINET.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.PEACEFUL_TABLE,
			"EPE,PNP,EPE",
			mapOf(
				'E' to Tags.Items.GEMS_EMERALD.asIngredient(),
				'P' to ItemTags.PLANKS.asIngredient(),
				'N' to Tags.Items.ENDER_PEARLS.asIngredient()
			)
		).save(recipeOutput)

		fun sickle(output: ItemLike, material: TagKey<Item>): ShapedRecipeBuilder {
			return shapedRecipe(
				output,
				" MM,  M,SMM",
				mapOf(
					'M' to material.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient()
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

		fun spike(spike: ItemLike, sword: Ingredient, middle: Ingredient, base: Ingredient) {
			shapedRecipe(
				spike,
				4,
				" S ,SIS,IBI",
				mapOf(
					'S' to sword,
					'I' to middle,
					'B' to base
				)
			).save(recipeOutput)
		}

		spike(
			ModBlocks.WOODEN_SPIKE,
			Items.WOODEN_SWORD.asIngredient(),
			ItemTags.PLANKS.asIngredient(),
			ItemTags.LOGS.asIngredient()
		)

		spike(
			ModBlocks.STONE_SPIKE,
			Items.STONE_SWORD.asIngredient(),
			Tags.Items.COBBLESTONES_NORMAL.asIngredient(),
			ModBlocks.getCompressedCobblestone(1).asIngredient()
		)

		spike(
			ModBlocks.IRON_SPIKE,
			Items.IRON_SWORD.asIngredient(),
			Tags.Items.INGOTS_IRON.asIngredient(),
			Tags.Items.STORAGE_BLOCKS_IRON.asIngredient()
		)

		spike(
			ModBlocks.GOLDEN_SPIKE,
			Items.GOLDEN_SWORD.asIngredient(),
			Tags.Items.INGOTS_GOLD.asIngredient(),
			Tags.Items.STORAGE_BLOCKS_GOLD.asIngredient()
		)

		spike(
			ModBlocks.DIAMOND_SPIKE,
			Items.DIAMOND_SWORD.asIngredient(),
			Tags.Items.GEMS_DIAMOND.asIngredient(),
			Tags.Items.STORAGE_BLOCKS_DIAMOND.asIngredient()
		)

		spike(
			ModBlocks.NETHERITE_SPIKE,
			Items.NETHERITE_SWORD.asIngredient(),
			Tags.Items.INGOTS_NETHERITE.asIngredient(),
			Tags.Items.STORAGE_BLOCKS_NETHERITE.asIngredient()
		)

		val enchantments = holderLookup.lookup(Registries.ENCHANTMENT).get()

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_FORTUNE_UPGRADE,
			" P ,RBR",
			mapOf(
				'P' to Items.IRON_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.FORTUNE), 1)
				}.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'B' to ModBlocks.ENDER_QUARRY_UPGRADE_BASE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_FORTUNE_TWO_UPGRADE,
			" P ,RUR",
			mapOf(
				'P' to Items.GOLDEN_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.FORTUNE), 1)
				}.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'U' to ModBlocks.ENDER_QUARRY_FORTUNE_UPGRADE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_FORTUNE_THREE_UPGRADE,
			"P P,RUR",
			mapOf(
				'P' to Items.DIAMOND_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.FORTUNE), 1)
				}.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'U' to ModBlocks.ENDER_QUARRY_FORTUNE_TWO_UPGRADE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_SILK_TOUCH_UPGRADE,
			" P ,RBR",
			mapOf(
				'P' to Items.IRON_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.SILK_TOUCH), 1)
				}.asIngredient(),
				'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
				'B' to ModBlocks.ENDER_QUARRY_UPGRADE_BASE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_SPEED_UPGRADE,
			" P ,SBS",
			mapOf(
				'P' to Items.DIAMOND_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.EFFICIENCY), 1)
				}.asIngredient(),
				'S' to ModItems.SPEED_UPGRADE.asIngredient(),
				'B' to ModBlocks.ENDER_QUARRY_UPGRADE_BASE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_SPEED_TWO_UPGRADE,
			" P ,SUS",
			mapOf(
				'P' to Items.DIAMOND_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.EFFICIENCY), 3)
				}.asIngredient(),
				'S' to ModItems.SPEED_UPGRADE_MAGICAL.asIngredient(),
				'U' to ModBlocks.ENDER_QUARRY_SPEED_UPGRADE.asIngredient(
				)
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_SPEED_THREE_UPGRADE,
			"P P,SUS",
			mapOf(
				'P' to Items.DIAMOND_PICKAXE.defaultInstance.apply {
					enchant(enchantments.getOrThrow(Enchantments.EFFICIENCY), 5)
				}.asIngredient(),
				'S' to ModItems.STACK_UPGRADE.asIngredient(),
				'U' to ModBlocks.ENDER_QUARRY_SPEED_TWO_UPGRADE.asIngredient()
			)
		).save(recipeOutput)

		shapedRecipe(
			ModBlocks.ENDER_QUARRY_WORLD_HOLE_UPGRADE,
			" T ,QBQ",
			mapOf(
				'T' to ModBlocks.TRASH_CAN.asIngredient(),
				'Q' to Blocks.QUARTZ_BLOCK.asIngredient(),
				'B' to ModBlocks.ENDER_QUARRY_UPGRADE_BASE.asIngredient()
			)
		).save(recipeOutput)

		for (color in DyeColor.entries) {
			val tag = ItemTags.create(
				ResourceLocation.fromNamespaceAndPath(
					"c",
					"dyes/${color.serializedName}"
				)
			)

			fun makeColoredRecipe(
				output: DeferredBlock<*>,
				ingredient: Ingredient,
			) {
				SpecialShapedRecipeBuilder(output.asItem())
					.type("keep_paintbrush", ::KeepPaintbrushRecipe)
					.pattern(
						"III",
						"IDI",
						"IPI"
					)
					.define('I', ingredient)
					.define('D', tag.asIngredient())
					.define('P', ModItems.PAINTBRUSH.asIngredient())
					.save(recipeOutput)
			}

			makeColoredRecipe(ModBlocks.getColoredStone(color), Tags.Items.STONES.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredCobblestone(color), Tags.Items.COBBLESTONES_NORMAL.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredStoneBricks(color), ItemTags.STONE_BRICKS.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredBricks(color), ModItemTagsProvider.BRICK_BLOCKS.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredPlanks(color), ItemTags.PLANKS.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredCoalBlock(color), Tags.Items.STORAGE_BLOCKS_COAL.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredRedstoneBlock(color), Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredLapisBlock(color), Tags.Items.STORAGE_BLOCKS_LAPIS.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredObsidian(color), Tags.Items.OBSIDIANS_NORMAL.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredQuartz(color), ModItemTagsProvider.QUARTZ_STORAGE_BLOCKS.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredSoulSand(color), ModItemTagsProvider.SOUL_SANDS.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredGlowstone(color), ModItemTagsProvider.GLOWSTONES.asIngredient())
			makeColoredRecipe(ModBlocks.getColoredRedstoneLamp(color), ModItemTagsProvider.REDSTONE_LAMPS.asIngredient())
		}
	}

	private fun buildShapelessRecipes(recipeOutput: RecipeOutput) {
		shapelessRecipe(
			ModBlocks.SQUARE_GLASS,
			listOf(
				ModBlocks.THICKENED_GLASS.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.CARVED_GLASS,
			listOf(
				ModBlocks.THICKENED_GLASS.asIngredient(),
				Tags.Items.GUNPOWDERS.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.EMINENCE_STONE,
			4,
			listOf(
				Tags.Items.DYES_MAGENTA.asIngredient(),
				Tags.Items.DYES_PURPLE.asIngredient(),
				Tags.Items.STONES.asIngredient(),
				Tags.Items.STONES.asIngredient(),
				Tags.Items.STONES.asIngredient(),
				Tags.Items.STONES.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.SOUND_MUFFLER,
			listOf(
				ItemTags.WOOL.asIngredient(),
				Blocks.NOTE_BLOCK.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.MAGICAL_PLANKS,
			4,
			listOf(
				ModBlocks.MAGICAL_WOOD.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.SUN_CRYSTAL,
			listOf(
				Tags.Items.GEMS_DIAMOND.asIngredient(),
				Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
				Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
				Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
				Tags.Items.DUSTS_GLOWSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.BORDER_STONE,
			4,
			listOf(
				Tags.Items.STONES.asIngredient(),
				Tags.Items.STONES.asIngredient(),
				Items.STONE_BRICKS.asIngredient(),
				Items.STONE_BRICKS.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.CROSSED_STONE,
			4,
			listOf(
				ModBlocks.BORDER_STONE.asIngredient(),
				ModBlocks.BORDER_STONE.asIngredient(),
				ModBlocks.BORDER_STONE.asIngredient(),
				ModBlocks.BORDER_STONE.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.POLISHED_STONE,
			4,
			listOf(
				Blocks.CHISELED_STONE_BRICKS.asIngredient(),
				Blocks.CHISELED_STONE_BRICKS.asIngredient(),
				Blocks.CHISELED_STONE_BRICKS.asIngredient(),
				Blocks.CHISELED_STONE_BRICKS.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.SANDY_GLASS,
			4,
			listOf(
				Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
				Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
				Tags.Items.SANDS.asIngredient(),
				Tags.Items.SANDS.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.RESONATING_REDSTONE_CRYSTAL,
			listOf(
				ModItems.ENDER_SHARD.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.EYE_OF_REDSTONE,
			listOf(
				Tags.Items.ENDER_PEARLS.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient(),
				ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.SPEED_UPGRADE,
			listOf(
				ModItems.UPGRADE_BASE.asIngredient(),
				Tags.Items.INGOTS_GOLD.asIngredient(),
				Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.STACK_UPGRADE,
			listOf(
				ModItems.UPGRADE_BASE.asIngredient(),
				Tags.Items.INGOTS_GOLD.asIngredient(),
				Tags.Items.GEMS_DIAMOND.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.WORLD_INTERACTION_UPGRADE,
			listOf(
				ModItems.UPGRADE_BASE.asIngredient(),
				Items.GOLDEN_PICKAXE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.CURSED_LASSO,
			listOf(
				ModItems.GOLDEN_LASSO.asIngredient(),
				ModItems.DROP_OF_EVIL.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.GLOWING_GLASS,
			2,
			listOf(
				Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
				Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
				Tags.Items.DUSTS_GLOWSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.INVERTED_ETHEREAL_GLASS,
			listOf(
				ModBlocks.ETHEREAL_GLASS.asIngredient(),
				Items.REDSTONE_TORCH.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.INVERTED_INEFFABLE_GLASS,
			listOf(
				ModBlocks.INEFFABLE_GLASS.asIngredient(),
				Items.REDSTONE_TORCH.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.TRANSFER_PIPE_FILTER,
			4,
			listOf(
				ModBlocks.TRANSFER_PIPE.asIngredient(),
				ModItems.ITEM_FILTER.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.SCANNER,
			listOf(
				Blocks.OBSERVER.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient(),
				Items.SPIDER_EYE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.MECHANICAL_MINER,
			listOf(
				Blocks.DROPPER.asIngredient(),
				ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				Items.IRON_PICKAXE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.MECHANICAL_USER,
			listOf(
				Blocks.DROPPER.asIngredient(),
				ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
				Items.LEVER.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModItems.KLEIN_BOTTLE,
			listOf(
				Items.GLASS_BOTTLE.asIngredient(),
				Tags.Items.ENDER_PEARLS.asIngredient(),
				Tags.Items.ENDER_PEARLS.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.MINI_CHEST,
			9,
			listOf(Tags.Items.CHESTS_WOODEN.asIngredient())
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.COOLER,
			listOf(
				ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
				Items.SNOWBALL.asIngredient(),
				Items.SNOWBALL.asIngredient(),
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.HUMIDIFIER,
			listOf(
				ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
				Items.WATER_BUCKET.asIngredient(),
				Items.WATER_BUCKET.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.DEHUMIDIFIER,
			listOf(
				ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
				Tags.Items.SANDS.asIngredient(),
				Tags.Items.SANDS.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.HEATER,
			listOf(
				ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
				Items.LAVA_BUCKET.asIngredient(),
				Items.LAVA_BUCKET.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.DEHOSTILIFIER,
			listOf(
				ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
				Items.MYCELIUM.asIngredient(),
				Items.MYCELIUM.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.MAGIC_ABSORPTION,
			listOf(
				ModBlocks.MAGICAL_WOOD.asIngredient(),
				ItemTags.ANVIL.asIngredient(),
				ItemTags.ANVIL.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.MAGIC_INFUSER,
			listOf(
				ModBlocks.MAGICAL_WOOD.asIngredient(),
				Items.ENCHANTING_TABLE.asIngredient(),
				Items.ENCHANTING_TABLE.asIngredient()
			)
		).save(recipeOutput)

		shapelessRecipe(
			ModBlocks.TRASH_CAN_CHEST,
			listOf(
				ModBlocks.TRASH_CAN.asIngredient(),
				Tags.Items.CHESTS_WOODEN.asIngredient(),
				Tags.Items.DUSTS_REDSTONE.asIngredient()
			)
		).save(recipeOutput)
	}

	private fun buildEnchanterRecipes(recipeOutput: RecipeOutput) {
		EnchanterRecipeBuilder(
			Tags.Items.BOOKSHELVES.asIngredient(),
			1,
			Tags.Items.GEMS_LAPIS.asIngredient(),
			1,
			64_00 / (80 * 20),
			80 * 20,
			ModBlocks.MAGICAL_WOOD.toStack()
		).save(recipeOutput)

		EnchanterRecipeBuilder(
			Tags.Items.STORAGE_BLOCKS_GOLD.asIngredient(),
			1,
			Tags.Items.GEMS_LAPIS.asIngredient(),
			9,
			24_000 / (20 * 30),
			20 * 30,
			ModBlocks.BLOCK_OF_ENCHANTED_METAL.toStack()
		).save(recipeOutput)

		EnchanterRecipeBuilder(
			Tags.Items.INGOTS_GOLD.asIngredient(),
			1,
			Tags.Items.GEMS_LAPIS.asIngredient(),
			1,
			8_000 / (20 * 10),
			20 * 10,
			ModItems.ENCHANTED_INGOT.toStack()
		).save(recipeOutput)

		EnchanterRecipeBuilder(
			Tags.Items.STORAGE_BLOCKS_IRON.asIngredient(),
			8,
			Items.NETHER_STAR.asIngredient(),
			1,
			192_000 / (20 * 240),
			20 * 240,
			ModBlocks.BLOCK_OF_EVIL_INFUSED_IRON.toStack(8)
		).save(recipeOutput)

		EnchanterRecipeBuilder(
			Tags.Items.INGOTS_IRON.asIngredient(),
			8,
			Items.NETHER_STAR.asIngredient(),
			1,
			64_000 / (20 * 20),
			20 * 20,
			ModItems.EVIL_INFUSED_IRON_INGOT.toStack(8)
		).save(recipeOutput)

		EnchanterRecipeBuilder(
			Items.APPLE.asIngredient(),
			16,
			Tags.Items.GEMS_LAPIS.asIngredient(),
			1,
			16_000 / (20 * 80),
			20 * 80,
			ModItems.MAGICAL_APPLE.toStack(16)
		).save(recipeOutput)

	}

	private fun buildResonatorRecipes(recipeOutput: RecipeOutput) {
		ResonatorRecipeBuilder(
			ModBlocks.POLISHED_STONE.asIngredient(),
			ModBlocks.STONEBURNT.toStack(),
			8.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			Blocks.QUARTZ_BLOCK.asIngredient(),
			ModBlocks.QUARTZBURNT.toStack(),
			8.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			ModBlocks.STONEBURNT.asIngredient(),
			ModBlocks.RAINBOW_STONE.toStack(),
			64.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			Tags.Items.GEMS_LAPIS.asIngredient(),
			ModItems.LUNAR_REACTIVE_DUST.toStack(),
			16.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			ItemTags.COALS.asIngredient(),
			ModItems.RED_COAL.toStack(),
			8.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.asIngredient(),
			ModItems.UPGRADE_BASE.toStack(),
			8.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			Blocks.IRON_BARS.asIngredient(),
			ModItems.HEATING_COIL.toStack(),
			16.0
		).save(recipeOutput)

		ResonatorRecipeBuilder(
			ModBlocks.THICKENED_GLASS.asIngredient(),
			ModBlocks.REDSTONE_GLASS.toStack(),
			1.0
		).save(recipeOutput)

	}

	private fun buildNamedRecipes(recipeOutput: RecipeOutput) {
		shapelessRecipe(
			Items.REDSTONE,
			8,
			listOf(ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient())
		).save(recipeOutput, modLoc("redstone_from_resonating_redstone_crystal"))

		shapelessRecipe(
			ModItems.WATERING_CAN,
			listOf(
				ModItems.WATERING_CAN
					.withComponent(ModDataComponents.IS_BROKEN.get(), Unit.INSTANCE)
					.asIngredient()
			)
		).save(recipeOutput, modLoc("watering_can_repair"))

		fun packed(
			bigger: ItemLike,
			smaller: ItemLike
		) {
			val biggerName = bigger.asItem().builtInRegistryHolder().key().location().path
			val smallerName = smaller.asItem().builtInRegistryHolder().key().location().path

			shapedRecipe(
				bigger,
				"PPP,PPP,PPP",
				mapOf('P' to smaller.asIngredient())
			).save(recipeOutput, modLoc("${biggerName}_from_${smallerName}"))

			shapelessRecipe(
				smaller,
				9,
				listOf(bigger.asIngredient())
			).save(recipeOutput, modLoc("${smallerName}_from_${biggerName}"))
		}

		packed(
			ModBlocks.BLOCK_OF_DEMON_METAL,
			ModItems.DEMON_INGOT,
		)

		packed(
			ModItems.DEMON_INGOT,
			ModItems.DEMON_NUGGET,
		)

		packed(
			ModBlocks.BLOCK_OF_ENCHANTED_METAL,
			ModItems.ENCHANTED_INGOT,
		)

		packed(
			ModBlocks.BLOCK_OF_EVIL_INFUSED_IRON,
			ModItems.EVIL_INFUSED_IRON_INGOT,
		)

		packed(
			ModItems.EVIL_INFUSED_IRON_INGOT,
			ModItems.EVIL_INFUSED_IRON_NUGGET
		)

		packed(
			ModBlocks.BLOCK_OF_BEDROCKIUM,
			ModItems.BEDROCKIUM_INGOT,
		)

		shapelessRecipe(
			ModBlocks.STONE_DRUM,
			listOf(ModBlocks.STONE_DRUM.asIngredient())
		).save(recipeOutput, modLoc("stone_drum_empty"))

		shapelessRecipe(
			ModBlocks.IRON_DRUM,
			listOf(ModBlocks.IRON_DRUM.asIngredient())
		).save(recipeOutput, modLoc("iron_drum_empty"))

		shapelessRecipe(
			ModBlocks.REINFORCED_LARGE_DRUM,
			listOf(ModBlocks.REINFORCED_LARGE_DRUM.asIngredient())
		).save(recipeOutput, modLoc("reinforced_large_drum_empty"))

		shapelessRecipe(
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM,
			listOf(ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.asIngredient())
		).save(recipeOutput, modLoc("demonically_gargantuan_drum_empty"))

		shapelessRecipe(
			ModBlocks.BEDROCKIUM_DRUM,
			listOf(ModBlocks.BEDROCKIUM_DRUM.asIngredient())
		).save(recipeOutput, modLoc("bedrockium_drum_empty"))

		shapelessRecipe(
			ModBlocks.CREATIVE_DRUM,
			listOf(ModBlocks.CREATIVE_DRUM.asIngredient())
		).save(recipeOutput, modLoc("creative_drum_empty"))

		shapelessRecipe(
			Blocks.CHEST,
			listOf(
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient(),
				ModBlocks.MINI_CHEST.asIngredient()
			)
		).save(recipeOutput, modLoc("chest_from_mini_chests"))

		shapelessRecipe(
			ModItems.UNSTABLE_INGOT,
			listOf(
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
				ModItems.SEMI_UNSTABLE_NUGGET.asIngredient(),
			)
		).save(recipeOutput, modLoc("mobius_ingot_from_semi_unstable_nuggets"))

		val cores = OpiniumCoreContentsComponent.getDefaultTiers()
		for ((i, core) in cores.withIndex()) {
			val tierName = core.name.string.split(".").last()
			val recipeName = "opinium_core_$tierName"

			if (i == 0) {
				shapedRecipe(
					ModItems.OPINIUM_CORE,
					" R ,RIR, R ",
					mapOf(
						'R' to ModItems.RED_COAL.asIngredient(),
						'I' to Tags.Items.STORAGE_BLOCKS_IRON.asIngredient()
					)
				).save(recipeOutput, modLoc(recipeName))

				continue
			}

			val inputCoreStack = cores[i - 1].getStack()
			val outputCoreStack = core.getStack()

			val (inner, outer) = core

			shapedRecipe(
				outputCoreStack,
				" O ,ABA, O ",
				mapOf(
					'O' to inputCoreStack.asIngredient(),
					'A' to outer.asIngredient(),
					'B' to inner.asIngredient()
				)
			).save(recipeOutput, modLoc(recipeName))
		}

	}

	private fun buildSmeltingRecipes(recipeOutput: RecipeOutput) {
		smeltingResultFromBase(recipeOutput, ModBlocks.THICKENED_GLASS, ModBlocks.SANDY_GLASS)
	}

	private fun buildQedRecipes(recipeOutput: RecipeOutput) {
		QedRecipeBuilder(ModBlocks.ITEM_RETRIEVAL_NODE.toStack())
			.pattern(
				" E ",
				"NMN",
				" E "
			)
			.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('N', ModBlocks.ITEM_TRANSFER_NODE.asIngredient())
			.define('M', Tags.Items.GEMS_EMERALD.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModBlocks.FLUID_RETRIEVAL_NODE.toStack())
			.pattern(
				" E ",
				"NDN",
				" E "
			)
			.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('N', ModBlocks.FLUID_TRANSFER_NODE.asIngredient())
			.define('D', Tags.Items.GEMS_DIAMOND.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModBlocks.ENERGY_RETRIEVAL_NODE.toStack())
			.pattern(
				" E ",
				"NRN",
				" E "
			)
			.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('N', ModBlocks.ENERGY_TRANSFER_NODE.asIngredient())
			.define('R', Tags.Items.DUSTS_REDSTONE.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModItems.ENDER_TRANSMITTER.toStack())
			.pattern(
				"ETE",
				"QEQ",
				"ETE"
			)
			.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('T', Items.REDSTONE_TORCH.asIngredient())
			.define('Q', Tags.Items.GEMS_QUARTZ.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModItems.ENDER_RECEIVER.toStack())
			.pattern(
				"ERE",
				"QEQ",
				"EQE"
			)
			.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('R', Tags.Items.DUSTS_REDSTONE.asIngredient())
			.define('Q', Tags.Items.GEMS_QUARTZ.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModBlocks.ENDER_QUARRY_UPGRADE_BASE.toStack())
			.pattern(
				" O ",
				"OQO",
				" O "
			)
			.define('O', ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient())
			.define('Q', ModBlocks.QUARTZBURNT.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModBlocks.MAGNUM_TORCH.toStack())
			.pattern(
				"RCH",
				"CLC",
				"CLC"
			)
			.define(
				'R',
				PotionContents.createItemStack(Items.POTION, Potions.STRONG_REGENERATION).asIngredient()
			)
			.define(
				'H',
				PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING).asIngredient()
			)
			.define('C', ModBlocks.CHANDELIER.asIngredient())
			.define('L', ItemTags.LOGS.asIngredient())
			.save(recipeOutput)

		QedRecipeBuilder(ModBlocks.ENDER_MARKER.toStack())
			.pattern(
				"E",
				"O",
				"O"
			)
			.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('O', ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient())
			.save(recipeOutput)
	}

	private fun buildGeneratorFuelRecipes(recipeOutput: RecipeOutput) {
		fun singleItem(name: String, type: SingleItemFuelRecipe.GeneratorType, ingredient: Ingredient, fePerTick: Int, duration: Int) {
			SingleItemFuelRecipeBuilder(
				type,
				ingredient,
				fePerTick,
				duration
			).save(recipeOutput, modLoc(name))
		}

		fun ender(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.ENDER, ingredient, fePerTick, duration)
		fun explosive(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.EXPLOSIVE, ingredient, fePerTick, duration)
		fun pink(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.PINK, ingredient, fePerTick, duration)
		fun netherStar(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.NETHER_STAR, ingredient, fePerTick, duration)
		fun frosty(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.FROSTY, ingredient, fePerTick, duration)
		fun halitosis(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.HALITOSIS, ingredient, fePerTick, duration)
		fun death(name: String, ingredient: Ingredient, fePerTick: Int, duration: Int) = singleItem(name, SingleItemFuelRecipe.GeneratorType.DEATH, ingredient, fePerTick, duration)


		ender("ender_pearls", Tags.Items.ENDER_PEARLS.asIngredient(), 40, 20 * (60 + 20)) // 1:20
		ender("ender_eye", Items.ENDER_EYE.asIngredient(), 80, 20 * (2 * 60 + 40)) // 2:40

		// TODO: Make sure these are correct

		pink("pink_dyes", Tags.Items.DYES_PINK.asIngredient(), 40, 10)
		pink("dyed_pink", Tags.Items.DYED_PINK.asIngredient(), 40, 10)

		death("bones", Tags.Items.BONES.asIngredient(), 40, 20 * 20)
		death("bone_blocks", Tags.Items.STORAGE_BLOCKS_BONE_MEAL.asIngredient(), 150, 20 * 20)
		death("bone_meal", Items.BONE_MEAL.asIngredient(), 40, 20 * 10)
		death("rotten_flesh", Items.ROTTEN_FLESH.asIngredient(), 20, 20 * 20)
		death("skeleton_skull", Items.SKELETON_SKULL.asIngredient(), 100, 20 * 20)
		death("wither_skeleton_skull", Items.WITHER_SKELETON_SKULL.asIngredient(), 150, 20 * 20)

		explosive("tnt", Items.TNT.asIngredient(), 160, 20 * (2 * 60 + 40)) // 2:40
		explosive("tnt_minecart", Items.TNT_MINECART.asIngredient(), 200, 20 * (2 * 60 + 40)) // 2:40
		explosive("gunpowder", Tags.Items.GUNPOWDERS.asIngredient(), 160, 20 * 20) // 20 seconds

		netherStar("nether_star", Items.NETHER_STAR.asIngredient(), 4_000, 20 * 60 * 2) // 2 minutes
		netherStar("firework_star", Items.FIREWORK_STAR.asIngredient(), 20, 20) // 20 seconds

		halitosis("dragon_breath", Items.DRAGON_BREATH.asIngredient(), 40, 20 * 60 * 10) // 10 minutes

		frosty("ice", Items.ICE.asIngredient(), 40, 20 * 2) // 2 seconds
		frosty("packed_ice", Items.PACKED_ICE.asIngredient(), 40, 20 * 2 * 9) // 18 seconds
		frosty("blue_ice", Items.BLUE_ICE.asIngredient(), 40, 20 * 2 * 9 * 9) // 162 seconds
		frosty("snowball", Items.SNOWBALL.asIngredient(), 40, 5) // 5 seconds
		frosty("snow_block", Items.SNOW_BLOCK.asIngredient(), 40, 20) // 20 seconds
		frosty("snow", Items.SNOW.asIngredient(), 40, 3) // 3 seconds

		fun magmatic(name: String, ingredient: SizedFluidIngredient, fePerTick: Int, duration: Int) {
			SingleFluidFuelRecipeBuilder(ingredient, fePerTick, duration).save(recipeOutput, modLoc(name))
		}

		magmatic("lavas", SizedFluidIngredient.of(Tags.Fluids.LAVA, 1), 100, 1) // 100,000 FE per bucket

		fun itemFluid(name: String, type: ItemAndFluidFuelRecipe.GeneratorType, itemIngredient: Ingredient, fluidIngredient: SizedFluidIngredient, fePerTick: Int, duration: Int) {
			ItemAndFluidFuelRecipeBuilder(
				type,
				itemIngredient,
				fluidIngredient,
				fePerTick,
				duration
			).save(recipeOutput, modLoc(name))
		}

		fun slimy(name: String, ingredient: Ingredient, fluidIngredient: SizedFluidIngredient, fePerTick: Int, duration: Int) = itemFluid(name, ItemAndFluidFuelRecipe.GeneratorType.SLIMY, ingredient, fluidIngredient, fePerTick, duration)
		fun heatedRedstone(name: String, ingredient: Ingredient, fluidIngredient: SizedFluidIngredient, fePerTick: Int, duration: Int) = itemFluid(name, ItemAndFluidFuelRecipe.GeneratorType.HEATED_REDSTONE, ingredient, fluidIngredient, fePerTick, duration)

		slimy(
			"slime_balls_and_milk",
			Tags.Items.SLIME_BALLS.asIngredient(),
			SizedFluidIngredient.of(Tags.Fluids.MILK, 250),
			10,
			5 * 20,
		)

		heatedRedstone(
			"redstone_and_lava",
			Tags.Items.DUSTS_REDSTONE.asIngredient(),
			SizedFluidIngredient.of(Tags.Fluids.LAVA, 250),
			3200, // 20,000 FE per cycle
			125, // 6.25 seconds
		)
	}

	private fun buildWorldInteractionRecipes(recipeOutput: RecipeOutput) {

		WorldInteractionItemRecipeBuilder(
			requiredOnBlock = BlockStateIngredient(Tags.Blocks.COBBLESTONES),
			requiredAdjacentBlocks = listOf(
				BlockStateIngredient(Blocks.WATER),
				BlockStateIngredient(Blocks.LAVA)
			),
			requiredBlockBehind = null,
			output = Items.COBBLESTONE.asItem().defaultInstance
		).save(recipeOutput, modLoc("cobblestone"))

		WorldInteractionItemRecipeBuilder(
			requiredOnBlock = BlockStateIngredient(Blocks.SOUL_SOIL),
			requiredAdjacentBlocks = listOf(
				BlockStateIngredient(Blocks.BLUE_ICE),
				BlockStateIngredient(Blocks.LAVA)
			),
			requiredBlockBehind = null,
			output = Items.BASALT.asItem().defaultInstance
		).save(recipeOutput, modLoc("basalt"))

		WorldInteractionItemRecipeBuilder(
			requiredOnBlock = BlockStateIngredient(Tags.Blocks.STONES),
			requiredAdjacentBlocks = listOf(
				BlockStateIngredient(Blocks.LAVA)
			),
			requiredBlockBehind = BlockStateIngredient(Blocks.WATER),
			output = Items.STONE.asItem().defaultInstance
		).save(recipeOutput, modLoc("stone"))

		WorldInteractionFluidRecipeBuilder(
			requiredOnBlock = BlockStateIngredient(Blocks.WATER),
			requiredAdjacentBlocks = listOf(
				BlockStateIngredient(Blocks.WATER),
				BlockStateIngredient(Blocks.WATER),
			),
			requiredBlockBehind = null,
			output = FluidStack(Fluids.WATER, 1)
		).save(recipeOutput, modLoc("water"))

	}

	private fun buildCrusherRecipes(recipeOutput: RecipeOutput) {
		fun recipe(
			ingredient: Ingredient,
			primaryOutput: ItemStack,
			secondaryOutput: ItemStack = ItemStack.EMPTY,
			secondaryChance: Float = 0f,
			name: String = ""
		) {
			val builder = CrusherRecipeBuilder(ingredient, primaryOutput, secondaryOutput, secondaryChance)
			if (name.isNotEmpty()) {
				builder.save(recipeOutput, modLoc(name))
			} else {
				val itemId = BuiltInRegistries.ITEM.getKey(primaryOutput.item)
				val location = modLoc("${itemId.namespace}/${itemId.path}")
				builder.save(recipeOutput, location)
			}
		}

		recipe(
			Tags.Items.ORES_DIAMOND.asIngredient(),
			Items.DIAMOND.defaultInstance,
			Items.DIAMOND.withCount(3),
			0.2f
		)

		recipe(
			Tags.Items.ORES_EMERALD.asIngredient(),
			Items.EMERALD.defaultInstance,
			Items.EMERALD.withCount(3),
			0.2f
		)

		recipe(
			Tags.Items.ORES_QUARTZ.asIngredient(),
			Items.QUARTZ.defaultInstance,
			Items.QUARTZ.withCount(3),
			0.2f
		)

		recipe(
			Tags.Items.ORES_GOLD.asIngredient(),
			Items.RAW_GOLD.withCount(2),
			Items.RAW_IRON.defaultInstance,
			0.1f
		)

		recipe(
			Tags.Items.ORES_IRON.asIngredient(),
			Items.RAW_IRON.withCount(2),
			Items.RAW_GOLD.defaultInstance,
			0.1f
		)

		recipe(
			Tags.Items.ORES_REDSTONE.asIngredient(),
			Items.REDSTONE.withCount(8)
		)

		recipe(
			Tags.Items.ORES_LAPIS.asIngredient(),
			Items.LAPIS_LAZULI.withCount(8)
		)

		recipe(
			Tags.Items.ORES_COAL.asIngredient(),
			Items.COAL.withCount(4)
		)

		recipe(
			Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
			Items.GLOWSTONE_DUST.withCount(4)
		)

		recipe(
			Tags.Items.COBBLESTONES.asIngredient(),
			Items.GRAVEL.defaultInstance,
			Items.SAND.defaultInstance,
			0.1f
		)

		recipe(
			Tags.Items.GRAVELS.asIngredient(),
			Items.SAND.defaultInstance,
		)

		recipe(
			Tags.Items.RODS_BLAZE.asIngredient(),
			Items.BLAZE_POWDER.withCount(2),
			Items.BLAZE_ROD.defaultInstance,
			0.4f
		)

		recipe(
			Tags.Items.BONES.asIngredient(),
			Items.BONE_MEAL.withCount(3),
			Items.BONE.defaultInstance,
			0.5f
		)

		fun flower(flower: Item, dye: Item, name: String) = recipe(flower.asIngredient(), dye.withCount(2), name = name)
		fun tallFlower(flower: Item, dye: Item, name: String) = recipe(flower.asIngredient(), dye.withCount(4), name = name)

		flower(Items.LILY_OF_THE_VALLEY, Items.WHITE_DYE, "lily_of_the_valley_to_white_dye")
		flower(Items.AZURE_BLUET, Items.LIGHT_GRAY_DYE, "azure_bluet_to_light_gray_dye")
		flower(Items.OXEYE_DAISY, Items.LIGHT_GRAY_DYE, "oxeye_daisy_to_light_gray_dye")
		flower(Items.WITHER_ROSE, Items.BLACK_DYE, "wither_rose_to_black_dye")
		flower(Items.COCOA_BEANS, Items.BROWN_DYE, "cocoa_beans_to_brown_dye")
		flower(Items.BEETROOT, Items.RED_DYE, "beetroot_to_red_dye")
		flower(Items.POPPY, Items.RED_DYE, "poppy_to_red_dye")
		flower(Items.RED_TULIP, Items.RED_DYE, "red_tulip_to_red_dye")
		tallFlower(Items.ROSE_BUSH, Items.RED_DYE, "rose_bush_to_red_dye")
		flower(Items.ORANGE_TULIP, Items.ORANGE_DYE, "orange_tulip_to_orange_dye")
		flower(Items.TORCHFLOWER, Items.ORANGE_DYE, "torchflower_to_orange_dye")
		flower(Items.DANDELION, Items.YELLOW_DYE, "dandelion_to_yellow_dye")
		tallFlower(Items.SUNFLOWER, Items.YELLOW_DYE, "sunflower_to_yellow_dye")
		flower(Items.CACTUS, Items.GREEN_DYE, "cactus_to_green_dye")
		tallFlower(Items.PITCHER_PLANT, Items.CYAN_DYE, "pitcher_plant_to_cyan_dye")
		flower(Items.BLUE_ORCHID, Items.BLUE_DYE, "blue_orchid_to_blue_dye")
		flower(Items.CORNFLOWER, Items.BLUE_DYE, "cornflower_to_blue_dye")
		flower(Items.ALLIUM, Items.MAGENTA_DYE, "allium_to_magenta_dye")
		tallFlower(Items.LILAC, Items.MAGENTA_DYE, "lilac_to_magenta_dye")
		tallFlower(Items.PEONY, Items.PINK_DYE, "peony_to_pink_dye")
		flower(Items.PINK_TULIP, Items.PINK_DYE, "pink_tulip_to_pink_dye")

		fun carpet(carpet: Item, dye: Item, name: String) = recipe(carpet.asIngredient(), Items.STRING.withCount(3), dye.defaultInstance, 0.1f, name)

		carpet(Items.WHITE_CARPET, Items.WHITE_DYE, "white_carpet")
		carpet(Items.LIGHT_GRAY_CARPET, Items.LIGHT_GRAY_DYE, "light_gray_carpet")
		carpet(Items.GRAY_CARPET, Items.GRAY_DYE, "gray_carpet")
		carpet(Items.BLACK_CARPET, Items.BLACK_DYE, "black_carpet")
		carpet(Items.BROWN_CARPET, Items.BROWN_DYE, "brown_carpet")
		carpet(Items.RED_CARPET, Items.RED_DYE, "red_carpet")
		carpet(Items.ORANGE_CARPET, Items.ORANGE_DYE, "orange_carpet")
		carpet(Items.YELLOW_CARPET, Items.YELLOW_DYE, "yellow_carpet")
		carpet(Items.GREEN_CARPET, Items.GREEN_DYE, "green_carpet")
		carpet(Items.CYAN_CARPET, Items.CYAN_DYE, "cyan_carpet")
		carpet(Items.BLUE_CARPET, Items.BLUE_DYE, "blue_carpet")
		carpet(Items.MAGENTA_CARPET, Items.MAGENTA_DYE, "magenta_carpet")
		carpet(Items.PINK_CARPET, Items.PINK_DYE, "pink_carpet")
	}

	private fun buildUnstableRecipes(recipeOutput: RecipeOutput) {

		fun unstable(output: ItemStack): SpecialShapedRecipeBuilder {
			return SpecialShapedRecipeBuilder(output)
				.type("unstable", ::ShapedUnstableRecipe)
		}

		fun unstable(output: ItemLike): SpecialShapedRecipeBuilder {
			return unstable(output.asItem().defaultInstance)
		}

		unstable(ModItems.MOON_STONE.withCount(9))
			.pattern(
				"LLL",
				"LIL",
				"LLL"
			)
			.define('L', ModItems.LUNAR_REACTIVE_DUST.asIngredient())
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.save(recipeOutput, modLoc("moon_stone_from_unstable_ingot"))

		unstable(ModBlocks.BLOCK_OF_UNSTABLE_INGOT)
			.pattern(
				"III",
				"III",
				"III"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.save(recipeOutput, modLoc("block_of_unstable_ingot_from_unstable_ingots"))

		unstable(ModBlocks.DEEP_DARK_PORTAL)
			.pattern(
				"4I4",
				"I5I",
				"4I4"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('4', ModBlocks.getCompressedCobblestone(4).asIngredient())
			.define('5', ModBlocks.getCompressedCobblestone(5).asIngredient())
			.save(recipeOutput)

		for (color in DyeColor.entries) {
			val bricks = ModBlocks.getColoredBricks(color)
			val lapisCaelestis = ModBlocks.getLapisCaelestis(color)

			unstable(lapisCaelestis.toStack(4))
				.pattern(
					"LLL",
					"LIL",
					"LLL"
				)
				.define('L', bricks.asIngredient())
				.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
				.save(recipeOutput)
		}

		fun angelRing(type: AngelRingItem.Type, base: Ingredient) {
			unstable(type.getStack())
				.pattern(
					"GIG",
					"ISI",
					"BUH"
				)
				.define('G', base)
				.define('I', Tags.Items.INGOTS_GOLD.asIngredient())
				.define('S', ModItems.RING_OF_THE_FLYING_SQUID.asIngredient())
				.define(
					'B', ModItems.GOLDEN_LASSO.withComponent(
						ModDataComponents.ENTITY_TYPE.get(),
						EntityType.BAT.builtInRegistryHolder()
					).asIngredient()
				)
				.define(
					'H', ModItems.CURSED_LASSO.withComponent(
						ModDataComponents.ENTITY_TYPE.get(),
						EntityType.GHAST.builtInRegistryHolder()
					).asIngredient()
				)
				.define('U', ModItems.UNSTABLE_INGOT.asIngredient())
				.save(recipeOutput, ExcessiveUtilities.modResource("angel_ring_${type.id}"))

			shapedRecipe(
				type.getStack(),
				"CR,C ",
				mapOf(
					'C' to base,
					'R' to ModItems.ANGEL_RING.asIngredient()
				)
			).save(recipeOutput, ExcessiveUtilities.modResource("angel_ring_conversion_${type.id}"))
		}

		angelRing(AngelRingItem.Type.INVISIBLE, Tags.Items.GLASS_BLOCKS.asIngredient())
		angelRing(AngelRingItem.Type.FEATHER, Tags.Items.FEATHERS.asIngredient())
		angelRing(AngelRingItem.Type.BUTTERFLY, Tags.Items.DYES_PINK.asIngredient())
		angelRing(AngelRingItem.Type.DEMON, Tags.Items.LEATHERS.asIngredient())
		angelRing(AngelRingItem.Type.GOLD, Tags.Items.NUGGETS_GOLD.asIngredient())
		angelRing(AngelRingItem.Type.BAT, ItemTags.COALS.asIngredient())

		unstable(ModItems.HEALING_AXE)
			.pattern(
				"II",
				"IO",
				" O"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('O', Tags.Items.OBSIDIANS_NORMAL.asIngredient())
			.save(recipeOutput)

		unstable(ModItems.REVERSING_HOE)
			.pattern(
				"II",
				"O ",
				"O "
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('O', Tags.Items.OBSIDIANS_NORMAL.asIngredient())
			.save(recipeOutput)

		unstable(ModItems.ETHERIC_SWORD)
			.pattern(
				"I",
				"I",
				"O"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('O', Tags.Items.OBSIDIANS_NORMAL.asIngredient())
			.save(recipeOutput)

		unstable(ModItems.EROSION_SHOVEL)
			.pattern(
				"I",
				"O",
				"O"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('O', Tags.Items.OBSIDIANS_NORMAL.asIngredient())
			.save(recipeOutput)

		unstable(ModItems.DESTRUCTION_PICKAXE)
			.pattern(
				"III",
				" O ",
				" O "
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('O', Tags.Items.OBSIDIANS_NORMAL.asIngredient())
			.save(recipeOutput)

		unstable(ModItems.PRECISION_SHEARS)
			.pattern(
				"AI",
				"IA"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('A', ModBlocks.ANGEL_BLOCK.asIngredient())
			.save(recipeOutput)

		unstable(ModItems.SONAR_GOGGLES)
			.pattern(
				"III",
				"EIE"
			)
			.define('I', ModItems.UNSTABLE_INGOT.asIngredient())
			.define('E', Items.ENDER_EYE.asIngredient())
			.save(recipeOutput)

		fun division(output: ItemStack): SpecialShapedRecipeBuilder {
			return SpecialShapedRecipeBuilder(output)
				.type("division", ::ShapedDivisionRecipe)
		}

		division(
			ModItems.UNSTABLE_INGOT.withComponent(
				ModDataComponents.COUNTDOWN.get(),
				UnstableIngotItem.MAX_COUNTDOWN
			)
		)
			.pattern(
				"I",
				"S",
				"D"
			)
			.define('I', Tags.Items.INGOTS_IRON.asIngredient())
			.define('S', ModItems.DIVISION_SIGIL.asIngredient())
			.define('D', Tags.Items.GEMS_DIAMOND.asIngredient())
			.save(recipeOutput)

		division(ModItems.SEMI_UNSTABLE_NUGGET.getDefaultInstance())
			.pattern(
				"N",
				"S",
				"D"
			)
			.define('N', Tags.Items.NUGGETS_IRON.asIngredient())
			.define('S', ModItems.DIVISION_SIGIL.asIngredient())
			.define('D', Tags.Items.GEMS_DIAMOND.asIngredient())
			.save(recipeOutput)


		SpecialShapedRecipeBuilder(ModItems.ENDER_SHARD.toStack(8))
			.type("damage_glass_cutter", ::DamageGlassCutterRecipe)
			.pattern("P", "C")
			.define('P', Tags.Items.ENDER_PEARLS.asIngredient())
			.define('C', ModItems.GLASS_CUTTER.asIngredient())
			.save(recipeOutput)

	}

	private fun buildCompressedBlockRecipes(recipeOutput: RecipeOutput) {
		val types = mapOf(
			"cobblestone" to ModBlocks.COMPRESSED_COBBLESTONES,
			"dirt" to ModBlocks.COMPRESSED_DIRTS,
			"sand" to ModBlocks.COMPRESSED_SANDS,
			"gravel" to ModBlocks.COMPRESSED_GRAVELS,
		)

		for ((name, list) in types) {
			for (i in list.indices) {
				val level = i + 1
				val nextLevel = level + 1
				if (nextLevel > list.size) break

				val current = list[i].get()
				val next = list[i + 1].get()

				shapedRecipe(
					next,
					"CCC,CCC,CCC",
					mapOf('C' to current.asIngredient())
				).save(recipeOutput, modLoc("compressed_${name}/${level}_to_${nextLevel}"))

				shapelessRecipe(
					current,
					9,
					listOf(next.asIngredient())
				).save(recipeOutput, modLoc("compressed_${name}/${nextLevel}_to_${level}"))
			}
		}

		shapedRecipe(
			ModBlocks.getCompressedCobblestone(1),
			"CCC,CCC,CCC",
			mapOf('C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient())
		).save(recipeOutput, modLoc("compressed_cobblestone/from_cobblestone"))

		shapelessRecipe(
			Items.COBBLESTONE,
			9,
			listOf(ModBlocks.getCompressedCobblestone(1).asIngredient())
		).save(recipeOutput, modLoc("compressed_cobblestone/to_cobblestone"))

		shapedRecipe(
			ModBlocks.getCompressedGravel(1),
			"CCC,CCC,CCC",
			mapOf('C' to Tags.Items.GRAVELS.asIngredient())
		).save(recipeOutput, modLoc("compressed_gravel/from_gravel"))

		shapelessRecipe(
			Items.GRAVEL,
			9,
			listOf(ModBlocks.getCompressedGravel(1).asIngredient())
		).save(recipeOutput, modLoc("compressed_gravel/to_gravel"))

		shapedRecipe(
			ModBlocks.getCompressedDirt(1),
			"CCC,CCC,CCC",
			mapOf('C' to ItemTags.DIRT.asIngredient())
		).save(recipeOutput, modLoc("compressed_dirt/from_dirt"))

		shapelessRecipe(
			Items.DIRT,
			9,
			listOf(ModBlocks.getCompressedDirt(1).asIngredient())
		).save(recipeOutput, modLoc("compressed_dirt/to_dirt"))

		shapedRecipe(
			ModBlocks.getCompressedSand(1),
			"CCC,CCC,CCC",
			mapOf('C' to ItemTags.SAND.asIngredient())
		).save(recipeOutput, modLoc("compressed_sand/from_sand"))

		shapelessRecipe(
			Items.SAND,
			9,
			listOf(ModBlocks.getCompressedSand(1).asIngredient())
		).save(recipeOutput, modLoc("compressed_sand/to_sand"))
	}

}