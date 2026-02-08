package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class FlatTransferNodeEntity : Entity {

	constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

	constructor(
		level: Level,
		blockPos: BlockPos,
		direction: Direction,
		isItemNode: Boolean
	) : this(ModEntityTypes.FLAT_TRANSFER_NODE.get(), level) {
		this.setPos(Vec3.atLowerCornerOf(blockPos))
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		TODO("Not yet implemented")
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		TODO("Not yet implemented")
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		TODO("Not yet implemented")
	}

}