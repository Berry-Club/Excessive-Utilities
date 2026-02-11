package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.serialization.AaronExtraCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome

class MagicalSnowGlobeProgressComponent(
	val requirements: HashMap<TagKey<Biome>, Boolean>
) {

	constructor(map: Map<TagKey<Biome>, Boolean>) : this(HashMap(map))

	companion object {
		val CODEC: Codec<MagicalSnowGlobeProgressComponent> =
			Codec.unboundedMap(
				TagKey.codec(Registries.BIOME),
				Codec.BOOL
			)
				.xmap(
					::MagicalSnowGlobeProgressComponent,
					MagicalSnowGlobeProgressComponent::requirements
				)

		val STREAM_CODEC: StreamCodec<ByteBuf?, MagicalSnowGlobeProgressComponent> =
			ByteBufCodecs.map(
				::HashMap,
				AaronExtraCodecs.tagKeyStreamCodec(Registries.BIOME),
				ByteBufCodecs.BOOL
			).map(::MagicalSnowGlobeProgressComponent, MagicalSnowGlobeProgressComponent::requirements)
	}

}