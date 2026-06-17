package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModBiomes
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBiomeTagsProvider(
	pOutput: PackOutput,
	pProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : BiomeTagsProvider(pOutput, pProvider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(BIOME_MARKER_BLACKLIST)
		tag(BiomeTags.HAS_MINESHAFT).add(ModBiomes.DEEP_DARK)
		tag(BiomeTags.HAS_DESERT_PYRAMID).add(ModBiomes.DEEP_DARK)
	}

	companion object {
		private fun create(name: String): TagKey<Biome> {
			return TagKey.create(Registries.BIOME, ExcessiveUtilities.modResource(name))
		}

		val BIOME_MARKER_BLACKLIST = create("biome_marker_blacklist")

	}

}
