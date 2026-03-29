package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModMessageLang {

	const val DOOM_EFFECT_TIME = "message.excessive_utilities.doom_effect_time"
	const val EAT_MAGICAL_APPLE = "message.excessive_utilities.eat_magical_apple"
	const val DOOM_DEATH = "death.attack.eu_doom"
	const val SET_CREATIVE_HARVEST = "message.excessive_utilities.set_creative_harvest"
	const val NO_REMOVE_SOUL_FRAGMENT = "message.excessive_utilities.no_remove_soul_fragment"
	const val SECOND_CHANCE_USED = "message.excessive_utilities.second_chance_used"

	const val DIVISION_OVERWORLD_ONLY = "message.excessive_utilities.division.overworld_only"
	const val DIVISION_SEE_SKY = "message.excessive_utilities.division.see_sky"
	const val DIVISION_REDSTONE = "message.excessive_utilities.division.redstone"
	const val DIVISION_REDSTONE_AT = "message.excessive_utilities.division.redstone_at"
	const val DIVISION_DIRT = "message.excessive_utilities.division.dirt"
	const val DIVISION_DIRT_AT = "message.excessive_utilities.division.dirt_at"
	const val DIVISION_MIDNIGHT = "message.excessive_utilities.division.midnight"
	const val DIVISION_DARKNESS = "message.excessive_utilities.division.darkness"
	const val DIVISION_READY_ONE = "message.excessive_utilities.division.ready_one"
	const val DIVISION_READY_TWO = "message.excessive_utilities.division.ready_two"

	const val INVERSION_END_ONLY = "message.excessive_utilities.inversion.end_only"
	const val INVERSION_MISSING_CHEST = "message.excessive_utilities.inversion.missing_chest"
	const val INVERSION_MISSING_REDSTONE = "message.excessive_utilities.inversion.missing_redstone"
	const val INVERSION_MISSING_STRING = "message.excessive_utilities.inversion.missing_string"
	const val INVERSION_MISSING_ITEMS = "message.excessive_utilities.inversion.missing_items"
	const val INVERSION_READY_ONE = "message.excessive_utilities.inversion.ready_one"
	const val INVERSION_READY_TWO = "message.excessive_utilities.inversion.ready_two"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(DOOM_EFFECT_TIME, "The Spectre of Death will arrive in %d seconds.")
			add(EAT_MAGICAL_APPLE, "You feel your luck changing.")
			add(DOOM_DEATH, "%s met their doom.")
			add(SET_CREATIVE_HARVEST, "Set mimic block to %s")
			add(NO_REMOVE_SOUL_FRAGMENT, "You don't have enough health to remove any more Soul Fragments!")
			add(SECOND_CHANCE_USED, "Your second chance has been used up!")

			add(DIVISION_OVERWORLD_ONLY, "You can only activate the Division Sigil in the Overworld!")
			add(DIVISION_SEE_SKY, "The Enchanting Table must be able to see the sky.")
			add(DIVISION_REDSTONE, "You must have Redstone surrounding the Enchanting Table.")
			add(DIVISION_REDSTONE_AT, "It's missing at %d, %d, %d.")
			add(DIVISION_DIRT, "You must have a 5x5 layer of Dirt under the Enchanting Table.")
			add(DIVISION_DIRT_AT, "It's missing at %d, %d, %d.")
			add(DIVISION_MIDNIGHT, "You can only activate the Division Sigil at midnight.")
			add(DIVISION_DARKNESS, "The Enchanting Table must be in darkness.")
			add(DIVISION_READY_ONE, "The Division Sigil is ready to be activated!")
			add(DIVISION_READY_TWO, "Kill a mob nearby the Enchanting Table.")

			add(INVERSION_END_ONLY, "You can only invert the Division Sigil in the End!")
			add(INVERSION_MISSING_CHEST, "Yuo must have a Chest 5 blocks to the %s.")
			add(INVERSION_MISSING_REDSTONE, "You are missing a Redstone at %d, %d, %d.")
			add(INVERSION_MISSING_STRING, "You are missing a String at %d, %d, %d.")
			add(INVERSION_MISSING_ITEMS, "You need at least %d items from the tag #%s in the Chest to the %s, but you only have %d.")
			add(INVERSION_READY_ONE, "The Division Sigil is ready to be inverted!")
			add(INVERSION_READY_TWO, "Kill an Iron Golem near the Beacon to begin the ritual.")
		}
	}

}