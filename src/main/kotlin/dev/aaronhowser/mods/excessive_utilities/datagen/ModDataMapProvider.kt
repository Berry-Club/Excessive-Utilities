package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.datamap.InversionRitualEnemyWeight
import dev.aaronhowser.mods.excessive_utilities.datamap.NetherLavaDunkConversion
import dev.aaronhowser.mods.excessive_utilities.datamap.ReversingHoeConversion
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.registries.BuiltInRegistries
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
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.PHANTOM),
				InversionRitualEnemyWeight(5.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ZOMBIE),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ZOMBIE_VILLAGER),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.HUSK),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SKELETON),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BOGGED),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.STRAY),
				InversionRitualEnemyWeight(10.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.WITHER_SKELETON),
				InversionRitualEnemyWeight(3.0),
				false
			)
			.add(
				BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ENDERMAN),
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
				BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.MAGMA_BLOCK),
				ReversingHoeConversion(Blocks.LAVA.defaultBlockState()),
				false
			)
			.add(
				BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.OBSIDIAN),
				ReversingHoeConversion(Blocks.LAVA.defaultBlockState()),
				false
			)
			.add(
				BlockTags.TERRACOTTA,
				ReversingHoeConversion(Blocks.CLAY.defaultBlockState()),
				false
			)
			.add(
				BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.FARMLAND),
				ReversingHoeConversion(Blocks.DIRT.defaultBlockState()),
				false
			)
			.add(
				BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.DIRT),
				ReversingHoeConversion(Blocks.GRASS_BLOCK.defaultBlockState()),
				false
			)
			.add(
				BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.DEAD_BUSH),
				ReversingHoeConversion(Blocks.OAK_SAPLING.defaultBlockState()),
				false
			)
	}

}