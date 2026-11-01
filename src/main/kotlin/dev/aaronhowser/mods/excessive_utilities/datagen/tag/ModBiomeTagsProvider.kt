package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModBiomes
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModBiomeTagsProvider(
	output: PackOutput,
	provider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : BiomeTagsProvider(output, provider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(BIOME_MARKER_BLACKLIST)
		tag(QUANTUM_QUARRY_RANDOM_BLACKLIST)
			.add(Biomes.THE_VOID)
		tag(Tags.Biomes.IS_OVERWORLD)
			.add(ModBiomes.THE_DEPTHS)
	}

	companion object {
		private fun create(name: String): TagKey<Biome> {
			return TagKey.create(Registries.BIOME, ExcessiveUtilities.modResource(name))
		}

		val BIOME_MARKER_BLACKLIST = create("biome_marker_blacklist")
		val QUANTUM_QUARRY_RANDOM_BLACKLIST = create("quantum_quarry_random_blacklist")

	}

}