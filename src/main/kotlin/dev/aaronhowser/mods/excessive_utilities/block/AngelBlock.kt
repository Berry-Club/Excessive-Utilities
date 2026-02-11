package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.event.level.BlockDropsEvent

class AngelBlock : Block(
	Properties.of()
		.strength(0f, 1200f)
		.mapColor(MapColor.COLOR_BLACK)
) {

	companion object {
		fun handleDropEvent(event: BlockDropsEvent) {
			val breaker = event.breaker ?: return

			val state = event.state
			if (!state.isBlock(ModBlocks.ANGEL_BLOCK)) return

			val drops = event.drops

			for (drop in drops) {
				drop.setPos(breaker.position())
				drop.setNoPickUpDelay()

				if (breaker is Player) {
					drop.playerTouch(breaker)
				}
			}
		}
	}

}