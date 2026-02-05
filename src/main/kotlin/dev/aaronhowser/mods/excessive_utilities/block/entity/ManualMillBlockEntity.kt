package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getPovResult
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class ManualMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.MANUAL_MILL.get(), pos, blockState) {

	private val playersCranking: MutableSet<Player> = mutableSetOf()
	private var isBeingCranked: Boolean = false

	var prevTurnDegrees: Float = 0f
	var turnDegrees: Float = 0f
		set(value) {
			field = turnDegrees % 360f
		}

	override fun getGpGeneration(): Double {
		return ServerConfig.CONFIG.manualMillGenerationPerPlayer.get() * playersCranking.size
	}

	fun startCranking(player: Player) {
		playersCranking.add(player)
	}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		playersCranking.removeIf { player ->
			val lookingAt = player.getPovResult().blockPos
			lookingAt != this.blockPos
		}

		val wasBeingCranked = isBeingCranked
		isBeingCranked = playersCranking.isNotEmpty()
		if (wasBeingCranked != isBeingCranked) {
			setChanged()
		}

		for (player in playersCranking) {
			player.swing(InteractionHand.MAIN_HAND, true)
		}
	}

	override fun clientTick(level: Level) {
		if (isBeingCranked) {
			prevTurnDegrees = turnDegrees
			turnDegrees += 15f
		}
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putBoolean(IS_BEING_CRANKED_KEY, isBeingCranked)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		isBeingCranked = tag.getBoolean(IS_BEING_CRANKED_KEY)
	}

	// Syncs with client
	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	companion object {
		const val IS_BEING_CRANKED_KEY = "IsBeingCranked"
	}

}