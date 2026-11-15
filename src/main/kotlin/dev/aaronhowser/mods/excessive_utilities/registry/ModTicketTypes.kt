package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.TicketType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModTicketTypes {

	val TICKET_TYPE_REGISTRY: DeferredRegister<TicketType> =
		DeferredRegister.create(BuiltInRegistries.TICKET_TYPE, ExcessiveUtilities.MOD_ID)

	val QUANTUM_QUARRY: DeferredHolder<TicketType, TicketType> =
		TICKET_TYPE_REGISTRY.register("quantum_quarry", Supplier {
			TicketType(TicketType.NO_TIMEOUT, TicketType.FLAG_LOADING)
		})

}