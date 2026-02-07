package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe.WirelessFeNetworkHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.*

class WirelessFeTransmitterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_TRANSMITTER.get(), pos, blockState) {

	var ownerUuid: UUID? = null

	fun getEnergyStorage(): IEnergyStorage? {
		val level = level as? ServerLevel ?: return null
		val uuid = ownerUuid ?: return null
		return WirelessFeNetworkHandler.get(level).getNetwork(uuid).energyStorage
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val uuid = ownerUuid
		if (uuid != null) {
			tag.putUUID(OWNER_UUID_NBT, uuid)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
	}

}