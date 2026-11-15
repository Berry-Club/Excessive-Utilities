package dev.aaronhowser.mods.excessive_utilities.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

	val ATTACHMENT_TYPE_REGISTRY: DeferredRegister<AttachmentType<*>> =
		DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ExcessiveUtilities.MOD_ID)

	val SOUL_DEBT: DeferredHolder<AttachmentType<*>, AttachmentType<SoulDebt>> =
		register(
			"soul_debt",
			AttachmentType
				.builder(::SoulDebt)
				.serialize(SoulDebt.CODEC.fieldOf("value"))
				.sync(SoulDebt.STREAM_CODEC)
				.copyOnDeath()
				.build()
		)

	val IS_CURSED: DeferredHolder<AttachmentType<*>, AttachmentType<Boolean>> =
		register(
			"is_cursed",
			AttachmentType
				.builder(Supplier { false })
				.serialize(Codec.BOOL.fieldOf("value"))
				.sync(ByteBufCodecs.BOOL)
				.build()
		)

	val WINGS: DeferredHolder<AttachmentType<*>, AttachmentType<AngelRingItem.Type>> =
		register(
			"wings",
			AttachmentType
				.builder(Supplier { AngelRingItem.Type.INVISIBLE })
				.sync(AngelRingItem.Type.STREAM_CODEC)
				.build()
		)

	private fun <T : Any> register(name: String, type: AttachmentType<T>): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return ATTACHMENT_TYPE_REGISTRY.register(name, Supplier { type })
	}

}