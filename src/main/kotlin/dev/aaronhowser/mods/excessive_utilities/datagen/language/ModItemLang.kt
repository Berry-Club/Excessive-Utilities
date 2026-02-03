package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.datagen.ModLanguageProvider

object ModItemLang {

	const val CREATIVE_TAB = "itemGroup.excessive_utilities"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(CREATIVE_TAB, "Excessive Utilities")
		}
	}

}