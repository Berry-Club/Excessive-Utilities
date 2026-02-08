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

class FlatTransferNodeEntity : Entity {

	constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		builder.define(IS_ITEM_NODE_DATA, true)
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		TODO("Not yet implemented")
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		TODO("Not yet implemented")
	}

	companion object {
		val IS_ITEM_NODE_DATA: EntityDataAccessor<Boolean> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.BOOLEAN)

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

			if (nodesAlreadyThere.any { it.direction == direction }) {
				return null
			}

			val node = FlatTransferNodeEntity(ModEntityTypes.FLAT_TRANSFER_NODE.get(), level)
			node.setPos(Vec3.atLowerCornerOf(blockPos))
			level.addFreshEntity(node)
			return node
		}
	}

}