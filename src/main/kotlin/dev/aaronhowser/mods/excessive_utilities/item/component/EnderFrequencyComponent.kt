package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.UUIDUtil
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*

data class EnderFrequencyComponent(
	val name: String,
	val owner: Optional<UUID>
) {

	val isPrivate: Boolean = owner.isPresent

	companion object {
		const val MAX_NAME_LENGTH = 32

		val CODEC: Codec<EnderFrequencyComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.STRING.fieldOf("name").forGetter(EnderFrequencyComponent::name),
					UUIDUtil.CODEC.optionalFieldOf("owner").forGetter(EnderFrequencyComponent::owner)
				).apply(instance, ::EnderFrequencyComponent)
			}

		val STREAM_CODEC: StreamCodec<ByteBuf, EnderFrequencyComponent> =
			StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, EnderFrequencyComponent::name,
				ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), EnderFrequencyComponent::owner,
				::EnderFrequencyComponent
			)

		fun create(name: String, isPrivate: Boolean, playerUuid: UUID): EnderFrequencyComponent? {
			val normalizedName = name.trim()
			if (normalizedName.isEmpty() || normalizedName.length > MAX_NAME_LENGTH) return null

			val owner = if (isPrivate) Optional.of(playerUuid) else Optional.empty()
			return EnderFrequencyComponent(normalizedName, owner)
		}
	}
}