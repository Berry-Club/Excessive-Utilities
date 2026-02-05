package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState

class ManualMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.MANUAL_MILL.get(), pos, blockState) {

	private val playersCranking: MutableSet<Player> = mutableSetOf()
	private var isBeingCranked: Boolean = false

	override fun getGp(): Int {
		return ServerConfig.CONFIG.manualMillGenerationPerPlayer.get() * playersCranking.size
	}

}