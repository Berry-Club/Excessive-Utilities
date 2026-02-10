package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects

object ModEffectLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			addEffect(ModMobEffects.DOOM, "Doom")
			addEffect(ModMobEffects.GRAVITY, "Gravity")
		}
	}

}