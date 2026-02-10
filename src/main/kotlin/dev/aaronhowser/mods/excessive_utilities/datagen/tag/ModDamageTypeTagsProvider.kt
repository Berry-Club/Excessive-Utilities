package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDamageTypeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModDamageTypeTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : DamageTypeTagsProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(DamageTypeTags.NO_ANGER)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.NO_IMPACT)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.NO_KNOCKBACK)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_ARMOR)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_ENCHANTMENTS)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_EFFECTS)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_INVULNERABILITY)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_SHIELD)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_COOLDOWN)
			.add(ModDamageTypeProvider.DOOM)

		tag(DamageTypeTags.BYPASSES_WOLF_ARMOR)
			.add(ModDamageTypeProvider.DOOM)
	}

}