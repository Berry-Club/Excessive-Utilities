package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toBlockPos
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DeepDarkConstants
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CaveVines
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.saveddata.SavedData

class DeepDarkHandler : SavedData() {

	private val rooms: MutableList<Room> = mutableListOf()

	fun teleportIntoDimension(entity: Entity, originLevel: ServerLevel, originPortalPos: BlockPos) {
		val ddLevel = getDeepDarkLevel(originLevel)

		val returnInfo = CompoundTag()
		returnInfo.putString(FROM_DIM, originLevel.dimension().location().toString())
		returnInfo.putLong(FROM_PORTAL_POS, originPortalPos.asLong())
		entity.persistentData.put(PLAYER_RETURN_INFO, returnInfo)

		val room = getOrCreateRoom(originLevel.dimension(), originPortalPos)

		placeStructureIfNeeded(ddLevel, room)
		teleportAbovePortal(entity, ddLevel, room.portalPos)
	}

	private fun getOrCreateRoom(
		originDimension: ResourceKey<Level>,
		originPortalPos: BlockPos
	): Room {
		val existing = rooms.firstOrNull {
			it.originDimension == originDimension && it.originPortalPos == originPortalPos
		}

		if (existing != null) return existing

		val wantedPortalPos = BlockPos(originPortalPos.x, TARGET_Y, originPortalPos.z)
		val wantedStructureMin = wantedPortalPos.subtract(PORTAL_OFFSET)
		val wantedBounds = getBounds(wantedStructureMin)

		val overlappingRoom = rooms.firstOrNull { it.bounds.intersects(wantedBounds) }
		if (overlappingRoom != null) return overlappingRoom

		val room = Room(originDimension, originPortalPos, wantedStructureMin)
		rooms.add(room)
		setDirty()

		return room
	}

	private fun placeStructureIfNeeded(ddLevel: ServerLevel, room: Room) {
		val blockState = ddLevel.getBlockState(room.portalPos)
		if (blockState.isBlock(ModBlocks.DEEP_DARK_PORTAL)) return

		val structure = ddLevel.structureManager.get(STRUCTURE).get()

		structure.placeInWorld(
			ddLevel,
			room.structureMin,
			BlockPos.ZERO,
			StructurePlaceSettings(),
			ddLevel.random,
			Block.UPDATE_ALL_IMMEDIATE
		)

		for (i in 0 until 8) {
			val berryPos = room.portalPos
				.offset(
					ddLevel.random.nextInt(-16, 16),
					-10,
					ddLevel.random.nextInt(-16, 16),
				)
				.mutable()

			while (ddLevel.isInWorldBounds(berryPos) && !ddLevel.isEmptyBlock(berryPos)) {
				berryPos.move(Direction.DOWN)
			}

			val length = ddLevel.random.nextInt(3, 9)
			for (dy in 0..length) {
				var state = Blocks.CAVE_VINES_PLANT.defaultBlockState()

				if (dy == 0 || ddLevel.random.nextInt(5) == 0) {
					state = state.setValue(CaveVines.BERRIES, true)
				}

				ddLevel.setBlockAndUpdate(berryPos, state)
				berryPos.move(Direction.DOWN)
			}
		}
	}

	fun returnFromDimension(entity: Entity) {
		val level = entity.level() as? ServerLevel ?: return
		val pData = entity.persistentData

		if (!pData.contains(PLAYER_RETURN_INFO)) {
			val targetLevel = level.server.overworld()
			val spawnPos = targetLevel.sharedSpawnPos

			entity.teleportTo(
				targetLevel,
				spawnPos.x + 0.5,
				spawnPos.y.toDouble(),
				spawnPos.z + 0.5,
				emptySet(),
				entity.yRot,
				entity.xRot,
			)

			return
		}

		val returnInfo = pData.getCompound(PLAYER_RETURN_INFO)
		val fromDimKey = ResourceKey.create(
			Registries.DIMENSION,
			ResourceLocation.parse(returnInfo.getString(FROM_DIM))
		)

		val targetLevel = level.server.getLevel(fromDimKey) ?: return
		val targetPortalPos = returnInfo.getLong(FROM_PORTAL_POS).toBlockPos()

		pData.remove(PLAYER_RETURN_INFO)
		teleportAbovePortal(entity, targetLevel, targetPortalPos)
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val listTag = tag.getList(ROOMS_NBT, Tag.TAG_COMPOUND.toInt())

		for (room in rooms) {
			val roomTag = CompoundTag()
			roomTag.putString(ORIGIN_DIM_NBT, room.originDimension.location().toString())
			roomTag.putLong(ORIGIN_PORTAL_POS_NBT, room.originPortalPos.asLong())
			roomTag.putLong(STRUCTURE_MIN_NBT, room.structureMin.asLong())
			listTag.add(roomTag)
		}

		tag.put(ROOMS_NBT, listTag)
		return tag
	}

