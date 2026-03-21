package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem

object ModMenuLang {

	const val SOUL_OF_A_FORGOTTEN_DEITY = "tooltip.excessive_utilities.soul_of_a_forgotten_deity"
	const val SOUL_DEBT = "tooltip.excessive_utilities.soul_debt"
	const val SOUL_SURPLUS = "tooltip.excessive_utilities.soul_surplus"
	const val SOUL_HEALTH_MODIFIER = "tooltip.excessive_utilities.soul_health_modifier"
	const val FE = "tooltip.geneticsresequenced.fe"
	const val ITEM_FILTER_INVERTED = "tooltip.excessive_utilities.item_filter.inverted"
	const val ITEM_FILTER_TAGS = "tooltip.excessive_utilities.item_filter.tags"
	const val ITEM_FILTER_IGNORE_DAMAGE = "tooltip.excessive_utilities.item_filter.ignore_damage"
	const val ITEM_FILTER_IGNORE_ALL_COMPONENTS = "tooltip.excessive_utilities.item_filter.ignore_all_components"
	const val FLUID_FILTER_INVERTED = "tooltip.excessive_utilities.fluid_filter.inverted"
	const val FLUID_FILTER_TAGS = "tooltip.excessive_utilities.fluid_filter.tags"
	const val FLUID_FILTER_IGNORE_ALL_COMPONENTS = "tooltip.excessive_utilities.fluid_filter.ignore_all_components"
	const val UNSTABLE_INGOT_CHEESED_1 = "tooltip.excessive_utilities.unstable_ingot.cheesed_1"
	const val UNSTABLE_INGOT_CHEESED_2 = "tooltip.excessive_utilities.unstable_ingot.cheesed_2"
	const val UNSTABLE_INGOT_CHEESED_3 = "tooltip.excessive_utilities.unstable_ingot.cheesed_3"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(SOUL_OF_A_FORGOTTEN_DEITY, "Soul of a Forgotten Deity")
			add(SOUL_DEBT, "Soul Debt: %d")
			add(SOUL_SURPLUS, "Soul Surplus: %d")
			add(SOUL_HEALTH_MODIFIER, "Current Health Modifier: %s")
			add(FE, "%1\$s/%2\$s FE")
			add(ITEM_FILTER_INVERTED, "Inverted")
			add(ITEM_FILTER_TAGS, "Use Tags")
			add(ITEM_FILTER_IGNORE_DAMAGE, "Ignore Damage")
			add(ITEM_FILTER_IGNORE_ALL_COMPONENTS, "Ignore All Components")
			add(FLUID_FILTER_INVERTED, "Inverted")
			add(FLUID_FILTER_TAGS, "Use Tags")
			add(FLUID_FILTER_IGNORE_ALL_COMPONENTS, "Ignore All Components")

			add(AngelRingItem.Type.INVISIBLE.langKey, "Invisible")
			add(AngelRingItem.Type.FEATHER.langKey, "Feather")
			add(AngelRingItem.Type.BUTTERFLY.langKey, "Butterfly")
			add(AngelRingItem.Type.DEMON.langKey, "Demon")
			add(AngelRingItem.Type.GOLD.langKey, "Gold")
			add(AngelRingItem.Type.BAT.langKey, "Bat")

			add(UNSTABLE_INGOT_CHEESED_1, "Naughty naughty!")
			add(UNSTABLE_INGOT_CHEESED_2, "Unstable Ingots must be crafted manually, by you.")
			add(UNSTABLE_INGOT_CHEESED_3, "If this one WAS crafted manually, try using a vanilla Crafting Table instead.")
		}
	}

}