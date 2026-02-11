package dev.aaronhowser.mods.excessive_utilities.datagen.recipe

import dev.aaronhowser.mods.aaron.datagen.AaronRecipeProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.asIngredient
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
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
		buildEnchanterRecipes(recipeOutput)
		buildNamedRecipes(recipeOutput)
		buildSmeltingRecipes(recipeOutput)
		buildQedRecipes(recipeOutput)
	}

	private fun buildShapedRecipes(recipeOutput: RecipeOutput) {
		val perfectOpiniumCore = OpiniumCoreContentsComponent.getDefaultTiers().last().getStack()

		val recipes = listOf(
			shapedRecipe(
				ModBlocks.MAGICAL_SNOW_GLOBE.toStack(),
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
			),
			shapedRecipe(
				ModItems.BUILDERS_WAND.toStack(),
				"  G, W ,W  ",
				mapOf(
					'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
					'W' to ModBlocks.MAGICAL_WOOD.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.DESTRUCTION_WAND.toStack(),
				" GG, WG,W  ",
				mapOf(
					'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
					'W' to ModBlocks.MAGICAL_WOOD.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.TRASH_CAN.toStack(),
				"SSS,CHC,CCC",
				mapOf(
					'S' to Tags.Items.STONES.asIngredient(),
					'H' to Tags.Items.CHESTS_WOODEN.asIngredient(),
					'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.TRASH_CAN_FLUID.toStack(),
				"SSS,CBC,CCC",
				mapOf(
					'S' to Tags.Items.STONES.asIngredient(),
					'B' to Items.BUCKET.asIngredient(),
					'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.TRASH_CAN_ENERGY.toStack(),
				"SSS,CRC,CCC",
				mapOf(
					'S' to Tags.Items.STONES.asIngredient(),
					'R' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
					'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.ANGEL_BLOCK.toStack(),
				" G ,FOF",
				mapOf(
					'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
					'F' to Tags.Items.FEATHERS.asIngredient(),
					'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.SOLAR_PANEL.toStack(3),
				"LLL,PRP",
				mapOf(
					'L' to Tags.Items.GEMS_LAPIS.asIngredient(),
					'P' to ModBlocks.POLISHED_STONE.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.LUNAR_PANEL.toStack(3),
				"LLL,PRP",
				mapOf(
					'L' to ModItems.LUNAR_REACTIVE_DUST.asIngredient(),
					'P' to ModBlocks.POLISHED_STONE.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.LAVA_MILL.toStack(),
				"SSS,SRS,SGS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'G' to Tags.Items.INGOTS_GOLD.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.WATER_MILL.toStack(),
				"SSS,GRG,SSS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'G' to ModItems.REDSTONE_GEAR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.WIND_MILL.toStack(),
				"SSS, GR,SSS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'G' to ModItems.REDSTONE_GEAR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.FIRE_MILL.toStack(),
				"SRS,SGS,SFS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'G' to ModItems.REDSTONE_GEAR.asIngredient(),
					'F' to Tags.Items.FENCES_NETHER_BRICK.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.MANUAL_MILL.toStack(),
				" G ,SRS",
				mapOf(
					'G' to ModItems.REDSTONE_GEAR.asIngredient(),
					'S' to ModBlocks.POLISHED_STONE.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.DRAGON_EGG_MILL.toStack(),
				"SGS,NGN,SIS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'G' to ModItems.REDSTONE_GEAR.asIngredient(),
					'N' to Items.NETHER_STAR.asIngredient(),
					'I' to ModItems.EYE_OF_REDSTONE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.REDSTONE_CLOCK.toStack(),
				"SRS,RTR,SRS",
				mapOf(
					'S' to Tags.Items.STONES.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'T' to Blocks.REDSTONE_TORCH.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.GLASS_CUTTER.toStack(),
				"  I, SI,I  ",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.RESONATOR.toStack(),
				"DCD,IRI,III",
				mapOf(
					'D' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'C' to Tags.Items.STORAGE_BLOCKS_COAL.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.REDSTONE_GEAR.toStack(),
				" T ,TPT, T ",
				mapOf(
					'T' to Items.REDSTONE_TORCH.asIngredient(),
					'P' to ItemTags.PLANKS.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.MOON_STONE,
				"LLL,LDL,LLL",
				mapOf(
					'L' to ModItems.LUNAR_REACTIVE_DUST.asIngredient(),
					'D' to Tags.Items.GEMS_DIAMOND.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.SPEED_UPGRADE_MAGICAL,
				"AIA,IUI,AIA",
				mapOf(
					'A' to ModItems.MAGICAL_APPLE.asIngredient(),
					'I' to ModItems.ENCHANTED_INGOT.asIngredient(),
					'U' to ModItems.SPEED_UPGRADE.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.SPEED_UPGRADE_ULTIMATE,
				"EIE,IUI,EIE",
				mapOf(
					'E' to ModItems.DROP_OF_EVIL.asIngredient(),
					'I' to ModItems.EVIL_INFUSED_IRON_INGOT.asIngredient(),
					'U' to ModItems.SPEED_UPGRADE_MAGICAL.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.CHICKEN_WING_RING,
				"FIF,ILI,RIR",
				mapOf(
					'F' to Tags.Items.FEATHERS.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'L' to
							ModItems.GOLDEN_LASSO.withComponent(
								ModDataComponents.ENTITY_TYPE.get(),
								EntityType.CHICKEN.builtInRegistryHolder()
							).asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.RING_OF_THE_FLYING_SQUID,
				"IDI,LCE,IDI",
				mapOf(
					'I' to Items.INK_SAC.asIngredient(),
					'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
					'L' to
							ModItems.GOLDEN_LASSO.withComponent(
								ModDataComponents.ENTITY_TYPE.get(),
								EntityType.SQUID.builtInRegistryHolder()
							).asIngredient(),
					'C' to ModItems.CHICKEN_WING_RING.asIngredient(),
					'E' to Tags.Items.ENDER_PEARLS.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.ANGEL_RING,
				"GIG,ISI,BIH",
				mapOf(
					'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
					'I' to Tags.Items.INGOTS_GOLD.asIngredient(),
					'S' to ModItems.RING_OF_THE_FLYING_SQUID.asIngredient(),
					'B' to
							ModItems.GOLDEN_LASSO.withComponent(
								ModDataComponents.ENTITY_TYPE.get(),
								EntityType.BAT.builtInRegistryHolder()
							).asIngredient(),
					'H' to
							ModItems.CURSED_LASSO.withComponent(
								ModDataComponents.ENTITY_TYPE.get(),
								EntityType.GHAST.builtInRegistryHolder()
							).asIngredient()
				)
			),
			shapedRecipe(
				ModItems.POWER_MANAGER,
				" R,SS,SS",
				mapOf(
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'S' to Tags.Items.STONES.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.BAG_OF_HOLDING,
				"GGG,CMC,GGG",
				mapOf(
					'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
					'C' to Tags.Items.CHESTS_WOODEN.asIngredient(),
					'M' to ModBlocks.MAGICAL_WOOD.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.THICKENED_GLASS_BORDERED,
				4,
				"GG,GG",
				mapOf(
					'G' to ModBlocks.THICKENED_GLASS.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.THICKENED_GLASS_PATTERNED,
				4,
				"GG,GG",
				mapOf(
					'G' to ModBlocks.THICKENED_GLASS_BORDERED.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.ETHEREAL_GLASS,
				8,
				"GGG,GMG,GGG",
				mapOf(
					'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
					'M' to ModItems.MOON_STONE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.INEFFABLE_GLASS,
				8,
				"GGG,GMG,GGG",
				mapOf(
					'G' to ModBlocks.THICKENED_GLASS.asIngredient(),
					'M' to ModItems.MOON_STONE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.DARK_INEFFABLE_GLASS,
				8,
				"GGG,GMG,GGG",
				mapOf(
					'G' to Blocks.TINTED_GLASS.asIngredient(),
					'M' to ModItems.MOON_STONE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.TRANSFER_PIPE,
				64,
				"SSS,GRG,SSS",
				mapOf(
					'S' to Items.STONE_SLAB.asIngredient(),
					'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.PLAYER_CHEST,
				"SSS,SES,SRS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'E' to Tags.Items.CHESTS_ENDER.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.WRENCH,
				" DI, IR,I  ",
				mapOf(
					'D' to Tags.Items.DYES_RED.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.MAGICAL_BOOMERANG,
				" M ,M M",
				mapOf(
					'M' to ModBlocks.MAGICAL_WOOD.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.ITEM_FILTER,
				"RSR,STS,RSR",
				mapOf(
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient(),
					'T' to Tags.Items.STRINGS.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.FLUID_FILTER,
				"LSL,STS,LSL",
				mapOf(
					'L' to Tags.Items.GEMS_LAPIS.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient(),
					'T' to Tags.Items.STRINGS.asIngredient()
				)
			),
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
			),
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
			),
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
			),
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
			),
			shapedRecipe(
				ModBlocks.INDEXER,
				"SRS,SFS,SRS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'F' to Items.ITEM_FRAME.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.INDEXER_REMOTE,
				"SES,SFS,SES",
				mapOf(
					'S' to Tags.Items.STONES.asIngredient(),
					'E' to ModItems.EYE_OF_REDSTONE.asIngredient(),
					'F' to Items.ITEM_FRAME.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.STONE_DRUM,
				"CSC,CBC,CSC",
				mapOf(
					'S' to Tags.Items.COBBLESTONES_NORMAL.asIngredient(),
					'C' to Blocks.COBBLESTONE_SLAB.asIngredient(),
					'B' to Items.BOWL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.IRON_DRUM,
				"IPI,ICI,IPI",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'P' to Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE.asIngredient(),
					'C' to Blocks.CAULDRON.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.REINFORCED_LARGE_DRUM,
				"DPD,DID,DPD",
				mapOf(
					'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
					'P' to Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.asIngredient(),
					'I' to ModBlocks.IRON_DRUM.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.DEMONICALLY_GARGANTUAN_DRUM,
				"DKD,DRD,DKD",
				mapOf(
					'D' to ModItems.DEMON_INGOT.asIngredient(),
					'K' to ModItems.KLEIN_BOTTLE.asIngredient(),
					'R' to ModBlocks.REINFORCED_LARGE_DRUM.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.BEDROCKIUM_DRUM,
				"DBD,DRD,DBD",
				mapOf(
					'D' to ModItems.BEDROCKIUM_INGOT.asIngredient(),
					'B' to ModBlocks.BEDROCKIUM_DRUM.asIngredient(),
					'R' to ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.MACHINE_BLOCK,
				4,
				"IRI,RCR,IRI",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'C' to Tags.Items.CHESTS_WOODEN.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.FURNACE,
				"BBB,BMB,BBB",
				mapOf(
					'B' to Tags.Items.BRICKS.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.CRUSHER,
				"IPI,IMI,IPI",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'P' to ModItemTagsProvider.PISTONS.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.ENCHANTER,
				" E ,DMD,III",
				mapOf(
					'E' to Items.ENCHANTED_BOOK.asIngredient(),
					'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.SURVIVAL_GENERATOR,
				"CCC,CIC,RFR",
				mapOf(
					'C' to Tags.Items.COBBLESTONES_NORMAL.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'F' to Items.FURNACE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.FURNACE_GENERATOR,
				"III,IMI,RFR",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'F' to Items.FURNACE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.CULINARY_GENERATOR,
				"PPP,PFP,RMR",
				mapOf(
					'P' to Tags.Items.CROPS.asIngredient(),
					'F' to Tags.Items.FOODS_COOKED_MEAT.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.MAGMATIC_GENERATOR,
				"GGG,GLG,RMR",
				mapOf(
					'G' to Tags.Items.INGOTS_GOLD.asIngredient(),
					'L' to Items.LAVA_BUCKET.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.HEATED_REDSTONE_GENERATOR,
				"RRR,RBR,RMR",
				mapOf(
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'B' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
					'M' to ModBlocks.MAGMATIC_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.ENDER_GENERATOR,
				"EEE,EOE,RMR",
				mapOf(
					'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
					'O' to Tags.Items.OBSIDIANS_NORMAL.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.POTION_GENERATOR,
				"BBB,BSB,RMR",
				mapOf(
					'B' to Tags.Items.RODS_BLAZE.asIngredient(),
					'S' to Blocks.BREWING_STAND.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.PINK_GENERATOR,
				"DDD,DWD,RMR",
				mapOf(
					'D' to Tags.Items.DYES_PINK.asIngredient(),
					'W' to Items.PINK_WOOL.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.HIGH_TEMPERATURE_FURNACE_GENERATOR,
				"III,ICI,RMR",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'C' to Blocks.COAL_BLOCK.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.EXPLOSIVE_GENERATOR,
				"GGG,GTG,RMR",
				mapOf(
					'G' to Tags.Items.GUNPOWDERS.asIngredient(),
					'T' to Blocks.TNT.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.NETHER_STAR_GENERATOR,
				"WWW,WSW,RMR",
				mapOf(
					'W' to Items.WITHER_SKELETON_SKULL.asIngredient(),
					'S' to Items.NETHER_STAR.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.HALITOSIS_GENERATOR,
				"PPP,PEP,RMR",
				mapOf(
					'P' to Blocks.PURPUR_BLOCK.asIngredient(),
					'E' to Blocks.END_ROD.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.FROSTY_GENERATOR,
				"SSS,SIS,RMR",
				mapOf(
					'S' to Items.SNOWBALL.asIngredient(),
					'I' to Items.ICE.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.DEATH_GENERATOR,
				"CCC,CEC,RMR",
				mapOf(
					'C' to ModItemTagsProvider.CORPSE_PARTS.asIngredient(),
					'E' to Items.SPIDER_EYE.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.DISENCHANTMENT_GENERATOR,
				"WWW,WEW,RMR",
				mapOf(
					'W' to ModBlocks.MAGICAL_WOOD.asIngredient(),
					'E' to Blocks.ENCHANTING_TABLE.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.SLIMY_GENERATOR,
				"sss,sSs,RMR",
				mapOf(
					's' to Tags.Items.SLIME_BALLS.asIngredient(),
					'S' to Blocks.SLIME_BLOCK.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'M' to ModBlocks.FURNACE_GENERATOR.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.DEEP_DARK_PORTAL,
				"CCC,C C,CCC",
				mapOf(
					'C' to ModBlocks.COMPRESSED_BLOCK.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.WIRELESS_FE_TRANSMITTER,
				4,
				"R,S",
				mapOf(
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					'S' to ModBlocks.STONEBURNT.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.WIRELESS_FE_BATTERY,
				"SSS,BRB,SSS",
				mapOf(
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'B' to Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.QUANTUM_QUARRY_ACTUATOR,
				"ERE,EDE,SSS",
				mapOf(
					'E' to Tags.Items.END_STONES.asIngredient(),
					'R' to Blocks.END_ROD.asIngredient(),
					'D' to Items.DIAMOND_PICKAXE.asIngredient(),
					'S' to ModBlocks.STONEBURNT.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.QUANTUM_QUARRY,
				"ESE,SGS,ESE",
				mapOf(
					'E' to Tags.Items.END_STONES.asIngredient(),
					'S' to ModBlocks.STONEBURNT.asIngredient(),
					'G' to ModBlocks.MAGICAL_SNOW_GLOBE.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.REDSTONE_LANTERN,
				"RSR,SCS,RPR",
				mapOf(
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'S' to ModBlocks.POLISHED_STONE.asIngredient(),
					'C' to ItemTags.COALS.asIngredient(),
					'P' to Items.REPEATER.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.SLIGHTLY_LARGER_CHEST,
				"SSS,SCS,SSS",
				mapOf(
					'S' to Tags.Items.RODS_WOODEN.asIngredient(),
					'C' to Tags.Items.CHESTS_WOODEN.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.MAGICAL_APPLE,
				"AAA,AWA,AAA",
				mapOf(
					'A' to Items.APPLE.asIngredient(),
					'W' to ModBlocks.MAGICAL_WOOD.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.TROWEL,
				"  I, B ,S  ",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'B' to Items.STONE_BUTTON.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.BIOME_MARKER,
				"PIP,ISI,PIP",
				mapOf(
					'P' to Tags.Items.DYES_PURPLE.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'S' to ItemTags.SAPLINGS.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.TERRAFORMER,
				"ECE,RPR",
				mapOf(
					'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
					'C' to ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
					'R' to Tags.Items.DUSTS_REDSTONE.asIngredient(),
					'P' to Items.REPEATER.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.ANTENNA,
				"E E,ISI, I ",
				mapOf(
					'E' to Blocks.END_ROD.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'S' to ItemTags.SAPLINGS.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.CLIMOGRAPH_BASE_UNIT,
				"IDI,SMS,IDI",
				mapOf(
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'D' to Tags.Items.GEMS_DIAMOND.asIngredient(),
					'S' to ItemTags.SAPLINGS.asIngredient(),
					'M' to ModBlocks.MACHINE_BLOCK.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.KIKOKU,
				"O,O,S",
				mapOf(
					'O' to perfectOpiniumCore.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.FLAT_TRANSFER_NODE_ITEMS,
				8,
				"A, ,N",
				mapOf(
					'A' to ItemTags.ANVIL.asIngredient(),
					'N' to ModBlocks.ITEM_TRANSFER_NODE.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.FLAT_TRANSFER_NODE_FLUIDS,
				8,
				"A, ,N",
				mapOf(
					'A' to ItemTags.ANVIL.asIngredient(),
					'N' to ModBlocks.FLUID_TRANSFER_NODE.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.COMPOUND_BOW,
				" OS,I S, OS",
				mapOf(
					'O' to perfectOpiniumCore.asIngredient(),
					'I' to Tags.Items.INGOTS_IRON.asIngredient(),
					'S' to Tags.Items.STRINGS.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.FIRE_AXE,
				"OO,OS, S",
				mapOf(
					'O' to perfectOpiniumCore.asIngredient(),
					'S' to Tags.Items.RODS_WOODEN.asIngredient()
				)
			),
			shapedRecipe(
				ModBlocks.ENDER_PORCUPINE,
				"SES,EME,SES",
				mapOf(
					'S' to Tags.Items.STRINGS.asIngredient(),
					'E' to Tags.Items.ENDER_PEARLS.asIngredient(),
					'M' to ModItems.POWER_MANAGER.asIngredient()
				)
			),
			shapedRecipe(
				ModItems.LUX_SABER,
				"EGE,ECE,ERE",
				mapOf(
					'E' to ModItems.EVIL_INFUSED_IRON_INGOT.asIngredient(),
					'G' to Tags.Items.GLASS_BLOCKS.asIngredient(),
					'C' to ModItems.SUN_CRYSTAL.asIngredient(),
					'R' to ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
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
			ModBlocks.COMPRESSED_BLOCK.asIngredient()
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

	}

	private fun buildShapelessRecipes(recipeOutput: RecipeOutput) {
		val recipes = listOf(
			shapelessRecipe(
				ModBlocks.SOUND_MUFFLER,
				listOf(
					ItemTags.WOOL.asIngredient(),
					Blocks.NOTE_BLOCK.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.MAGICAL_PLANKS,
				4,
				listOf(
					ModBlocks.MAGICAL_WOOD.asIngredient()
				)
			),
			shapelessRecipe(
				ModItems.ENDER_SHARD,
				8,
				listOf(
					Tags.Items.ENDER_PEARLS.asIngredient(),
					ModItems.GLASS_CUTTER.asIngredient() // TODO: Damage the glass cutter
				)
			),
			shapelessRecipe(
				ModItems.SUN_CRYSTAL,
				listOf(
					Tags.Items.GEMS_DIAMOND.asIngredient(),
					Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
					Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
					Tags.Items.DUSTS_GLOWSTONE.asIngredient(),
					Tags.Items.DUSTS_GLOWSTONE.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.BORDER_STONE,
				4,
				listOf(
					Tags.Items.STONES.asIngredient(),
					Tags.Items.STONES.asIngredient(),
					Items.STONE_BRICKS.asIngredient(),
					Items.STONE_BRICKS.asIngredient(),
				)
			),
			shapelessRecipe(
				ModBlocks.CROSSED_STONE,
				4,
				listOf(
					ModBlocks.BORDER_STONE.asIngredient(),
					ModBlocks.BORDER_STONE.asIngredient(),
					ModBlocks.BORDER_STONE.asIngredient(),
					ModBlocks.BORDER_STONE.asIngredient(),
				)
			),
			shapelessRecipe(
				ModBlocks.POLISHED_STONE,
				4,
				listOf(
					Blocks.CHISELED_STONE_BRICKS.asIngredient(),
					Blocks.CHISELED_STONE_BRICKS.asIngredient(),
					Blocks.CHISELED_STONE_BRICKS.asIngredient(),
					Blocks.CHISELED_STONE_BRICKS.asIngredient(),
				)
			),
			shapelessRecipe(
				ModBlocks.SANDY_GLASS,
				4,
				listOf(
					Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
					Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
					Tags.Items.SANDS.asIngredient(),
					Tags.Items.SANDS.asIngredient(),
				)
			),
			shapelessRecipe(
				ModBlocks.TRUCHET,
				4,
				listOf(
					ModBlocks.POLISHED_STONE.asIngredient(),
					ModBlocks.POLISHED_STONE.asIngredient(),
					ModBlocks.BORDER_STONE.asIngredient(),
					ModBlocks.BORDER_STONE.asIngredient(),
				)
			),
			shapelessRecipe(
				ModItems.RESONATING_REDSTONE_CRYSTAL,
				listOf(
					ModItems.ENDER_SHARD.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient(),
				)
			),
			shapelessRecipe(
				ModItems.EYE_OF_REDSTONE,
				listOf(
					Tags.Items.ENDER_PEARLS.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient(),
					ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient()
				)
			),
			shapelessRecipe(
				ModItems.SPEED_UPGRADE,
				listOf(
					ModItems.UPGRADE_BASE.asIngredient(),
					Tags.Items.INGOTS_GOLD.asIngredient(),
					Tags.Items.STORAGE_BLOCKS_REDSTONE.asIngredient()
				)
			),
			shapelessRecipe(
				ModItems.STACK_UPGRADE,
				listOf(
					ModItems.UPGRADE_BASE.asIngredient(),
					Tags.Items.INGOTS_GOLD.asIngredient(),
					Tags.Items.GEMS_DIAMOND.asIngredient()
				)
			),
			shapelessRecipe(
				ModItems.WORLD_INTERACTION_UPGRADE,
				listOf(
					ModItems.UPGRADE_BASE.asIngredient(),
					Items.GOLDEN_PICKAXE.asIngredient()
				)
			),
			shapelessRecipe(
				ModItems.CURSED_LASSO,
				listOf(
					ModItems.GOLDEN_LASSO.asIngredient(),
					ModItems.DROP_OF_EVIL.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.GLOWING_GLASS,
				2,
				listOf(
					Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
					Tags.Items.GLASS_BLOCKS_COLORLESS.asIngredient(),
					Tags.Items.DUSTS_GLOWSTONE.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.INVERTED_ETHEREAL_GLASS,
				listOf(
					ModBlocks.ETHEREAL_GLASS.asIngredient(),
					Items.REDSTONE_TORCH.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.INVERTED_INEFFABLE_GLASS,
				listOf(
					ModBlocks.INEFFABLE_GLASS.asIngredient(),
					Items.REDSTONE_TORCH.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.TRANSFER_PIPE_FILTER,
				4,
				listOf(
					ModBlocks.TRANSFER_PIPE.asIngredient(),
					ModItems.ITEM_FILTER.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.SCANNER,
				listOf(
					Blocks.OBSERVER.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient(),
					Items.SPIDER_EYE.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.MECHANICAL_MINER,
				listOf(
					Blocks.DROPPER.asIngredient(),
					ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					Items.IRON_PICKAXE.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.MECHANICAL_USER,
				listOf(
					Blocks.DROPPER.asIngredient(),
					ModItems.RESONATING_REDSTONE_CRYSTAL.asIngredient(),
					Items.LEVER.asIngredient()
				)
			),
			shapelessRecipe(
				ModItems.KLEIN_BOTTLE,
				listOf(
					Items.GLASS_BOTTLE.asIngredient(),
					Tags.Items.ENDER_PEARLS.asIngredient(),
					Tags.Items.ENDER_PEARLS.asIngredient(),
				)
			),
			shapelessRecipe(
				ModBlocks.MINI_CHEST,
				9,
				listOf(Tags.Items.CHESTS_WOODEN.asIngredient())
			),
			shapelessRecipe(
				ModBlocks.COOLER,
				listOf(
					ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
					Items.SNOWBALL.asIngredient(),
					Items.SNOWBALL.asIngredient(),
				)
			),
			shapelessRecipe(
				ModBlocks.HUMIDIFIER,
				listOf(
					ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
					Items.WATER_BUCKET.asIngredient(),
					Items.WATER_BUCKET.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.DEHUMIDIFIER,
				listOf(
					ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
					Tags.Items.SANDS.asIngredient(),
					Tags.Items.SANDS.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.HEATER,
				listOf(
					ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
					Items.LAVA_BUCKET.asIngredient(),
					Items.LAVA_BUCKET.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.DEHOSTILIFIER,
				listOf(
					ModBlocks.CLIMOGRAPH_BASE_UNIT.asIngredient(),
					Items.MYCELIUM.asIngredient(),
					Items.MYCELIUM.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.MAGIC_ABSORPTION,
				listOf(
					ModBlocks.MAGICAL_WOOD.asIngredient(),
					ItemTags.ANVIL.asIngredient(),
					ItemTags.ANVIL.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.MAGIC_INFUSER,
				listOf(
					ModBlocks.MAGICAL_WOOD.asIngredient(),
					Items.ENCHANTING_TABLE.asIngredient(),
					Items.ENCHANTING_TABLE.asIngredient()
				)
			),
			shapelessRecipe(
				ModBlocks.TRASH_CAN_CHEST,
				listOf(
					ModBlocks.TRASH_CAN.asIngredient(),
					Tags.Items.CHESTS_WOODEN.asIngredient(),
					Tags.Items.DUSTS_REDSTONE.asIngredient()
				)
			)
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}
	}

	private fun buildEnchanterRecipes(recipeOutput: RecipeOutput) {
		val recipes = listOf(
			EnchanterRecipeBuilder(
				Tags.Items.BOOKSHELVES.asIngredient(),
				1,
				Tags.Items.GEMS_LAPIS.asIngredient(),
				1,
				64_00 / (80 * 20),
				80 * 20,
				ModBlocks.MAGICAL_WOOD.toStack()
			),
			EnchanterRecipeBuilder(
				Tags.Items.STORAGE_BLOCKS_GOLD.asIngredient(),
				1,
				Tags.Items.GEMS_LAPIS.asIngredient(),
				9,
				24_000 / (20 * 30),
				20 * 30,
				ModBlocks.BLOCK_OF_ENCHANTED_METAL.toStack()
			),
			EnchanterRecipeBuilder(
				Tags.Items.INGOTS_GOLD.asIngredient(),
				1,
				Tags.Items.GEMS_LAPIS.asIngredient(),
				1,
				8_000 / (20 * 10),
				20 * 10,
				ModItems.ENCHANTED_INGOT.toStack()
			),
			EnchanterRecipeBuilder(
				Tags.Items.STORAGE_BLOCKS_IRON.asIngredient(),
				8,
				Items.NETHER_STAR.asIngredient(),
				1,
				192_000 / (20 * 240),
				20 * 240,
				ModBlocks.BLOCK_OF_EVIL_INFUSED_IRON.toStack(8)
			),
			EnchanterRecipeBuilder(
				Tags.Items.INGOTS_IRON.asIngredient(),
				8,
				Items.NETHER_STAR.asIngredient(),
				1,
				64_000 / (20 * 20),
				20 * 20,
				ModItems.EVIL_INFUSED_IRON_INGOT.toStack(8)
			),
			EnchanterRecipeBuilder(
				Items.APPLE.asIngredient(),
				16,
				Tags.Items.GEMS_LAPIS.asIngredient(),
				1,
				16_000 / (20 * 80),
				20 * 80,
				ModItems.MAGICAL_APPLE.toStack(16)
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

	private fun buildNamedRecipes(recipeOutput: RecipeOutput) {
		shapelessRecipe(
			ModItems.WATERING_CAN,
			listOf(
				ModItems.WATERING_CAN
					.withComponent(ModDataComponents.IS_BROKEN.get(), Unit.INSTANCE)
					.asIngredient()
			)
		).save(recipeOutput, modLoc("watering_can_repair"))

		shapedRecipe(
			ModItems.MOON_STONE,
			9,
			"LLL,LIL,LLL",
			mapOf(
				'L' to ModItems.LUNAR_REACTIVE_DUST.asIngredient(),
				'I' to ModItems.UNSTABLE_INGOT.asIngredient()
			)
		).save(recipeOutput, modLoc("moon_stone_from_unstable_ingot"))

		nineBlockStorageRecipes(
			recipeOutput,
			RecipeCategory.MISC,
			ModItems.DEMON_INGOT,
			RecipeCategory.MISC,
			ModBlocks.BLOCK_OF_DEMON_METAL
		)

		nineBlockStorageRecipes(
			recipeOutput,
			RecipeCategory.MISC,
			ModItems.ENCHANTED_INGOT,
			RecipeCategory.MISC,
			ModBlocks.BLOCK_OF_ENCHANTED_METAL
		)

		nineBlockStorageRecipes(
			recipeOutput,
			RecipeCategory.MISC,
			ModItems.EVIL_INFUSED_IRON_INGOT,
			RecipeCategory.MISC,
			ModBlocks.BLOCK_OF_EVIL_INFUSED_IRON
		)

		nineBlockStorageRecipes(
			recipeOutput,
			RecipeCategory.MISC,
			ModItems.BEDROCKIUM_INGOT,
			RecipeCategory.MISC,
			ModBlocks.BLOCK_OF_BEDROCKIUM
		)

		nineBlockStorageRecipes(
			recipeOutput,
			RecipeCategory.MISC,
			ModItems.UNSTABLE_INGOT,
			RecipeCategory.MISC,
			ModBlocks.BLOCK_OF_UNSTABLE_INGOT
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
		val recipes = listOf(
			QEDRecipeBuilder(ModBlocks.ITEM_RETRIEVAL_NODE.toStack())
				.pattern(
					" E ",
					"NMN",
					" E "
				)
				.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
				.define('N', ModBlocks.ITEM_TRANSFER_NODE.asIngredient())
				.define('M', Tags.Items.GEMS_EMERALD.asIngredient()),
			QEDRecipeBuilder(ModBlocks.FLUID_RETRIEVAL_NODE.toStack())
				.pattern(
					" E ",
					"NDN",
					" E "
				)
				.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
				.define('N', ModBlocks.FLUID_TRANSFER_NODE.asIngredient())
				.define('D', Tags.Items.GEMS_DIAMOND.asIngredient()),
			QEDRecipeBuilder(ModBlocks.ENERGY_RETRIEVAL_NODE.toStack())
				.pattern(
					" E ",
					"NRN",
					" E "
				)
				.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
				.define('N', ModBlocks.ENERGY_TRANSFER_NODE.asIngredient())
				.define('R', Tags.Items.DUSTS_REDSTONE.asIngredient()),
			QEDRecipeBuilder(ModItems.ENDER_TRANSMITTER.toStack())
				.pattern(
					"ETE",
					"QEQ",
					"ETE"
				)
				.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
				.define('T', Items.REDSTONE_TORCH.asIngredient())
				.define('Q', Tags.Items.GEMS_QUARTZ.asIngredient()),
			QEDRecipeBuilder(ModItems.ENDER_RECEIVER.toStack())
				.pattern(
					"ERE",
					"QEQ",
					"EQE"
				)
				.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
				.define('R', Tags.Items.DUSTS_REDSTONE.asIngredient())
				.define('Q', Tags.Items.GEMS_QUARTZ.asIngredient()),
			QEDRecipeBuilder(ModBlocks.ENDER_QUARRY_UPGRADE_BASE.toStack())
				.pattern(
					" O ",
					"OQO",
					" O "
				)
				.define('O', ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient())
				.define('Q', ModBlocks.QUARTZBURNT.asIngredient()),
			QEDRecipeBuilder(ModBlocks.MAGNUM_TORCH.toStack())
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
				.define('L', ItemTags.LOGS.asIngredient()),
			QEDRecipeBuilder(ModBlocks.ENDER_MARKER.toStack())
				.pattern(
					"E",
					"O",
					"O"
				)
				.define('E', Tags.Items.ENDER_PEARLS.asIngredient())
				.define('O', ModBlocks.ENDER_INFUSED_OBSIDIAN.asIngredient())
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}
	}

	private fun modLoc(name: String) = ExcessiveUtilities.modResource(name)

}