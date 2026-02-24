package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModEnchantmentProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EnchantmentTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEnchantmentTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : EnchantmentTagsProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(BOOMERANG_BLOCK_INTERACTION)
			.add(
				ModEnchantmentProvider.BOOMEREAPERANG,
				ModEnchantmentProvider.KABOOMERANG
			)
	}

	companion object {
		private fun create(id: String): TagKey<Enchantment> = TagKey.create(
			Registries.ENCHANTMENT,
			ExcessiveUtilities.modResource(id)
		)

		val BOOMERANG_BLOCK_INTERACTION = create("boomerang_block_interaction")
	}

}