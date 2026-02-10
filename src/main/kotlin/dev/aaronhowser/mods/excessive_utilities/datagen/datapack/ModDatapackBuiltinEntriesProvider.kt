package dev.aaronhowser.mods.excessive_utilities.datagen.datapack

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture

class ModDatapackBuiltinEntriesProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
	output,
	registries,
	BUILDER,
	setOf(ExcessiveUtilities.MOD_ID)
) {

	companion object {
		val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
	}

}