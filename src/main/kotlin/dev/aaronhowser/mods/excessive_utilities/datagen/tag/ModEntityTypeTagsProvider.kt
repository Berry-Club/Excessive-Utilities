package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
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
		tag(BOOMERANG_PICKUP)
			.add(EntityType.ITEM, EntityType.EXPERIENCE_ORB)

		tag(CURSED_EARTH_BLACKLIST)

		tag(MECHANICAL_USER_LEFT_CLICK_BLACKLIST)
			.add(EntityType.ITEM, EntityType.EXPERIENCE_ORB)

		tag(MECHANICAL_USER_RIGHT_CLICK_BLACKLIST)

		tag(INVERSION_RITUAL_SACRIFICE)
			.add(EntityType.IRON_GOLEM)

		tag(INVERSION_RITUAL_DESPAWN_ON_START)
			.add(EntityType.ENDERMAN)
	}

	companion object {
		private fun create(id: String): TagKey<EntityType<*>> =
			TagKey.create(Registries.ENTITY_TYPE, ExcessiveUtilities.modResource(id))

		val BOOMERANG_PICKUP = create("boomerang_pickup")
		val CURSED_EARTH_BLACKLIST = create("cursed_earth_blacklist")
		val MECHANICAL_USER_LEFT_CLICK_BLACKLIST = create("mechanical_user_left_click_blacklist")
		val MECHANICAL_USER_RIGHT_CLICK_BLACKLIST = create("mechanical_user_right_click_blacklist")

		val INVERSION_RITUAL_SACRIFICE = create("inversion_ritual_sacrifice")
		val INVERSION_RITUAL_DESPAWN_ON_START = create("inversion_ritual_despawn_on_start")
	}

}