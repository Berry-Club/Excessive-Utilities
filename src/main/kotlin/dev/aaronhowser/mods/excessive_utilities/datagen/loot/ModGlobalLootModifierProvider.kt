package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import java.util.concurrent.CompletableFuture

class ModGlobalLootModifierProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : GlobalLootModifierProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID) {

	override fun start() {

		add(
			"resonating_crystal_from_redstone_ore",
			AddTableLootModifier(
				arrayOf(
					LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(Blocks.REDSTONE_ORE)
						.build()
				),
				ModBlockLootTablesSubProvider.RESONATING_REDSTONE_CRYSTAL
			)
		)
	}

}