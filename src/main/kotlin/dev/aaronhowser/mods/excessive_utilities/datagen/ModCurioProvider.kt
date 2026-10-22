package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import top.theillusivec4.curios.api.CuriosDataProvider
import java.util.concurrent.CompletableFuture

class ModCurioProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : CuriosDataProvider(ExcessiveUtilities.MOD_ID, output, registries) {

	override fun generate(registries: HolderLookup.Provider) {
		createEntities(PLAYERS_RULE)
			.addPlayer()
			.addSlots(RING_SLOT)

		createSlot(RING_SLOT)
			.size(1)
			.addCosmetic(true)
	}

	companion object {
		const val RING_SLOT = "ring"
		const val PLAYERS_RULE = "players"
	}

}
