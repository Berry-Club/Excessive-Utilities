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

	lateinit var stoneDrumCapacity: ModConfigSpec.DoubleValue
	lateinit var ironDrumCapacity: ModConfigSpec.DoubleValue
	lateinit var reinforcedLargeDrumCapacity: ModConfigSpec.DoubleValue
	lateinit var demonicallyGargantuanDrumCapacity: ModConfigSpec.DoubleValue

	init {
		general()
		heatingCoil()
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

	companion

	object {
		private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}