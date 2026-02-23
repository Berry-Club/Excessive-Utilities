package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import net.minecraft.util.StringRepresentable
import java.util.function.DoubleSupplier

enum class EnderQuarryUpgrade(
	val id: String,
	val feMultiplierGetter: DoubleSupplier
) : StringRepresentable {
	NONE("NONE", { 1.0 }),
	SILK_TOUCH("SILK_TOUCH", ServerConfig.CONFIG.eqSilkTouchCostMultiplier),
	FORTUNE_ONE("FORTUNE_ONE", ServerConfig.CONFIG.eqFortuneOneCostMultiplier),
	FORTUNE_TWO("FORTUNE_TWO", ServerConfig.CONFIG.eqFortuneTwoCostMultiplier),
	FORTUNE_THREE("FORTUNE_THREE", ServerConfig.CONFIG.eqFortuneThreeCostMultiplier),
	SPEED_ONE("SPEED_ONE", ServerConfig.CONFIG.eqSpeedOneCostMultiplier),
	SPEED_TWO("SPEED_TWO", ServerConfig.CONFIG.eqSpeedTwoCostMultiplier),
	SPEED_THREE("SPEED_THREE", ServerConfig.CONFIG.eqSpeedThreeCostMultiplier),
	WORLD_HOLE("WORLD_HOLE", ServerConfig.CONFIG.eqWorldHoleCostMultiplier)

	;

	override fun getSerializedName(): String = id
}