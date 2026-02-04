package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
	output: PackOutput,
	private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun registerStatesAndModels() {

	}

}