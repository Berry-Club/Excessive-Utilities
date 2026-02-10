package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.renderer.RenderType
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
		singleTextureBlocks()
		blackoutCurtain()
		athenaBlocks()
		slightlyLargerChest()
	}

	private fun slightlyLargerChest() {
		val block = ModBlocks.SLIGHTLY_LARGER_CHEST.get()
		val name = name(block)

		val front = modLoc("block/slightly_larger_chest/front")
		val side = modLoc("block/slightly_larger_chest/side")
		val top = modLoc("block/slightly_larger_chest/top")

		val model = models()
			.orientableWithBottom(name, side, front, top, top )

		simpleBlockWithItem(block, model)
	}

	private fun blackoutCurtain() {
		val block = ModBlocks.BLACKOUT_CURTAIN.get()

		val texture = modLoc("block/blackout_curtain")

		itemModels()
			.withExistingParent(name(block), "item/generated")
			.texture("layer0", texture)

		val post = models()
			.withExistingParent(name(block) + "_post", mcLoc("block/glass_pane_post"))
			.texture("pane", texture)
			.texture("edge", texture)

		val side = models()
			.withExistingParent(name(block) + "_side", mcLoc("block/glass_pane_side"))
			.texture("pane", texture)
			.texture("edge", texture)
		val sideAlt = models()
			.withExistingParent(name(block) + "_side_alt", mcLoc("block/glass_pane_side_alt"))
			.texture("pane", texture)
			.texture("edge", texture)

		val noside = models()
			.withExistingParent(name(block) + "_noside", mcLoc("block/glass_pane_noside"))
			.texture("pane", texture)
			.texture("edge", texture)
		val nosideAlt = models()
			.withExistingParent(name(block) + "_noside_alt", mcLoc("block/glass_pane_noside_alt"))
			.texture("pane", texture)
			.texture("edge", texture)

		var multipartBuilder = getMultipartBuilder(block)
			.part()
			.modelFile(post)
			.addModel()
			.end()

		for (direction in Direction.Plane.HORIZONTAL) {

			when (direction) {
				Direction.NORTH -> {
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
				}

				Direction.EAST -> {
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

				Direction.SOUTH -> {
					multipartBuilder = multipartBuilder
						.part()
						.modelFile(sideAlt)
						.addModel()
						.condition(CrossCollisionBlock.SOUTH, true)
						.end()

						.part()
						.modelFile(nosideAlt)
						.rotationY(90)
						.addModel()
						.condition(CrossCollisionBlock.SOUTH, false)
						.end()
				}

				Direction.WEST -> {
					multipartBuilder = multipartBuilder
						.part()
						.modelFile(sideAlt)
						.rotationY(90)
						.addModel()
						.condition(CrossCollisionBlock.WEST, true)
						.end()

						.part()
						.modelFile(noside)
						.rotationY(270)
						.addModel()
						.condition(CrossCollisionBlock.WEST, false)
						.end()
				}

				else -> {}
			}

		}

	}

	private fun singleTextureBlocks() {
		val blocks = listOf(
			ModBlocks.ANGEL_BLOCK.get(),
			ModBlocks.ENDER_CORE.get(),
			ModBlocks.BLOCK_OF_BEDROCKIUM.get(),
			ModBlocks.BEDROCK_BRICKS.get(),
			ModBlocks.BEDROCK_COBBLESTONE.get(),
			ModBlocks.CREATIVE_HARVEST.get(),
			ModBlocks.CREATIVE_ENERGY_SOURCE.get(),
			ModBlocks.DEEP_DARK_PORTAL.get(),
			ModBlocks.BLOCK_OF_DEMON_METAL.get(),
			ModBlocks.BLOCK_OF_ENCHANTED_METAL.get(),
			ModBlocks.POWER_OVERLOAD.get(),
			ModBlocks.SOUND_MUFFLER.get()
		)

		for (block in blocks) {
			simpleBlockWithItem(block, cubeAll(block))
		}
	}

	private fun athenaBlocks() {
		val blocks = listOf(
			ModBlocks.BLOCK_OF_EVIL_INFUSED_IRON.get(),
			ModBlocks.MAGICAL_PLANKS.get(),
			ModBlocks.QUARTZBURNT.get(),
			ModBlocks.STONEBURNT.get(),
			ModBlocks.BORDER_STONE.get(),
			ModBlocks.CROSSED_STONE.get(),
		)

		val translucent = listOf(
			ModBlocks.INEFFABLE_GLASS.get(),
			ModBlocks.DARK_INEFFABLE_GLASS.get(),
			ModBlocks.ETHEREAL_GLASS.get(),
			ModBlocks.INVERTED_ETHEREAL_GLASS.get()
		)

		for (block in blocks) {
			val path = BuiltInRegistries.BLOCK.getKey(block).path
			val texture = modLoc("block/$path/particle")

			val model = models()
				.cubeAll(name(block), texture)

			simpleBlockItem(block, model)
		}

		for (block in translucent) {
			val path = BuiltInRegistries.BLOCK.getKey(block).path
			val texture = modLoc("block/$path/particle")

			val model = models()
				.cubeAll(name(block), texture)
				.renderType(RenderType.translucent().name)

			simpleBlockItem(block, model)
		}
	}

	private fun name(block: Block): String {
		return BuiltInRegistries.BLOCK.getKey(block).path
	}

}