	companion object {
		private const val SAVED_DATA_NAME = "eu_deep_dark"

		private const val ROOMS_NBT = "Rooms"
		private const val ORIGIN_DIM_NBT = "OriginDimension"
		private const val ORIGIN_PORTAL_POS_NBT = "OriginPortalPos"
		private const val STRUCTURE_MIN_NBT = "StructureMin"

		const val PLAYER_RETURN_INFO = "eu_deep_dark_return_info"
		private const val FROM_DIM = "from_dimension"
		private const val FROM_PORTAL_POS = "from_portal_pos"

		private const val TARGET_Y = DeepDarkConstants.CEILING_BOTTOM + 20
		private val STRUCTURE = ExcessiveUtilities.modResource("deep_dark_room")
		private val PORTAL_OFFSET = BlockPos(3, 0, 3)
		private val STRUCTURE_SIZE = BlockPos(7, 6, 7)

		private fun getBounds(structureMin: BlockPos): BoundingBox {
			return BoundingBox.fromCorners(structureMin, structureMin.offset(STRUCTURE_SIZE).offset(-1, -1, -1))
		}

		private fun teleportAbovePortal(entity: Entity, targetLevel: ServerLevel, portalPos: BlockPos) {
			val targetPos = portalPos.above().bottomCenter

			entity.teleportTo(
				targetLevel,
				targetPos.x,
				targetPos.y,
				targetPos.z,
				emptySet(),
				entity.yRot,
				entity.xRot,
			)
		}

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): DeepDarkHandler {
			val data = DeepDarkHandler()

			val listTag = tag.getList(ROOMS_NBT, Tag.TAG_COMPOUND.toInt())
			for (i in listTag.indices) {
				val roomTag = listTag.getCompound(i)

				val originDimension = ResourceKey.create(
					Registries.DIMENSION,
					ResourceLocation.parse(roomTag.getString(ORIGIN_DIM_NBT))
				)

				val originPortalPos = roomTag.getLong(ORIGIN_PORTAL_POS_NBT).toBlockPos()
				val structureMin = roomTag.getLong(STRUCTURE_MIN_NBT).toBlockPos()

				data.rooms.add(Room(originDimension, originPortalPos, structureMin))
			}

			return data
		}

		fun get(level: ServerLevel): DeepDarkHandler {
			if (level.dimension() != DeepDarkConstants.LEVEL_KEY) {
				return get(getDeepDarkLevel(level))
			}

			val storage = level.dataStorage
			val factory = Factory(::DeepDarkHandler, ::load)

			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}

		private fun getDeepDarkLevel(level: ServerLevel): ServerLevel {
			return level.server.getLevel(DeepDarkConstants.LEVEL_KEY)!!
		}

		fun handleGrue(player: ServerPlayer) {
			val interval = ServerConfig.CONFIG.deepDarkDamageInterval.get()
			val level = player.serverLevel()
			if (level.gameTime % interval != 0L) return

			val lightLevel = level.getMaxLocalRawBrightness(player.blockPosition())
			val safeLightLevel = ServerConfig.CONFIG.deepDarkSafeLightLevel.get()
			if (lightLevel >= safeLightLevel) return

			val damageAmount = ServerConfig.CONFIG.deepDarkDamageAmount.get().toFloat()
			val damageSource = player.damageSources().source(ModDamageTypeProvider.DARKNESS)

			player.hurt(damageSource, damageAmount)
		}
	}

	private data class Room(
		val originDimension: ResourceKey<Level>,
		val originPortalPos: BlockPos,
		val structureMin: BlockPos
	) {
		val portalPos: BlockPos = structureMin.offset(PORTAL_OFFSET)
		val bounds: BoundingBox = getBounds(structureMin)
	}

}
