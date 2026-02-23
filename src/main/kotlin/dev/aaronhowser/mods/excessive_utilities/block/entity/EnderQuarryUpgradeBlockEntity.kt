package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.base.EnderQuarryUpgrade
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderQuarryUpgradeBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY_UPGRADE.get(), pos, state) {

	var upgrade: EnderQuarryUpgrade = EnderQuarryUpgrade.NONE
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	var quarryPos: BlockPos? = null
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putString(UPGRADE_TAG, upgrade.id)
		val qPos = quarryPos
		if (qPos != null) {
			tag.putLong(QUARRY_POS_TAG, qPos.asLong())
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		upgrade = EnderQuarryUpgrade.valueOf(tag.getString(UPGRADE_TAG))
		if (tag.contains(QUARRY_POS_TAG)) {
			quarryPos = BlockPos.of(tag.getLong(QUARRY_POS_TAG))
		}
	}

	companion object {
		const val UPGRADE_TAG = "Upgrade"
		const val QUARRY_POS_TAG = "QuarryPos"
	}

}