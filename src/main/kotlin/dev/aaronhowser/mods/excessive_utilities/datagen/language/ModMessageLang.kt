package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModMessageLang {

	const val DOOM_EFFECT_TIME = "message.excessive_utilities.doom_effect_time"
	const val EAT_MAGICAL_APPLE = "message.excessive_utilities.eat_magical_apple"
	const val DOOM_DEATH = "death.attack.eu_doom"
	const val SET_CREATIVE_HARVEST = "message.excessive_utilities.set_creative_harvest"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(DOOM_EFFECT_TIME, "The Spectre of Death will arrive in %d seconds.")
			add(EAT_MAGICAL_APPLE, "You feel your luck changing.")
			add(DOOM_DEATH, "%s met their doom.")
			add(SET_CREATIVE_HARVEST, "Set mimic block to %s")
		}
	}

}