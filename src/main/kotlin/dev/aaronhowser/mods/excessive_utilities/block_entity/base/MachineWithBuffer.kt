package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.item.ItemResource

interface MachineWithBuffer {

	fun collectReceivingItemHandlers(level: ServerLevel): Collection<ResourceHandler<ItemResource>>

	fun pushOutItems(
		level: ServerLevel,
		bufferContainer: Container,
		bufferItemHandler: ResourceHandler<ItemResource>
	) {
		if (bufferContainer.isEmpty) return

		val targets = collectReceivingItemHandlers(level)
		for (target in targets) {
			if (bufferContainer.isEmpty) break

			ResourceHandlerUtil.moveStacking(bufferItemHandler, target, { true }, Int.MAX_VALUE, null)
		}

	}

}
