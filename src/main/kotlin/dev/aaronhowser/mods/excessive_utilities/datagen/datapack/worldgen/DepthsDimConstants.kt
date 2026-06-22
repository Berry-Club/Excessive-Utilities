package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

object DepthsDimConstants {

	const val NAME = "the_depths"

	val LEVEL_KEY: ResourceKey<Level> =
		ResourceKey.create(Registries.DIMENSION, ExcessiveUtilities.modResource(NAME))

	const val MIN_Y = -64
	const val MAX_Y = 208
	const val HEIGHT = MAX_Y - MIN_Y

	const val FLOOR_TOP = 64
	const val CEILING_BOTTOM = 128
	const val BLEND_THICKNESS = 8

}