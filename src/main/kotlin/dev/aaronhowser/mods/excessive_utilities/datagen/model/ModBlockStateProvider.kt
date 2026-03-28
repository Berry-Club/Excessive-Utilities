package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.aaron.misc.AaronDsls.element
import dev.aaronhowser.mods.aaron.misc.AaronDsls.face
import dev.aaronhowser.mods.aaron.misc.AaronDsls.transform
import dev.aaronhowser.mods.aaron.misc.AaronDsls.transforms
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDyeName
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.particle
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.*
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.CrossCollisionBlock
import net.minecraft.world.level.block.RedstoneLampBlock
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
	output: PackOutput,
	private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun registerStatesAndModels() {
		singleTextureBlocks()
		singleTextureCutout()
		blackoutCurtain()
		athenaBlocks()
		slightlyLargerChest()
		miniChest()
		generators()
		machineBlock()
		enderQuarry()
		enderMarker()
		enderQuarryUpgrades()
		filingCabinets()
		moonStoneOre()
		resonator()
		quantumQuarryActuator()
		peacefulTable()
		cursedEarth()
		chandelier()
		magnumTorch()
		trashCans()
		trashChest()
		gpPanels()
		dragonEggMill()
		creativeMill()
		waterMill()
		windMill()
		manualMill()
		fireMill()
		lavaMill()
		creativeChest()
		conveyorBelt()
		tradingPost()
		coloredBlocks()
		lapisCaelesti()
		qed()
		enderFluxCrystal()
		furnace()
		magicalSnowGlobe()
		wirelessFeBattery()
		wirelessFeTransmitter()
		redstoneLantern()
		redstoneClock()
		spikes()
		drums()
		transferPipes()
		transferNodes()
		enderLily()
		redOrchid()
		enderPorcupine()
		crusher()
		enchanter()
		playerChest()
		terraformerBlocks()
		antenna()
		mechanicalBlocks()
		rainbowSlabs()
		compressedBlocks()
		indexer()
		enderCollector()
	}

	private fun enderCollector() {
		val block = ModBlocks.ENDER_COLLECTOR.get()
		val name = name(block)

		val antennaTopOff = modLoc("block/ender_collector/antenna_top_off")
		val antennaTopOn = modLoc("block/ender_collector/antenna_top_on")
		val baseTop = modLoc("block/ender_collector/base_top")
		val bottom = modLoc("block/ender_collector/bottom")
		val sideOff = modLoc("block/ender_collector/side_off")
		val sideOn = modLoc("block/ender_collector/side_on")

		val modelEnabled = models()
			.withExistingParent(name, mcLoc("block/block"))
			.texture("antenna_top", antennaTopOn)
			.texture("base_top", baseTop)
			.texture("side", sideOn)
			.texture("bottom", bottom)
			.particle(sideOn)

			.element {
				from(1f, 0f, 4f)
				to(15f, 2f, 12f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#base_top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)

					if (dir == Direction.DOWN) {
						fb.cullface(dir)
					}
				}
			}

			.element {
				from(4f, 0f, 1f)
				to(12f, 2f, 15f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#base_top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)

					if (dir == Direction.DOWN) {
						fb.cullface(dir)
					}
				}
			}

			.element {
				from(4f, 2f, 4f)
				to(12f, 4f, 12f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#base_top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			.element {
				from(5f, 4f, 5f)
				to(11f, 6f, 11f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#base_top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			.element {
				from(6f, 6f, 6f)
				to(10f, 16f, 10f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#base_top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			.element {
				from(6f, 10f, 1f)
				to(10f, 14f, 15f)

				allFaces { dir, fb ->
					if (dir.axis == Direction.Axis.Y) {
						fb.texture("#antenna_top")
					} else {
						fb.texture("#side")
					}
				}
			}

			.element {
				from(1f, 10f, 6f)
				to(15f, 14f, 10f)

				allFaces { dir, fb ->
					if (dir.axis == Direction.Axis.Y) {
						fb.texture("#antenna_top")
					} else {
						fb.texture("#side")
					}
				}
			}

		val modelDisabled = models()
			.withExistingParent(name + "_disabled", modLoc("block/$name"))
			.texture("antenna_top", antennaTopOff)
			.texture("side", sideOff)

		getVariantBuilder(block)
			.forAllStates {
				val enabled = it.getValue(EnderCollectorBlock.POWERED)
				val model = if (enabled) modelDisabled else modelEnabled

				val facing = it.getValue(EnderCollectorBlock.FACING)
				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val xRot = when (facing.axis) {
					Direction.Axis.Y -> 0
					Direction.Axis.X -> 90
					Direction.Axis.Z -> 90
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationX(xRot)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, modelEnabled)
	}

	private fun indexer() {
		val block = ModBlocks.INDEXER.get()

		val front = modLoc("block/indexer/front")
		val side = modLoc("block/indexer/side")

		val model = models()
			.cube(name(block), front, side, front, side, side, side)
			.particle(side)

		simpleBlockWithItem(block, model)
	}

	private fun compressedBlocks() {

		val types = mapOf(
			"cobblestone" to ModBlocks.COMPRESSED_COBBLESTONES,
			"cobbled_deepslate" to ModBlocks.COMPRESSED_COBBLED_DEEPSLATES,
			"dirt" to ModBlocks.COMPRESSED_DIRTS,
			"sand" to ModBlocks.COMPRESSED_SANDS,
			"gravel" to ModBlocks.COMPRESSED_GRAVELS,
		)

		for ((original, list) in types) {
			val originalBlockTexture = mcLoc("block/${original}")

			for ((i, b) in list.withIndex()) {
				val block = b.get()
				val name = name(block)

				val level = i + 1
				val overlay = modLoc("block/compressed/$level")

				val model = models()
					.withExistingParent(name, mcLoc("block/block"))
					.texture("texture", originalBlockTexture)
					.particle(originalBlockTexture)
					.texture("overlay", overlay)
					.renderType(RenderType.cutout().name)

					.element {
						from(0f, 0f, 0f)
						to(16f, 16f, 16f)

						allFaces { dir, fb ->
							fb.texture("#texture")
							fb.cullface(dir)
						}
					}

					.element {
						from(0f, 0f, 0f)
						to(16f, 16f, 16f)

						allFaces { dir, fb ->
							fb.texture("#overlay")
							fb.cullface(dir)
						}
					}

				simpleBlockWithItem(block, model)
			}
		}

	}

	private fun rainbowSlabs() {
		val center = modLoc("block/rainbow_generator_slab/center")
		val topSide = modLoc("block/rainbow_generator_slab/top")
		val bottomSide = modLoc("block/rainbow_generator_slab/bottom")
		val actual = modLoc("block/rainbow_generator")

		val topBlock = ModBlocks.RAINBOW_GENERATOR_TOP_SLAB.get()
		val bottomBlock = ModBlocks.RAINBOW_GENERATOR_BOTTOM_SLAB.get()

		val topModel = models()
			.withExistingParent(name(topBlock), mcLoc("block/block"))
			.texture("side", topSide)
			.texture("top", actual)
			.texture("bottom", center)
			.particle(actual)
			.element {
				from(0f, 8f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

		val bottomModel = models()
			.withExistingParent(name(bottomBlock), mcLoc("block/block"))
			.texture("top", center)
			.texture("side", bottomSide)
			.texture("bottom", topSide)
			.particle(actual)
			.element {
				from(0f, 0f, 0f)
				to(16f, 8f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#center"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

		simpleBlockWithItem(topBlock, topModel)
		simpleBlockWithItem(bottomBlock, bottomModel)
	}

	private fun mechanicalBlocks() {
		mechanicalBlock(ModBlocks.MECHANICAL_USER.get(), "user")
		mechanicalBlock(ModBlocks.MECHANICAL_MINER.get(), "miner")
		mechanicalBlock(ModBlocks.SCANNER.get(), "scanner")
	}

	//TODO: Orientation
	private fun mechanicalBlock(block: Block, textureName: String) {
		val side = modLoc("block/mechanical/side")
		val back = modLoc("block/mechanical/back")
		val texture = modLoc("block/mechanical/$textureName")

		val model = models()
			.cubeBottomTop(name(block), side, back, texture)

		simpleBlockWithItem(block, model)
	}

	private fun antenna() {
		val block = ModBlocks.ANTENNA.get()

		val base = modLoc("block/terraformer/antenna/base")
		val side = modLoc("block/terraformer/antenna/side")
		val top = modLoc("block/terraformer/antenna/top")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("base", base)
			.texture("side", side)
			.texture("top", top)
			.particle(side)
			.renderType(RenderType.cutout().name)

			// Base
			.element {
				from(2f, 0f, 2f)
				to(14f, 1f, 14f)

				allFaces { dir, fb ->
					texture("#base")
				}
			}

			// Pole
			.element {
				from(7f, 1f, 7f)
				to(9f, 6f, 9f)

				allFaces { dir, fb ->
					texture("#side")
				}
			}

			// Arm
			.element {
				from(4f, 6f, 6f)
				to(12f, 10f, 10f)

				allFaces { dir, fb ->
					texture("#top")
				}
			}

			// Arm
			.element {
				from(6f, 6f, 4f)
				to(10f, 10f, 12f)

				allFaces { dir, fb ->
					texture("#top")
				}

				allFaces { dir, fb ->
					val texture = when (dir.axis) {
						Direction.Axis.Y -> "#top"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			// North prong
			.element {
				from(0f, 5f, 6f)
				to(4f, 16f, 10f)

				allFaces { dir, fb ->
					val texture = when (dir.axis) {
						Direction.Axis.Y -> "#top"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			// South prong
			.element {
				from(12f, 5f, 6f)
				to(16f, 16f, 10f)

				allFaces { dir, fb ->
					val texture = when (dir.axis) {
						Direction.Axis.Y -> "#top"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			// West prong
			.element {
				from(6f, 5f, 0f)
				to(10f, 16f, 4f)

				allFaces { dir, fb ->
					val texture = when (dir.axis) {
						Direction.Axis.Y -> "#top"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

			// East prong
			.element {
				from(6f, 5f, 12f)
				to(10f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir.axis) {
						Direction.Axis.Y -> "#top"
						else -> "#side"
					}

					fb.texture(texture)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun terraformerBlocks() {
		terraformerBlock(ModBlocks.TERRAFORMER.get(), "controller")
		terraformerBlock(ModBlocks.COOLER.get(), "cooler")
		terraformerBlock(ModBlocks.DEHOSTILIFIER.get(), "dehostilifier")
		terraformerBlock(ModBlocks.DEHUMIDIFIER.get(), "dehumidifier")
		terraformerBlock(ModBlocks.HEATER.get(), "heater")
		terraformerBlock(ModBlocks.HUMIDIFIER.get(), "humidifier")
		terraformerBlock(ModBlocks.MAGIC_ABSORPTION.get(), "magic_absorption")
		terraformerBlock(ModBlocks.MAGIC_INFUSER.get(), "magic_infuser")
		terraformerBlock(ModBlocks.CLIMOGRAPH_BASE_UNIT.get(), "base")
	}

	private fun terraformerBlock(
		block: Block,
		sideName: String
	) {
		val bottom = modLoc("block/terraformer/top")
		val side = modLoc("block/terraformer/$sideName")
		val top = modLoc("block/terraformer/top")

		val model = models()
			.cubeBottomTop(name(block), side, bottom, top)

		simpleBlockWithItem(block, model)
	}

	private fun enchanter() {
		val block = ModBlocks.ENCHANTER.get()

		val name = name(block)

		val model = models()
			.withExistingParent(name + "_off", mcLoc("block/block"))
			.texture("top_overlay", modLoc("block/enchanter/top"))
			.texture("top", modLoc("block/machine_block/top"))
			.texture("side", modLoc("block/enchanter/side"))
			.texture("front", modLoc("block/machine_block/side"))
			.texture("bottom", modLoc("block/machine_block/bottom"))
			.texture("overlay", modLoc("block/enchanter/face_off"))
			.particle(modLoc("block/enchanter/top"))
			.renderType(RenderType.cutout().name)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						Direction.NORTH -> "#front"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				face(Direction.NORTH) {
					texture("#overlay")
					cullface(Direction.NORTH)
				}

				face(Direction.UP) {
					texture("#top_overlay")
					cullface(Direction.UP)
				}
			}

		val modelOn = models()
			.withExistingParent(name + "_on", modLoc("block/$name" + "_off"))
			.texture("overlay", modLoc("block/enchanter/face_on"))

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(EnchanterBlock.FACING)
				val enabled = it.getValue(EnchanterBlock.ENABLED)

				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val model = if (enabled) modelOn else model

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, model)
	}

	private fun crusher() {
		val block = ModBlocks.CRUSHER.get()

		val name = name(block)

		val modelOff = models()
			.withExistingParent(name + "_off", mcLoc("block/block"))
			.texture("side", modLoc("block/machine_block/side"))
			.texture("top", modLoc("block/machine_block/top"))
			.texture("bottom", modLoc("block/machine_block/bottom"))
			.texture("overlay", modLoc("block/crusher/off"))
			.particle(modLoc("block/machine_block/side"))
			.renderType(RenderType.cutout().name)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 0f)

				face(Direction.NORTH) {
					texture("#overlay")
					cullface(Direction.NORTH)
				}
			}

		val modelOn = models()
			.withExistingParent(name + "_on", modLoc("block/$name" + "_off"))
			.texture("overlay", modLoc("block/crusher/on"))

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(CrusherBlock.FACING)
				val enabled = it.getValue(CrusherBlock.ENABLED)

				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val model = if (enabled) modelOn else modelOff

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, modelOff)
	}

	private fun enderPorcupine() {
		val block = ModBlocks.ENDER_PORCUPINE.get()

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("texture", modLoc("block/ender_porcupine"))
			.particle(modLoc("block/ender_porcupine"))
			.renderType(RenderType.cutout().name)

			.element {
				from(2f, 0f, 0f)
				to(2f, 16f, 16f)

				face(Direction.EAST) {
					texture("#texture")
				}

				face(Direction.WEST) {
					texture("#texture")
				}
			}

			.element {
				from(14f, 0f, 0f)
				to(14f, 16f, 16f)

				face(Direction.EAST) {
					texture("#texture")
				}

				face(Direction.WEST) {
					texture("#texture")
				}
			}

			.element {
				from(0f, 0f, 2f)
				to(16f, 16f, 2f)

				face(Direction.SOUTH) {
					texture("#texture")
				}

				face(Direction.NORTH) {
					texture("#texture")
				}
			}

			.element {
				from(0f, 0f, 14f)
				to(16f, 16f, 14f)

				face(Direction.SOUTH) {
					texture("#texture")
				}

				face(Direction.NORTH) {
					texture("#texture")
				}
			}

			.element {
				from(0f, 2f, 0f)
				to(16f, 2f, 16f)

				face(Direction.UP) {
					texture("#texture")
				}

				face(Direction.DOWN) {
					texture("#texture")
				}
			}

			.element {
				from(0f, 14f, 0f)
				to(16f, 14f, 16f)

				face(Direction.UP) {
					texture("#texture")
				}

				face(Direction.DOWN) {
					texture("#texture")
				}
			}

		simpleBlockWithItem(block, model)
	}

	//TODO: Add the base also
	private fun redOrchid() {
		val block = ModBlocks.RED_ORCHID.get()

		getVariantBuilder(block)
			.forAllStates {
				val age = it.getValue(CropBlock.AGE)
				val nameWithAge = name(block) + "_$age"

				val model = models()
					.cross(nameWithAge, modLoc("block/red_orchid/$age"))
					.renderType(RenderType.cutout().name)

				ConfiguredModel
					.builder()
					.modelFile(model)
					.build()
			}
	}

	private fun enderLily() {
		val block = ModBlocks.ENDER_LILY.get()

		getVariantBuilder(block)
			.forAllStates {
				val age = it.getValue(CropBlock.AGE)
				val nameWithAge = name(block) + "_$age"

				val model = models()
					.cross(nameWithAge, modLoc("block/ender_lily/$age"))
					.renderType(RenderType.cutout().name)

				ConfiguredModel
					.builder()
					.modelFile(model)
					.build()
			}

	}

	//TODO: Make this not nullable, make textures
	private fun transferPipe(
		block: TransferPipeBlock,
		textureName: String,
		blockedTextureName: String? = null
	) {
		val texture = modLoc(textureName)

		val coreModel = models()
			.withExistingParent(name(block) + "_core", mcLoc("block/block"))
			.texture("pipe", texture)
			.particle(texture)
			.renderType(RenderType.cutout().name)

			.element {
				from(6f, 6f, 6f)
				to(10f, 10f, 10f)

				allFaces { dir, fb ->
					fb.texture("#pipe")
					fb.uvs(6f, 6f, 10f, 10f)
				}
			}

		// Aiming north
		val armModel = models()
			.withExistingParent(name(block) + "_arm", mcLoc("block/block"))
			.texture("pipe", texture)
			.particle(texture)

			.element {
				from(6f, 6f, 0f)
				to(10f, 10f, 6f)

				allFaces { dir, fb ->
					fb.texture("#pipe")

					val uvs = when (dir) {
						Direction.NORTH -> arrayOf(6f, 6f, 10f, 10f)
						Direction.EAST -> arrayOf(0f, 6f, 6f, 10f)
						Direction.SOUTH -> arrayOf(6f, 6f, 10f, 10f)
						Direction.WEST -> arrayOf(0f, 6f, 6f, 10f)
						Direction.UP -> arrayOf(6f, 0f, 10f, 6f)
						Direction.DOWN -> arrayOf(6f, 0f, 10f, 6f)
					}

					fb.uvs(uvs[0], uvs[1], uvs[2], uvs[3])
				}
			}

		val builder = getMultipartBuilder(block)
			.part()
			.modelFile(coreModel)
			.addModel()
			.end()

		fun addArms(connectionType: TransferPipeBlock.ConnectionType, model: BlockModelBuilder) {
			builder
				.part()
				.modelFile(model)
				.addModel()
				.condition(TransferPipeBlock.NORTH, connectionType)
				.end()

				.part()
				.modelFile(model)
				.rotationY(90)
				.addModel()
				.condition(TransferPipeBlock.EAST, connectionType)
				.end()

				.part()
				.modelFile(model)
				.rotationY(180)
				.addModel()
				.condition(TransferPipeBlock.SOUTH, connectionType)
				.end()

				.part()
				.modelFile(model)
				.rotationY(270)
				.addModel()
				.condition(TransferPipeBlock.WEST, connectionType)
				.end()

				.part()
				.modelFile(model)
				.rotationX(270)
				.addModel()
				.condition(TransferPipeBlock.UP, connectionType)
				.end()

				.part()
				.modelFile(model)
				.rotationX(90)
				.addModel()
				.condition(TransferPipeBlock.DOWN, connectionType)
				.end()
		}

		addArms(TransferPipeBlock.ConnectionType.CONNECTED, armModel)

		if (blockedTextureName != null) {
			val blockedTexture = modLoc(blockedTextureName)

			val armBlockedModel = models()
				.withExistingParent(name(block) + "_arm_blocked", modLoc("block/" + name(block) + "_arm"))
				.texture("pipe", blockedTexture)

			addArms(TransferPipeBlock.ConnectionType.BLOCKED, armBlockedModel)
		}

		val itemModel = itemModels()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("pipe", texture)
			.renderType(RenderType.cutout().name)

			.transforms {

				transform(ItemDisplayContext.GUI) {
					rotation(30f, 225f, 0f)
					translation(0f, 0f, 0f)
					scale(0.625f, 0.625f, 0.625f)
				}

				transform(ItemDisplayContext.GROUND) {
					translation(0f, 3f, 0f)
					scale(0.25f, 0.25f, 0.25f)
				}

				transform(ItemDisplayContext.FIXED) {
					rotation(0f, 180f, 0f)
					scale(0.5f, 0.5f, 0.5f)
				}

				transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
					rotation(75f, 45f, 0f)
					translation(0f, 2.5f, 0f)
					scale(0.375f, 0.375f, 0.375f)
				}

				transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
					rotation(75f, 45f, 0f)
					translation(0f, 2.5f, 0f)
					scale(0.375f, 0.375f, 0.375f)
				}

				transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
					rotation(0f, 45f, 0f)
					scale(0.4f, 0.4f, 0.4f)
				}

				transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
					rotation(0f, 225f, 0f)
					scale(0.4f, 0.4f, 0.4f)
				}

			}

			// Vertical
			.element {
				from(6f, 0f, 6f)
				to(10f, 16f, 10f)

				allFaces { dir, fb ->
					fb.texture("#pipe")
					fb.uvs(6f, 0f, 10f, 16f)
				}
			}

			.element {
				from(0f, 6f, 6f)
				to(16f, 10f, 10f)

				allFaces { dir, fb ->
					fb.texture("#pipe")
					fb.uvs(0f, 6f, 16f, 10f)
				}
			}

			.element {
				from(6f, 6f, 0f)
				to(10f, 10f, 16f)

				allFaces { dir, fb ->
					fb.texture("#pipe")
					fb.uvs(6f, 6f, 10f, 16f)
				}
			}

		simpleBlockItem(block, itemModel)
	}

	private fun transferPipes() {
		transferPipe(
			ModBlocks.TRANSFER_PIPE.get(),
			"block/transfer_pipe/pipe",
			"block/transfer_pipe/blocked"
		)

//		transferPipe(
//			ModBlocks.RATIONING_PIPE.get(),
//			"block/transfer_pipe/rationing",
//			null
//		)
//
//		transferPipe(
//			ModBlocks.HYPER_RATIONING_PIPE.get(),
//			"block/transfer_pipe/hyper_rationing",
//			null
//		)
	}

	private fun transferNodes() {
		val templateName = "transfer_node_template"

		val armModel = models()
			.getExistingFile(modLoc("block/transfer_pipe_arm"))

		models()
			.withExistingParent(templateName, mcLoc("block/block"))
			.texture("particle", "#node")

			.element {
				from(6f, 6f, 6f)
				to(10f, 10f, 10f)

				allFaces { _, fb ->
					fb.texture("#pipe")
					fb.uvs(6f, 6f, 10f, 10f)
				}
			}

			.element {
				from(6f, 2f, 6f)
				to(10f, 6f, 10f)

				allFaces { dir, fb ->
					val uvs = when (dir) {
						Direction.DOWN -> arrayOf(6f, 6f, 10f, 10f)
						Direction.EAST -> arrayOf(2f, 6f, 6f, 10f)
						Direction.UP -> arrayOf(6f, 6f, 10f, 10f)
						Direction.WEST -> arrayOf(2f, 6f, 6f, 10f)
						Direction.NORTH -> arrayOf(6f, 2f, 10f, 6f)
						Direction.SOUTH -> arrayOf(6f, 2f, 10f, 6f)
					}

					fb.texture("#pipe")
					fb.uvs(uvs[0], uvs[1], uvs[2], uvs[3])
				}
			}

			.element {
				from(1f, 0f, 1f)
				to(15f, 1f, 15f)

				allFaces { dir, fb ->
					when (dir) {
						Direction.DOWN -> arrayOf(15f, 1f, 1f, 15f)
						Direction.EAST -> arrayOf(14f, 1f, 15f, 15f)
						Direction.UP -> arrayOf(1f, 1f, 15f, 15f)
						Direction.WEST -> arrayOf(1f, 1f, 2f, 15f)
						Direction.NORTH -> arrayOf(1f, 1f, 15f, 2f)
						Direction.SOUTH -> arrayOf(1f, 14f, 15f, 15f)
					}

					val texture = if (dir == Direction.DOWN) "#back" else "#node"
					fb.texture(texture)
				}
			}

			.element {
				from(3f, 1f, 3f)
				to(13f, 3f, 13f)

				allFacesExcept(
					{ dir, fb ->
						val uvs = when (dir) {
							Direction.EAST -> arrayOf(1f, 3f, 3f, 13f)
							Direction.UP -> arrayOf(3f, 3f, 13f, 13f)
							Direction.WEST -> arrayOf(13f, 3f, 15f, 13f)
							Direction.NORTH -> arrayOf(3f, 13f, 13f, 15f)
							Direction.SOUTH -> arrayOf(3f, 1f, 13f, 3f)
							else -> return@allFacesExcept
						}

						fb.texture("#node")
						fb.uvs(uvs[0], uvs[1], uvs[2], uvs[3])
					},
					setOf(Direction.DOWN)
				)
			}

		fun specific(block: TransferNodeBlock, texture: String) {
			val nodeTexture = modLoc(texture)

			val name = name(block)

			val model = models()
				.withExistingParent(name, modLoc("block/$templateName"))
				.texture("pipe", modLoc("block/transfer_pipe/pipe"))
				.texture("node", nodeTexture)
				.texture("back", modLoc("block/transfer_node/back"))
				.particle(nodeTexture)

			val builder = getMultipartBuilder(block)

			builder.part()
				.modelFile(model)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.DOWN)
				.end()

				.part()
				.modelFile(model)
				.rotationX(180)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.UP)
				.end()

				.part()
				.modelFile(model)
				.rotationY(180)
				.rotationX(90)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.NORTH)
				.end()

				.part()
				.modelFile(model)
				.rotationX(90)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.SOUTH)
				.end()

				.part()
				.modelFile(model)
				.rotationX(90)
				.rotationY(270)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.EAST)
				.end()

				.part()
				.modelFile(model)
				.rotationX(90)
				.rotationY(90)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.WEST)
				.end()

				.part()
				.modelFile(armModel)
				.addModel()
				.condition(TransferNodeBlock.NORTH, true)
				.end()

				.part()
				.modelFile(armModel)
				.rotationY(90)
				.addModel()
				.condition(TransferNodeBlock.EAST, true)
				.end()

				.part()
				.modelFile(armModel)
				.rotationY(180)
				.addModel()
				.condition(TransferNodeBlock.SOUTH, true)
				.end()

				.part()
				.modelFile(armModel)
				.rotationY(270)
				.addModel()
				.condition(TransferNodeBlock.WEST, true)
				.end()

				.part()
				.modelFile(armModel)
				.rotationX(270)
				.addModel()
				.condition(TransferNodeBlock.UP, true)
				.end()

				.part()
				.modelFile(armModel)
				.rotationX(90)
				.addModel()
				.condition(TransferNodeBlock.DOWN, true)
				.end()

			itemModels()
				.withExistingParent(name, modLoc("block/$name"))
		}

		specific(ModBlocks.ITEM_TRANSFER_NODE.get(), "block/transfer_node/item")
		specific(ModBlocks.FLUID_TRANSFER_NODE.get(), "block/transfer_node/fluid")
		specific(ModBlocks.ENERGY_TRANSFER_NODE.get(), "block/transfer_node/energy")
		specific(ModBlocks.ITEM_RETRIEVAL_NODE.get(), "block/transfer_node/retrieval/item")
		specific(ModBlocks.FLUID_RETRIEVAL_NODE.get(), "block/transfer_node/retrieval/fluid")
		specific(ModBlocks.ENERGY_RETRIEVAL_NODE.get(), "block/transfer_node/retrieval/energy")

	}

	private fun drums() {
		val drums = listOf(
			ModBlocks.STONE_DRUM.get(),
			ModBlocks.IRON_DRUM.get(),
			ModBlocks.REINFORCED_LARGE_DRUM.get(),
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(),
			ModBlocks.BEDROCKIUM_DRUM.get(),
			ModBlocks.CREATIVE_DRUM.get()
		)

		for (drum in drums) {
			val name = name(drum)
			val blockModel = models().getExistingFile(modLoc("block/$name"))
			simpleBlock(drum, blockModel)

			itemModels()
				.withExistingParent(name, modLoc("block/$name"))
				.transforms {
					transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
						rotation(10f, -45f, 170f)
						translation(0f, 1.5f, -2.75f)
						scale(0.375f, 0.375f, 0.375f)
					}

					transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
						rotation(10f, 45f, -170f)
						translation(0f, 1.5f, -2.75f)
						scale(0.375f, 0.375f, 0.375f)
					}

					transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
						rotation(0f, -90f, 25f)
						translation(1.13f, 3.2f, 1.13f)
						scale(0.5f, 0.5f, 0.5f)
					}

					transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
						rotation(0f, 90f, -25f)
						translation(1.13f, 3.2f, 1.13f)
						scale(0.5f, 0.5f, 0.5f)
					}

					transform(ItemDisplayContext.GUI) {
						rotation(30f, 225f, 0f)
						translation(0f, 0f, 0f)
						scale(0.625f, 0.625f, 0.625f)
					}

					transform(ItemDisplayContext.GROUND) {
						translation(0f, 3f, 0f)
						scale(0.25f, 0.25f, 0.25f)
					}

					transform(ItemDisplayContext.FIXED) {
						rotation(0f, 180f, 0f)
						translation(0f, 0f, 0f)
						scale(0.5f, 0.5f, 0.5f)
					}
				}
		}
	}

	private fun spikes() {
		val spikes = listOf(
			ModBlocks.WOODEN_SPIKE.get(),
			ModBlocks.STONE_SPIKE.get(),
			ModBlocks.IRON_SPIKE.get(),
			ModBlocks.GOLDEN_SPIKE.get(),
			ModBlocks.DIAMOND_SPIKE.get(),
			ModBlocks.NETHERITE_SPIKE.get(),
			ModBlocks.CREATIVE_SPIKE.get()
		)

		for (spike in spikes) {
			val name = name(spike)
			val model = models().getExistingFile(modLoc("block/$name"))
			simpleBlock(spike, model)

			itemModels()
				.withExistingParent(name, modLoc("block/$name"))
				.transforms {
					transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
						rotation(10f, -45f, 170f)
						translation(0f, 1.5f, -2.75f)
						scale(0.375f, 0.375f, 0.375f)
					}

					transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
						rotation(10f, 45f, -170f)
						translation(0f, 1.5f, -2.75f)
						scale(0.375f, 0.375f, 0.375f)
					}

					transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
						rotation(0f, -90f, 25f)
						translation(1.13f, 3.2f, 1.13f)
						scale(0.5f, 0.5f, 0.5f)
					}

					transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
						rotation(0f, 90f, -25f)
						translation(1.13f, 3.2f, 1.13f)
						scale(0.5f, 0.5f, 0.5f)
					}

					transform(ItemDisplayContext.GUI) {
						rotation(30f, 225f, 0f)
						translation(0f, 0f, 0f)
						scale(0.625f, 0.625f, 0.625f)
					}

					transform(ItemDisplayContext.GROUND) {
						translation(0f, 3f, 0f)
						scale(0.25f, 0.25f, 0.25f)
					}

					transform(ItemDisplayContext.FIXED) {
						rotation(0f, 180f, 0f)
						translation(0f, 0f, 0f)
						scale(0.5f, 0.5f, 0.5f)
					}
				}

		}
	}

	private fun redstoneClock() {
		val block = ModBlocks.REDSTONE_CLOCK.get()

		val on = modLoc("block/redstone_clock/on")
		val off = modLoc("block/redstone_clock/off")

		val modelOn = models()
			.cubeAll(name(block), on)

		val modelOff = models()
			.cubeAll(name(block) + "_off", off)

		getVariantBuilder(block)
			.forAllStates {
				val powered = it.getValue(RedstoneClockBlock.POWERED)
				val model = if (powered) modelOn else modelOff

				ConfiguredModel
					.builder()
					.modelFile(model)
					.build()
			}

		simpleBlockItem(block, modelOn)
	}

	private fun redstoneLantern() {
		val block = ModBlocks.REDSTONE_LANTERN.get()

		val base = modLoc("block/redstone_lantern/base")
		val numbers = modLoc("block/redstone_lantern/numbers")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("base", base)
			.texture("numbers", numbers)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					fb.texture("#base")
					fb.cullface(dir)
					fb.uvs(1f, 1f, 15f, 15f)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun wirelessFeTransmitter() {
		val block = ModBlocks.WIRELESS_FE_TRANSMITTER.get()

		val top = modLoc("block/wireless_fe_transmitter/top")
		val side = modLoc("block/wireless_fe_transmitter/side")
		val bottom = modLoc("block/wireless_fe_transmitter/bottom")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("side", side)
			.texture("bottom", bottom)
			.particle(side)

			.element {
				from(4f, 0f, 4f)
				to(12f, 2f, 12f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

		fun dangler(leftX: Float, topY: Float) {
			model
				.element {
					from(leftX, 0f, topY)
					to(leftX + 2f, 1f, topY + 2f)

					allFaces { dir, fb ->
						val texture = when (dir) {
							Direction.UP -> "#top"
							Direction.DOWN -> "#bottom"
							else -> "#side"
						}

						fb.texture(texture)
						fb.cullface(dir)
					}
				}
		}

		dangler(7f, 2f)
		dangler(2f, 7f)
		dangler(12f, 7f)
		dangler(7f, 12f)

		simpleBlockWithItem(block, model)
	}

	private fun wirelessFeBattery() {
		val block = ModBlocks.WIRELESS_FE_BATTERY.get()

		val top = modLoc("block/wireless_fe_battery/top")
		val side = modLoc("block/wireless_fe_battery/side")

		val model = models()
			.cubeTop(name(block), side, top)
			.particle(side)

		simpleBlockWithItem(block, model)
	}

	private fun magicalSnowGlobe() {
		val block = ModBlocks.MAGICAL_SNOW_GLOBE.get()

		val texture = modLoc("block/magical_snow_globe")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("texture", texture)
			.particle(texture)
			.renderType(RenderType.translucent().name)

			.element {
				from(4f, 0f, 4f)
				to(12f, 8f, 12f)

				allFaces { dir, fb ->
					fb.texture("#texture")

					when (dir) {
						Direction.UP -> fb.uvs(0f, 0f, 8f, 8f)
						Direction.DOWN -> fb.uvs(8f, 0f, 16f, 8f)
						else -> fb.uvs(0f, 8f, 8f, 16f)
					}
				}
			}

			.element {
				from(4f, 0.1f, 4f)
				to(12f, 0.1f, 12f)

				face(Direction.UP) {
					texture("#texture")
					uvs(8f, 0f, 16f, 8f)
				}
			}

		simpleBlockWithItem(block, model)
	}

	//TODO: On
	private fun enderFluxCrystal() {
		val block = ModBlocks.ENDER_FLUX_CRYSTAL.get()

		val top = modLoc("block/ender_flux_crystal/top")
		val side = modLoc("block/ender_flux_crystal/side")
		val bottom = modLoc("block/ender_flux_crystal/bottom")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("side", side)
			.texture("bottom", bottom)
			.particle(side)

			.element {
				from(1f, 0f, 1f)
				to(15f, 7f, 15f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			.element {
				from(4f, 7f, 4f)
				to(12f, 15f, 12f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(EnderFluxCrystalBlock.FACING)

				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val xRot = when (facing) {
					Direction.UP -> 0
					Direction.DOWN -> 180
					else -> 90
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationX(xRot)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, model)
	}

	//TODO: On
	private fun qed() {
		val block = ModBlocks.QED.get()

		val top = modLoc("block/qed/top")
		val side = modLoc("block/qed/side")
		val bottom = modLoc("block/ender_infused_obsidian/particle")

		val model = models()
			.cubeBottomTop(name(block), side, bottom, top)

		simpleBlockWithItem(block, model)
	}

	private fun lapisCaelesti() {
		models()
			.withExistingParent("lapis_caelestis_template", mcLoc("block/block"))
			.texture("all", modLoc("block/lapis_caelestis"))
			.particle(modLoc("block/lapis_caelestis"))

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)
				shade(false)

				allFaces { dir, fb ->
					fb.texture("#all")
					fb.cullface(dir)
					fb.tintindex(0)
					fb.emissivity(15, 15)
				}
			}

		for (dyeColor in DyeColor.entries) {
			val block = ModBlocks.getLapisCaelestis(dyeColor).get()

			val model = models()
				.withExistingParent(name(block), modLoc("block/lapis_caelestis_template"))

			simpleBlockWithItem(block, model)
		}
	}

	private fun coloredBlocks() {
		val map = mapOf(
			"stone" to ModBlocks::getColoredStone,
			"cobblestone" to ModBlocks::getColoredCobblestone,
			"stone_bricks" to ModBlocks::getColoredStoneBricks,
			"bricks" to ModBlocks::getColoredBricks,
			"planks" to ModBlocks::getColoredPlanks,
			"coal_block" to ModBlocks::getColoredCoalBlock,
			"redstone_block" to ModBlocks::getColoredRedstoneBlock,
			"lapis_block" to ModBlocks::getColoredLapisBlock,
			"quartz" to ModBlocks::getColoredQuartz,
			"obsidian" to ModBlocks::getColoredObsidian,
			"soul_sand" to ModBlocks::getColoredSoulSand,
			"glowstone" to ModBlocks::getColoredGlowstone,
		)

		for ((name, getter) in map) {
			for (color in DyeColor.entries) {
				val colorName = color.getDyeName()
				val texture = modLoc("block/colored/$colorName/$name")

				val block = getter(color).get()
				val model = models()
					.cubeAll(name(block), texture)

				simpleBlockWithItem(block, model)
			}
		}

		for (color in DyeColor.entries) {
			val block = ModBlocks.getColoredRedstoneLamp(color).get()
			val name = name(block)
			val dyeName = color.getDyeName()

			val textureOff = modLoc("block/colored/$dyeName/redstone_lamp")
			val textureOn = modLoc("block/colored/$dyeName/redstone_lamp_on")

			val modelOff = models()
				.cubeAll(name + "_off", textureOff)

			val modelOn = models()
				.cubeAll(name, textureOn)

			getVariantBuilder(block)
				.forAllStates {
					val lit = it.getValue(RedstoneLampBlock.LIT)
					val model = if (lit) modelOn else modelOff

					ConfiguredModel
						.builder()
						.modelFile(model)
						.build()
				}

			simpleBlockItem(block, modelOff)
		}
	}

	private fun tradingPost() {
		val block = ModBlocks.TRADING_POST.get()

		val side = modLoc("block/trading_post/side")
		val top = modLoc("block/trading_post/top")
		val bottom = mcLoc("block/oak_planks")

		val model = models()
			.cubeBottomTop(name(block), side, bottom, top)

		simpleBlockWithItem(block, model)
	}

	private fun conveyorBelt() {
		val block = ModBlocks.CONVEYOR_BELT.get()

		val side = modLoc("block/conveyor_belt/side")
		val top = modLoc("block/conveyor_belt/top")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", side)
			.texture("top", top)
			.particle(top)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.EAST, Direction.WEST -> "#side"
						else -> "#top"
					}

					if (dir == Direction.WEST) {
						fb.rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN)
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(ConveyorBeltBlock.FACING)

				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, model)
	}

	private fun creativeMill() {
		val block = ModBlocks.CREATIVE_MILL.get()

		val side = modLoc("block/creative_mill/side")
		val top = modLoc("block/creative_mill/top")

		val model = models()
			.cubeTop(name(block), side, top)

		simpleBlockWithItem(block, model)
	}

	private fun manualMill() {
		val block = ModBlocks.MANUAL_MILL.get()

		val side = modLoc("block/mill/manual/side")
		val top = modLoc("block/mill/manual/top")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", side)
			.texture("top", top)
			.particle(side)

			.element {
				from(0f, 0f, 0f)
				to(16f, 6f, 16f)

				allFaces { dir, fb ->
					val texture = if (dir.axis == Direction.Axis.Y) "#top" else "#side"

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun windMill() {
		val block = ModBlocks.WIND_MILL.get()

		val top = modLoc("block/mill/wind")
		val base = modLoc("block/mill/base")
		val fan = modLoc("block/mill/fan_spinning")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("base", base)
			.texture("fan", fan)
			.particle(top)
			.renderType(RenderType.cutout().name)

			// Top
			.element {
				from(0f, 15f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.DOWN -> "#base"
						else -> "#top"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// Bottom
			.element {
				from(0f, 0f, 0f)
				to(16f, 1f, 16f)

				allFaces { dir, fb ->
					fb.texture("#base")
					fb.cullface(dir)
				}
			}

			// West
			.element {
				from(0f, 1f, 0f)
				to(1f, 15f, 16f)

				allFaces { dir, fb ->
					fb.texture("#base")
					fb.cullface(dir)
				}
			}

			// East
			.element {
				from(15f, 1f, 0f)
				to(16f, 15f, 16f)

				allFaces { dir, fb ->
					fb.texture("#base")
					fb.cullface(dir)
				}
			}

			// Fan
			.element {
				from(1f, 1f, 7f)
				to(15f, 15f, 7.1f)

				face(Direction.NORTH) {
					texture("#fan")
					cullface(Direction.NORTH)
				}

				face(Direction.SOUTH) {
					texture("#fan")
					cullface(Direction.SOUTH)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun waterMill() {
		val block = ModBlocks.WATER_MILL.get()

		val top = modLoc("block/mill/water")
		val fan = modLoc("block/mill/fan_spinning_small")
		val base = modLoc("block/mill/base")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("base", base)
			.texture("fan", fan)
			.particle(top)
			.renderType(RenderType.cutout().name)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						else -> "#base"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				for (face in Direction.Plane.HORIZONTAL) {
					face(face) {
						texture("#fan")
						cullface(face)
					}
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun lavaMill() {
		val block = ModBlocks.LAVA_MILL.get()

		val top = modLoc("block/mill/lava")
		val base = modLoc("block/mill/base")

		val model = models()
			.cubeBottomTop(name(block), base, base, top)

		simpleBlockWithItem(block, model)
	}

	private fun fireMill() {
		val block = ModBlocks.FIRE_MILL.get()

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", modLoc("block/mill/fire"))
			.texture("base", modLoc("block/mill/base"))
			.texture("fan", modLoc("block/mill/fan_spinning"))
			.particle(modLoc("block/mill/fire"))
			.renderType(RenderType.cutout().name)

			// North
			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 1f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.SOUTH -> "#base"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// South
			.element {
				from(0f, 0f, 15f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.NORTH -> "#base"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// East
			.element {
				from(15f, 0f, 0f)
				to(16f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.WEST -> "#base"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// West
			.element {
				from(0f, 0f, 0f)
				to(1f, 16f, 16f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.EAST -> "#base"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// Fan
			.element {
				from(1f, 7f, 1f)
				to(15f, 7f, 15f)

				face(Direction.UP) {
					texture("#fan")
					cullface(Direction.UP)
				}

				face(Direction.DOWN) {
					texture("#fan")
					cullface(Direction.DOWN)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun dragonEggMill() {
		val block = ModBlocks.DRAGON_EGG_MILL.get()

		val side = modLoc("block/mill/dragon_egg/side")
		val top = modLoc("block/mill/dragon_egg/top")
		val bottom = modLoc("block/mill/base")

		val model = models()
			.cubeBottomTop(name(block), side, bottom, top)

		simpleBlockWithItem(block, model)
	}

	private fun gpPanels() {
		val blocks = mapOf(
			"lunar" to ModBlocks.LUNAR_PANEL.get(),
			"solar" to ModBlocks.SOLAR_PANEL.get()
		)

		val side = modLoc("block/mill/panel/side")
		val base = modLoc("block/mill/base")

		for ((type, block) in blocks) {
			val top = modLoc("block/mill/panel/$type")

			val model = models()
				.withExistingParent(name(block), mcLoc("block/block"))
				.texture("side", side)
				.texture("top", top)
				.texture("base", base)
				.particle(side)

				.element {
					from(0f, 0f, 0f)
					to(16f, 4f, 16f)

					allFaces { dir, fb ->
						val texture = when (dir) {
							Direction.UP -> "#top"
							Direction.DOWN -> "#base"
							else -> "#side"
						}

						fb.texture(texture)
						fb.cullface(dir)
					}
				}

			simpleBlockWithItem(block, model)
		}
	}

	// TODO: Rotation
	private fun playerChest() {
		val block = ModBlocks.PLAYER_CHEST.get()

		val side = modLoc("block/player_chest/side")
		val front = modLoc("block/player_chest/front")
		val top = modLoc("block/player_chest/top")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", side)
			.texture("front", front)
			.texture("top", top)
			.particle(side)

			// Bottom half
			.element {
				from(1f, 0f, 1f)
				to(15f, 14f, 15f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP, Direction.DOWN -> "#top"
						Direction.NORTH -> "#front"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// Latch
			.element {
				from(6f, 6f, 0f)
				to(10f, 11f, 1f)

				allFacesExcept(
					{ dir, fb ->
						val texture = when (dir) {
							Direction.UP -> "#top"
							Direction.DOWN -> "#bottom"
							Direction.NORTH -> "#front"
							else -> "#side"
						}

						fb.texture(texture)
						fb.cullface(dir)
					},
					setOf(Direction.SOUTH)
				)
			}

//		getVariantBuilder(block)
//			.forAllStates {
//				val facing = it.getValue(TrashCanBlock.FACING)
//
//				val yRot = when (facing) {
//					Direction.NORTH -> 0
//					Direction.EAST -> 90
//					Direction.SOUTH -> 180
//					Direction.WEST -> 270
//					else -> 0
//				}
//
//				ConfiguredModel
//					.builder()
//					.modelFile(model)
//					.rotationY(yRot)
//					.build()
//			}

		simpleBlockWithItem(block, model)
	}

	private fun creativeChest() {
		val block = ModBlocks.CREATIVE_CHEST.get()

		val side = modLoc("block/creative_chest/side")
		val front = modLoc("block/creative_chest/front")
		val top = modLoc("block/creative_chest/top")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", side)
			.texture("front", front)
			.texture("top", top)
			.particle(side)

			// Bottom half
			.element {
				from(1f, 0f, 1f)
				to(15f, 14f, 15f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP, Direction.DOWN -> "#top"
						Direction.NORTH -> "#front"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// Latch
			.element {
				from(6f, 6f, 0f)
				to(10f, 11f, 1f)

				allFacesExcept(
					{ dir, fb ->
						val texture = when (dir) {
							Direction.UP -> "#top"
							Direction.DOWN -> "#bottom"
							Direction.NORTH -> "#front"
							else -> "#side"
						}

						fb.texture(texture)
						fb.cullface(dir)
					},
					setOf(Direction.SOUTH)
				)
			}

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(TrashCanBlock.FACING)

				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, model)
	}

	private fun trashChest() {
		val block = ModBlocks.TRASH_CAN_CHEST.get()

		val side = modLoc("block/trash_can/chest/side")
		val front = modLoc("block/trash_can/chest/front")
		val top = modLoc("block/trash_can/chest/top")
		val bottom = modLoc("block/trash_can/chest/bottom")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", side)
			.texture("front", front)
			.texture("top", top)
			.texture("bottom", bottom)
			.particle(side)

			// Bottom half
			.element {
				from(1f, 0f, 1f)
				to(15f, 14f, 15f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						Direction.NORTH -> "#front"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

			// Latch
			.element {
				from(6f, 5f, 0f)
				to(10f, 12f, 1f)

				allFacesExcept(
					{ dir, fb ->
						val texture = when (dir) {
							Direction.UP -> "#top"
							Direction.DOWN -> "#bottom"
							Direction.NORTH -> "#front"
							else -> "#side"
						}

						fb.texture(texture)
						fb.cullface(dir)
					},
					setOf(Direction.SOUTH)
				)
			}

			// Handle
			.element {
				from(5f, 14f, 6f)
				to(11f, 15f, 10f)

				allFacesExcept(
					{ dir, fb ->
						val texture = if (dir == Direction.UP) "#top" else "#side"
						fb.texture(texture)
						fb.cullface(dir)
					},
					setOf(Direction.DOWN)
				)
			}

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(TrashCanBlock.FACING)

				val yRot = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRot)
					.build()
			}

		simpleBlockItem(block, model)
	}

	private fun trashCans() {
		val blocks = mapOf(
			"item" to ModBlocks.TRASH_CAN.get(),
			"fluid" to ModBlocks.TRASH_CAN_FLUID.get(),
			"energy" to ModBlocks.TRASH_CAN_ENERGY.get(),
		)

		for ((type, block) in blocks) {

			val side = modLoc("block/trash_can/${type}/side")
			val top = modLoc("block/trash_can/${type}/top")
			val bottom = modLoc("block/trash_can/${type}/bottom")

			val model = models()
				.withExistingParent(name(block), mcLoc("block/block"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.particle(side)

				// Bottom half
				.element {
					from(2f, 0f, 2f)
					to(14f, 10f, 14f)

					allFacesExcept(
						{ dir, fb ->
							val texture = when (dir) {
								Direction.DOWN -> "#bottom"
								else -> "#side"
							}

							fb.texture(texture)
							fb.cullface(dir)
						},
						setOf(Direction.UP)
					)
				}

				// Lid
				.element {
					from(1f, 10f, 1f)
					to(15f, 14f, 15f)

					allFaces { dir, fb ->
						val texture = when (dir) {
							Direction.UP -> "#top"
							Direction.DOWN -> "#bottom"
							else -> "#side"
						}

						fb.texture(texture)
						fb.cullface(dir)
					}
				}

				// Handle
				.element {
					from(5f, 14f, 6f)
					to(11f, 15f, 10f)

					allFacesExcept(
						{ dir, fb ->
							val texture = if (dir == Direction.UP) "#top" else "#side"
							fb.texture(texture)
							fb.cullface(dir)
						},
						setOf(Direction.DOWN)
					)
				}

			getVariantBuilder(block)
				.forAllStates {
					val facing = it.getValue(TrashCanBlock.FACING)

					val yRot = when (facing) {
						Direction.NORTH -> 0
						Direction.EAST -> 90
						Direction.SOUTH -> 180
						Direction.WEST -> 270
						else -> 0
					}

					ConfiguredModel
						.builder()
						.modelFile(model)
						.rotationY(yRot)
						.build()
				}

			simpleBlockItem(block, model)
		}

	}

	private fun magnumTorch() {
		val block = ModBlocks.MAGNUM_TORCH.get()

		val side = modLoc("block/magnum_torch/side")
		val top = modLoc("block/magnum_torch/top")
		val bottom = modLoc("block/magnum_torch/bottom")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("side", side)
			.texture("top", top)
			.texture("bottom", bottom)
			.particle(side)
			.renderType(RenderType.cutout().name)

			.element {
				from(6f, 0f, 6f)
				to(10f, 16f, 10f)
				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun chandelier() {
		val block = ModBlocks.CHANDELIER.get()

		val texture = modLoc("block/chandelier")

		val model = models()
			.withExistingParent(name(block), "block/block")
			.particle(texture)
			.texture("texture", texture)
			.renderType(RenderType.cutout().name)

			.element {
				from(8f, 0f, 0.8f)
				to(8f, 16f, 15.2f)
				shade(false)

				rotation()
					.origin(8f, 8f, 8f)
					.axis(Direction.Axis.Y)
					.angle(45f)
					.end()

				face(Direction.EAST) {
					texture("#texture")
					uvs(0f, 0f, 16f, 16f)
				}

				face(Direction.WEST) {
					texture("#texture")
					uvs(0f, 0f, 16f, 16f)
				}
			}

			.element {
				from(0.8f, 0f, 8f)
				to(15.2f, 16f, 8f)
				shade(false)

				rotation()
					.origin(8f, 8f, 8f)
					.axis(Direction.Axis.Y)
					.angle(45f)
					.end()

				face(Direction.NORTH) {
					texture("#texture")
					uvs(0f, 0f, 16f, 16f)
				}

				face(Direction.SOUTH) {
					texture("#texture")
					uvs(0f, 0f, 16f, 16f)
				}
			}

		simpleBlock(block, model)

		itemModels()
			.withExistingParent(name(block), "item/generated")
			.texture("layer0", texture)
	}

	private fun cursedEarth() {
		val block = ModBlocks.CURSED_EARTH.get()

		val top = modLoc("block/cursed_earth/top")
		val side = modLoc("block/cursed_earth/side")
		val bottom = mcLoc("block/dirt")

		val model = models().cubeBottomTop(name(block), side, bottom, top)

		simpleBlockWithItem(block, model)
	}

	private fun peacefulTable() {
		val block = ModBlocks.PEACEFUL_TABLE.get()

		val top = modLoc("block/peaceful_table/top")
		val bottom = modLoc("block/peaceful_table/bottom")
		val side = modLoc("block/peaceful_table/side")

		var model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("bottom", bottom)
			.texture("side", side)
			.particle(top)

			.element {
				from(0f, 12f, 0f)
				to(16f, 16f, 16f)
				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}


		fun leg(minX: Float, minZ: Float) {
			model = model
				.element {
					from(minX, 0f, minZ)
					to(minX + 4, 12f, minZ + 4)
					allFacesExcept(
						{ dir, fb ->
							val texture = when (dir) {
								Direction.DOWN -> "#bottom"
								else -> "#side"
							}

							fb.texture(texture)
							fb.cullface(dir)
						},
						setOf(Direction.UP)
					)
				}
		}

		leg(1f, 1f)
		leg(11f, 1f)
		leg(1f, 11f)
		leg(11f, 11f)

		simpleBlockWithItem(block, model)
	}

	private fun quantumQuarryActuator() {
		val block = ModBlocks.QUANTUM_QUARRY_ACTUATOR.get()

		val top = modLoc("block/quantum_quarry_actuator/top")
		val bottom = modLoc("block/quantum_quarry_actuator/bottom")
		val side = modLoc("block/quantum_quarry_actuator/side")

		val model = models().orientableWithBottom(name(block), side, side, bottom, top)

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(QuantumQuarryActuatorBlock.FACING)

				val yRotation = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val xRotation = when (facing) {
					Direction.UP -> 0
					Direction.DOWN -> 180
					else -> 90
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRotation)
					.rotationX(xRotation)
					.build()
			}

		simpleBlockItem(block, model)
	}

	private fun resonator() {
		val block = ModBlocks.RESONATOR.get()

		val top = modLoc("block/resonator/top")
		val bottom = modLoc("block/resonator/bottom")
		val side = modLoc("block/resonator/side")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("bottom", bottom)
			.texture("side", side)
			.particle(side)

			.element {
				from(0f, 0f, 0f)
				to(16f, 15f, 16f)
				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun moonStoneOre() {
		val ores = mapOf(
			ModBlocks.MOON_STONE_ORE.get() to "stone",
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get() to "deepslate"
		)

		for ((block, baseTexture) in ores) {
			val name = name(block)

			val dayModel = models()
				.cubeAll(name + "_day", mcLoc("block/$baseTexture"))

			val nightModel = models()
				.withExistingParent(name + "_night", mcLoc("block/block"))
				.texture("stone", mcLoc("block/$baseTexture"))
				.texture("overlay", modLoc("block/moon_stone_ore_overlay"))
				.particle(mcLoc("block/stone"))
				.renderType(RenderType.cutout().name)

				.element {
					from(0f, 0f, 0f)
					to(16f, 16f, 16f)
					allFaces { dir, fb ->
						fb.texture("#stone")
						fb.cullface(dir)
						fb.uvs(0f, 0f, 16f, 16f)
					}
				}

				.element {
					from(0f, 0f, 0f)
					to(16f, 16f, 16f)
					allFaces { dir, fb ->
						fb.texture("#overlay")
						fb.cullface(dir)
						fb.uvs(0f, 0f, 16f, 16f)
					}
					emissivity(8, 8)
				}

			getVariantBuilder(block)
				.forAllStates {
					val isVisible = it.getValue(MoonStoneOreBlock.VISIBLE)

					val modelFile = if (isVisible) nightModel else dayModel

					ConfiguredModel
						.builder()
						.modelFile(modelFile)
						.build()
				}

			simpleBlockItem(block, nightModel)
		}

	}

	private fun filingCabinets() {
		val base = ModBlocks.FILING_CABINET.get()
		val advanced = ModBlocks.ADVANCED_FILING_CABINET.get()

		val modelBase = models()
			.orientable(
				name(base),
				modLoc("block/filing_cabinet/base_side"),
				modLoc("block/filing_cabinet/base_front"),
				modLoc("block/filing_cabinet/base_side"),
			)

		val modelAdvanced = models()
			.orientable(
				name(advanced),
				modLoc("block/filing_cabinet/advanced_side"),
				modLoc("block/filing_cabinet/advanced_front"),
				modLoc("block/filing_cabinet/advanced_side"),
			)

		getVariantBuilder(base)
			.forAllStates {
				val facing = it.getValue(MiniChestBlock.FACING)

				val yRotation = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(modelBase)
					.rotationY(yRotation)
					.build()
			}

		getVariantBuilder(advanced)
			.forAllStates {
				val facing = it.getValue(MiniChestBlock.FACING)

				val yRotation = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(modelAdvanced)
					.rotationY(yRotation)
					.build()
			}

		simpleBlockItem(base, modelBase)
		simpleBlockItem(advanced, modelAdvanced)
	}

	// TODO: The weird arm connector thing, needs a block state also
	private fun enderQuarryUpgrades() {
		val blocks = mapOf(
			ModBlocks.ENDER_QUARRY_UPGRADE_BASE to "base",
			ModBlocks.ENDER_QUARRY_WORLD_HOLE_UPGRADE to "world_hole",
			ModBlocks.ENDER_QUARRY_SILK_TOUCH_UPGRADE to "silk_touch",
			ModBlocks.ENDER_QUARRY_FORTUNE_UPGRADE to "fortune_one",
			ModBlocks.ENDER_QUARRY_FORTUNE_TWO_UPGRADE to "fortune_two",
			ModBlocks.ENDER_QUARRY_FORTUNE_THREE_UPGRADE to "fortune_three",
			ModBlocks.ENDER_QUARRY_SPEED_UPGRADE to "speed_one",
			ModBlocks.ENDER_QUARRY_SPEED_TWO_UPGRADE to "speed_two",
			ModBlocks.ENDER_QUARRY_SPEED_THREE_UPGRADE to "speed_three",
		)

		for ((deferred, textureSubname) in blocks) {
			val block = deferred.get()

			val texture = modLoc("block/ender_quarry_upgrade/$textureSubname")

			val model = models()
				.withExistingParent(name(block), mcLoc("block/block"))
				.texture("texture", texture)
				.particle(texture)
				.element {
					from(1f, 1f, 1f)
					to(15f, 15f, 15f)
					allFaces { dir, fb ->
						fb.texture("#texture")
						fb.cullface(dir)
						fb.uvs(1f, 1f, 15f, 15f)
					}
				}

			simpleBlockWithItem(block, model)
		}
	}

	private fun enderMarker() {
		val block = ModBlocks.ENDER_MARKER.get()

		val texture = modLoc("block/ender_marker")

		val model = models()
			.withExistingParent(name(block), "block/block")
			.texture("texture", texture)
			.particle(texture)
			.element {
				from(7f, 0f, 7f)
				to(9f, 13f, 9f)

				allFaces { dir, fb ->
					fb.texture("#texture")
					fb.cullface(dir)

					if (dir == Direction.UP) {
						fb.uvs(7f, 3f, 8f, 4f)
					} else if (dir == Direction.DOWN) {
						fb.uvs(7f, 5f, 8f, 6f)
					}
				}
			}

		simpleBlockWithItem(block, model)
	}

	//TODO: Block state for active and finished
	private fun enderQuarry() {
		val block = ModBlocks.ENDER_QUARRY.get()

		val side = modLoc("block/ender_quarry/side")
		modLoc("block/ender_quarry/side_on")
		modLoc("block/ender_quarry/side_finished")
		val top = modLoc("block/ender_quarry/top")
		val bottom = modLoc("block/ender_quarry/bottom")

		val model = models()
			.orientableWithBottom(name(block), side, side, bottom, top)

		simpleBlockWithItem(block, model)
	}

	private fun machineBlock() {
		val block = ModBlocks.MACHINE_BLOCK.get()

		val side = modLoc("block/machine_block/side")
		val top = modLoc("block/machine_block/top")

		val model = models()
			.cubeTop(name(block), side, top)

		simpleBlockWithItem(block, model)
	}

	private fun furnace() {
		val block = ModBlocks.FURNACE.get()

		val side = modLoc("block/machine_block/side")
		val top = modLoc("block/machine_block/top")
		val bottom = modLoc("block/machine_block/bottom")
		val overlayOn = modLoc("block/furnace/on")
		val overlayOff = modLoc("block/furnace/off")

		val modelOff = models()
			.withExistingParent(name(block) + "_off", mcLoc("block/block"))
			.texture("side", side)
			.texture("top", top)
			.texture("bottom", bottom)
			.texture("overlay", overlayOff)
			.particle(side)
			.renderType(RenderType.cutout().name)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)
				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)
					fb.uvs(0f, 0f, 16f, 16f)
				}
			}

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				face(Direction.NORTH) {
					texture("#overlay")
					uvs(0f, 0f, 16f, 16f)
				}
			}

		val modelOn = models()
			.withExistingParent(name(block) + "_on", modLoc("block/furnace_off"))
			.texture("overlay", overlayOn)

		getVariantBuilder(block)
			.forAllStates {
				val isLit = it.getValue(EUFurnaceBlock.LIT)
				val direction = it.getValue(EUFurnaceBlock.FACING)

				val yRotation = when (direction) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val model = if (isLit) modelOn else modelOff

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRotation)
					.build()
			}

		simpleBlockItem(block, modelOff)
	}

	private fun generators() {
		makeGenerator(ModBlocks.FURNACE_GENERATOR.get(), "furnace")
		makeGenerator(ModBlocks.MAGMATIC_GENERATOR.get(), "magmatic")
		makeGenerator(ModBlocks.NETHER_STAR_GENERATOR.get(), "nether_star")
		makeGenerator(ModBlocks.FROSTY_GENERATOR.get(), "frosty")
		makeGenerator(ModBlocks.HALITOSIS_GENERATOR.get(), "halitosis")
		makeGenerator(ModBlocks.PINK_GENERATOR.get(), "pink")
		makeGenerator(ModBlocks.DEATH_GENERATOR.get(), "death")
		makeGenerator(ModBlocks.ENDER_GENERATOR.get(), "ender")
		makeGenerator(ModBlocks.EXPLOSIVE_GENERATOR.get(), "explosive")
		makeGenerator(ModBlocks.CULINARY_GENERATOR.get(), "culinary")
		makeGenerator(ModBlocks.DISENCHANTMENT_GENERATOR.get(), "disenchantment")
		makeGenerator(ModBlocks.POTION_GENERATOR.get(), "potion")
		makeGenerator(ModBlocks.OVERCLOCKED_GENERATOR.get(), "overclocked")
		makeGenerator(ModBlocks.HEATED_REDSTONE_GENERATOR.get(), "heated_redstone")
		makeGenerator(ModBlocks.SLIMY_GENERATOR.get(), "slimy")
		makeGenerator(ModBlocks.SURVIVALIST_GENERATOR.get(), "survival")
	}

	private fun makeGenerator(
		generatorBlock: GeneratorBlock,
		directory: String
	) {
		val name = name(generatorBlock)

		val frontOff = modLoc("block/generator/$directory/front")
		val frontOn = modLoc("block/generator/$directory/front_on")
		val side = modLoc("block/generator/$directory/side")
		val top = modLoc("block/generator/$directory/top")

		val modelOff = models()
			.cube(name + "_off", top, top, frontOff, side, side, side)
			.particle(side)

		val modelOn = models()
			.cube(name + "_on", top, top, frontOn, side, side, side)
			.particle(side)

		getVariantBuilder(generatorBlock)
			.forAllStates {
				val direction = it.getValue(GeneratorBlock.FACING)
				val isLit = it.getValue(GeneratorBlock.LIT)

				val yRotation = when (direction) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				val modelFile = if (isLit) modelOn else modelOff

				ConfiguredModel
					.builder()
					.modelFile(modelFile)
					.rotationY(yRotation)
					.build()
			}

		simpleBlockItem(generatorBlock, modelOn)
	}

	//TODO: Texture needs a latch on the front
	private fun miniChest() {
		val block = ModBlocks.MINI_CHEST.get()

		val name = name(block)
		val texture = modLoc("block/mini_chest")

		val model = models()
			.withExistingParent(name, mcLoc("block/block"))
			.texture("texture", texture)
			.particle(texture)

			.element {
				from(5f, 0f, 5f)
				to(11f, 6f, 11f)
				allFaces { dir, fb ->
					when (dir) {
						Direction.NORTH -> {
							fb.texture("#texture")
							fb.uvs(8f, 8f, 16f, 16f)
						}

						Direction.UP -> {
							fb.texture("#texture")
							fb.uvs(0f, 0f, 8f, 8f)
						}

						Direction.DOWN -> {
							fb.texture("#texture")
							fb.uvs(8f, 0f, 16f, 8f)
							fb.cullface(Direction.DOWN)
						}

						else -> {
							fb.texture("#texture")
							fb.uvs(0f, 8f, 8f, 16f)
						}
					}
				}
			}

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(MiniChestBlock.FACING)

				val yRotation = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRotation)
					.build()
			}

		simpleBlockItem(block, model)
	}

	private fun slightlyLargerChest() {
		val block = ModBlocks.SLIGHTLY_LARGER_CHEST.get()
		val name = name(block)

		val front = modLoc("block/slightly_larger_chest/front")
		val side = modLoc("block/slightly_larger_chest/side")
		val top = modLoc("block/slightly_larger_chest/top")

		val model = models().orientableWithBottom(name, side, front, top, top)

		getVariantBuilder(block)
			.forAllStates {
				val facing = it.getValue(SlightlyLargerChestBlock.FACING)

				val yRotation = when (facing) {
					Direction.NORTH -> 0
					Direction.EAST -> 90
					Direction.SOUTH -> 180
					Direction.WEST -> 270
					else -> 0
				}

				ConfiguredModel
					.builder()
					.modelFile(model)
					.rotationY(yRotation)
					.build()
			}

		simpleBlockItem(block, model)
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
			ModBlocks.CREATIVE_HARVEST.get(),
			ModBlocks.CREATIVE_ENERGY_SOURCE.get(),
			ModBlocks.DEEP_DARK_PORTAL.get(),
			ModBlocks.BLOCK_OF_DEMON_METAL.get(),
			ModBlocks.BLOCK_OF_ENCHANTED_METAL.get(),
			ModBlocks.SOUND_MUFFLER.get(),
			ModBlocks.RAINBOW_GENERATOR.get(),
			ModBlocks.SANDY_GLASS.get(),
			ModBlocks.QUANTUM_QUARRY.get(),
			ModBlocks.DIAMOND_ETCHED_COMPUTATIONAL_MATRIX.get(),
			ModBlocks.MAGICAL_WOOD.get(),
			ModBlocks.LAST_MILLENNIUM_PORTAL.get()
		)

		for (block in blocks) {
			simpleBlockWithItem(block, cubeAll(block))
		}
	}

	private fun singleTextureCutout() {
		val blocks = listOf(
			ModBlocks.RESTURBED_MOB_SPAWNER.get(),
			ModBlocks.SQUARE_GLASS.get()
		)

		for (block in blocks) {
			val name = name(block)
			val texture = modLoc("block/$name")
			val model = models()
				.cubeAll(name, texture)
				.renderType(RenderType.cutout().name)

			simpleBlockWithItem(block, model)
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
			ModBlocks.ENDER_INFUSED_OBSIDIAN.get(),
			ModBlocks.POLISHED_STONE.get(),
			ModBlocks.ENDER_SAND_ALLOY.get(),
			ModBlocks.GRAVEL_ROAD.get(),
			ModBlocks.GRAVEL_BRICKS.get(),
			ModBlocks.FROSTED_STONE.get(),
			ModBlocks.EMINENCE_STONE.get(),
			ModBlocks.RAINBOW_STONE.get(),
			ModBlocks.DIAGONAL_WOOD.get(),
			ModBlocks.EDGED_STONE_BRICKS.get()
		)

		val translucent = listOf(
			ModBlocks.INEFFABLE_GLASS.get(),
			ModBlocks.INVERTED_INEFFABLE_GLASS.get(),
			ModBlocks.DARK_INEFFABLE_GLASS.get(),
			ModBlocks.ETHEREAL_GLASS.get(),
			ModBlocks.INVERTED_ETHEREAL_GLASS.get(),
			ModBlocks.OBSIDIAN_GLASS.get(),
			ModBlocks.REINFORCED_DARK_GLASS.get()
		)

		val cutout = listOf(
			ModBlocks.THICKENED_GLASS.get(),
			ModBlocks.EDGED_GLASS.get(),
			ModBlocks.SWIRLING_GLASS.get(),
			ModBlocks.HEART_GLASS.get(),
			ModBlocks.GLASS_BRICKS.get(),
			ModBlocks.CARVED_GLASS.get(),
			ModBlocks.GOLDEN_EDGED_GLASS.get(),
			ModBlocks.GLOWING_GLASS.get(),
			ModBlocks.BLOCK_OF_UNSTABLE_INGOT.get(),
			ModBlocks.PATTERNED_GLASS.get(),
			ModBlocks.REDSTONE_GLASS.get()
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

		for (block in cutout) {
			val path = BuiltInRegistries.BLOCK.getKey(block).path
			val texture = modLoc("block/$path/particle")

			val model = models()
				.cubeAll(name(block), texture)
				.renderType(RenderType.cutout().name)

			simpleBlockItem(block, model)
		}
	}

	private fun name(block: Block): String {
		return BuiltInRegistries.BLOCK.getKey(block).path
	}

}