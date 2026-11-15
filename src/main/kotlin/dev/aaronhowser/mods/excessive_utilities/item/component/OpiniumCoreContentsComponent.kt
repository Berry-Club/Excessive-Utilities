package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import java.util.Optional

data class OpiniumCoreContentsComponent(
	private val center: Optional<ItemStackTemplate>,
	private val outer: Optional<ItemStackTemplate>,
	val name: Component
) {

	fun getStack(): ItemStack = getStackTemplate().create()

	fun getStackTemplate(): ItemStackTemplate {
		val components = DataComponentPatch.builder()
			.set(ModDataComponents.OPINIUM_CORE_CONTENTS.get(), this)
			.build()

		return ItemStackTemplate(ModItems.OPINIUM_CORE.get(), 1, components)
	}

	fun getCenter(): ItemStack = center.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY)
	fun getOuter(): ItemStack = outer.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY)
	fun getCenterTemplate(): ItemStackTemplate? = center.orElse(null)
	fun getOuterTemplate(): ItemStackTemplate? = outer.orElse(null)

	companion object {
		val CODEC: Codec<OpiniumCoreContentsComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ItemStackTemplate.CODEC
						.optionalFieldOf("center")
						.forGetter(OpiniumCoreContentsComponent::center),
					ItemStackTemplate.CODEC
						.optionalFieldOf("outer")
						.forGetter(OpiniumCoreContentsComponent::outer),
					ComponentSerialization.CODEC
						.fieldOf("name")
						.forGetter(OpiniumCoreContentsComponent::name)
				).apply(instance, ::OpiniumCoreContentsComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, OpiniumCoreContentsComponent> =
			StreamCodec.composite(
				ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), OpiniumCoreContentsComponent::center,
				ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), OpiniumCoreContentsComponent::outer,
				ComponentSerialization.STREAM_CODEC, OpiniumCoreContentsComponent::name,
				::OpiniumCoreContentsComponent
			)

		fun getDefaultTiers(): List<OpiniumCoreContentsComponent> {
			return listOf(
				pathetic(), mediocre(), passable(),
				decent(), good(), damnGood(),
				amazing(), inspiring(), perfected()
			)
		}

		fun pathetic() = OpiniumCoreContentsComponent(
			Optional.empty(),
			Optional.empty(),
			ModItemLang.OPINIUM_CORE_PATHETIC.toComponent()
		)

		fun mediocre() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.GOLD_BLOCK, 1)),
			Optional.of(ItemStackTemplate(Items.IRON_BLOCK, 1)),
			ModItemLang.OPINIUM_CORE_MEDIOCRE.toComponent()
		)

		fun passable() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.DIAMOND_BLOCK, 1)),
			Optional.of(ItemStackTemplate(Items.GOLD_BLOCK, 1)),
			ModItemLang.OPINIUM_CORE_PASSABLE.toComponent()
		)

		fun decent() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.EMERALD_BLOCK, 1)),
			Optional.of(ItemStackTemplate(Items.DIAMOND_BLOCK, 1)),
			ModItemLang.OPINIUM_CORE_DECENT.toComponent()
		)

		fun good() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.CHORUS_FLOWER, 1)),
			Optional.of(ItemStackTemplate(Items.EMERALD_BLOCK, 1)),
			ModItemLang.OPINIUM_CORE_GOOD.toComponent()
		)

		fun damnGood() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.EXPERIENCE_BOTTLE, 1)),
			Optional.of(ItemStackTemplate(Items.CHORUS_FLOWER, 1)),
			ModItemLang.OPINIUM_CORE_DAMN_GOOD.toComponent()
		)

		fun amazing() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.ELYTRA, 1)),
			Optional.of(ItemStackTemplate(Items.EXPERIENCE_BOTTLE, 1)),
			ModItemLang.OPINIUM_CORE_AMAZING.toComponent()
		)

		fun inspiring() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.NETHER_STAR, 1)),
			Optional.of(ItemStackTemplate(Items.ELYTRA, 1)),
			ModItemLang.OPINIUM_CORE_INSPIRING.toComponent()
		)

		fun perfected() = OpiniumCoreContentsComponent(
			Optional.of(ItemStackTemplate(Items.IRON_INGOT, 1)),
			Optional.of(ItemStackTemplate(Items.NETHER_STAR, 1)),
			ModItemLang.OPINIUM_CORE_PERFECTED.toComponent()
		)
	}

}