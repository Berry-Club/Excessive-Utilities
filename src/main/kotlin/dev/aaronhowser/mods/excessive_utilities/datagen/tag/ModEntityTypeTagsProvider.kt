package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
	output: PackOutput,
	provider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : EntityTypeTagsProvider(output, provider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(LASSO_BLACKLIST)
			.add(
				EntityType.PLAYER
			)
			.addOptional(ResourceLocation.fromNamespaceAndPath("doggytalents", "dog"))

		tag(BOOMERANG_PICKUP)
			.add(
				EntityType.ITEM,
				EntityType.EXPERIENCE_ORB
			)

		tag(CURSED_EARTH_BLACKLIST)

		tag(DIVISION_SIGIL_INVERSION_SACRIFICE)
			.add(
				EntityType.IRON_GOLEM
			)
	}

	companion object {
		private fun create(id: String): TagKey<EntityType<*>> =
			TagKey.create(Registries.ENTITY_TYPE, ExcessiveUtilities.modResource(id))

		val LASSO_BLACKLIST = create("lasso_blacklist")
		val BOOMERANG_PICKUP = create("boomerang_pickup")
		val CURSED_EARTH_BLACKLIST = create("cursed_earth_blacklist")
		val DIVISION_SIGIL_INVERSION_SACRIFICE = create("division_sigil_inversion_sacrifice")
	}

}