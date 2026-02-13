package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.BiConsumer

class ModBlockLootTablesSubProvider(
	provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

	private val noDropSelfBlocks: Set<Block> = buildSet {
		add(ModBlocks.ANGEL_BLOCK.get())
	}

	override fun getKnownBlocks(): Iterable<Block> {
		return ModBlocks.BLOCK_REGISTRY.entries.map(DeferredHolder<Block, out Block>::get)
	}

	override fun generate() {
		val dropSelfBlocks = this.knownBlocks - this.noDropSelfBlocks

		for (block in dropSelfBlocks) {
			dropSelf(block)
		}

		dropOther(ModBlocks.ANGEL_BLOCK.get(), ModItems.ANGEL_BLOCK)

	}

	override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
		super.generate(output)

		output.accept(
			RESONATING_REDSTONE_CRYSTAL,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.RESONATING_REDSTONE_CRYSTAL.get(), 30)
				)
		)
	}

	companion object {
		private fun createPoolRk(name: String): ResourceKey<LootTable> {
			return ResourceKey.create(
				Registries.LOOT_TABLE,
				ExcessiveUtilities.modResource(name)
			)
		}

		private fun singleItemPool(item: Item, chance: Int) = LootPool.lootPool()
			.add(EmptyLootItem.emptyItem().setWeight(100 - chance))
			.add(LootItem.lootTableItem(item).setWeight(chance))

		val RESONATING_REDSTONE_CRYSTAL = createPoolRk("resonating_redstone_crystal")
	}

}