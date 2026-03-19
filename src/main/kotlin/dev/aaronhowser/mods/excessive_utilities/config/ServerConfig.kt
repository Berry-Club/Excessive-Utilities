package dev.aaronhowser.mods.excessive_utilities.config

import dev.aaronhowser.mods.aaron.misc.AaronDsls.section
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var chandelierRadius: ModConfigSpec.IntValue
	lateinit var magnumTorchRadius: ModConfigSpec.IntValue
	lateinit var basicFilingCabinetCapacity: ModConfigSpec.IntValue
	lateinit var advancedFilingCabinetCapacity: ModConfigSpec.IntValue
	lateinit var flatItemTransferNodeSpeed: ModConfigSpec.IntValue
	lateinit var flatFluidTransferNodeSpeed: ModConfigSpec.IntValue
	lateinit var peacefulTableOnlyInPeaceful: ModConfigSpec.BooleanValue
	lateinit var peacefulTableChancePerTick: ModConfigSpec.DoubleValue
	lateinit var healthPerSoulFragment: ModConfigSpec.DoubleValue
	lateinit var soulFragmentResetOnDeath: ModConfigSpec.EnumValue<SoulDebt.OnDeathConfig>
	lateinit var healingAxeHealthTransferAmount: ModConfigSpec.DoubleValue
	lateinit var conveyorBeltSpeed: ModConfigSpec.DoubleValue
	lateinit var funnyEnderLilyTeleporting: ModConfigSpec.BooleanValue
	lateinit var enderPorcupineMarchTime: ModConfigSpec.IntValue

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

	lateinit var redCoalGpCost: ModConfigSpec.DoubleValue
	lateinit var redCoalBurnTimeMultiplier: ModConfigSpec.DoubleValue
	lateinit var speedUpgradeGpCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var chickenWingRingGpCost: ModConfigSpec.DoubleValue
	lateinit var flyingSquidRingGpCost: ModConfigSpec.DoubleValue
	lateinit var angelRingGpCost: ModConfigSpec.DoubleValue
	lateinit var wirelessFeTransmitterGpCostPerConnection: ModConfigSpec.DoubleValue

	lateinit var furnaceGeneratorBurnTimeMultiplier: ModConfigSpec.DoubleValue
	lateinit var furnaceGeneratorFePerTick: ModConfigSpec.IntValue
	lateinit var survivalistGeneratorBurnTimeMultiplier: ModConfigSpec.DoubleValue
	lateinit var survivalistGeneratorFePerTick: ModConfigSpec.IntValue
	lateinit var overclockedGeneratorFePerBurnTick: ModConfigSpec.IntValue
	lateinit var netherStarGeneratorEffectRadius: ModConfigSpec.DoubleValue
	lateinit var deathGeneratorEffectRadius: ModConfigSpec.DoubleValue
	lateinit var rainbowGeneratorFePerTick: ModConfigSpec.IntValue

	lateinit var isWateringCanBreakable: ModConfigSpec.BooleanValue
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

	lateinit var enderQuarryFePerBlock: ModConfigSpec.DoubleValue
	lateinit var enderQuarryBlocksPerTick: ModConfigSpec.DoubleValue

	lateinit var eqSpeedOneSpeedMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqSpeedTwoSpeedMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqSpeedThreeSpeedMultiplier: ModConfigSpec.DoubleValue

	lateinit var eqSilkTouchCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqFortuneOneCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqFortuneTwoCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqFortuneThreeCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqSpeedOneCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqSpeedTwoCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqSpeedThreeCostMultiplier: ModConfigSpec.DoubleValue
	lateinit var eqWorldHoleCostMultiplier: ModConfigSpec.DoubleValue

	lateinit var quantumQuarryFePerBlock: ModConfigSpec.DoubleValue
	lateinit var quantumQuarryBlocksPerTick: ModConfigSpec.DoubleValue

	lateinit var boomerangItemPickupRadius: ModConfigSpec.DoubleValue
	lateinit var boomereaperangRadius: ModConfigSpec.DoubleValue

	lateinit var cursedEarthPeriod: ModConfigSpec.IntValue
	lateinit var cursedEarthChance: ModConfigSpec.DoubleValue
	lateinit var cursedEarthMaxSpawnedMobs: ModConfigSpec.IntValue
	lateinit var cursedEarthCheckRadius: ModConfigSpec.DoubleValue

	lateinit var furnaceFePerTick: ModConfigSpec.IntValue
	lateinit var furnaceTicksPerRecipe: ModConfigSpec.IntValue

	lateinit var wirelessFeTransmitterRange: ModConfigSpec.IntValue
	lateinit var wirelessFeTransmitterRate: ModConfigSpec.IntValue

	init {
		general()

		machines()
		gridPower()
		wateringCan()
		rings()
	}

	private fun machines() {
		builder.section("machines") {
			feGenerators()

			builder.section("furnace") {
				furnaceFePerTick = builder
					.comment("The amount of FE the Furnace will burn per tick while active.")
					.defineInRange("furnaceFePerTick", 20, 0, Int.MAX_VALUE)

				furnaceTicksPerRecipe = builder
					.comment("The number of ticks it takes for the Furnace to complete a recipe.")
					.defineInRange("furnaceTicksPerRecipe", 20 * 5, 1, Int.MAX_VALUE)
			}

			builder.section("wireless_fe_transmitter") {
				wirelessFeTransmitterRange = builder
					.comment("The maximum distance in blocks that Wireless FE Transmitters will connect to blocks.")
					.defineInRange("wirelessFeTransmitterRange", 4, 1, Int.MAX_VALUE)

				wirelessFeTransmitterRate = builder
					.comment("The amount of FE per tick that Wireless FE Transmitters will transmit.")
					.defineInRange("wirelessFeTransmitterRate", 80, 1, Int.MAX_VALUE)
			}

			enderQuarry()
			quantumQuarry()
		}
	}

	private fun quantumQuarry() {
		builder.section("quantum_quarry") {
			quantumQuarryFePerBlock = builder
				.comment("The amount of FE the Quantum Quarry will use per block mined.")
				.defineInRange("quantumQuarryFePerBlock", 20_000.0, 0.0, Double.MAX_VALUE)

			quantumQuarryBlocksPerTick = builder
				.comment("The maximum number of blocks the Quantum Quarry will mine per tick.")
				.defineInRange("quantumQuarryBlocksPerTick", 2.0, 0.0, Double.MAX_VALUE)
		}
	}

	private fun enderQuarry() {
		builder.section("ender_quarry") {
			enderQuarryFencePerimeterLimit = builder
				.comment("The maximum number of fence blocks that can be used to create the perimeter for an Ender Quarry.")
				.defineInRange("enderQuarryFencePerimeterLimit", 100_000, 4, Int.MAX_VALUE)

			enderQuarryMarkerSearchDistance = builder
				.comment("The maximum distance in blocks that the Ender Quarry will search for its markers.")
				.defineInRange("enderQuarryMarkerSearchDistance", 1_000, 1, Int.MAX_VALUE)

			enderQuarryFePerBlock = builder
				.comment("The amount of FE the Ender Quarry will use per block mined.")
				.defineInRange("enderQuarryFePerBlock", 10.0, 0.0, Double.MAX_VALUE)

			enderQuarryBlocksPerTick = builder
				.comment("The maximum number of blocks the Ender Quarry will mine per tick.")
				.defineInRange("enderQuarryBlocksPerTick", 0.5, 0.0, Double.MAX_VALUE)

			builder.section("eq_upgrades") {
				eqSpeedOneSpeedMultiplier = builder
					.comment("How many times faster the Ender Quarry mines with a Speed I Upgrade.")
					.defineInRange("eqSpeedOneSpeedMultiplier", 1.5, 0.0, Double.MAX_VALUE)

				eqSpeedTwoSpeedMultiplier = builder
					.comment("How many times faster the Ender Quarry mines with a Speed II Upgrade.")
					.defineInRange("eqSpeedTwoSpeedMultiplier", 2.0, 0.0, Double.MAX_VALUE)

				eqSpeedThreeSpeedMultiplier = builder
					.comment("How many times faster the Ender Quarry mines with a Speed III Upgrade.")
					.defineInRange("eqSpeedThreeSpeedMultiplier", 3.0, 0.0, Double.MAX_VALUE)

				builder.section("eq_upgrade_costs") {
					eqSilkTouchCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Silk Touch Upgrade.")
						.defineInRange("eqSilkTouchCostMultiplier", 1.5, 0.0, Double.MAX_VALUE)

					eqFortuneOneCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Fortune I Upgrade.")
						.defineInRange("eqFortuneOneCostMultiplier", 5.0, 0.0, Double.MAX_VALUE)

					eqFortuneTwoCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Fortune II Upgrade.")
						.defineInRange("eqFortuneTwoCostMultiplier", 20.0, 0.0, Double.MAX_VALUE)

					eqFortuneThreeCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Fortune III Upgrade.")
						.defineInRange("eqFortuneThreeCostMultiplier", 80.0, 0.0, Double.MAX_VALUE)

					eqSpeedOneCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Speed I Upgrade.")
						.defineInRange("eqSpeedOneCostMultiplier", 1.0, 0.0, Double.MAX_VALUE)

					eqSpeedTwoCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Speed II Upgrade.")
						.defineInRange("eqSpeedTwoCostMultiplier", 1.5, 0.0, Double.MAX_VALUE)

					eqSpeedThreeCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a Speed III Upgrade.")
						.defineInRange("eqSpeedThreeCostMultiplier", 2.0, 0.0, Double.MAX_VALUE)

					eqWorldHoleCostMultiplier = builder
						.comment("How many times more FE the Ender Quarry will drain when it has a World Hole Upgrade.")
						.defineInRange("eqWorldHoleCostMultiplier", 1.0, 0.0, Double.MAX_VALUE)
				}
			}
		}
	}

	private fun rings() {
		builder.section("rings") {
			builder.comment("GP costs are defined elsewhere!")

			builder.section("chicken_wing_ring") {
				chickenWingRingFallSpeed = builder
					.comment("The max fall speed when using a Chicken Wing Ring.")
					.defineInRange("chickenWingRingFallSpeed", 0.1, 0.0, Double.MAX_VALUE)

				chickenWingRingDurationTicks = builder
					.comment("The number of ticks that the Chicken Wing Ring will last.")
					.defineInRange("chickenWingRingDurationTicks", 20 * 10, 1, Int.MAX_VALUE)

				chickenWingRingRechargeTicks = builder
					.comment("The number of ticks that the Chicken Wing Ring will take to fully recharge.")
					.defineInRange("chickenWingRingRechargeTicks", 20 * 5, 1, Int.MAX_VALUE)
			}

			builder.section("flying_squid_ring") {
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
			}
		}
	}

	private fun wateringCan() {
		builder.section("watering_can") {
			isWateringCanBreakable = builder
				.comment("Whether the Watering Can should be broken when used by a fake player.")
				.define("isWateringCanBreakable", true)

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
		}
	}

	private fun feGenerators() {
		builder.section("fe_generators") {
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

			overclockedGeneratorFePerBurnTick = builder
				.comment("How much FE the Overclocked Generator burns for each tick tha the item would have burned in a Furnace.")
				.defineInRange("overclockedGeneratorFePerBurnTick", 40, 1, Int.MAX_VALUE)

			netherStarGeneratorEffectRadius = builder
				.comment("The radius in blocks that the Nether Star Generator applies the Withering II effect while active.")
				.defineInRange("netherStarGeneratorEffectRadius", 10.0, 0.0, Double.MAX_VALUE)

			deathGeneratorEffectRadius = builder
				.comment("The radius in blocks that the Death Generator applies the Doom effect while active.")
				.defineInRange("deathGeneratorEffectRadius", 3.0, 0.0, Double.MAX_VALUE)

			rainbowGeneratorFePerTick = builder
				.comment("How much FE per tick the Rainbow Generator produces when active.")
				.defineInRange("rainbowGeneratorFePerTick", 25_000_000, 1, Int.MAX_VALUE)
		}
	}

	private fun gridPower() {
		builder.section("grid_power") {
			builder.section("gp_generation") {
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
			}

			builder.section("gp_usage") {
				redCoalGpCost = builder
					.comment("The amount of GP to be occupied while a piece of Red Coal is burning in a Furnace.")
					.defineInRange("redCoalGpCost", 4.0, 0.0, Double.MAX_VALUE)

				redCoalBurnTimeMultiplier = builder
					.comment("How many times longer should a piece of Red Coal burn in a Furnace compared to regular coal.")
					.defineInRange("redCoalBurnTimeMultiplier", 8.0, 0.0, Double.MAX_VALUE)

				speedUpgradeGpCostMultiplier = builder
					.comment(
						"The multiplier applied to the GP cost of Speed Upgrades.",
						"Note that you can also change the GP cost formula directly using KubeJS.",
					)
					.defineInRange("speedUpgradeGpCostMultiplier", 1.0, 0.0, Double.MAX_VALUE)

				chickenWingRingGpCost = builder
					.comment("The amount of GP to be occupied while using a Chicken Wing Ring.")
					.defineInRange("chickenWingRingGpCost", 1.0, 0.0, Double.MAX_VALUE)

				flyingSquidRingGpCost = builder
					.comment("The amount of GP to be occupied while using a Flying Squid Ring.")
					.defineInRange("flyingSquidRingGpCost", 16.0, 0.0, Double.MAX_VALUE)

				angelRingGpCost = builder
					.comment("The amount of GP to be occupied while flying with an Angel Ring.")
					.defineInRange("angelRingGpCost", 64.0, 0.0, Double.MAX_VALUE)

				wirelessFeTransmitterGpCostPerConnection = builder
					.comment("The amount of GP to be occupied per connection while using a Wireless FE Transmitter.")
					.defineInRange("wirelessFeTransmitterGpCostPerConnection", 1.0, 0.0, Double.MAX_VALUE)
			}
		}
	}

	private fun general() {
		chandelierRadius = builder
			.comment("The radius in blocks that a Chandelier prevents monster spawns.")
			.defineInRange("chandelierRadius", 16, 1, Int.MAX_VALUE)

		magnumTorchRadius = builder
			.comment("The radius in blocks that a Magnum Torch prevents monster spawns.")
			.defineInRange("magnumTorchRadius", 64, 1, Int.MAX_VALUE)

		healingAxeHealthTransferAmount = builder
			.comment("The amount of health that the Healing Axe will transfer between you and the entity you hit.")
			.defineInRange("healingAxeHealthTransferAmount", 4.0, 0.0, Double.MAX_VALUE)

		conveyorBeltSpeed = builder
			.comment("The speed in blocks per tick that Conveyor Belts will move entities.")
			.defineInRange("conveyorBeltSpeed", 0.1, 0.0, Double.MAX_VALUE)

		funnyEnderLilyTeleporting = builder
			.comment("Whether Ender Lilies should teleport away when they get wet.")
			.define("funnyEnderLilyTeleporting", true)

		enderPorcupineMarchTime = builder
			.comment("The number of ticks that the Ender Porcupine will wait at each position before moving to the next one")
			.defineInRange("enderPorcupineMarchTime", 20, 1, Int.MAX_VALUE)

		builder.section("filing_cabinets") {
			basicFilingCabinetCapacity = builder
				.comment("The maximum number of items that a Basic Filing Cabinet can hold.")
				.defineInRange("basicFilingCabinetCapacity", 270, 1, Int.MAX_VALUE)

			advancedFilingCabinetCapacity = builder
				.comment("The maximum number of items that an Advanced Filing Cabinet can hold.")
				.defineInRange("advancedFilingCabinetCapacity", 540, 1, Int.MAX_VALUE)
		}

		builder.section("flat_transfer_nodes") {
			flatItemTransferNodeSpeed = builder
				.comment("The number of items per tick that Flat Item Transfer Nodes will transfer.")
				.defineInRange("flatItemTransferNodeSpeed", 64 / (20 * 2), 1, Int.MAX_VALUE)

			flatFluidTransferNodeSpeed = builder
				.comment("The amount of fluid in millibuckets per tick that Flat Fluid Transfer Nodes will transfer.")
				.defineInRange("flatFluidTransferNodeSpeed", 1000 / 20, 1, Int.MAX_VALUE)
		}

		builder.section("peaceful_table") {
			peacefulTableOnlyInPeaceful = builder
				.comment("Whether the Peaceful Table should only work when the difficulty is set to Peaceful.")
				.define("peacefulTableOnlyInPeaceful", true)

			peacefulTableChancePerTick = builder
				.comment("The chance per tick that the Peaceful Table will try to generate a drop.")
				.defineInRange("peacefulTableChancePerTick", 1.0 / 20 / 30, 0.0, 1.0)
		}

		builder.section("cursed_earth") {
			cursedEarthPeriod = builder
				.comment("The number of ticks between each time that Cursed Earth blocks will do work.")
				.defineInRange("cursedEarthPeriod", 8, 1, Int.MAX_VALUE)

			cursedEarthChance = builder
				.comment("The chance per tick that a Cursed Earth block will try to spawn a mob.")
				.defineInRange("cursedEarthChance", 1.0 / 40, 0.0, 1.0)

			cursedEarthMaxSpawnedMobs = builder
				.comment("The maximum number of mobs around a Cursed Earth before it stops trying to spawn more.")
				.defineInRange("cursedEarthMaxSpawnedMobs", 10, 1, Int.MAX_VALUE)

			cursedEarthCheckRadius = builder
				.comment("The radius in blocks around a Cursed Earth block that it checks for nearby mobs before trying to spawn more.")
				.defineInRange("cursedEarthCheckRadius", 8.0, 0.0, Double.MAX_VALUE)
		}

		builder.section("heating_coil") {
			heatingCoilBurnTime = builder
				.comment("The number of ticks a Heating Coil will burn in a Furnace.")
				.defineInRange("heatingCoilBurnTime", 20, 1, Int.MAX_VALUE)

			heatingCoilBurnCost = builder
				.comment("How much FE will be spent when a Heating Coil is used as Furnace fuel.")
				.defineInRange("heatingCoilBurnCost", 50, 1, Int.MAX_VALUE)

			heatingCoilMaxEnergy = builder
				.comment("The maximum amount of FE a Heating Coil can store.")
				.defineInRange("heatingCoilMaxEnergy", 1_500_000, 1, Int.MAX_VALUE)
		}

		builder.section("drums") {
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
		}

		builder.section("boomerang") {
			boomerangItemPickupRadius = builder
				.comment("The radius in blocks around the Boomerang that it will pick up items.")
				.defineInRange("boomerangItemPickupRadius", 4.0, 0.0, Double.MAX_VALUE)

			boomereaperangRadius = builder
				.comment("The radius in blocks around the Boomerang that it will break plants if it has the Boomereaperang enchantment.")
				.defineInRange("boomereaperangRadius", 4.0, 0.0, Double.MAX_VALUE)
		}

		builder.section("soul_fragments") {
			healthPerSoulFragment = builder
				.comment("The amount of health that each Soul Fragment is worth.")
				.defineInRange("healthPerSoulFragment", 2.0, 0.0, Double.MAX_VALUE)

			soulFragmentResetOnDeath = builder
				.comment(
					"What should happen to your Soul Debt/Surplus when you die.",
					"KEEP: You keep all of your Soul Debt/Surplus",
					"RESET: You reset to having 0 Soul Debt/Surplus",
					"REMOVE_NEGATIVE: You keep Soul Surplus, but reset Soul Debt to 0"
				)
				.defineEnum("soulFragmentResetOnDeath", SoulDebt.OnDeathConfig.KEEP)
		}
	}

	companion object {
		private val configPair: Pair<ServerConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ServerConfig)

		val CONFIG: ServerConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}