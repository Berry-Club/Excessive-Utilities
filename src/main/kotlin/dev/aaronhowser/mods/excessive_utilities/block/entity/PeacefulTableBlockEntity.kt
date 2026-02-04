package dev.aaronhowser.mods.excessive_utilities.block.entity

import com.mojang.authlib.GameProfile
import com.mojang.datafixers.util.Either
import dev.aaronhowser.mods.aaron.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.AaronUtil
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.extensions.IOwnedSpawner
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.items.ItemHandlerHelper
import java.lang.ref.WeakReference
import java.util.*
import kotlin.jvm.optionals.getOrNull

class PeacefulTableBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.PEACEFUL_TABLE.get(), pos, blockState), IOwnedSpawner {

	private var uuid: UUID? = null
	private var fakePlayer: WeakReference<FakePlayer>? = null

	private fun initFakePlayer() {
		val level = level as? ServerLevel ?: return

		if (this.uuid == null) {
			this.uuid = UUID.randomUUID()
			setChanged()
		}

		val gameProfile = GameProfile(this.uuid, "EU_PeacefulTable")
		val fakePlayer = FakePlayerFactory.get(level, gameProfile)

		fakePlayer.isSilent = true
		fakePlayer.setOnGround(true)

		this.fakePlayer = WeakReference(fakePlayer)
		setChanged()
	}

	private fun tick() {
		val level = this.level as? ServerLevel ?: return
		if (level.difficulty != Difficulty.PEACEFUL) return

		val adjacentInventories = Direction.entries
			.mapNotNull { direction ->
				level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos.relative(direction), direction.opposite)
			}

		val sword = adjacentInventories
			.asSequence()
			.flatMap { inventory ->
				(0 until inventory.slots).asSequence().map { inventory.getStackInSlot(it) }
			}
			.firstOrNull { it.item is SwordItem }

		if (sword == null) {
			return
		}

		val mob = getMobToSpawn(this) ?: return
		val drops = getDrops(mob)

		for (drop in drops) {
			var copy = drop.copy()

			for (inv in adjacentInventories) {
				if (copy.isEmpty) break

				copy = ItemHandlerHelper.insertItemStacked(inv, copy, false)
			}
		}

	}

	override fun getOwner(): Either<BlockEntity, Entity> {
		return Either.left(this)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val uuid = this.uuid
		if (uuid != null) {
			tag.putUUID(UUID_NBT, uuid)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		this.uuid = tag.getUuidOrNull(UUID_NBT)
	}

	companion object {
		const val UUID_NBT = "UUID"

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: PeacefulTableBlockEntity
		) {
			if (level.isServerSide) {
				blockEntity.tick()
			}
		}

		private fun getMobToSpawn(table: PeacefulTableBlockEntity): Mob? {
			val level = table.level as? ServerLevel ?: return null
			val pos = table.blockPos

			val possibleMobTypes = level.getBiome(pos).value().mobSettings.getMobs(MobCategory.MONSTER)

			val entityType = possibleMobTypes
				.getRandom(level.random)
				.getOrNull()
				?.type
				?: return null

			val mob = entityType.create(level) as? Mob ?: return null
			mob.moveTo(pos.bottomCenter)

			EventHooks.finalizeMobSpawnSpawner(
				mob,
				level,
				DifficultyInstance(
					Difficulty.HARD,
					level.dayTime,
					level.getChunkAt(pos).inhabitedTime,
					level.moonBrightness
				),
				MobSpawnType.SPAWNER,
				null,
				table,
				true
			)

			return mob
		}

		private fun getDrops(mob: Mob): List<ItemStack> {
			val level = mob.level() as? ServerLevel ?: return emptyList()
			val damageSource = level.damageSources().genericKill()

			mob.captureDrops(mutableListOf())

			val lootTable = level.server.reloadableRegistries().getLootTable(mob.lootTable)
			val lootParamsBuilder = LootParams.Builder(level)
				.withParameter(LootContextParams.THIS_ENTITY, mob)
				.withParameter(LootContextParams.ORIGIN, mob.position())
				.withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
				.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.entity)
				.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.directEntity)

			val lootParams = lootParamsBuilder.create(LootContextParamSets.ENTITY)
			lootTable.getRandomItems(lootParams, mob.lootTableSeed, mob::spawnAtLocation)

			val drops = mob.captureDrops(null) ?: return emptyList()

			val list = mutableListOf<ItemStack>()

			if (!CommonHooks.onLivingDrops(mob, damageSource, drops, true)) {
				for (drop in drops) {
					list.add(drop.item)
				}
			}

			return AaronUtil.flattenStacks(list)
		}
	}

}