package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import net.neoforged.neoforge.common.ModConfigSpec

object ModConfigLang {

	fun add(provider: ModLanguageProvider) {
		fun addConfig(name: String, value: String) {
			provider.add("excessive_utilities.configuration.$name", value)
		}

		fun addConfig(config: ModConfigSpec.ConfigValue<*>, value: String) {
			addConfig(config.path.last(), value)
		}

		addConfig("general", "General")
		addConfig(ServerConfig.CONFIG.chandelierRadius, "Chandelier Radius")
		addConfig(ServerConfig.CONFIG.magnumTorchRadius, "Magnum Torch Radius")
		addConfig(ServerConfig.CONFIG.soundMufflerRadius, "Sound Muffler Radius")
		addConfig(ServerConfig.CONFIG.conveyorBeltSpeed, "Conveyor Belt Speed")
		addConfig(ServerConfig.CONFIG.funnyEnderLilyTeleporting, "Funny Ender Lily Teleporting")
		addConfig(ServerConfig.CONFIG.enderPorcupineMarchTime, "Ender Porcupine March Time (ticks)")

		addConfig("healing_axe", "Healing Axe")
		addConfig(ServerConfig.CONFIG.healingAxeChancePerTick, "Healing Axe Chance per Tick")
		addConfig(ServerConfig.CONFIG.healingAxeFoodAmount, "Healing Axe Food Amount")
		addConfig(ServerConfig.CONFIG.healingAxeSaturationAmount, "Healing Axe Saturation Amount")
		addConfig(ServerConfig.CONFIG.healingAxeHealthTransferAmount, "Healing Axe Health Transfer Amount")

		addConfig("filing_cabinets", "Filing Cabinets")
		addConfig(ServerConfig.CONFIG.basicFilingCabinetCapacity, "Basic Filing Cabinet Capacity")
		addConfig(ServerConfig.CONFIG.advancedFilingCabinetCapacity, "Advanced Filing Cabinet Capacity")

		addConfig("flat_transfer_nodes", "Flat Transfer Nodes")
		addConfig(ServerConfig.CONFIG.flatItemTransferNodeSpeed, "Flat Item Transfer Node Speed (items/tick)")
		addConfig(ServerConfig.CONFIG.flatFluidTransferNodeSpeed, "Flat Fluid Transfer Node Speed (mb/tick)")

		addConfig("peaceful_table", "Peaceful Table")
		addConfig(ServerConfig.CONFIG.peacefulTableOnlyInPeaceful, "Peaceful Table Only in Peaceful")
		addConfig(ServerConfig.CONFIG.peacefulTableChancePerTick, "Peaceful Table Chance Per Tick")

		addConfig("cursed_earth", "Cursed Earth")
		addConfig(ServerConfig.CONFIG.cursedEarthBurnInDay, "Cursed Earth Burns in Daylight")
		addConfig(ServerConfig.CONFIG.cursedEarthMaxSpawnedMobs, "Cursed Earth Max Spawned Mobs")
		addConfig(ServerConfig.CONFIG.cursedEarthCheckRadius, "Cursed Earth Check Radius")
		addConfig(ServerConfig.CONFIG.cursedEarthBonusStrength, "Cursed Earth Bonus Strength")
		addConfig(ServerConfig.CONFIG.cursedEarthBonusSpeed, "Cursed Earth Bonus Speed")
		addConfig("cursedEarthChance", "Cursed Earth Chance per Check")
		addConfig("cursedEarthPeriod", "Cursed Earth Period (ticks)")

		addConfig("inversion_ritual", "Inversion Ritual")
		addConfig(ServerConfig.CONFIG.inversionRitualInterval, "Inversion Ritual Interval (ticks)")
		addConfig(ServerConfig.CONFIG.inversionRitualSpawnsPer, "Inversion Ritual Spawns per Wave")
		addConfig(ServerConfig.CONFIG.inversionRitualMaxSpawnedMonsters, "Inversion Ritual Max Spawned Monsters")
		addConfig(ServerConfig.CONFIG.inversionRitualKillsRequired, "Inversion Ritual Kills Required")

		addConfig("the_depths", "The Depths")
		addConfig(ServerConfig.CONFIG.deepDarkDamageAmount, "Deep Dark Damage Amount")
		addConfig(ServerConfig.CONFIG.deepDarkDamageInterval, "Deep Dark Damage Interval (ticks)")
		addConfig(ServerConfig.CONFIG.deepDarkSafeLightLevel, "Deep Dark Safe Light Level")

		addConfig("heating_coil", "Heating Coil")
		addConfig(ServerConfig.CONFIG.heatingCoilBurnTime, "Heating Coil Burn Time")
		addConfig(ServerConfig.CONFIG.heatingCoilBurnCost, "Heating Coil Burn Cost")
		addConfig(ServerConfig.CONFIG.heatingCoilMaxEnergy, "Heating Coil Max Energy")

		addConfig("drums", "Drums")
		addConfig(ServerConfig.CONFIG.stoneDrumCapacity, "Stone Drum Capacity")
		addConfig(ServerConfig.CONFIG.ironDrumCapacity, "Iron Drum Capacity")
		addConfig(ServerConfig.CONFIG.reinforcedLargeDrumCapacity, "Reinforced Large Drum Capacity")
		addConfig(ServerConfig.CONFIG.demonicallyGargantuanDrumCapacity, "Demonically Gargantuan Drum Capacity")
		addConfig(ServerConfig.CONFIG.bedrockDrumCapacity, "Bedrockium Drum Capacity")

		addConfig("boomerang", "Boomerang")
		addConfig(ServerConfig.CONFIG.boomerangItemPickupRadius, "Boomerang Item Pickup Radius")
		addConfig(ServerConfig.CONFIG.boomereaperangRadius, "Boomereaperang Radius")

		addConfig("soul_fragments", "Soul Fragments")
		addConfig(ServerConfig.CONFIG.healthPerSoulFragment, "Health per Soul Fragment")
		addConfig(ServerConfig.CONFIG.soulFragmentResetOnDeath, "Soul Fragment Reset on Death")

		addConfig("machines", "Machines")
		addConfig("fe_generators", "FE Generators")
		addConfig(ServerConfig.CONFIG.furnaceGeneratorBurnTimeMultiplier, "Furnace Generator Burn Time Multiplier")
		addConfig(ServerConfig.CONFIG.furnaceGeneratorFePerTick, "Furnace Generator FE/tick")
		addConfig(ServerConfig.CONFIG.survivalistGeneratorBurnTimeMultiplier, "Survivalist Generator Burn Time Multiplier")
		addConfig(ServerConfig.CONFIG.survivalistGeneratorFePerTick, "Survivalist Generator FE/tick")
		addConfig(ServerConfig.CONFIG.overclockedGeneratorFePerBurnTick, "Overclocked Generator FE per Burn Tick")
		addConfig(ServerConfig.CONFIG.netherStarGeneratorEffectRadius, "Nether Star Generator Effect Radius")
		addConfig(ServerConfig.CONFIG.deathGeneratorEffectRadius, "Death Generator Effect Radius")
		addConfig(ServerConfig.CONFIG.rainbowGeneratorFePerTick, "Rainbow Generator FE/tick")

		addConfig("furnace", "Furnace")
		addConfig(ServerConfig.CONFIG.furnaceFePerTick, "Furnace FE/tick")
		addConfig(ServerConfig.CONFIG.furnaceTicksPerRecipe, "Furnace Ticks per Recipe")

		addConfig("wireless_fe_transmitter", "Wireless FE Transmitter")
		addConfig(ServerConfig.CONFIG.wirelessFeTransmitterRange, "Wireless FE Transmitter Range")
		addConfig(ServerConfig.CONFIG.wirelessFeTransmitterRate, "Wireless FE Transmitter Rate (FE/tick)")

		addConfig(ServerConfig.CONFIG.qedRadius, "QED Radius")

		addConfig("ender_quarry", "Ender Quarry")
		addConfig(ServerConfig.CONFIG.enderQuarryFencePerimeterLimit, "Ender Quarry Fence Perimeter Limit")
		addConfig(ServerConfig.CONFIG.enderQuarryMarkerSearchDistance, "Ender Quarry Marker Search Distance")
		addConfig(ServerConfig.CONFIG.enderQuarryFePerBlock, "Ender Quarry FE/block")
		addConfig(ServerConfig.CONFIG.enderQuarryBlocksPerTick, "Ender Quarry Blocks/tick")

		addConfig("eq_upgrades", "Ender Quarry Upgrades")
		addConfig(ServerConfig.CONFIG.eqSpeedOneSpeedMultiplier, "Ender Quarry Speed One Speed Multiplier")
		addConfig(ServerConfig.CONFIG.eqSpeedTwoSpeedMultiplier, "Ender Quarry Speed Two Speed Multiplier")
		addConfig(ServerConfig.CONFIG.eqSpeedThreeSpeedMultiplier, "Ender Quarry Speed Three Speed Multiplier")

		addConfig("eq_upgrade_costs", "Ender Quarry Upgrade Costs")
		addConfig(ServerConfig.CONFIG.eqSilkTouchCostMultiplier, "Ender Quarry Silk Touch Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqFortuneOneCostMultiplier, "Ender Quarry Fortune One Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqFortuneTwoCostMultiplier, "Ender Quarry Fortune Two Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqFortuneThreeCostMultiplier, "Ender Quarry Fortune Three Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqSpeedOneCostMultiplier, "Ender Quarry Speed One Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqSpeedTwoCostMultiplier, "Ender Quarry Speed Two Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqSpeedThreeCostMultiplier, "Ender Quarry Speed Three Cost Multiplier")
		addConfig(ServerConfig.CONFIG.eqWorldHoleCostMultiplier, "Ender Quarry World Hole Cost Multiplier")

		addConfig("quantum_quarry", "Quantum Quarry")
		addConfig(ServerConfig.CONFIG.quantumQuarryFePerBlock, "Quantum Quarry FE/block")
		addConfig(ServerConfig.CONFIG.quantumQuarryBlocksPerTick, "Quantum Quarry Blocks/tick")

		addConfig("grid_power", "Grid Power")
		addConfig("gp_generation", "GP Generation")
		addConfig(ServerConfig.CONFIG.manualMillGenerationPerPlayer, "Manual Mill Generation per Player")
		addConfig(ServerConfig.CONFIG.solarPanelGeneration, "Solar Panel Generation")
		addConfig(ServerConfig.CONFIG.lunarPanelGeneration, "Lunar Panel Generation")
		addConfig(ServerConfig.CONFIG.lavaMillGeneration, "Lava Mill Generation")
		addConfig(ServerConfig.CONFIG.fireMillGeneration, "Fire Mill Generation")
		addConfig(ServerConfig.CONFIG.waterMillGenerationPerSide, "Water Mill Generation per Side")
		addConfig(ServerConfig.CONFIG.dragonEggMillGeneration, "Dragon Egg Mill Generation")
		addConfig(ServerConfig.CONFIG.creativeMillGeneration, "Creative Mill Generation")

		addConfig("gp_usage", "GP Usage")
		addConfig(ServerConfig.CONFIG.redCoalGpCost, "Red Coal GP Cost")
		addConfig(ServerConfig.CONFIG.redCoalBurnTimeMultiplier, "Red Coal Burn Time Multiplier")
		addConfig(ServerConfig.CONFIG.speedUpgradeGpCostMultiplier, "Speed Upgrade GP Cost Multiplier")
		addConfig(ServerConfig.CONFIG.chickenWingRingGpCost, "Chicken Wing Ring GP Cost")
		addConfig(ServerConfig.CONFIG.flyingSquidRingGpCost, "Flying Squid Ring GP Cost")
		addConfig(ServerConfig.CONFIG.angelRingGpCost, "Angel Ring GP Cost")
		addConfig(ServerConfig.CONFIG.wirelessFeTransmitterGpCostPerConnection, "Wireless FE Transmitter GP Cost per Connection")

		addConfig("watering_can", "Watering Can")
		addConfig(ServerConfig.CONFIG.isWateringCanBreakable, "Is Watering Can Breakable")
		addConfig(ServerConfig.CONFIG.wateringCanRadius, "Watering Can Radius")
		addConfig(ServerConfig.CONFIG.wateringCanTickChance, "Watering Can Chance per Tick")
		addConfig(ServerConfig.CONFIG.wateringCanWaterUsagePerTick, "Watering Can Water Usage per Tick")
		addConfig(ServerConfig.CONFIG.reinforcedWateringCanRadius, "Reinforced Watering Can Radius")
		addConfig(ServerConfig.CONFIG.reinforcedWateringCanTickChance, "Reinforced Watering Can Chance per Tick")
		addConfig(ServerConfig.CONFIG.reinforcedWateringCanWaterUsagePerTick, "Reinforced Watering Can Water Usage per Tick")

		addConfig("rings", "Rings")
		addConfig("chicken_wing_ring", "Chicken Wing Ring")
		addConfig(ServerConfig.CONFIG.chickenWingRingFallSpeed, "Chicken Wing Ring Fall Speed")
		addConfig(ServerConfig.CONFIG.chickenWingRingDurationTicks, "Chicken Wing Ring Duration (ticks)")
		addConfig(ServerConfig.CONFIG.chickenWingRingRechargeTicks, "Chicken Wing Ring Recharge Time (ticks)")

		addConfig("flying_squid_ring", "Flying Squid Ring")
		addConfig(ServerConfig.CONFIG.flyingSquidRingThrustMultiplier, "Flying Squid Ring Thrust Multiplier")
		addConfig(ServerConfig.CONFIG.flyingSquidRingMaxUpwardSpeed, "Flying Squid Ring Max Upward Speed")
		addConfig(ServerConfig.CONFIG.flyingSquidRingDurationTicks, "Flying Squid Ring Duration (ticks)")
		addConfig(ServerConfig.CONFIG.flyingSquidRingRechargeTicks, "Flying Squid Ring Recharge Time (ticks)")

		addConfig(ClientConfig.CONFIG.generatorParticleDensity, "Generator Particle Density")
		addConfig(ClientConfig.CONFIG.athenaTooltip, "Athena Tooltip")
		addConfig(ClientConfig.CONFIG.disableVomit, "Disable Vomit Particles")

		addConfig("rainbow_generator", "Rainbow Generator")
		addConfig(ClientConfig.CONFIG.rainbowGeneratorTimeFactor, "Rainbow Generator Time Factor")
		addConfig(ClientConfig.CONFIG.rainbowGeneratorRayWidth, "Rainbow Generator Ray Width")
		addConfig(ClientConfig.CONFIG.rainbowGeneratorRayLength, "Rainbow Generator Ray Length")

		addConfig("ender_porcupine", "Ender Porcupine")
		addConfig(ClientConfig.CONFIG.enderPorcupineSearchVolumeColor, "Ender Porcupine Search Volume Color")
		addConfig(ClientConfig.CONFIG.enderPorcupineCurrentTargetColor, "Ender Porcupine Current Target Color")

		addConfig("tesseract", "Tesseract")
		addConfig(ClientConfig.CONFIG.tesseractSpeed, "Tesseract Speed")
		addConfig(ClientConfig.CONFIG.tesseractInnerColor, "Tesseract Inner Color")
		addConfig(ClientConfig.CONFIG.tesseractOuterColor, "Tesseract Outer Color")
	}

}
