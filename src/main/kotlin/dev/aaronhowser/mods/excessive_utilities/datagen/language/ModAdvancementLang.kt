package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModAdvancementLang {

	const val ROOT_TITLE = "advancements.excessive_utilities.root.title"
	const val ROOT_DESC = "advancements.excessive_utilities.root.desc"

	const val GP_PRODUCERS_TITLE = "advancements.excessive_utilities.gp_producers.title"
	const val GP_PRODUCERS_DESC = "advancements.excessive_utilities.gp_producers.desc"
	const val RESONATOR_TITLE = "advancements.excessive_utilities.resonator.title"
	const val RESONATOR_DESC = "advancements.excessive_utilities.resonator.desc"
	const val TLM_TITLE = "advancements.excessive_utilities.tlm.title"
	const val TLM_DESC = "advancements.excessive_utilities.tlm.desc"
	const val QUANTUM_QUARRY_TITLE = "advancements.excessive_utilities.quantum_quarry.title"
	const val QUANTUM_QUARRY_DESC = "advancements.excessive_utilities.quantum_quarry.desc"
	const val QED_TITLE = "advancements.excessive_utilities.qed.title"
	const val QED_DESC = "advancements.excessive_utilities.qed.desc"
	const val ENDER_QUARRY_TITLE = "advancements.excessive_utilities.ender_quarry.title"
	const val ENDER_QUARRY_DESC = "advancements.excessive_utilities.ender_quarry.desc"
	const val ENDER_QUARRY_UPGRADE_TITLE = "advancements.excessive_utilities.ender_quarry_upgrade.title"
	const val ENDER_QUARRY_UPGRADE_DESC = "advancements.excessive_utilities.ender_quarry.upgrade.desc"

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

	const val FIND_SIGIL_TITLE = "advancements.excessive_utilities.find_sigil.title"
	const val FIND_SIGIL_DESC = "advancements.excessive_utilities.find_sigil.desc"
	const val ACTIVATE_SIGIL_TITLE = "advancements.excessive_utilities.activate_sigil.title"
	const val ACTIVATE_SIGIL_DESC = "advancements.excessive_utilities.activate_sigil.desc"
	const val INVERT_SIGIL_TITLE = "advancements.excessive_utilities.invert_sigil.title"
	const val INVERT_SIGIL_DESC = "advancements.excessive_utilities.invert_sigil.desc"

	const val ANGEL_RING_TITLE = "advancements.excessive_utilities.angel_ring.title"
	const val ANGEL_RING_DESC = "advancements.excessive_utilities.angel_ring.desc"
	const val UNSTABLE_TOOL_TITLE = "advancements.excessive_utilities.unstable_tool.title"
	const val UNSTABLE_TOOL_DESC = "advancements.excessive_utilities.unstable_tool.desc"
	const val DEPTHS_TITLE = "advancements.excessive_utilities.depths.title"
	const val DEPTHS_DESC = "advancements.excessive_utilities.depths.desc"

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
			add(ANY_GENERATOR_TITLE, "Generator General")
			add(ANY_GENERATOR_DESC, "Make any Generator")
			add(EVERY_GENERATOR_TITLE, "Power Hungry")
			add(EVERY_GENERATOR_DESC, "Make every Generator")
			add(RAINBOW_GENERATOR_TITLE, "RAINBOW")
			add(RAINBOW_GENERATOR_DESC, "Make the Rainbow Generator")
			add(RESONATOR_TITLE, "Tuning Spork")
			add(RESONATOR_DESC, "Make a Resonator")
			add(TLM_TITLE, "The End of Time")
			add(TLM_DESC, "Go to The Last Millennium")
			add(QUANTUM_QUARRY_TITLE, "Theoretical Miner")
			add(QUANTUM_QUARRY_DESC, "Make a Quantum Quarry")
			add(QED_TITLE, "QED")
			add(QED_DESC, "Make a QED")
			add(ENDER_QUARRY_TITLE, "Powered by Fences!")
			add(ENDER_QUARRY_DESC, "Make an Ender Quarry")
			add(ENDER_QUARRY_UPGRADE_TITLE, "World Hole")
			add(ENDER_QUARRY_UPGRADE_DESC, "Make an upgrade for the Ender Quarry")
			add(FIND_SIGIL_TITLE, "Mathematical!")
			add(FIND_SIGIL_DESC, "Find a Division Sigil")
			add(ACTIVATE_SIGIL_TITLE, "Divide by Diamond")
			add(ACTIVATE_SIGIL_DESC, "Activate a Division Sigil")
			add(INVERT_SIGIL_TITLE, "Inverted Sigil")
			add(INVERT_SIGIL_DESC, "Complete the Inversion Sigil ritual")
			add(DEPTHS_TITLE, "The Deeper Dark")
			add(DEPTHS_DESC, "Enter the Depths")
			add(UNSTABLE_TOOL_TITLE, "Useful for More Than Just Eating!")
			add(UNSTABLE_TOOL_DESC, "Make any Unstable tool")
			add(ANGEL_RING_TITLE, "Two-Winged Angel")
			add(ANGEL_RING_DESC, "Make an Angel Ring")
		}
	}

}
