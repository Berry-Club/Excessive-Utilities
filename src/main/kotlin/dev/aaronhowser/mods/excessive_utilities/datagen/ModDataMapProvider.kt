package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorFuelDataMap
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {

		builder(GeneratorFuelDataMap.DATA_MAP_TYPE)
			.add(
				Tags.Items.DYED_PINK,
				GeneratorFuelDataMap("pink", 10, 100),
				false
			)
			.add(
				Tags.Items.DYES_PINK,
				GeneratorFuelDataMap("pink", 10, 100),
				false
			)

	}

}