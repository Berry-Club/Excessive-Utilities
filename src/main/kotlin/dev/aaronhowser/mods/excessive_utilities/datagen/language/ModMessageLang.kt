package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModMessageLang {

	const val DOOM_EFFECT_TIME = "message.excessive_utilities.doom_effect_time"
	const val DOOM_DEATH = "message.excessive_utilities.doom_death"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(DOOM_EFFECT_TIME, "The Spectre of Death will arrive in %d seconds.")
			add(DOOM_DEATH, "%s met their doom.")
		}
	}

}