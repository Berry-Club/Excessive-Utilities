package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import java.util.*

data class GridGroupResult(
	val status: Status,
	val inviterUuid: UUID? = null
) {

	val isSuccess: Boolean
		get() = status == Status.SUCCESS

	enum class Status {
		SUCCESS,

		// Target validation
		CANNOT_TARGET_SELF,
		ALREADY_IN_SAME_GROUP,
		TARGET_IN_ANOTHER_GROUP,

		// Invitation state
		NO_INVITATION,
		INVALID_INVITATION,

		// Group authorization and membership
		NOT_GROUP_OWNER,
		NOT_IN_SHARED_GROUP,
		TARGET_NOT_IN_GROUP
	}
}