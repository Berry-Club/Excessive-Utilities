package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import java.util.concurrent.CompletableFuture

class ModMobEffectTagsProvider(
	output: PackOutput,
	provider: CompletableFuture<HolderLookup.Provider>
) : TagsProvider<MobEffect>(output, Registries.MOB_EFFECT, provider, ExcessiveUtilities.MOD_ID) {

	override fun addTags(provider: HolderLookup.Provider) {
		getOrCreateRawBuilder(PURGING_BLACKLIST)
			.addElement(ModMobEffects.PURGING.id)
	}

	companion object {
		private fun create(id: String): TagKey<MobEffect> =
			TagKey.create(Registries.MOB_EFFECT, ExcessiveUtilities.modResource(id))

		val PURGING_BLACKLIST = create("purging_blacklist")
	}

}
