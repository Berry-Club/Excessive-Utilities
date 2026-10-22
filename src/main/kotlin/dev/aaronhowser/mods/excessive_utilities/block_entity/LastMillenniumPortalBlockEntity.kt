package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class LastMillenniumPortalBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.LAST_MILLENNIUM_PORTAL.get(), pos, blockState) {

	var owner: UUID = UUID.randomUUID()

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.putIntArray(OWNER_NBT, UUIDUtil.uuidToIntArray(owner))
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		val ownerInts = tag.getIntArray(OWNER_NBT).orElse(IntArray(0))
		owner = if (ownerInts.size == 4) UUIDUtil.uuidFromIntArray(ownerInts) else UUID.randomUUID()
	}

	companion object {
		const val OWNER_NBT = "Owner"
	}

}
