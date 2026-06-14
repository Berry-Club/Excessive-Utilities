package dev.aaronhowser.mods.excessive_utilities.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
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
				.serialize(SoulDebt.CODEC)
				.sync(SoulDebt.STREAM_CODEC)
				.copyOnDeath()
				.build()
		)

	val IS_CURSED: DeferredHolder<AttachmentType<*>, AttachmentType<Boolean>> =
		register(
			"is_cursed",
			AttachmentType
				.builder(Supplier { false })
				.serialize(Codec.BOOL)
				.sync(ByteBufCodecs.BOOL)
				.build()
		)

	private fun <T> register(name: String, type: AttachmentType<T>): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return ATTACHMENT_TYPE_REGISTRY.register(name, Supplier { type })
	}

}