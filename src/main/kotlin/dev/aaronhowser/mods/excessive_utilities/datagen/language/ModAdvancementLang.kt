package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModAdvancementLang {

	const val ROOT_TITLE = "advancements.excessive_utilities.root.title"
	const val ROOT_DESCRIPTION = "advancements.excessive_utilities.root.description"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(ROOT_TITLE, "Excessive Utilities")
			add(ROOT_DESCRIPTION, "A collection of excessively useful tools, machines, and magic")
		}
	}

}
