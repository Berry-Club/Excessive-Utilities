package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModTooltipLang {

	const val SOUL_OF_A_FORGOTTEN_DEITY = "tooltip.excessive_utilities.soul_of_a_forgotten_deity"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(SOUL_OF_A_FORGOTTEN_DEITY, "Soul of a Forgotten Deity")
		}
	}

}