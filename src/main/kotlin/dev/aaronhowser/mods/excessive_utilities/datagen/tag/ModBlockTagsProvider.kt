package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.add
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : BlockTagsProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(
				ModBlocks.COMPRESSED_BLOCK,
				ModBlocks.ANGEL_BLOCK,
				ModBlocks.CONVEYOR_BELT,
				ModBlocks.ENDER_COLLECTOR,
				ModBlocks.ENDER_CORE,
				ModBlocks.DEEP_DARK_PORTAL,
				ModBlocks.LAST_MILLENNIUM_PORTAL,
				ModBlocks.CREATIVE_HARVEST,
				ModBlocks.MAGICAL_SNOW_GLOBE,
				ModBlocks.RESTURBED_MOB_SPAWNER,
				ModBlocks.SCANNER,
				ModBlocks.MOON_STORE_ORE,
				ModBlocks.DEEPSLATE_MOON_STONE_ORE,
				ModBlocks.REDSTONE_LANTERN,
				ModBlocks.MECHANICAL_MINER,
				ModBlocks.MECHANICAL_USER,
				ModBlocks.REDSTONE_CLOCK,
				ModBlocks.WIRELESS_FE_BATTERY,
				ModBlocks.WIRELESS_FE_TRANSMITTER,
				ModBlocks.RESONATOR,
				ModBlocks.ENDER_QUARRY,
				ModBlocks.ENDER_MARKER,
				ModBlocks.QED,
				ModBlocks.ENDER_FLUX_CRYSTAL,
				ModBlocks.MACHINE_BLOCK,
				ModBlocks.CRUSHER,
				ModBlocks.FURNACE,
				ModBlocks.ENCHANTER,
				ModBlocks.QUANTUM_QUARRY,
				ModBlocks.QUANTUM_QUARRY_ACTUATOR,
				ModBlocks.ENDER_QUARRY_UPGRADE_BASE,
				ModBlocks.ENDER_QUARRY_FORTUNE_UPGRADE,
				ModBlocks.ENDER_QUARRY_FORTUNE_TWO_UPGRADE,
				ModBlocks.ENDER_QUARRY_FORTUNE_THREE_UPGRADE,
				ModBlocks.ENDER_QUARRY_SILK_TOUCH_UPGRADE,
				ModBlocks.ENDER_QUARRY_SPEED_UPGRADE,
				ModBlocks.ENDER_QUARRY_SPEED_TWO_UPGRADE,
				ModBlocks.ENDER_QUARRY_SPEED_THREE_UPGRADE,
				ModBlocks.ENDER_QUARRY_WORLD_HOLE_UPGRADE,
				ModBlocks.STONE_SPIKE,
				ModBlocks.IRON_SPIKE,
				ModBlocks.GOLDEN_SPIKE,
				ModBlocks.DIAMOND_SPIKE,
				ModBlocks.NETHERITE_SPIKE,
				ModBlocks.CREATIVE_SPIKE,
				ModBlocks.PLAYER_CHEST,
				ModBlocks.FILING_CABINET,
				ModBlocks.ADVANCED_FILING_CABINET,
				ModBlocks.STONE_DRUM,
				ModBlocks.IRON_DRUM,
				ModBlocks.REINFORCED_LARGE_DRUM,
				ModBlocks.DEMONICALLY_GARGANTUAN_DRUM,
				ModBlocks.BEDROCKIUM_DRUM,
				ModBlocks.CREATIVE_DRUM,
				ModBlocks.CREATIVE_CHEST,
				ModBlocks.TRASH_CAN,
				ModBlocks.TRASH_CAN_CHEST,
				ModBlocks.TRASH_CAN_ENERGY,
				ModBlocks.TRASH_CAN_FLUID,
				ModBlocks.TRANSFER_PIPE,
				ModBlocks.ITEM_TRANSFER_NODE,
				ModBlocks.FLUID_TRANSFER_NODE,
				ModBlocks.ENERGY_TRANSFER_NODE,
				ModBlocks.ITEM_RETRIEVAL_NODE,
				ModBlocks.FLUID_RETRIEVAL_NODE,
				ModBlocks.ENERGY_RETRIEVAL_NODE,
				ModBlocks.TRANSFER_FILTER,
				ModBlocks.TRANSFER_PIPE_FILTER,
				ModBlocks.CREATIVE_ENERGY_SOURCE,
				ModBlocks.SURVIVAL_GENERATOR,
				ModBlocks.FURNACE_GENERATOR,
				ModBlocks.MAGMATIC_GENERATOR,
				ModBlocks.ENDER_GENERATOR,
				ModBlocks.HEATED_REDSTONE_GENERATOR,
				ModBlocks.CULINARY_GENERATOR,
				ModBlocks.POTION_GENERATOR,
				ModBlocks.EXPLOSIVE_GENERATOR,
				ModBlocks.PINK_GENERATOR,
				ModBlocks.OVERCLOCKED_GENERATOR,
				ModBlocks.NETHER_STAR_GENERATOR,
				ModBlocks.DISENCHANTMENT_GENERATOR,
				ModBlocks.FROSTY_GENERATOR,
				ModBlocks.HALITOSIS_GENERATOR,
				ModBlocks.SLIMY_GENERATOR,
				ModBlocks.DEATH_GENERATOR,
				ModBlocks.RAINBOW_GENERATOR,
				ModBlocks.MANUAL_MILL,
				ModBlocks.WATER_MILL,
				ModBlocks.WIND_MILL,
				ModBlocks.FIRE_MILL,
				ModBlocks.LAVA_MILL,
				ModBlocks.SOLAR_PANEL,
				ModBlocks.LUNAR_PANEL,
				ModBlocks.DRAGON_EGG_MILL,
				ModBlocks.CREATIVE_MILL,
				ModBlocks.TERRAFORMER,
				ModBlocks.CLIMOGRAPH_BASE_UNIT,
				ModBlocks.ANTENNA,
				ModBlocks.COOLER,
				ModBlocks.DEHOSTILIFIER,
				ModBlocks.DEHUMIDIFIER,
				ModBlocks.HEATER,
				ModBlocks.HUMIDIFIER,
				ModBlocks.MAGIC_ABSORPTION,
				ModBlocks.MAGIC_INFUSER,
				ModBlocks.BLOCK_OF_BEDROCKIUM,
				ModBlocks.BLOCK_OF_UNSTABLE_INGOT,
				ModBlocks.BLOCK_OF_EVIL_INFUSED_IRON,
				ModBlocks.BLOCK_OF_DEMON_METAL,
				ModBlocks.BLOCK_OF_ENCHANTED_METAL,
				ModBlocks.ENDER_INFUSED_OBSIDIAN,
				ModBlocks.POLISHED_STONE,
				ModBlocks.BORDER_STONE,
				ModBlocks.CROSSED_STONE,
				ModBlocks.EMINENCE_STONE,
				ModBlocks.DIAMOND_ETCHED_COMPUTATIONAL_MATRIX,
				ModBlocks.EDGED_STONE_BRICKS,
				ModBlocks.ENDER_SAND_ALLOY,
				ModBlocks.FROSTED_STONE,
				ModBlocks.GRAVEL_BRICKS,
				ModBlocks.GRAVEL_ROAD,
				ModBlocks.LAPIS_CAELESTIS,
				ModBlocks.BEDROCK_BRICKS,
				ModBlocks.BEDROCK_COBBLESTONE,
				ModBlocks.BEDROCK_SLABS,
				ModBlocks.BLUE_QUARTZ,
				ModBlocks.STONEBURNT,
				ModBlocks.TRUCHET,
				ModBlocks.QUARTZBURNT,
				ModBlocks.RAINBOW_STONE,
				ModBlocks.CARVED_GLASS,
				ModBlocks.EDGED_GLASS,
				ModBlocks.GOLDEN_EDGED_GLASS,
				ModBlocks.GLOWING_GLASS,
				ModBlocks.HEART_GLASS,
				ModBlocks.OBSIDIAN_GLASS,
				ModBlocks.REINFORCED_DARK_GLASS,
				ModBlocks.SANDY_GLASS,
				ModBlocks.SQUARE_GLASS,
				ModBlocks.SWIRLING_GLASS,
				ModBlocks.THICKENED_GLASS,
				ModBlocks.THICKENED_GLASS_BORDERED,
				ModBlocks.THICKENED_GLASS_PATTERNED,
				ModBlocks.REDSTONE_GLASS,
				ModBlocks.ETHEREAL_GLASS,
				ModBlocks.INVERTED_ETHEREAL_GLASS,
				ModBlocks.INEFFABLE_GLASS,
				ModBlocks.INVERTED_INEFFABLE_GLASS,
				ModBlocks.DARK_INEFFABLE_GLASS,
			)
			.add(*ModBlocks.COLORED_STONES.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_COBBLESTONES.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_STONE_BRICKS.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_BRICKS.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_COAL_BLOCKS.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_REDSTONE_BLOCKS.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_LAPIS_BLOCKS.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_OBSIDIANS.map { it.value }.toTypedArray())
			.add(*ModBlocks.COLORED_QUARTZES.map { it.value }.toTypedArray())

		tag(Tags.Blocks.GLASS_BLOCKS)
			.add(
				ModBlocks.CARVED_GLASS,
				ModBlocks.EDGED_GLASS,
				ModBlocks.GOLDEN_EDGED_GLASS,
				ModBlocks.GLOWING_GLASS,
				ModBlocks.HEART_GLASS,
				ModBlocks.OBSIDIAN_GLASS,
				ModBlocks.REINFORCED_DARK_GLASS,
				ModBlocks.SQUARE_GLASS,
				ModBlocks.SWIRLING_GLASS,
				ModBlocks.THICKENED_GLASS,
				ModBlocks.THICKENED_GLASS_BORDERED,
				ModBlocks.THICKENED_GLASS_PATTERNED,
				ModBlocks.REDSTONE_GLASS,
				ModBlocks.ETHEREAL_GLASS,
				ModBlocks.INVERTED_ETHEREAL_GLASS,
				ModBlocks.INEFFABLE_GLASS,
				ModBlocks.INVERTED_INEFFABLE_GLASS,
				ModBlocks.DARK_INEFFABLE_GLASS,
			)

		tag(BlockTags.MINEABLE_WITH_SHOVEL)
			.add(
				ModBlocks.CURSED_EARTH
			)
			.add(*ModBlocks.COLORED_SOUL_SANDS.map { it.value }.toTypedArray())

		tag(BlockTags.MINEABLE_WITH_AXE)
			.add(
				ModBlocks.MAGICAL_WOOD,
				ModBlocks.MAGNUM_TORCH,
				ModBlocks.PEACEFUL_TABLE,
				ModBlocks.TRADING_POST,
				ModBlocks.WOODEN_SPIKE,
				ModBlocks.SLIGHTLY_LARGER_CHEST,
				ModBlocks.MINI_CHEST,
				ModBlocks.MAGICAL_PLANKS,
				ModBlocks.DIAGONAL_WOOD
			)
			.add(*ModBlocks.COLORED_PLANKS.map { it.value }.toTypedArray())

		tag(SICKLE_MINEABLE)
			.addTags(
				BlockTags.FLOWERS,
				BlockTags.LEAVES
			)
			.add(
				Blocks.SHORT_GRASS,
				Blocks.TALL_GRASS,
				Blocks.FERN,
				Blocks.LARGE_FERN
			)

		tag(RENDER_GP_WHILE_LOOKING_AT)
			.add(
				ModBlocks.MANUAL_MILL,
				ModBlocks.WATER_MILL,
				ModBlocks.WIND_MILL,
				ModBlocks.FIRE_MILL,
				ModBlocks.LAVA_MILL,
				ModBlocks.SOLAR_PANEL,
				ModBlocks.LUNAR_PANEL,
				ModBlocks.DRAGON_EGG_MILL,
				ModBlocks.CREATIVE_MILL,
				ModBlocks.RESONATOR,
				ModBlocks.WIRELESS_FE_TRANSMITTER,
				ModBlocks.ENCHANTER
			)

		tag(VALID_FOR_DRAGON_EGG_MILL)
			.add(
				Blocks.DRAGON_EGG
			)

		tag(CREATIVE_HARVEST_BLACKLIST)
		tag(ENDER_QUARRY_BLACKLIST)
			.add(
				Blocks.DIRT,
				Blocks.BEDROCK,
				Blocks.COBBLESTONE
			)

		tag(ENDER_QUARRY_PART)
			.add(
				ModBlocks.ENDER_QUARRY,
				ModBlocks.ENDER_QUARRY_UPGRADE_BASE,
				ModBlocks.ENDER_QUARRY_FORTUNE_UPGRADE,
				ModBlocks.ENDER_QUARRY_FORTUNE_TWO_UPGRADE,
				ModBlocks.ENDER_QUARRY_FORTUNE_THREE_UPGRADE,
				ModBlocks.ENDER_QUARRY_SILK_TOUCH_UPGRADE,
				ModBlocks.ENDER_QUARRY_SPEED_UPGRADE,
				ModBlocks.ENDER_QUARRY_SPEED_TWO_UPGRADE,
				ModBlocks.ENDER_QUARRY_SPEED_THREE_UPGRADE,
				ModBlocks.ENDER_QUARRY_WORLD_HOLE_UPGRADE
			)

		tag(FIRE_AXE_MINEABLE)
			.addTag(BlockTags.LOGS)

		tag(DESTRUCTION_PICKAXE_TARGET)
			.addTags(
				Tags.Blocks.STONES,
				Tags.Blocks.NETHERRACKS
			)

		tag(EROSION_SHOVEL_TARGET)
			.addTags(
				BlockTags.DIRT,
				Tags.Blocks.SANDS,
				Tags.Blocks.GRAVELS
			)
			.add(
				Blocks.SOUL_SAND
			)

		tag(COLLECTABLE_BY_BOOMEREAPERANG)
			.addTag(BlockTags.FLOWERS)
			.add(
				Blocks.SHORT_GRASS,
				Blocks.TALL_GRASS,
				Blocks.FERN,
				Blocks.LARGE_FERN
			)

		tag(CURSED_EARTH_REPLACEABLE)
			.addTags(
				BlockTags.DIRT
			)
			.remove(
				ModBlocks.CURSED_EARTH.get()
			)

		tag(BlockTags.DIRT)
			.add(ModBlocks.CURSED_EARTH.get())

		tag(GLASS_CUTTER_MINEABLE)
			.addTags(
				Tags.Blocks.GLASS_BLOCKS,
				Tags.Blocks.GLASS_PANES
			)

		tag(BlockTags.LOGS)
			.add(ModBlocks.MAGICAL_WOOD)

		tag(BlockTags.PLANKS)
			.add(ModBlocks.MAGICAL_PLANKS)

		for (color in DyeColor.entries) {
			val tagName = common("dyed/${color.serializedName}")

			val stone = ModBlocks.getColoredStone(color).get()
			val cobble = ModBlocks.getColoredCobblestone(color).get()
			val stoneBricks = ModBlocks.getColoredStoneBricks(color).get()
			val bricks = ModBlocks.getColoredBricks(color).get()
			val planks = ModBlocks.getColoredPlanks(color).get()
			val coal = ModBlocks.getColoredCoalBlock(color).get()
			val redstone = ModBlocks.getColoredRedstoneBlock(color).get()
			val lapis = ModBlocks.getColoredLapisBlock(color).get()
			val obsidian = ModBlocks.getColoredObsidian(color).get()
			val quartz = ModBlocks.getColoredQuartz(color).get()
			val soulSand = ModBlocks.getColoredSoulSand(color).get()
			val glowstone = ModBlocks.getColoredGlowstone(color).get()
			val redstoneLamp = ModBlocks.getColoredRedstoneLamp(color).get()

			tag(tagName)
				.add(
					stone,
					cobble,
					stoneBricks,
					bricks,
					planks,
					coal,
					redstone,
					lapis,
					obsidian,
					quartz,
					soulSand,
					glowstone,
					redstoneLamp
				)

			tag(PAINTBRUSH_STONES).add(stone)
			tag(PAINTBRUSH_COBBLESTONES).add(cobble)
			tag(PAINTBRUSH_STONE_BRICKS).add(stoneBricks)
			tag(PAINTBRUSH_BRICKS).add(bricks)
			tag(PAINTBRUSH_COAL_BLOCKS).add(coal)
			tag(PAINTBRUSH_LAPIS_BLOCKS).add(lapis)
			tag(PAINTBRUSH_PLANKS).add(planks)
			tag(PAINTBRUSH_OBSIDIANS).add(obsidian)
			tag(PAINTBRUSH_QUARTZES).add(quartz)
			tag(PAINTBRUSH_GLOWSTONES).add(glowstone)
			tag(PAINTBRUSH_REDSTONE_BLOCKS).add(redstone)
			tag(PAINTBRUSH_REDSTONE_LAMPS).add(redstoneLamp)
			tag(PAINTBRUSH_SOUL_SANDS).add(soulSand)
		}

		tag(PAINTBRUSH_STONES)
			.addTag(Tags.Blocks.STONES)

		tag(PAINTBRUSH_COBBLESTONES)
			.addTag(Tags.Blocks.COBBLESTONES_NORMAL)

		tag(PAINTBRUSH_STONE_BRICKS)
			.addTag(BlockTags.STONE_BRICKS)

		tag(PAINTBRUSH_BRICKS)
			.add(Blocks.BRICKS)

		tag(PAINTBRUSH_COAL_BLOCKS)
			.addTag(Tags.Blocks.STORAGE_BLOCKS_COAL)

		tag(PAINTBRUSH_LAPIS_BLOCKS)
			.addTag(Tags.Blocks.STORAGE_BLOCKS_LAPIS)

		tag(PAINTBRUSH_OBSIDIANS)
			.addTag(Tags.Blocks.OBSIDIANS_NORMAL)

		tag(PAINTBRUSH_PLANKS)
			.addTag(BlockTags.PLANKS)

		tag(PAINTBRUSH_REDSTONE_BLOCKS)
			.addTag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE)

		tag(PAINTBRUSH_SOUL_SANDS)
			.add(Blocks.SOUL_SAND)

		tag(PAINTBRUSH_WOOLS)
			.add(
				Blocks.WHITE_WOOL,
				Blocks.ORANGE_WOOL,
				Blocks.MAGENTA_WOOL,
				Blocks.LIGHT_BLUE_WOOL,
				Blocks.YELLOW_WOOL,
				Blocks.LIME_WOOL,
				Blocks.PINK_WOOL,
				Blocks.GRAY_WOOL,
				Blocks.LIGHT_GRAY_WOOL,
				Blocks.CYAN_WOOL,
				Blocks.PURPLE_WOOL,
				Blocks.BLUE_WOOL,
				Blocks.BROWN_WOOL,
				Blocks.GREEN_WOOL,
				Blocks.RED_WOOL,
				Blocks.BLACK_WOOL
			)

		tag(Tags.Blocks.DYED)
			.add(*ModBlocks.COLORED_STONES.values.toTypedArray())
			.add(*ModBlocks.COLORED_COBBLESTONES.values.toTypedArray())
			.add(*ModBlocks.COLORED_STONE_BRICKS.values.toTypedArray())
			.add(*ModBlocks.COLORED_BRICKS.values.toTypedArray())
			.add(*ModBlocks.COLORED_PLANKS.values.toTypedArray())
			.add(*ModBlocks.COLORED_COAL_BLOCKS.values.toTypedArray())
			.add(*ModBlocks.COLORED_REDSTONE_BLOCKS.values.toTypedArray())
			.add(*ModBlocks.COLORED_LAPIS_BLOCKS.values.toTypedArray())
			.add(*ModBlocks.COLORED_OBSIDIANS.values.toTypedArray())
			.add(*ModBlocks.COLORED_QUARTZES.values.toTypedArray())
			.add(*ModBlocks.COLORED_SOUL_SANDS.values.toTypedArray())

		tag(Tags.Blocks.STONES)
			.add(*ModBlocks.COLORED_STONES.values.toTypedArray())

		tag(Tags.Blocks.COBBLESTONES_NORMAL)
			.add(*ModBlocks.COLORED_COBBLESTONES.values.toTypedArray())

		tag(BlockTags.STONE_BRICKS)
			.add(*ModBlocks.COLORED_STONE_BRICKS.values.toTypedArray())

		tag(Tags.Blocks.STORAGE_BLOCKS_COAL)
			.add(*ModBlocks.COLORED_COAL_BLOCKS.values.toTypedArray())

		tag(Tags.Blocks.STORAGE_BLOCKS_LAPIS)
			.add(*ModBlocks.COLORED_LAPIS_BLOCKS.values.toTypedArray())

		tag(Tags.Blocks.OBSIDIANS_NORMAL)
			.add(*ModBlocks.COLORED_OBSIDIANS.values.toTypedArray())

		tag(BlockTags.PLANKS)
			.add(*ModBlocks.COLORED_PLANKS.values.toTypedArray())

		tag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE)
			.add(*ModBlocks.COLORED_REDSTONE_BLOCKS.values.toTypedArray())

		tag(BlockTags.SOUL_SPEED_BLOCKS)
			.add(*ModBlocks.COLORED_SOUL_SANDS.values.toTypedArray())

		tag(BlockTags.SOUL_FIRE_BASE_BLOCKS)
			.add(*ModBlocks.COLORED_SOUL_SANDS.values.toTypedArray())

		tag(BlockTags.WITHER_SUMMON_BASE_BLOCKS)
			.add(*ModBlocks.COLORED_SOUL_SANDS.values.toTypedArray())

		tag(FE_TRANSMITTER_BLACKLIST)
			.add(
				ModBlocks.WIRELESS_FE_BATTERY,
				ModBlocks.WIRELESS_FE_TRANSMITTER
			)

		tag(ENDER_PORCUPINE_BLACKLIST)
			.add(
				ModBlocks.ENDER_PORCUPINE
			)

		tag(PAINTBRUSH_BLACKLIST)

		tag(PAINTBRUSH_WOOLS)
			.add(
				Blocks.WHITE_WOOL,
				Blocks.ORANGE_WOOL,
				Blocks.MAGENTA_WOOL,
				Blocks.LIGHT_BLUE_WOOL,
				Blocks.YELLOW_WOOL,
				Blocks.LIME_WOOL,
				Blocks.PINK_WOOL,
				Blocks.GRAY_WOOL,
				Blocks.LIGHT_GRAY_WOOL,
				Blocks.CYAN_WOOL,
				Blocks.PURPLE_WOOL,
				Blocks.BLUE_WOOL,
				Blocks.BROWN_WOOL,
				Blocks.GREEN_WOOL,
				Blocks.RED_WOOL,
				Blocks.BLACK_WOOL
			)
	}

	companion object {
		private fun create(id: String): TagKey<Block> = BlockTags.create(ExcessiveUtilities.modResource(id))
		private fun common(id: String): TagKey<Block> = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", id))

		val SICKLE_MINEABLE = common("mineable/sickle")
		val GLASS_CUTTER_MINEABLE = create("mineable/glass_cutter")
		val FIRE_AXE_MINEABLE = create("mineable/fire_axe")

		val DESTRUCTION_PICKAXE_TARGET = create("destruction_pickaxe_target")
		val EROSION_SHOVEL_TARGET = create("erosion_shovel_target")

		val RENDER_GP_WHILE_LOOKING_AT = create("render_gp_while_looking_at")
		val VALID_FOR_DRAGON_EGG_MILL = create("valid_for_dragon_egg_mill")

		val CREATIVE_HARVEST_BLACKLIST = create("creative_harvest_blacklist")
		val ENDER_QUARRY_BLACKLIST = create("ender_quarry_blacklist")
		val PAINTBRUSH_BLACKLIST = create("paintbrush_blacklist")
		val FE_TRANSMITTER_BLACKLIST = create("fe_transmitter_blacklist")
		val ENDER_PORCUPINE_BLACKLIST = create("ender_porcupine_blacklist")

		val ENDER_QUARRY_PART = create("ender_quarry_part")
		val COLLECTABLE_BY_BOOMEREAPERANG = create("collectable_by_boomereaperang")
		val CURSED_EARTH_REPLACEABLE = create("cursed_earth_replaceable")

		val PAINTBRUSH_WOOLS = create("paintbrush_wools")
		val PAINTBRUSH_STONES = create("paintbrush_stones")
		val PAINTBRUSH_COBBLESTONES = create("paintbrush_cobblestones")
		val PAINTBRUSH_STONE_BRICKS = create("paintbrush_stone_bricks")
		val PAINTBRUSH_BRICKS = create("paintbrush_bricks")
		val PAINTBRUSH_COAL_BLOCKS = create("paintbrush_coal_blocks")
		val PAINTBRUSH_LAPIS_BLOCKS = create("paintbrush_lapis_blocks")
		val PAINTBRUSH_PLANKS = create("paintbrush_planks")
		val PAINTBRUSH_OBSIDIANS = create("paintbrush_obsidians")
		val PAINTBRUSH_QUARTZES = create("paintbrush_quartzes")
		val PAINTBRUSH_GLOWSTONES = create("paintbrush_glowstones")
		val PAINTBRUSH_REDSTONE_BLOCKS = create("paintbrush_redstone_blocks")
		val PAINTBRUSH_REDSTONE_LAMPS = create("paintbrush_redstone_lamps")
		val PAINTBRUSH_SOUL_SANDS = create("paintbrush_soul_sands")

	}

}