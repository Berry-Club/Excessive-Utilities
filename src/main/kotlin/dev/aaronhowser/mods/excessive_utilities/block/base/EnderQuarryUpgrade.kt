package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import java.util.function.DoubleSupplier

enum class EnderQuarryUpgrade(
	val feMultiplierGetter: DoubleSupplier
) {
	NONE({ 1.0 }),
	SILK_TOUCH(ServerConfig.CONFIG.eqSilkTouchCostMultiplier),
	FORTUNE_ONE(ServerConfig.CONFIG.eqFortuneOneCostMultiplier),
	FORTUNE_TWO(ServerConfig.CONFIG.eqFortuneTwoCostMultiplier),
	FORTUNE_THREE(ServerConfig.CONFIG.eqFortuneThreeCostMultiplier),
	SPEED_ONE(ServerConfig.CONFIG.eqSpeedOneCostMultiplier),
	SPEED_TWO(ServerConfig.CONFIG.eqSpeedTwoCostMultiplier),
	SPEED_THREE(ServerConfig.CONFIG.eqSpeedThreeCostMultiplier),
	WORLD_HOLE(ServerConfig.CONFIG.eqWorldHoleCostMultiplier)

	;
}