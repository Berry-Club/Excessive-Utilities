package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.registryHolder
import dev.aaronhowser.mods.excessive_utilities.datamap.InversionRitualEnemyWeight
import dev.aaronhowser.mods.excessive_utilities.datamap.NetherLavaDunkConversion
import dev.aaronhowser.mods.excessive_utilities.datamap.ReversingHoeConversion
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {

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
			.add(
				Tags.Items.NUGGETS_GOLD,
				NetherLavaDunkConversion(ModItems.DEMON_NUGGET.getDefaultInstance()),
				false
			)

		builder(InversionRitualEnemyWeight.DATA_MAP)
			.add(
				EntityType.PHANTOM.registryHolder(),
				InversionRitualEnemyWeight(5.0),
				false
			)
			.add(
				EntityType.ZOMBIE.registryHolder(),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				EntityType.ZOMBIE_VILLAGER.registryHolder(),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				EntityType.HUSK.registryHolder(),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				EntityType.SKELETON.registryHolder(),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				EntityType.BOGGED.registryHolder(),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				EntityType.STRAY.registryHolder(),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				EntityType.WITHER_SKELETON.registryHolder(),
				InversionRitualEnemyWeight(3.0),
				false
			)
			.add(
				EntityType.ENDERMAN.registryHolder(),
				InversionRitualEnemyWeight(6.0),
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
				Blocks.MAGMA_BLOCK.registryHolder(),
				ReversingHoeConversion(Blocks.LAVA.defaultBlockState()),
				false
			)
			.add(
				Blocks.OBSIDIAN.registryHolder(),
				ReversingHoeConversion(Blocks.LAVA.defaultBlockState()),
				false
			)
			.add(
				BlockTags.TERRACOTTA,
				ReversingHoeConversion(Blocks.CLAY.defaultBlockState()),
				false
			)
			.add(
				Blocks.FARMLAND.registryHolder(),
				ReversingHoeConversion(Blocks.DIRT.defaultBlockState()),
				false
			)
			.add(
				Blocks.DIRT.registryHolder(),
				ReversingHoeConversion(Blocks.GRASS_BLOCK.defaultBlockState()),
				false
			)
			.add(
				Blocks.DEAD_BUSH.registryHolder(),
				ReversingHoeConversion(Blocks.OAK_SAPLING.defaultBlockState()),
				false
			)
	}

}