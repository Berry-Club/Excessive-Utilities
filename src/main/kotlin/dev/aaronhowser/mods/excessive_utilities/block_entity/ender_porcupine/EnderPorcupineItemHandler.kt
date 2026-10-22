package dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine

import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource

class EnderPorcupineItemHandler(
	private val porcupine: EnderPorcupineBlockEntity,
	private val direction: Direction?
) : DelegatingResourceHandler<ItemResource>({
	porcupine.findTargetItemHandler(direction)
		?: error("Ender Porcupine item target disappeared while transferring")
})
