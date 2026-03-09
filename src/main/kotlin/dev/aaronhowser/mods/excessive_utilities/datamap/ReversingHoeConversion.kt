package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.datamaps.DataMapType

class ReversingHoeConversion(
	val outputState: BlockState
) {

	companion object {
		val CODEC: Codec<ReversingHoeConversion> =
			BlockState.CODEC.xmap(::ReversingHoeConversion, ReversingHoeConversion::outputState)

		val DATA_MAP: DataMapType<Block?, ReversingHoeConversion> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("reversing_hoe_conversion"),
					Registries.BLOCK,
					CODEC
				)
				.synced(CODEC, true)
				.build()
	}

}