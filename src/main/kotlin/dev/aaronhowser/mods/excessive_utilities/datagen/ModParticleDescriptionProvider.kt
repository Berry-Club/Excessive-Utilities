package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.registry.ModParticleTypes
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider

class ModParticleDescriptionProvider(
	output: PackOutput,
	existingFileHelper: ExistingFileHelper
) : ParticleDescriptionProvider(output, existingFileHelper) {

	override fun addDescriptions() {
		spriteSet(
			ModParticleTypes.VOMIT.get(),
			ResourceLocation.withDefaultNamespace("generic"),
			8,
			false
		)
	}

}
