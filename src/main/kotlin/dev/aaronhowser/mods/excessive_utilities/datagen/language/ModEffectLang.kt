package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects

object ModEffectLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			addEffect(ModMobEffects.DOOM, "Doom")
			addEffect(ModMobEffects.GRAVITY, "Gravity")
			addEffect(ModMobEffects.OILY, "Oily")
			addEffect(ModMobEffects.GREEK_FIRE, "Greek Fire")
			addEffect(ModMobEffects.LOVE, "Love")
			addEffect(ModMobEffects.SECOND_CHANCE, "Second Chance")
			addEffect(ModMobEffects.PURGING, "Purging")
			addEffect(ModMobEffects.RELAPSE, "Relapse")
		}
	}

}