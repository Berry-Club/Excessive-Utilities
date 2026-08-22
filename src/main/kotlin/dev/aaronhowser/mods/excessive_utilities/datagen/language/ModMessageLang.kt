package dev.aaronhowser.mods.excessive_utilities.datagen.language

object ModMessageLang {

	const val DOOM_EFFECT_TIME = "message.excessive_utilities.doom_effect_time"
	const val EAT_MAGICAL_APPLE = "message.excessive_utilities.eat_magical_apple"
	const val DOOM_DEATH = "death.attack.eu_doom"
	const val DARKNESS_DEATH = "death.attack.eu_darkness"
	const val SET_CREATIVE_HARVEST = "message.excessive_utilities.set_creative_harvest"
	const val NO_REMOVE_SOUL_FRAGMENT = "message.excessive_utilities.no_remove_soul_fragment"
	const val SECOND_CHANCE_USED = "message.excessive_utilities.second_chance_used"

	const val GRID_INVITE_SELF = "message.excessive_utilities.grid.invite.self"
	const val GRID_ALREADY_MEMBER = "message.excessive_utilities.grid.invite.already_member"
	const val GRID_TARGET_GROUPED = "message.excessive_utilities.grid.invite.target_grouped"
	const val GRID_INVITE_NOT_OWNER = "message.excessive_utilities.grid.invite.not_owner"
	const val GRID_INVITE_FAILED = "message.excessive_utilities.grid.invite.failed"
	const val GRID_INVITE_SENT = "message.excessive_utilities.grid.invite.sent"
	const val GRID_INVITE_RECEIVED = "message.excessive_utilities.grid.invite.received"
	const val GRID_NO_INVITATION = "message.excessive_utilities.grid.invitation.none"
	const val GRID_INVALID_INVITATION = "message.excessive_utilities.grid.invitation.invalid"
	const val GRID_JOINED = "message.excessive_utilities.grid.joined"
	const val GRID_PLAYER_JOINED = "message.excessive_utilities.grid.player_joined"
	const val GRID_INVITATION_DECLINED = "message.excessive_utilities.grid.invitation.declined"
	const val GRID_NOT_SHARED = "message.excessive_utilities.grid.not_shared"
	const val GRID_LEFT = "message.excessive_utilities.grid.left"
	const val GRID_MEMBERS = "message.excessive_utilities.grid.members"
	const val GRID_KICK_SELF = "message.excessive_utilities.grid.kick.self"
	const val GRID_KICK_NOT_OWNER = "message.excessive_utilities.grid.kick.not_owner"
	const val GRID_TARGET_NOT_MEMBER = "message.excessive_utilities.grid.kick.target_not_member"
	const val GRID_KICK_FAILED = "message.excessive_utilities.grid.kick.failed"
	const val GRID_MEMBER_REMOVED = "message.excessive_utilities.grid.kick.removed"
	const val GRID_REMOVED = "message.excessive_utilities.grid.kick.received"

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

	const val INVERSION_RITUAL_TOO_FAR = "message.excessive_utilities.inversion.ritual.too_far"

	const val LASSO_FAIL_OWNERSHIP = "message.excessive_utilities.lasso_fail_ownership"
	const val LASSO_FAIL_PASSIVE_ONLY = "message.excessive_utilities.lasso_fail_passive_only"
	const val LASSO_FAIL_HOSTILE_ONLY = "message.excessive_utilities.lasso_fail_hostile_only"
	const val LASSO_FAIL_BLACKLIST = "message.excessive_utilities.lasso_fail_blacklist"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(DOOM_EFFECT_TIME, "The Spectre of Death will arrive in %d seconds.")
			add(EAT_MAGICAL_APPLE, "You feel your luck changing.")
			add(DOOM_DEATH, "%s met their doom.")
			add(DARKNESS_DEATH, "%s was eaten by a Grue.")
			add(SET_CREATIVE_HARVEST, "Set mimic block to %s")
			add(NO_REMOVE_SOUL_FRAGMENT, "You don't have enough health to remove any more Soul Fragments!")
			add(SECOND_CHANCE_USED, "Your second chance has been used up!")

			add(GRID_INVITE_SELF, "You cannot invite yourself.")
			add(GRID_ALREADY_MEMBER, "That player is already in your grid.")
			add(GRID_TARGET_GROUPED, "That player is already in a shared grid.")
			add(GRID_INVITE_NOT_OWNER, "Only the grid owner can invite players.")
			add(GRID_INVITE_FAILED, "That player cannot be invited to your grid.")
			add(GRID_INVITE_SENT, "Invited %s to your grid.")
			add(GRID_INVITE_RECEIVED, "%s invited you to their grid. Use /excessive-utilities grid accept to join.")
			add(GRID_NO_INVITATION, "You do not have a grid invitation.")
			add(GRID_INVALID_INVITATION, "Your grid invitation is no longer valid.")
			add(GRID_JOINED, "Joined %s's grid.")
			add(GRID_PLAYER_JOINED, "%s joined your grid.")
			add(GRID_INVITATION_DECLINED, "Declined %s's grid invitation.")
			add(GRID_NOT_SHARED, "You are not in a shared grid.")
			add(GRID_LEFT, "Left the shared grid.")
			add(GRID_MEMBERS, "Grid members: %s")
			add(GRID_KICK_SELF, "Use the leave command to leave your grid.")
			add(GRID_KICK_NOT_OWNER, "Only the grid owner can remove players.")
			add(GRID_TARGET_NOT_MEMBER, "That player is not in your grid.")
			add(GRID_KICK_FAILED, "You cannot remove that player from the grid.")
			add(GRID_MEMBER_REMOVED, "Removed %s from the grid.")
			add(GRID_REMOVED, "You were removed from %s's grid.")

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

			add(INVERSION_RITUAL_TOO_FAR, "You've left the Inversion Ritual area!")

			add(LASSO_FAIL_OWNERSHIP, "You can't pick up this entity because it belongs to someone else.")
			add(LASSO_FAIL_PASSIVE_ONLY, "This Lasso can only hold passive mobs.")
			add(LASSO_FAIL_HOSTILE_ONLY, "This Lasso can only hold hostile mobs.")
			add(LASSO_FAIL_BLACKLIST, "This entity can't be picked up with a Lasso.")
		}
	}

}