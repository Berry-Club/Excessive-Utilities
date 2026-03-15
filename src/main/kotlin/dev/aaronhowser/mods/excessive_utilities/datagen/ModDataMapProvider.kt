package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorItemFuel
import dev.aaronhowser.mods.excessive_utilities.datamap.MagmaticGeneratorFuel
import dev.aaronhowser.mods.excessive_utilities.datamap.NetherLavaDunkConversion
import dev.aaronhowser.mods.excessive_utilities.datamap.ReversingHoeConversion
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {

		fun addFuel(
			map: DataMapType<Item, GeneratorItemFuel>,
			item: ItemLike,
			fePerTick: Int,
			burnTime: Int
		) {
			builder(map).add(
				item.asItem().builtInRegistryHolder(),
				GeneratorItemFuel(fePerTick, burnTime),
				false
			)
		}

		fun addFuel(
			map: DataMapType<Item, GeneratorItemFuel>,
			itemTag: TagKey<Item>,
			fePerTick: Int,
			burnTime: Int
		) {
			builder(map).add(
				itemTag,
				GeneratorItemFuel(fePerTick, burnTime),
				false
			)
		}

		builder(MagmaticGeneratorFuel.DATA_MAP)
			.add(
				Tags.Fluids.LAVA,
				MagmaticGeneratorFuel(100_000 / 20, 125 * 20),
				false
			)

		builder(NetherLavaDunkConversion.DATA_MAP)
			.add(
				Tags.Items.INGOTS_GOLD,
				NetherLavaDunkConversion(ModItems.DEMON_INGOT.getDefaultInstance()),
				false
			)
			.add(
				Tags.Items.STORAGE_BLOCKS_GOLD,
				NetherLavaDunkConversion(ModItems.BLOCK_OF_DEMON_METAL.getDefaultInstance()),
				false
			)

		builder(ReversingHoeConversion.DATA_MAP)
			.add(
				Tags.Blocks.COBBLESTONES,
				ReversingHoeConversion(Blocks.STONE.defaultBlockState()),
				false
			)
			.add(
				Tags.Blocks.GRAVELS,
				ReversingHoeConversion(Blocks.COBBLESTONE.defaultBlockState()),
				false
			)
			.add(
				Tags.Blocks.SANDS,
				ReversingHoeConversion(Blocks.GRAVEL.defaultBlockState()),
				false
			)
			.add(
				Blocks.MAGMA_BLOCK.builtInRegistryHolder(),
				ReversingHoeConversion(Blocks.LAVA.defaultBlockState()),
				false
			)
			.add(
				Blocks.OBSIDIAN.builtInRegistryHolder(),
				ReversingHoeConversion(Blocks.LAVA.defaultBlockState()),
				false
			)
			.add(
				BlockTags.TERRACOTTA,
				ReversingHoeConversion(Blocks.CLAY.defaultBlockState()),
				false
			)
			.add(
				Blocks.FARMLAND.builtInRegistryHolder(),
				ReversingHoeConversion(Blocks.DIRT.defaultBlockState()),
				false
			)
			.add(
				Blocks.DIRT.builtInRegistryHolder(),
				ReversingHoeConversion(Blocks.GRASS_BLOCK.defaultBlockState()),
				false
			)
			.add(
				Blocks.DEAD_BUSH.builtInRegistryHolder(),
				ReversingHoeConversion(Blocks.OAK_SAPLING.defaultBlockState()),
				false
			)
	}

}