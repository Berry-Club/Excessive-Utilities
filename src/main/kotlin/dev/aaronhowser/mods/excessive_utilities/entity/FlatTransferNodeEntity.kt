package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class FlatTransferNodeEntity(entityType: EntityType<*>, level: Level) : Entity(entityType, level) {

	var isItemNode: Boolean
		get() = entityData.get(IS_ITEM_NODE_DATA)
		private set(value) = entityData.set(IS_ITEM_NODE_DATA, value)

	var placedOnFace: Direction
		get() = entityData.get(PLACED_ON_FACE_DATA)
		private set(value) = entityData.set(PLACED_ON_FACE_DATA, value)

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		builder.define(IS_ITEM_NODE_DATA, true)
		builder.define(PLACED_ON_FACE_DATA, Direction.NORTH)
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		compound.putBoolean(IS_ITEM_NODE_TAG, isItemNode)
		compound.putInt(PLACED_ON_FACE_TAG, placedOnFace.ordinal)
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		isItemNode = compound.getBoolean(IS_ITEM_NODE_TAG)
		val directionOrdinal = compound.getInt(PLACED_ON_FACE_TAG)
		placedOnFace = Direction.entries[directionOrdinal]
	}

	companion object {
		val IS_ITEM_NODE_DATA: EntityDataAccessor<Boolean> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.BOOLEAN)
		val PLACED_ON_FACE_DATA: EntityDataAccessor<Direction> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.DIRECTION)

		const val IS_ITEM_NODE_TAG = "IsItemNode"
		const val PLACED_ON_FACE_TAG = "PlacedOnFace"

		fun place(
			level: Level,
			blockPos: BlockPos,
			direction: Direction,
			isItemNode: Boolean = true
		): FlatTransferNodeEntity? {
			val nodesAlreadyThere = level.getEntitiesOfClass(
				FlatTransferNodeEntity::class.java,
				AABB(blockPos)
			)

			if (nodesAlreadyThere.any { it.placedOnFace == direction }) {
				return null
			}

			val node = FlatTransferNodeEntity(ModEntityTypes.FLAT_TRANSFER_NODE.get(), level)

			node.setPos(Vec3.atLowerCornerOf(blockPos))
			node.isItemNode = isItemNode

			level.addFreshEntity(node)
			return node
		}
	}

}