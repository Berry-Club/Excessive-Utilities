package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import net.minecraft.util.StringRepresentable
import java.util.function.DoubleSupplier
import java.util.function.IntSupplier

enum class FurnaceFuelGeneratorType(
	val id: String,
	private val burnTimeMultiplierGetter: DoubleSupplier,
	private val fePerTickGetter: IntSupplier
) : StringRepresentable {
	FURNACE(
		"furnace",
		ServerConfig.CONFIG.furnaceGeneratorBurnTimeMultiplier,
		ServerConfig.CONFIG.furnaceGeneratorFePerTick
	),
	SURVIVAL(
		"survival",
		ServerConfig.CONFIG.survivalistGeneratorBurnTimeMultiplier,
		ServerConfig.CONFIG.survivalistGeneratorFePerTick
	)


	;

	val burnTimeMultiplier: Double
		get() = burnTimeMultiplierGetter.asDouble

	val fePerTick: Int
		get() = fePerTickGetter.asInt

	override fun getSerializedName(): String = id

}