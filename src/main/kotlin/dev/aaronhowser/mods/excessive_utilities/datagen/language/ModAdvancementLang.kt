package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModAdvancementLang {

	const val ROOT_TITLE = "advancements.excessive_utilities.root.title"
	const val ROOT_DESC = "advancements.excessive_utilities.root.desc"

	const val GP_PRODUCERS_TITLE = "advancements.excessive_utilities.gp_producers.title"
	const val GP_PRODUCERS_DESC = "advancements.excessive_utilities.gp_producers.desc"

	const val PERFECT_OPINIUM_TITLE = "advancements.excessive_utilities.perfect_opinum.title"
	const val PERFECT_OPINIUM_DESC = "advancements.excessive_utilities.perfect_opinum.desc"
	const val KIKOKU_TITLE = "advancements.excessive_utilities.kikoku.title"
	const val KIKOKU_DESC = "advancements.excessive_utilities.kikoku.desc"

	const val ANY_GENERATOR_TITLE = "advancements.excessive_utilities.any_generator.title"
	const val ANY_GENERATOR_DESC = "advancements.excessive_utilities.any_generator.desc"
	const val EVERY_GENERATOR_TITLE = "advancements.excessive_utilities.every_generator.title"
	const val EVERY_GENERATOR_DESC = "advancements.excessive_utilities.every_generator.desc"
	const val RAINBOW_GENERATOR_TITLE = "advancements.excessive_utilities.rainbow_generator.title"
	const val RAINBOW_GENERATOR_DESC = "advancements.excessive_utilities.rainbow_generator.desc"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(ROOT_TITLE, "Excessive Utilities")
			add(ROOT_DESC, "A collection of excessively useful items!")
			add(GP_PRODUCERS_TITLE, "Milling About")
			add(GP_PRODUCERS_DESC, "Make something that produces Grid Power")
			add(PERFECT_OPINIUM_TITLE, "Objectively Correct")
			add(PERFECT_OPINIUM_DESC, "Get an Opinium Core (Perfected)")
			add(KIKOKU_TITLE, "All According To Kikoku")
			add(KIKOKU_DESC, "Make the Kikoku")
			add(ANY_GENERATOR_TITLE, "Any Generator")
			add(ANY_GENERATOR_DESC, "Make any Generator")
			add(EVERY_GENERATOR_TITLE, "Every Generator")
			add(EVERY_GENERATOR_DESC, "Make every Generator")
			add(RAINBOW_GENERATOR_TITLE, "Rainbow Generator")
			add(RAINBOW_GENERATOR_DESC, "Make the Rainbow Generator")
		}
	}

}
