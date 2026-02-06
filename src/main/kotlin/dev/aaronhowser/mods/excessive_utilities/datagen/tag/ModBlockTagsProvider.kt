package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : BlockTagsProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
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
				ModBlocks.MANUAL_MILL.get(),
				ModBlocks.WATER_MILL.get(),
				ModBlocks.WIND_MILL.get(),
				ModBlocks.FIRE_MILL.get(),
				ModBlocks.LAVA_MILL.get(),
				ModBlocks.SOLAR_PANEL.get(),
				ModBlocks.LUNAR_PANEL.get(),
				ModBlocks.DRAGON_EGG_MILL.get(),
				ModBlocks.CREATIVE_MILL.get(),
				ModBlocks.RESONATOR.get(),
				ModBlocks.WIRELESS_FE_TRANSMITTER.get()
			)

		tag(VALID_FOR_DRAGON_EGG_MILL)
			.add(
				Blocks.DRAGON_EGG
			)
	}

	companion object {
		private fun create(id: String): TagKey<Block> = BlockTags.create(ExcessiveUtilities.modResource(id))
		private fun common(id: String): TagKey<Block> = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", id))

		val MINEABLE_WITH_SICKLE = common("mineable/sickle")
		val RENDER_GP_WHILE_LOOKING_AT = create("render_gp_while_looking_at")
		val VALID_FOR_DRAGON_EGG_MILL = create("valid_for_dragon_egg_mill")
	}

}