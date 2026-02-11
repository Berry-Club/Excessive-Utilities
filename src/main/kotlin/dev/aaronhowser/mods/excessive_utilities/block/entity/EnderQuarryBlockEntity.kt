package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	private var minPos: BlockPos? = null
		set(value) {
			field = value
			setChanged()
		}

	private var maxPos: BlockPos? = null
		set(value) {
			field = value
			setChanged()
		}

	private var targetPos: BlockPos? = null
		set(value) {
			field = value
			setChanged()
		}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val min = minPos
		val max = maxPos

		if (min != null && max != null) {
			tag.putLong(MIN_POS_NBT, min.asLong())
			tag.putLong(MAX_POS_NBT, max.asLong())
		}

		val target = targetPos
		if (target != null) {
			tag.putLong(TARGET_POS_NBT, target.asLong())
		}

	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		if (tag.contains(MIN_POS_NBT) && tag.contains(MAX_POS_NBT)) {
			minPos = BlockPos.of(tag.getLong(MIN_POS_NBT))
			maxPos = BlockPos.of(tag.getLong(MAX_POS_NBT))
		}

		if (tag.contains(TARGET_POS_NBT)) {
			targetPos = BlockPos.of(tag.getLong(TARGET_POS_NBT))
		}
	}

	companion object {
		const val MIN_POS_NBT = "MinPos"
		const val MAX_POS_NBT = "MaxPos"
		const val TARGET_POS_NBT = "TargetPos"
	}

}