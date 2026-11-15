package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.registry.ModParticleTypes
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider

class ModParticleDescriptionProvider(
	output: PackOutput
) : ParticleDescriptionProvider(output) {

	override fun addDescriptions() {
		spriteSet(
			ModParticleTypes.VOMIT.get(),
			Identifier.withDefaultNamespace("generic"),
			8,
			false
		)
	}

}
