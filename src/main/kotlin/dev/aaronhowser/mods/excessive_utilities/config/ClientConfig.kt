package dev.aaronhowser.mods.excessive_utilities.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(
	private val builder: ModConfigSpec.Builder
) {

	lateinit var generatorParticleDensity: ModConfigSpec.DoubleValue
	lateinit var soundMufflerRadius: ModConfigSpec.DoubleValue

	init {
		general()
	}

	private fun general() {

		generatorParticleDensity = builder
			.comment("The density of particles emitted by certain generators")
			.defineInRange("generatorParticleDensity", 1.0, 0.0, Double.MAX_VALUE)

		soundMufflerRadius = builder
			.comment("The radius in blocks that sound mufflers will affect")
			.defineInRange("soundMufflerRadius", 8.0, 0.0, Double.MAX_VALUE)

	}

	companion object {
		private val configPair: Pair<ClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ClientConfig)

		val CONFIG: ClientConfig = configPair.left
		val CONFIG_SPEC: ModConfigSpec = configPair.right
	}

}