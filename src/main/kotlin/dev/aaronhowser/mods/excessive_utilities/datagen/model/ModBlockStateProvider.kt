package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
	output: PackOutput,
	private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun registerStatesAndModels() {
		blackoutCurtain()
	}

	fun blackoutCurtain() {
		val block = ModBlocks.BLACKOUT_CURTAIN.get()

		models()
			.withExistingParent("blackout_curtain_side", "block/glass_pane_side")
			.texture("pane", "block/blackout_curtain")

	}

	private fun name(block: Block): String {
		return BuiltInRegistries.BLOCK.getKey(block).path
	}

}