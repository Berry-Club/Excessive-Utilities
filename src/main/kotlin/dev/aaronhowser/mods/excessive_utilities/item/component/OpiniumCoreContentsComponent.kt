package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

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

		fun getDefaultTiers(): List<OpiniumCoreContentsComponent> {
			return listOf(
				OpiniumCoreContentsComponent(
					Items.IRON_BLOCK.defaultInstance,
					ModItems.RED_COAL.getDefaultInstance(),
					ModItemLang.OPINIUM_CORE_PATHETIC.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.GOLD_BLOCK.defaultInstance,
					Items.IRON_BLOCK.defaultInstance,
					ModItemLang.OPINIUM_CORE_MEDIOCRE.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.DIAMOND_BLOCK.defaultInstance,
					Items.GOLD_BLOCK.defaultInstance,
					ModItemLang.OPINIUM_CORE_PASSABLE.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.EMERALD_BLOCK.defaultInstance,
					Items.DIAMOND_BLOCK.defaultInstance,
					ModItemLang.OPINIUM_CORE_DECENT.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.CHORUS_FLOWER.defaultInstance,
					Items.EMERALD_BLOCK.defaultInstance,
					ModItemLang.OPINIUM_CORE_GOOD.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.EXPERIENCE_BOTTLE.defaultInstance,
					Items.CHORUS_FLOWER.defaultInstance,
					ModItemLang.OPINIUM_CORE_DAMN_GOOD.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.ELYTRA.defaultInstance,
					Items.EXPERIENCE_BOTTLE.defaultInstance,
					ModItemLang.OPINIUM_CORE_AMAZING.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.NETHER_STAR.defaultInstance,
					Items.ELYTRA.defaultInstance,
					ModItemLang.OPINIUM_CORE_INSPIRING.toComponent()
				),
				OpiniumCoreContentsComponent(
					Items.IRON_INGOT.defaultInstance,
					Items.NETHER_STAR.defaultInstance,
					ModItemLang.OPINIUM_CORE_PERFECTED.toComponent()
				)
			)
		}
	}

}