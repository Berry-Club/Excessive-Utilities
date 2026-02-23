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
				ModBlocks.ADVANCED_OBSERVER,
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
				ModBlocks.DEEPSLATE_MOON_STONE_ORE
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
				ModBlocks.TRADING_POST
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
	}

}