package dev.aaronhowser.mods.excessive_utilities.datagen.model

import dev.aaronhowser.mods.aaron.misc.AaronDsls.element
import dev.aaronhowser.mods.aaron.misc.AaronDsls.face
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.*
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
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
		fireMill()
		lavaMill()
		creativeChest()
		conveyorBelt()
		tradingPost()
		coloredBlocks()
		qed()
		enderFluxCrystal()
		furnace()
		magicalSnowGlobe()
		wirelessFeBattery()
		wirelessFeTransmitter()
		redstoneLantern()
		redstoneClock()
		spikes()
		transferPipes()
		transferNodes()
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
			.texture("particle", texture)
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
			.texture("particle", texture)

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

				allFaces { dir, fb ->
					fb.texture("#pipe")
					fb.uvs(6f, 6f, 10f, 10f)
				}
			}

			.element {
				from(6f, 6f, 2f)
				to(10f, 10f, 6f)

				allFaces { dir, fb ->
					val uvs = when (dir) {
						Direction.NORTH -> arrayOf(6f, 6f, 10f, 10f)
						Direction.EAST -> arrayOf(2f, 6f, 6f, 10f)
						Direction.SOUTH -> arrayOf(6f, 6f, 10f, 10f)
						Direction.WEST -> arrayOf(2f, 6f, 6f, 10f)
						Direction.UP -> arrayOf(6f, 2f, 10f, 6f)
						Direction.DOWN -> arrayOf(6f, 2f, 10f, 6f)
					}

					fb.texture("#pipe")
					fb.uvs(uvs[0], uvs[1], uvs[2], uvs[3])
				}
			}

			.element {
				from(1f, 1f, 0f)
				to(15f, 15f, 1f)

				allFaces { dir, fb ->

					when (dir) {
						Direction.NORTH -> arrayOf(15f, 1f, 1f, 15f)
						Direction.EAST -> arrayOf(14f, 1f, 15f, 15f)
						Direction.SOUTH -> arrayOf(1f, 1f, 15f, 15f)
						Direction.WEST -> arrayOf(1f, 1f, 2f, 15f)
						Direction.UP -> arrayOf(1f, 1f, 15f, 2f)
						Direction.DOWN -> arrayOf(1f, 14f, 15f, 15f)
					}

					val texture = if (dir == Direction.NORTH) "#back" else "#node"
					fb.texture(texture)
				}
			}

			.element {
				from(3f, 3f, 1f)
				to(13f, 13f, 3f)

				allFacesExcept(
					{ dir, fb ->
						val uvs = when (dir) {
							Direction.EAST -> arrayOf(1f, 3f, 3f, 13f)
							Direction.SOUTH -> arrayOf(3f, 3f, 13f, 13f)
							Direction.WEST -> arrayOf(13f, 3f, 15f, 13f)
							Direction.UP -> arrayOf(3f, 13f, 13f, 15f)
							Direction.DOWN -> arrayOf(3f, 1f, 13f, 3f)
							else -> return@allFacesExcept
						}

						fb.texture("#node")
						fb.uvs(uvs[0], uvs[1], uvs[2], uvs[3])
					},
					setOf(Direction.NORTH)
				)
			}

		fun specific(block: TransferNodeBlock, texture: String) {
			val nodeTexture = modLoc(texture)

			val model = models()
				.withExistingParent(name(block), modLoc("block/$templateName"))
				.texture("pipe", modLoc("block/transfer_pipe/pipe"))
				.texture("node", nodeTexture)
				.texture("back", modLoc("block/transfer_node/back"))
				.texture("particle", nodeTexture)

			val builder = getMultipartBuilder(block)

			builder.part()
				.modelFile(model)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.NORTH)
				.end()

				.part()
				.modelFile(model)
				.rotationY(90)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.EAST)
				.end()

				.part()
				.modelFile(model)
				.rotationY(180)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.SOUTH)
				.end()

				.part()
				.modelFile(model)
				.rotationY(270)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.WEST)
				.end()

				.part()
				.modelFile(model)
				.rotationX(270)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.UP)
				.end()

				.part()
				.modelFile(model)
				.rotationX(90)
				.addModel()
				.condition(TransferNodeBlock.PLACED_ON, Direction.DOWN)
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

		}

		specific(ModBlocks.ITEM_TRANSFER_NODE.get(), "block/transfer_node/item")
		specific(ModBlocks.FLUID_TRANSFER_NODE.get(), "block/transfer_node/fluid")
		specific(ModBlocks.ENERGY_TRANSFER_NODE.get(), "block/transfer_node/energy")
		specific(ModBlocks.ITEM_RETRIEVAL_NODE.get(), "block/transfer_node/retrieval/item")
		specific(ModBlocks.FLUID_RETRIEVAL_NODE.get(), "block/transfer_node/retrieval/fluid")
		specific(ModBlocks.ENERGY_RETRIEVAL_NODE.get(), "block/transfer_node/retrieval/energy")

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
			simpleBlockWithItem(spike, model)
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
			.texture("particle", side)

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
			.texture("particle", side)

		simpleBlockWithItem(block, model)
	}

	private fun magicalSnowGlobe() {
		val block = ModBlocks.MAGICAL_SNOW_GLOBE.get()

		val top = modLoc("block/magical_snow_globe/top")
		val bottom = modLoc("block/magical_snow_globe/bottom")
		val side = modLoc("block/magical_snow_globe/side")

		val model = models()
			.withExistingParent(name(block), mcLoc("block/block"))
			.texture("top", top)
			.texture("bottom", bottom)
			.texture("side", side)
			.texture("particle", side)
			.renderType(RenderType.translucent().name)

			.element {
				from(2f, 0f, 2f)
				to(14f, 12f, 14f)

				allFaces { dir, fb ->
					val texture = when (dir) {
						Direction.UP -> "#top"
						Direction.DOWN -> "#bottom"
						else -> "#side"
					}

					fb.texture(texture)
					fb.cullface(dir)

					if (dir == Direction.DOWN) {
						fb.uvs(3f, 3f, 13f, 13f)
					}
				}
			}

			.element {
				from(2f, 0.01f, 2f)
				to(14f, 0.01f, 14f)

				face(Direction.UP) {
					texture("#bottom")
					cullface(Direction.UP)
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
			.texture("particle", side)

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
			val texture = modLoc("block/colored/$name")
			for (color in DyeColor.entries) {
				val block = getter(color).get()
				val model = models()
					.withExistingParent(name(block), mcLoc("block/block"))
					.texture("all", texture)
					.texture("particle", texture)
					.element {
						from(0f, 0f, 0f)
						to(16f, 16f, 16f)
						allFaces { dir, fb ->
							fb.texture("#all")
							fb.cullface(dir)
							fb.uvs(0f, 0f, 16f, 16f)
							fb.tintindex(0)
						}
					}

				simpleBlockWithItem(block, model)
			}
		}

		for (color in DyeColor.entries) {
			val block = ModBlocks.getColoredRedstoneLamp(color).get()
			val name = name(block)

			val textureOff = modLoc("block/colored/redstone_lamp")
			val textureOn = modLoc("block/colored/redstone_lamp_on")

			val modelOff = models()
				.withExistingParent(name, mcLoc("block/block"))
				.texture("all", textureOff)
				.texture("particle", textureOff)
				.element {
					from(0f, 0f, 0f)
					to(16f, 16f, 16f)
					allFaces { dir, fb ->
						fb.texture("#all")
						fb.cullface(dir)
						fb.uvs(0f, 0f, 16f, 16f)
						fb.tintindex(0)
					}
				}

			val modelOn = models()
				.withExistingParent(name + "_on", modLoc("block/$name"))
				.texture("all", textureOn)
				.texture("particle", textureOn)

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
			.texture("particle", top)

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
			.texture("particle", top)
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
			.texture("particle", top)
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

				for (dir in Direction.Plane.HORIZONTAL) {
					face(dir) {
						texture("#fan")
						cullface(dir)
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
			.texture("particle", modLoc("block/mill/fire"))
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
				.texture("particle", side)

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
			.texture("particle", side)

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
				from(6f, 5f, 0f)
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
			.texture("particle", side)

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
				.texture("particle", side)

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
			.texture("particle", side)
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

					when (dir) {
						Direction.UP, Direction.DOWN -> fb.uvs(4f, 4f, 8f, 8f)
						else -> fb.uvs(6f, 0f, 10f, 16f)
					}
				}
			}

		simpleBlockWithItem(block, model)
	}

	private fun chandelier() {
		val block = ModBlocks.CHANDELIER.get()

		val texture = modLoc("block/chandelier")

		val model = models()
			.withExistingParent(name(block), "block/block")
			.texture("particle", texture)
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
			.texture("particle", top)

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
			.texture("particle", side)

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
			ModBlocks.MOON_STORE_ORE.get() to "stone",
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
				.texture("particle", mcLoc("block/stone"))
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
					val isVisible = it.getValue(MoonStoreOreBlock.VISIBLE)

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

		for ((deferred, texture) in blocks) {
			val block = deferred.get()

			val model = models()
				.withExistingParent(name(block), mcLoc("block/block"))
				.texture("texture", modLoc("block/ender_quarry_upgrade/$texture"))
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
			.texture("particle", texture)
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
		val bottom = modLoc("block/machine_block/bottom")

		val model = models()
			.orientableWithBottom(name(block), side, side, bottom, top)

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
			.texture("particle", side)
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
		models()
			.withExistingParent("generator_base", mcLoc("block/block"))
			.texture("bottom", modLoc("block/machine_base/bottom"))
			.texture("top", modLoc("block/machine_base/top"))
			.texture("side", modLoc("block/machine_base/side"))
			.texture("particle", modLoc("block/machine_base/side"))
			.renderType(RenderType.cutout().name)

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)
				allFaces { dir, fb ->
					fb.uvs(0f, 0f, 16f, 16f)
					fb.cullface(dir)
					fb.tintindex(0)

					when (dir) {
						Direction.DOWN -> fb.texture("#bottom")
						Direction.UP -> fb.texture("#top")
						else -> fb.texture("#side")
					}
				}
			}

			.element {
				from(0f, 0f, 0f)
				to(16f, 16f, 16f)

				face(Direction.UP) {
					uvs(0f, 0f, 16f, 16f)
					cullface(Direction.UP)
					texture("#top_overlay")
					rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN)
				}

				face(Direction.NORTH) {
					uvs(0f, 0f, 16f, 16f)
					cullface(Direction.NORTH)
					texture("#front_overlay")
				}

			}

		survivalGenerator()
		makeGenerator(ModBlocks.FURNACE_GENERATOR.get(), "nothing")
		makeGenerator(ModBlocks.MAGMATIC_GENERATOR.get(), "nothing")
		makeGenerator(ModBlocks.NETHER_STAR_GENERATOR.get(), "nothing")
		makeGenerator(ModBlocks.FROSTY_GENERATOR.get(), "nothing")
		makeGenerator(ModBlocks.HALITOSIS_GENERATOR.get(), "nothing")
		makeGenerator(ModBlocks.PINK_GENERATOR.get(), "pink")
		makeGenerator(ModBlocks.DEATH_GENERATOR.get(), "death")
		makeGenerator(ModBlocks.ENDER_GENERATOR.get(), "ender")
		makeGenerator(ModBlocks.EXPLOSIVE_GENERATOR.get(), "explosive")
		makeGenerator(ModBlocks.CULINARY_GENERATOR.get(), "culinary")
		makeGenerator(ModBlocks.DISENCHANTMENT_GENERATOR.get(), "disenchantment")
		makeGenerator(ModBlocks.POTION_GENERATOR.get(), "potion")
	}

	private fun survivalGenerator() {
		val block = ModBlocks.SURVIVAL_GENERATOR.get()
		val name = name(block)
		val modelOff = models()
			.withExistingParent(name + "_off", modLoc("generator_base"))
			.texture("top_overlay", modLoc("block/generator/top/nothing"))
			.texture("front_overlay", modLoc("block/generator/off"))
			.texture("bottom", mcLoc("block/furnace_top"))
			.texture("top", mcLoc("block/furnace_top"))
			.texture("particle", mcLoc("block/furnace_top"))
			.texture("side", mcLoc("block/furnace_side"))

		val modelOn = models()
			.withExistingParent(name + "_on", modLoc("generator_base"))
			.texture("top_overlay", modLoc("block/generator/top/nothing"))
			.texture("front_overlay", modLoc("block/generator/on"))

		getVariantBuilder(block)
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

		simpleBlockItem(block, modelOff)
	}

	private fun makeGenerator(
		generatorBlock: GeneratorBlock,
		topOverlayName: String
	) {
		val name = name(generatorBlock)

		val modelOff = models()
			.withExistingParent(name + "_off", modLoc("generator_base"))
			.texture("top_overlay", modLoc("block/generator/top/$topOverlayName"))
			.texture("front_overlay", modLoc("block/generator/off"))

		val modelOn = models()
			.withExistingParent(name + "_on", modLoc("generator_base"))
			.texture("top_overlay", modLoc("block/generator/top/$topOverlayName"))
			.texture("front_overlay", modLoc("block/generator/on"))

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

		simpleBlockItem(generatorBlock, modelOff)
	}

	private fun miniChest() {
		val block = ModBlocks.MINI_CHEST.get()
		val name = name(block)

		val front = modLoc("block/mini_chest/front")
		val side = modLoc("block/mini_chest/side")
		val top = modLoc("block/mini_chest/top")
		val bottom = modLoc("block/mini_chest/bottom")

		val model = models()
			.withExistingParent(name, mcLoc("block/block"))
			.texture("front", front)
			.texture("side", side)
			.texture("top", top)
			.texture("bottom", bottom)
			.texture("particle", side)

			.element {
				from(5f, 0f, 5f)
				to(11f, 6f, 11f)
				allFaces { dir, fb ->
					when (dir) {
						Direction.NORTH -> fb.texture("#front")
						Direction.UP -> fb.texture("#top")
						Direction.DOWN -> fb.texture("#bottom")
						else -> fb.texture("#side")
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
			ModBlocks.BEDROCK_BRICKS.get(),
			ModBlocks.BEDROCK_COBBLESTONE.get(),
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
			ModBlocks.MAGICAL_WOOD.get()
		)

		for (block in blocks) {
			simpleBlockWithItem(block, cubeAll(block))
		}
	}

	private fun singleTextureCutout() {
		val blocks = listOf(
			ModBlocks.RESTURBED_MOB_SPAWNER.get()
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
			ModBlocks.POLISHED_STONE.get()
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