package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toBlockPos
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toChunkPos
import dev.aaronhowser.mods.aaron.misc.AaronUtil
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.ListTag
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedDataType
import java.util.*

class LastMillenniumHandler : SavedData() {

	private val playerChunks: MutableMap<UUID, ChunkPos> = mutableMapOf()

	fun teleportIntoDimension(entity: Entity, target: UUID) {
		val level = entity.level() as? ServerLevel ?: return

		val data = entity.persistentData
		val returnInfo = CompoundTag()
		returnInfo.putString(FROM_DIM, level.dimension().identifier().toString())
		returnInfo.putLong(FROM_POS, entity.blockPosition().asLong())
		data.put(PLAYER_RETURN_INFO, returnInfo)

		val targetLevel = getLastMillenniumLevel(level)
		val chunkPos = getChunk(target)

		placeStructureIfNeeded(targetLevel, chunkPos)

		val targetPos = chunkPos.getMiddleBlockPosition(64).bottomCenter

		entity.teleportTo(
			targetLevel,
			targetPos.x,
			targetPos.y,
			targetPos.z,
			emptySet(),
			entity.yRot,
			entity.xRot,
		true
		)
	}

	private fun placeStructureIfNeeded(tlmLevel: ServerLevel, chunkPos: ChunkPos) {
		val minPos = chunkPos.worldPosition.atY(63)
		val portalPos = minPos.offset(8, 0, 8)

		val blockState = tlmLevel.getBlockState(portalPos)
		if (blockState.isBlock(ModBlocks.LAST_MILLENNIUM_PORTAL)) return

		val structure = tlmLevel.structureManager.get(STRUCTURE).get()

		structure.placeInWorld(
			tlmLevel,
			minPos,
			BlockPos.ZERO,
			StructurePlaceSettings(),
			tlmLevel.random,
			Block.UPDATE_ALL_IMMEDIATE
		)
	}

	fun returnFromDimension(entity: Entity) {
		val level = entity.level() as? ServerLevel ?: return

		val data = entity.persistentData

		if (!data.contains(PLAYER_RETURN_INFO)) {
			val targetLevel = level.server.overworld()
			val spawnPos = targetLevel.respawnData.pos()

			entity.teleportTo(
				targetLevel,
				spawnPos.x + 0.5,
				spawnPos.y.toDouble(),
				spawnPos.z + 0.5,
				emptySet(),
				entity.yRot,
				entity.xRot,
			true
			)

			return
		}

		val returnInfo = data.getCompound(PLAYER_RETURN_INFO).orElseThrow()

		val fromDimString = returnInfo.getString(FROM_DIM).orElseThrow()
		val fromPosLong = returnInfo.getLong(FROM_POS).orElseThrow()

		val fromDimKey = ResourceKey.create(
			Registries.DIMENSION,
			Identifier.parse(fromDimString)
		)

		val fromPos = fromPosLong.toBlockPos()

		val targetLevel = level.server.getLevel(fromDimKey) ?: return

		data.remove(PLAYER_RETURN_INFO)

		entity.teleportTo(
			targetLevel,
			fromPos.x + 0.5,
			fromPos.y.toDouble(),
			fromPos.z + 0.5,
			emptySet(),
			entity.yRot,
			entity.xRot,
		true
		)
	}

	fun getChunk(entity: Entity): ChunkPos {
		return getChunk(entity.uuid)
	}

	fun getChunk(uuid: UUID): ChunkPos {
		val existing = playerChunks[uuid]
		if (existing != null) {
			return existing
		}

		val nextIndex = playerChunks.size
		val (x, y) = AaronUtil.getGridSpiralPos(nextIndex)
		val chunksBetween = ServerConfig.CONFIG.tlmChunkDistance.get()
		val chunkPos = ChunkPos(x * chunksBetween, y * chunksBetween)
		playerChunks[uuid] = chunkPos
		return chunkPos
	}

	fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val listTag = ListTag()

		for ((uuid, chunkPos) in playerChunks) {
			val chunkTag = CompoundTag()
			chunkTag.store(PLAYER_NBT, UUIDUtil.CODEC, uuid)
			chunkTag.putLong(CHUNK_NBT, chunkPos.pack())
			listTag.add(chunkTag)
		}

		tag.put(CHUNK_LIST_NBT, listTag)
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_last_millennium"

		const val CHUNK_LIST_NBT = "Chunks"
		const val PLAYER_NBT = "Player"
		const val CHUNK_NBT = "Chunk"

		const val PLAYER_RETURN_INFO = "eu_tlm_return_info"
		const val FROM_DIM = "from_dimension"
		const val FROM_POS = "from_pos"

		val STRUCTURE = ExcessiveUtilities.modResource("the_last_millennium")

		val LEVEL_KEY: ResourceKey<Level> =
			ResourceKey.create(Registries.DIMENSION, ExcessiveUtilities.modResource("the_last_millennium"))

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): LastMillenniumHandler {
			val data = LastMillenniumHandler()

			val listTag = tag.getList(CHUNK_LIST_NBT).orElseThrow()
			for (i in listTag.indices) {
				val coordinateTag = listTag.getCompound(i).orElseThrow()

				val uuid = coordinateTag.read(PLAYER_NBT, UUIDUtil.CODEC).orElseThrow()
				val chunkLong = coordinateTag.getLong(CHUNK_NBT).orElseThrow()
				val chunkPos = chunkLong.toChunkPos()

				data.playerChunks[uuid] = chunkPos
			}

			return data
		}

		fun get(level: ServerLevel): LastMillenniumHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val type = SavedDataType(
                dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities.modResource(SAVED_DATA_NAME),
                ::LastMillenniumHandler,
                CompoundTag.CODEC.xmap(
                    { tag -> load(tag, level.registryAccess()) },
                    { data -> data.save(CompoundTag(), level.registryAccess()) }
                ),
                DataFixTypes.SAVED_DATA_COMMAND_STORAGE
            )
            return storage.computeIfAbsent(type)
		}

		fun getLastMillenniumLevel(level: ServerLevel): ServerLevel {
			return level.server.getLevel(LEVEL_KEY)!!
		}
	}
}