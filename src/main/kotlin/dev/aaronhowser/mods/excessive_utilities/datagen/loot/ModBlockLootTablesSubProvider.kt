package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredHolder

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

}