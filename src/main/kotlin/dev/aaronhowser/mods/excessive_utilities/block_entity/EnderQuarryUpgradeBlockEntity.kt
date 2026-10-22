package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.EnderQuarryUpgradeType
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderQuarryUpgradeBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY_UPGRADE.get(), pos, state) {

	var upgradeType: EnderQuarryUpgradeType = EnderQuarryUpgradeType.NONE
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	var parentBlock: BlockPos? = null
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	fun getQuarryPos(): BlockPos? {
		val level = level ?: return null
		val parent = parentBlock ?: return null

		if (level.getBlockState(parent).isBlock(ModBlocks.ENDER_QUARRY)) {
			return parent
		}

		val parentBe = level.getBlockEntity(parent)
		if (parentBe is EnderQuarryUpgradeBlockEntity) {
			return parentBe.getQuarryPos()
		}

		return null
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.putString(UPGRADE_TYPE_TAG, upgradeType.id)
		val pos = parentBlock
		if (pos != null) {
			tag.putLong(PARENT_POS_TAG, pos.asLong())
		}
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		upgradeType = EnderQuarryUpgradeType.valueOf(tag.getStringOr(UPGRADE_TYPE_TAG, EnderQuarryUpgradeType.NONE.id))
		tag.getLong(PARENT_POS_TAG).ifPresent { parentBlock = BlockPos.of(it) }
	}

	companion object {
		const val UPGRADE_TYPE_TAG = "UpgradeType"
		const val PARENT_POS_TAG = "ParentPos"
	}

}
