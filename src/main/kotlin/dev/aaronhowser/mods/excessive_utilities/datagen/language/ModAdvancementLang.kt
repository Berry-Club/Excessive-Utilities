package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModAdvancementLang {

	const val ROOT_TITLE = "advancements.excessive_utilities.root.title"
	const val ROOT_DESC = "advancements.excessive_utilities.root.desc"

	const val GP_PRODUCERS_TITLE = "advancements.excessive_utilities.gp_producers.title"
	const val GP_PRODUCERS_DESC = "advancements.excessive_utilities.gp_producers.desc"

	const val PERFECT_OPINIUM_TITLE = "advancements.excessive_utilities.perfect_opinum.title"
	const val PERFECT_OPINIUM_DESC = "advancements.excessive_utilities.perfect_opinum.desc"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(ROOT_TITLE, "Excessive Utilities")
			add(ROOT_DESC, "A collection of excessively useful items!")
			add(GP_PRODUCERS_TITLE, "Milling About")
			add(GP_PRODUCERS_DESC, "Make something that produces Grid Power")
			add(PERFECT_OPINIUM_TITLE, "Objectively Correct")
			add(PERFECT_OPINIUM_DESC, "Get an Opinium Core (Perfected)")
		}
	}

}
