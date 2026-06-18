package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toBlockPos
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DeepDarkConstants
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class DeepDarkPortalBlock : Block(Properties.ofFullCopy(Blocks.STONE)) {

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		if (level !is ServerLevel) return InteractionResult.PASS

		if (level.dimension() == DeepDarkConstants.LEVEL_KEY) {
			returnFromDeepDark(player)
		} else {
			teleportToDeepDark(level, pos, player, state)
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		private const val RETURN_INFO_NBT = "eu_deep_dark_return_info"
		private const val FROM_DIM_NBT = "from_dimension"
		private const val FROM_PORTAL_POS_NBT = "from_portal_pos"
		private const val TARGET_Y = DeepDarkConstants.CEILING_BOTTOM + 20

		private fun teleportToDeepDark(level: ServerLevel, portalPos: BlockPos, entity: Entity, portalState: BlockState) {
			val targetLevel = level.server.getLevel(DeepDarkConstants.LEVEL_KEY) ?: return

			val returnInfo = CompoundTag()
			returnInfo.putString(FROM_DIM_NBT, level.dimension().location().toString())
			returnInfo.putLong(FROM_PORTAL_POS_NBT, portalPos.asLong())
			entity.persistentData.put(RETURN_INFO_NBT, returnInfo)

			val targetPortalPos = BlockPos(portalPos.x, TARGET_Y, portalPos.z)
			prepareDestinationRoom(targetLevel, targetPortalPos, portalState)
			teleportAbovePortal(entity, targetLevel, targetPortalPos)
		}

		private fun returnFromDeepDark(entity: Entity) {
			val level = entity.level() as? ServerLevel ?: return
			val returnInfo = entity.persistentData.getCompound(RETURN_INFO_NBT)

			val targetLevel: ServerLevel
			val targetPortalPos: BlockPos

			if (returnInfo.isEmpty) {
				targetLevel = level.server.overworld()
				targetPortalPos = targetLevel.sharedSpawnPos
			} else {
				val fromDimKey = ResourceKey.create(
					Registries.DIMENSION,
					ResourceLocation.parse(returnInfo.getString(FROM_DIM_NBT))
				)

				targetLevel = level.server.getLevel(fromDimKey) ?: return
				targetPortalPos = returnInfo.getLong(FROM_PORTAL_POS_NBT).toBlockPos()
				entity.persistentData.remove(RETURN_INFO_NBT)
			}

			teleportAbovePortal(entity, targetLevel, targetPortalPos)
		}

		private fun prepareDestinationRoom(level: ServerLevel, portalPos: BlockPos, portalState: BlockState) {
			val hRad = 3

			val mutablePos = portalPos.mutable()
			val x = portalPos.x
			val y = portalPos.y
			val z = portalPos.z

			for (xOffset in -hRad..hRad) {
				for (yOffset in 1..5) {
					for (zOffset in -hRad..hRad) {
						val targetPos = mutablePos.set(x + xOffset, y + yOffset, z + zOffset)
						if (targetPos == portalPos) continue

						level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), UPDATE_ALL_IMMEDIATE)
					}
				}
			}

			level.setBlock(portalPos, portalState, UPDATE_ALL_IMMEDIATE)
		}

		private fun teleportAbovePortal(entity: Entity, level: ServerLevel, portalPos: BlockPos) {
			val targetPos = portalPos.above().bottomCenter

			entity.teleportTo(
				level,
				targetPos.x,
				targetPos.y,
				targetPos.z,
				emptySet(),
				entity.yRot,
				entity.xRot,
			)
		}
	}

}
