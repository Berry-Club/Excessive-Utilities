package dev.aaronhowser.mods.excessive_utilities.block

import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CrossCollisionBlock

class BlackoutCurtainBlock : CrossCollisionBlock(
	1f, 1f,
	16f, 16f, 16f,
	Properties.ofFullCopy(Blocks.BLACK_WOOL)
) {

	override fun codec(): MapCodec<out CrossCollisionBlock> = CODEC

	companion object {
		val CODEC: MapCodec<BlackoutCurtainBlock> = simpleCodec { BlackoutCurtainBlock() }
	}

}