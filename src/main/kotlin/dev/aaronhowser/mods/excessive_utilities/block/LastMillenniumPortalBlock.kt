package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Portal
import net.minecraft.world.level.portal.DimensionTransition

class LastMillenniumPortalBlock : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)), Portal {

	override fun getPortalDestination(level: ServerLevel, entity: Entity, pos: BlockPos): DimensionTransition? {
		TODO("Not yet implemented")
	}

}