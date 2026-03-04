package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.add
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
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
				ModBlocks.HIGH_TEMPERATURE_FURNACE_GENERATOR,
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

		tag(MINEABLE_WITH_SICKLE)
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
			)

		tag(EROSION_SHOVEL_TARGET)
			.addTags(
				BlockTags.DIRT,
				Tags.Blocks.SANDS,
				Tags.Blocks.GRAVELS
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
	}

	companion object {
		private fun create(id: String): TagKey<Block> = BlockTags.create(ExcessiveUtilities.modResource(id))
		private fun common(id: String): TagKey<Block> = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", id))

		val MINEABLE_WITH_SICKLE = common("mineable/sickle")
		val RENDER_GP_WHILE_LOOKING_AT = create("render_gp_while_looking_at")
		val VALID_FOR_DRAGON_EGG_MILL = create("valid_for_dragon_egg_mill")
		val CREATIVE_HARVEST_BLACKLIST = create("creative_harvest_blacklist")
		val ENDER_QUARRY_BLACKLIST = create("ender_quarry_blacklist")
		val ENDER_QUARRY_PART = create("ender_quarry_part")
		val FIRE_AXE_MINEABLE = create("mineable/fire_axe")
		val DESTRUCTION_PICKAXE_TARGET = create("destruction_pickaxe_target")
		val EROSION_SHOVEL_TARGET = create("erosion_shovel_target")
		val COLLECTABLE_BY_BOOMEREAPERANG = create("collectable_by_boomereaperang")
		val CURSED_EARTH_REPLACEABLE = create("cursed_earth_replaceable")
		val GLASS_CUTTER_MINEABLE = create("mineable/glass_cutter")
	}

}