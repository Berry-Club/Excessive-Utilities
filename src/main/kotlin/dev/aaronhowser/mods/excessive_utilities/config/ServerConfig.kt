package dev.aaronhowser.mods.excessive_utilities.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var chandelierRadius: ModConfigSpec.IntValue
	lateinit var magnumTorchRadius: ModConfigSpec.IntValue

	lateinit var heatingCoilBurnTime: ModConfigSpec.IntValue
	lateinit var heatingCoilBurnCost: ModConfigSpec.IntValue
	lateinit var heatingCoilMaxEnergy: ModConfigSpec.IntValue

	lateinit var stoneDrumCapacity: ModConfigSpec.IntValue
	lateinit var ironDrumCapacity: ModConfigSpec.IntValue
	lateinit var reinforcedLargeDrumCapacity: ModConfigSpec.IntValue
	lateinit var demonicallyGargantuanDrumCapacity: ModConfigSpec.IntValue

	lateinit var manualMillGenerationPerPlayer: ModConfigSpec.IntValue
	lateinit var solarPanelGeneration: ModConfigSpec.IntValue
	lateinit var lunarPanelGeneration: ModConfigSpec.IntValue
	lateinit var lavaMillGeneration: ModConfigSpec.IntValue
	lateinit var fireMillGeneration: ModConfigSpec.IntValue
	lateinit var waterMillGenerationPerSide: ModConfigSpec.IntValue
	lateinit var dragonEggMillGeneration: ModConfigSpec.IntValue
	lateinit var creativeMillGeneration: ModConfigSpec.IntValue

	init {
		general()
		heatingCoil()
		drums()
		gridPower()
	}

	private fun gridPower() {
		builder.push("grid_power")

		builder.push("gp_generation")

		manualMillGenerationPerPlayer = builder
			.comment("The amount of GP the Manual Mill generates per player cranking it.")
			.defineInRange("manualMillGenerationPerPlayer", 15, 1, Int.MAX_VALUE)

		solarPanelGeneration = builder
			.comment("The amount of GP the Solar Panel generates when it can see the sun.")
			.defineInRange("solarPanelGeneration", 1, 1, Int.MAX_VALUE)

		lunarPanelGeneration = builder
			.comment("The amount of GP the Lunar Panel generates when it can see the moon.")
			.defineInRange("lunarPanelGeneration", 1, 1, Int.MAX_VALUE)

		lavaMillGeneration = builder
			.comment("The amount of GP the Lava Mill generates when Lava is adjacent.")
			.defineInRange("lavaMillGeneration", 2, 1, Int.MAX_VALUE)

		fireMillGeneration = builder
			.comment("The amount of GP the Fire Mill generates when Fire is below it.")
			.defineInRange("fireMillGeneration", 4, 1, Int.MAX_VALUE)

		waterMillGenerationPerSide = builder
			.comment("The amount of GP the Water Mill generates per side with flowing water adjacent.")
			.defineInRange("waterMillGenerationPerSide", 4, 1, Int.MAX_VALUE)

		dragonEggMillGeneration = builder
			.comment("The amount of GP the Dragon Egg Mill generates when a Dragon Egg is on top of it.")
			.defineInRange("dragonEggMillGeneration", 500, 1, Int.MAX_VALUE)

		creativeMillGeneration = builder
			.comment("The amount of GP the Creative Mill generates.")
			.defineInRange("creativeMillGeneration", 10_000, 1, Int.MAX_VALUE)

		builder.pop()

		builder.push("gp_usage")

		builder.pop()

		builder.pop()
	}

	private fun general() {

		chandelierRadius = builder
			.comment("The radius in blocks that a Chandelier prevents monster spawns.")
			.defineInRange("chandelierRadius", 16, 1, Int.MAX_VALUE)

		magnumTorchRadius = builder
			.comment("The radius in blocks that a Magnum Torch prevents monster spawns.")
			.defineInRange("magnumTorchRadius", 64, 1, Int.MAX_VALUE)

	}

	private fun heatingCoil() {
		builder.push("heating_coil")

		heatingCoilBurnTime = builder
			.comment("The number of ticks a Heating Coil will burn in a Furnace.")
			.defineInRange("heatingCoilBurnTime", 20, 1, Int.MAX_VALUE)

		heatingCoilBurnCost = builder
			.comment("How much FE will be spent when a Heating Coil is used as Furnace fuel.")
			.defineInRange("heatingCoilBurnCost", 50, 1, Int.MAX_VALUE)

		heatingCoilMaxEnergy = builder
			.comment("The maximum amount of FE a Heating Coil can store.")
			.defineInRange("heatingCoilMaxEnergy", 1_500_000, 1, Int.MAX_VALUE)

		builder.pop()
	}

	private fun drums() {
		builder.push("drums")

		stoneDrumCapacity = builder
			.comment("The fluid capacity of Stone Drums in millibuckets.")
			.defineInRange("stoneDrumCapacity", 16_000, 1, Int.MAX_VALUE)

		ironDrumCapacity = builder
			.comment("The fluid capacity of Iron Drums in millibuckets.")
			.defineInRange("ironDrumCapacity", 256_000, 1, Int.MAX_VALUE)

		reinforcedLargeDrumCapacity = builder
			.comment("The fluid capacity of Reinforced Large Drums in millibuckets.")
			.defineInRange("reinforcedLargeDrumCapacity", 4_096_000, 1, Int.MAX_VALUE)

		demonicallyGargantuanDrumCapacity = builder
			.comment("The fluid capacity of Demonically Gargantuan Drums in millibuckets.")
			.defineInRange("demonicallyGargantuanDrumCapacity", 65_536_000, 1, Int.MAX_VALUE)

		builder.pop()
	}

	companion

	object {
		private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}