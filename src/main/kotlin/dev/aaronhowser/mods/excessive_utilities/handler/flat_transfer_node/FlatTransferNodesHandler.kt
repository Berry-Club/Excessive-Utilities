package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import dev.aaronhowser.mods.aaron.misc.ObservableMutableSet
import dev.aaronhowser.mods.aaron.misc.observableMutableSetOf
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData

class FlatTransferNodesHandler : SavedData() {

	private val nodes: ObservableMutableSet<FlatTransferNode> = observableMutableSetOf { setDirty() }

	fun tick(level: ServerLevel) {
		for (node in nodes) {
			node.tick(level)
		}
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		TODO("Not yet implemented")
	}


}