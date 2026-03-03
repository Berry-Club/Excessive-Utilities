package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.IntRange
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.predicates.TimeCheck
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.registries.DeferredHolder

class ModBlockLootTablesSubProvider(
	provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

	override fun getKnownBlocks(): Iterable<Block> {
		return ModBlocks.BLOCK_REGISTRY.entries.map(DeferredHolder<Block, out Block>::get)
	}

	override fun generate() {
		val noDropSelfBlocks = setOf(
			ModBlocks.MOON_STORE_ORE.get(),
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get(),
			ModBlocks.CURSED_EARTH.get()
		)

		val dropSelfBlocks = knownBlocks - noDropSelfBlocks

		for (block in dropSelfBlocks) {
			dropSelf(block)
		}

		add(
			ModBlocks.MOON_STORE_ORE.get(),
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(ModItems.MOON_STONE))
						.`when`(TimeCheck.time(IntRange.range(13000, 23000)))
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.COBBLESTONE))
				)
		)

		add(
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get(),
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(ModItems.MOON_STONE))
						.`when`(TimeCheck.time(IntRange.range(13000, 23000)))
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.COBBLED_DEEPSLATE))
				)
		)

		add(ModBlocks.CURSED_EARTH.get()) { block -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT) }

	}

}