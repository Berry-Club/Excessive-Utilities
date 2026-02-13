package dev.aaronhowser.mods.excessive_utilities.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var chandelierRadius: ModConfigSpec.IntValue
	lateinit var magnumTorchRadius: ModConfigSpec.IntValue
	lateinit var flatItemTransferNodeSpeed: ModConfigSpec.IntValue
	lateinit var flatFluidTransferNodeSpeed: ModConfigSpec.IntValue
	lateinit var peacefulTableOnlyInPeaceful: ModConfigSpec.BooleanValue
	lateinit var peacefulTableChancePerTick: ModConfigSpec.DoubleValue

	lateinit var heatingCoilBurnTime: ModConfigSpec.IntValue
	lateinit var heatingCoilBurnCost: ModConfigSpec.IntValue
	lateinit var heatingCoilMaxEnergy: ModConfigSpec.IntValue

	lateinit var stoneDrumCapacity: ModConfigSpec.IntValue
	lateinit var ironDrumCapacity: ModConfigSpec.IntValue
	lateinit var reinforcedLargeDrumCapacity: ModConfigSpec.IntValue
	lateinit var demonicallyGargantuanDrumCapacity: ModConfigSpec.IntValue
	lateinit var bedrockDrumCapacity: ModConfigSpec.IntValue

	lateinit var manualMillGenerationPerPlayer: ModConfigSpec.DoubleValue
	lateinit var solarPanelGeneration: ModConfigSpec.DoubleValue
	lateinit var lunarPanelGeneration: ModConfigSpec.DoubleValue
	lateinit var lavaMillGeneration: ModConfigSpec.DoubleValue
	lateinit var fireMillGeneration: ModConfigSpec.DoubleValue
	lateinit var waterMillGenerationPerSide: ModConfigSpec.DoubleValue
	lateinit var dragonEggMillGeneration: ModConfigSpec.DoubleValue
	lateinit var creativeMillGeneration: ModConfigSpec.DoubleValue

	lateinit var speedUpgradeGpCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var chickenWingRingGpCost: ModConfigSpec.DoubleValue
	lateinit var flyingSquidRingGpCost: ModConfigSpec.DoubleValue
	lateinit var angelRingGpCost: ModConfigSpec.DoubleValue

	lateinit var culinaryFePerFoodValue: ModConfigSpec.DoubleValue
	lateinit var culinaryTicksPerSaturationValue: ModConfigSpec.DoubleValue
	lateinit var furnaceGeneratorBurnTimeMultiplier: ModConfigSpec.DoubleValue
	lateinit var furnaceGeneratorFePerTick: ModConfigSpec.IntValue
	lateinit var survivalistGeneratorBurnTimeMultiplier: ModConfigSpec.DoubleValue
	lateinit var survivalistGeneratorFePerTick: ModConfigSpec.IntValue
	lateinit var netherStarGeneratorEffectRadius: ModConfigSpec.DoubleValue
	lateinit var deathGeneratorEffectRadius: ModConfigSpec.DoubleValue
	lateinit var rainbowGeneratorFePerTick: ModConfigSpec.IntValue

	lateinit var wateringCanRadius: ModConfigSpec.IntValue
	lateinit var wateringCanTickChance: ModConfigSpec.DoubleValue
	lateinit var wateringCanWaterUsagePerTick: ModConfigSpec.IntValue
	lateinit var reinforcedWateringCanRadius: ModConfigSpec.IntValue
	lateinit var reinforcedWateringCanTickChance: ModConfigSpec.DoubleValue
	lateinit var reinforcedWateringCanWaterUsagePerTick: ModConfigSpec.IntValue

	lateinit var enderQuarryFencePerimeterLimit: ModConfigSpec.IntValue
	lateinit var enderQuarryMarkerSearchDistance: ModConfigSpec.IntValue

	lateinit var chickenWingRingFallSpeed: ModConfigSpec.DoubleValue
	lateinit var chickenWingRingDurationTicks: ModConfigSpec.IntValue
	lateinit var chickenWingRingRechargeTicks: ModConfigSpec.IntValue

	lateinit var flyingSquidRingThrustMultiplier: ModConfigSpec.DoubleValue
	lateinit var flyingSquidRingMaxUpwardSpeed: ModConfigSpec.DoubleValue
	lateinit var flyingSquidRingDurationTicks: ModConfigSpec.IntValue
	lateinit var flyingSquidRingRechargeTicks: ModConfigSpec.IntValue

	init {
		general()
		heatingCoil()
		drums()
		wateringCan()
		gridPower()
		feGenerators()
		enderQuarry()
		rings()
	}

	private fun rings() {
		builder.push("rings")

		builder.comment("GP costs are defined elsewhere!")

		builder.push("chicken_wing_ring")

		chickenWingRingFallSpeed = builder
			.comment("The max fall speed when using a Chicken Wing Ring.")
			.defineInRange("chickenWingRingFallSpeed", 0.1, 0.0, Double.MAX_VALUE)

		chickenWingRingDurationTicks = builder
			.comment("The number of ticks that the Chicken Wing Ring will last.")
			.defineInRange("chickenWingRingDurationTicks", 20 * 10, 1, Int.MAX_VALUE)

		chickenWingRingRechargeTicks = builder
			.comment("The number of ticks that the Chicken Wing Ring will take to fully recharge.")
			.defineInRange("chickenWingRingRechargeTicks", 20 * 5, 1, Int.MAX_VALUE)

		builder.pop()

		builder.push("flying_squid_ring")

		flyingSquidRingThrustMultiplier = builder
			.comment("How many times stronger than your gravity the upward thrust of the Ring of the Flying Squid is.")
			.defineInRange("flyingSquidRingThrustMultiplier", 1.1, 0.0, Double.MAX_VALUE)

		flyingSquidRingMaxUpwardSpeed = builder
			.comment("The maximum upward speed in blocks per tick that the Ring of the Flying Squid will push you.")
			.defineInRange("flyingSquidRingMaxUpwardSpeed", 1.0, 0.0, Double.MAX_VALUE)

		flyingSquidRingDurationTicks = builder
			.comment("The number of ticks that the Ring of the Flying Squid will last.")
			.defineInRange("flyingSquidRingDurationTicks", 20 * 15, 1, Int.MAX_VALUE)

		flyingSquidRingRechargeTicks = builder
			.comment("The number of ticks that the Ring of the Flying Squid will take to fully recharge.")
			.defineInRange("flyingSquidRingRechargeTicks", 20 * 10, 1, Int.MAX_VALUE)

		builder.pop()

		builder.pop()
	}

	private fun enderQuarry() {
		builder.push("ender_quarry")

		enderQuarryFencePerimeterLimit = builder
			.comment("The maximum number of fence blocks that can be used to create the perimeter for an Ender Quarry.")
			.defineInRange("enderQuarryFencePerimeterLimit", 100_000, 4, Int.MAX_VALUE)

		enderQuarryMarkerSearchDistance = builder
			.comment("The maximum distance in blocks that the Ender Quarry will search for its markers.")
			.defineInRange("enderQuarryMarkerSearchDistance", 1_000, 1, Int.MAX_VALUE)

		builder.pop()
	}

	private fun wateringCan() {
		builder.push("watering_can")

		wateringCanRadius = builder
			.comment("The radius in blocks around the player that the Watering Can will effect.")
			.defineInRange("wateringCanRadius", 3, 1, Int.MAX_VALUE)

		wateringCanTickChance = builder
			.comment("The chance that each block in the radius will get ticked when the Watering Can is used.")
			.defineInRange("wateringCanTickChance", 0.3, 0.0, 1.0)

		wateringCanWaterUsagePerTick = builder
			.comment(
				"The amount of water in millibuckets that the Watering Can will use per tick.",
				"Setting this to 0 will make it not require water at all."
			)
			.defineInRange("wateringCanWaterUsagePerTick", 10, 0, Int.MAX_VALUE)

		reinforcedWateringCanRadius = builder
			.comment("The radius in blocks around the player that the Reinforced Watering Can will effect.")
			.defineInRange("reinforcedWateringCanRadius", 5, 1, Int.MAX_VALUE)

		reinforcedWateringCanTickChance = builder
			.comment("The chance that each block in the radius will get ticked when the Reinforced Watering Can is used.")
			.defineInRange("reinforcedWateringCanTickChance", 0.5, 0.0, 1.0)

		reinforcedWateringCanWaterUsagePerTick = builder
			.comment(
				"The amount of water in millibuckets that the Reinforced Watering Can will use per tick.",
				"Setting this to 0 will make it not require water at all."
			)
			.defineInRange("reinforcedWateringCanWaterUsagePerTick", 10, 0, Int.MAX_VALUE)

		builder.pop()
	}

	private fun feGenerators() {
		builder.push("fe_generators")

		culinaryFePerFoodValue = builder
			.comment("How much FE the Culinary Generator produces per point of food value in the input item.")
			.defineInRange("culinaryFePerFoodValue", 1.0, 0.0, Double.MAX_VALUE)

		culinaryTicksPerSaturationValue = builder
			.comment("How many ticks of burn time the Culinary Generator gets per point of saturation value in the input item.")
			.defineInRange("culinaryFePerSaturationValue", 1.0, 0.0, Double.MAX_VALUE)

		furnaceGeneratorBurnTimeMultiplier = builder
			.comment("How many times longer should a furnace fuel burn in the Furnace Generator compared to a regular furnace.")
			.defineInRange("furnaceGeneratorBurnTimeMultiplier", 2.5, 0.0, Double.MAX_VALUE)

		furnaceGeneratorFePerTick = builder
			.comment("How much FE per tick the Furnace Generator produces when active.")
			.defineInRange("furnaceGeneratorFePerTick", 40, 1, Int.MAX_VALUE)

		survivalistGeneratorBurnTimeMultiplier = builder
			.comment("How many times longer should a furnace fuel burn in the Survivalist Generator compared to a regular furnace.")
			.defineInRange("survivalistGeneratorBurnTimeMultiplier", 100.0, 0.0, Double.MAX_VALUE)

		survivalistGeneratorFePerTick = builder
			.comment("How much FE per tick the Survivalist Generator produces when active.")
			.defineInRange("survivalistGeneratorFePerTick", 5, 1, Int.MAX_VALUE)

		netherStarGeneratorEffectRadius = builder
			.comment("The radius in blocks that the Nether Star Generator applies the Withering II effect while active.")
			.defineInRange("netherStarGeneratorEffectRadius", 10.0, 0.0, Double.MAX_VALUE)

		deathGeneratorEffectRadius = builder
			.comment("The radius in blocks that the Death Generator applies the Doom effect while active.")
			.defineInRange("deathGeneratorEffectRadius", 3.0, 0.0, Double.MAX_VALUE)

		rainbowGeneratorFePerTick = builder
			.comment("How much FE per tick the Rainbow Generator produces when active.")
			.defineInRange("rainbowGeneratorFePerTick", 25_000_000, 1, Int.MAX_VALUE)

		builder.pop()
	}

	private fun gridPower() {
		builder.push("grid_power")

		builder.push("gp_generation")

		manualMillGenerationPerPlayer = builder
			.comment("The amount of GP the Manual Mill generates per player cranking it.")
			.defineInRange("manualMillGenerationPerPlayer", 15.0, 1.0, Double.MAX_VALUE)

		solarPanelGeneration = builder
			.comment("The amount of GP the Solar Panel generates when it can see the sun.")
			.defineInRange("solarPanelGeneration", 1.0, 1.0, Double.MAX_VALUE)

		lunarPanelGeneration = builder
			.comment("The amount of GP the Lunar Panel generates when it can see the moon.")
			.defineInRange("lunarPanelGeneration", 1.0, 1.0, Double.MAX_VALUE)

		lavaMillGeneration = builder
			.comment("The amount of GP the Lava Mill generates when Lava is adjacent.")
			.defineInRange("lavaMillGeneration", 2.0, 1.0, Double.MAX_VALUE)

		fireMillGeneration = builder
			.comment("The amount of GP the Fire Mill generates when Fire is below it.")
			.defineInRange("fireMillGeneration", 4.0, 1.0, Double.MAX_VALUE)

		waterMillGenerationPerSide = builder
			.comment("The amount of GP the Water Mill generates per side with flowing water adjacent.")
			.defineInRange("waterMillGenerationPerSide", 4.0, 1.0, Double.MAX_VALUE)

		dragonEggMillGeneration = builder
			.comment("The amount of GP the Dragon Egg Mill generates when a Dragon Egg is on top of it.")
			.defineInRange("dragonEggMillGeneration", 500.0, 1.0, Double.MAX_VALUE)

		creativeMillGeneration = builder
			.comment("The amount of GP the Creative Mill generates.")
			.defineInRange("creativeMillGeneration", 10_000.0, 1.0, Double.MAX_VALUE)

		builder.pop()

		builder.push("gp_usage")

		speedUpgradeGpCostMultiplier = builder
			.comment(
				"The multiplier applied to the GP cost of Speed Upgrades.",
				"Note that you can also change the GP cost formula directly using KubeJS.",
			)
			.defineInRange("speedUpgradeGpCostMultiplier", 1.0, 0.0, Double.MAX_VALUE)

		chickenWingRingGpCost = builder
			.comment("The amount of GP to be consumed while using a Chicken Wing Ring.")
			.defineInRange("chickenWingRingGpCost", 1.0, 0.0, Double.MAX_VALUE)

		flyingSquidRingGpCost = builder
			.comment("The amount of GP to be consumed while using a Flying Squid Ring.")
			.defineInRange("flyingSquidRingGpCost", 16.0, 0.0, Double.MAX_VALUE)

		angelRingGpCost = builder
			.comment("The amount of GP to be consumed while flying with an Angel Ring.")
			.defineInRange("angelRingGpCost", 64.0, 0.0, Double.MAX_VALUE)

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

		flatItemTransferNodeSpeed = builder
			.comment("The number of items per tick that Flat Item Transfer Nodes will transfer.")
			.defineInRange("flatItemTransferNodeSpeed", 64 / (20 * 2), 1, Int.MAX_VALUE)

		flatFluidTransferNodeSpeed = builder
			.comment("The amount of fluid in millibuckets per tick that Flat Fluid Transfer Nodes will transfer.")
			.defineInRange("flatFluidTransferNodeSpeed", 1000 / 20, 1, Int.MAX_VALUE)

		peacefulTableOnlyInPeaceful = builder
			.comment("Whether the Peaceful Table should only work when the difficulty is set to Peaceful.")
			.define("peacefulTableOnlyInPeaceful", true)

		peacefulTableChancePerTick = builder
			.comment("The chance per tick that the Peaceful Table will try to generate a drop.")
			.defineInRange("peacefulTableChancePerTick", 1.0 / 20 / 30, 0.0, 1.0)

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

		bedrockDrumCapacity = builder
			.comment("The fluid capacity of Bedrock Drums in millibuckets.")
			.defineInRange("bedrockDrumCapacity", 1_048_576_000, 1, Int.MAX_VALUE)

		builder.pop()
	}

	companion

	object {
		private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}