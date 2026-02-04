package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CrossCollisionBlock
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

		val post = models()
			.withExistingParent(name(block) + "_post", mcLoc("block/glass_pane_post"))

		val side = models()
			.withExistingParent(name(block) + "_side", mcLoc("block/glass_pane_side"))
		val sideAlt = models()
			.withExistingParent(name(block) + "_side_alt", mcLoc("block/glass_pane_side_alt"))

		val noside = models()
			.withExistingParent(name(block) + "_noside", mcLoc("block/glass_pane_noside"))
		val nosideAlt = models()
			.withExistingParent(name(block) + "_noside_alt", mcLoc("block/glass_pane_noside_alt"))

		var multipartBuilder = getMultipartBuilder(block)
			.part()
			.modelFile(post)
			.addModel()
			.end()

		for (direction in Direction.Plane.HORIZONTAL) {

			if (direction == Direction.NORTH) {
				multipartBuilder = multipartBuilder
					.part()
					.modelFile(side)
					.addModel()
					.condition(CrossCollisionBlock.NORTH, true)
					.end()

					.part()
					.modelFile(noside)
					.addModel()
					.condition(CrossCollisionBlock.NORTH, false)
					.end()
			} else if (direction == Direction.EAST) {
				multipartBuilder = multipartBuilder
					.part()
					.modelFile(side)
					.rotationY(90)
					.addModel()
					.condition(CrossCollisionBlock.EAST, true)
					.end()

					.part()
					.modelFile(nosideAlt)
					.addModel()
					.condition(CrossCollisionBlock.EAST, false)
					.end()
			}

		}

	}

	private fun name(block: Block): String {
		return BuiltInRegistries.BLOCK.getKey(block).path
	}

}