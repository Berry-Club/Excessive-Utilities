package dev.aaronhowser.mods.excessive_utilities.block_entity

import com.mojang.datafixers.util.Either
import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.BaseSpawner
import net.minecraft.world.level.Level
import net.minecraft.world.level.SpawnData
import net.minecraft.world.level.Spawner
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

//TODO: Apply the same logic as Cursed Earth... which currently does nothing
class ResturbedMobSpawnerBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SyncingBlockEntity(ModBlockEntityTypes.RESTURBED_MOB_SPAWNER.get(), pos, blockState), Spawner {

	val spawner: BaseSpawner =
		object : BaseSpawner() {
			override fun broadcastEvent(level: Level, pos: BlockPos, eventId: Int) {
				level.blockEvent(pos, blockState.block, eventId, 0)
			}

			override fun setNextSpawnData(level: Level?, pos: BlockPos, nextSpawnData: SpawnData) {
				super.setNextSpawnData(level, pos, nextSpawnData)

				if (level != null) {
					val state = level.getBlockState(pos)
					level.sendBlockUpdated(pos, state, state, 4)
				}
			}

			override fun getOwner(): Either<BlockEntity, Entity> {
				return Either.left(this@ResturbedMobSpawnerBlockEntity)
			}

			override fun clientTick(level: Level, pos: BlockPos) {
				if (level.hasNeighborSignal(pos)) {
					oSpin = spin
					return
				}

				super.clientTick(level, pos)
			}
		}

	override fun setEntityId(entityType: EntityType<*>, random: RandomSource) {
		spawner.setEntityId(entityType, level, random, blockPos)
		setChanged()
	}

	override fun triggerEvent(id: Int, type: Int): Boolean {
		val level = level
		return (level != null && spawner.onEventTriggered(level, id)) || super.triggerEvent(id, type)
	}

	private fun serverTick(level: ServerLevel) {
		if (level.hasNeighborSignal(blockPos)) return
		spawner.serverTick(level, blockPos)
	}

	private fun clientTick(level: Level) {
		spawner.clientTick(level, blockPos)
	}

	override fun collectImplicitComponents(components: DataComponentMap.Builder) {
		super.collectImplicitComponents(components)

		val level = level ?: return
		val nbt = saveCustomOnly(level.registryAccess())
		addEntityType(nbt, type)
		components.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(nbt))
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		spawner.save(tag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		spawner.load(level, blockPos, tag)
	}

	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag {
		val tag = saveCustomOnly(pRegistries)
		tag.remove("SpawnPotentials")
		return tag
	}

	companion object {
		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: ResturbedMobSpawnerBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			} else {
				blockEntity.clientTick(level)
			}
		}
	}

}