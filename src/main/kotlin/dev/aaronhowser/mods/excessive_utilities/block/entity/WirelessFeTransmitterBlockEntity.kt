package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe.WirelessFeNetworkHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage

class WirelessFeTransmitterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.WIRELESS_FE_TRANSMITTER.get(), pos, blockState) {

	private val destinationCache: MutableSet<IEnergyStorage> = mutableSetOf()
	private var destinationsUsedLastTick: Int = 0

	fun getEnergyStorage(): IEnergyStorage? {
		val level = level as? ServerLevel ?: return null
		val uuid = ownerUuid ?: return null
		return WirelessFeNetworkHandler.get(level).getNetwork(uuid).energyStorage
	}

	override fun getGpUsage(): Double {
		val per = ServerConfig.CONFIG.wirelessFeTransmitterGpCostPerConnection.get()
		return per * destinationsUsedLastTick
	}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		if (level.gameTime % 20 == 0L) {
			recalculateDestinationCache(level)
		}

		val ownerUuid = ownerUuid ?: return
		val networkEnergy = WirelessFeNetworkHandler.get(level).getNetwork(ownerUuid).energyStorage

		destinationsUsedLastTick = 0
		val fePerTick = ServerConfig.CONFIG.wirelessFeTransmitterRate.get()

		for (destination in destinationCache) {
			if (networkEnergy.energyStored <= 0) break

			val maxAmount = minOf(fePerTick, networkEnergy.energyStored)
			val actualAmount = destination.receiveEnergy(maxAmount, true)

			if (actualAmount > 0) {
				destination.receiveEnergy(actualAmount, false)
				networkEnergy.extractEnergy(actualAmount, false)
				destinationsUsedLastTick++
			}
		}
	}

	fun recalculateDestinationCache(level: ServerLevel) {
		destinationCache.clear()

		val radius = ServerConfig.CONFIG.wirelessFeTransmitterRange.get()

		val blocks = BlockPos.betweenClosed(
			blockPos.offset(-radius, -radius, -radius),
			blockPos.offset(radius, radius, radius)
		)

		val directions = listOf(null, *Direction.entries.toTypedArray())

		posLoop@
		for (pos in blocks) {

			dirLoop@
			for (dir in directions) {
				val stateThere = level.getBlockState(pos)
				if (stateThere.isBlock(ModBlockTagsProvider.FE_TRANSMITTER_BLACKLIST)) continue@dirLoop

				@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
				val energyHandler = level.getCapability(
					Capabilities.EnergyStorage.BLOCK,
					pos,
					dir
				)

				if (energyHandler != null && energyHandler.canReceive()) {
					destinationCache.add(energyHandler)
					continue@posLoop
				}
			}
		}
	}

	companion object {
		fun getEnergyCapability(transmitter: WirelessFeTransmitterBlockEntity, direction: Direction?): IEnergyStorage? {
			return transmitter.getEnergyStorage()
		}

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: WirelessFeTransmitterBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)

			}
		}
	}

}