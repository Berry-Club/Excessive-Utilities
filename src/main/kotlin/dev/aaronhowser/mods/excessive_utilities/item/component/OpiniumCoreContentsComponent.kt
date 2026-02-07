package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

data class OpiniumCoreContentsComponent(
	val center: ItemStack,
	val outer: ItemStack,
	val name: Component
) {

	companion object {
		val CODEC: Codec<OpiniumCoreContentsComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ItemStack.CODEC
						.fieldOf("center")
						.forGetter(OpiniumCoreContentsComponent::center),
					ItemStack.CODEC
						.fieldOf("outer")
						.forGetter(OpiniumCoreContentsComponent::outer),
					ComponentSerialization.CODEC
						.fieldOf("name")
						.forGetter(OpiniumCoreContentsComponent::name)
				).apply(instance, ::OpiniumCoreContentsComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, OpiniumCoreContentsComponent> =
			StreamCodec.composite(
				ItemStack.STREAM_CODEC, OpiniumCoreContentsComponent::center,
				ItemStack.STREAM_CODEC, OpiniumCoreContentsComponent::outer,
				ComponentSerialization.STREAM_CODEC, OpiniumCoreContentsComponent::name,
				::OpiniumCoreContentsComponent
			)
	}

}