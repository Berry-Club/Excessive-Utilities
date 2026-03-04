package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModTooltipLang {

	const val SOUL_OF_A_FORGOTTEN_DEITY = "tooltip.excessive_utilities.soul_of_a_forgotten_deity"
	const val SOUL_DEBT = "tooltip.excessive_utilities.soul_debt"
	const val SOUL_SURPLUS = "tooltip.excessive_utilities.soul_surplus"
	const val SOUL_HEALTH_MODIFIER = "tooltip.excessive_utilities.soul_health_modifier"
	const val FE = "tooltip.geneticsresequenced.fe"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(SOUL_OF_A_FORGOTTEN_DEITY, "Soul of a Forgotten Deity")
			add(SOUL_DEBT, "Soul Debt: %d")
			add(SOUL_SURPLUS, "Soul Surplus: %d")
			add(SOUL_HEALTH_MODIFIER, "Current Health Modifier: %s")
			add(FE, "%1\$s/%2\$s FE")
		}
	}

}