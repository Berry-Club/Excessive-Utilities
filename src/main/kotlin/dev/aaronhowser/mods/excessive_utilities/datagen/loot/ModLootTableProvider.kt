package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

class ModLootTableProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : LootTableProvider(output, setOf(), SUB_PROVIDERS, registries) {

	companion object {
		val SUB_PROVIDERS = listOf(
			SubProviderEntry(
				::ModBlockLootTablesSubProvider,
				LootContextParamSets.BLOCK
			)
		)
	}

}