package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.UniformFloat
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver
import net.minecraft.world.level.levelgen.carver.WorldCarver
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight

object ModConfiguredCarvers {

	val DEPTHS_CAVE = rk("depths_cave")

	fun bootstrap(context: BootstrapContext<ConfiguredWorldCarver<*>>) {
		val blocks = context.lookup(Registries.BLOCK)

		context.register(
			DEPTHS_CAVE,
			WorldCarver.CAVE.configured(
				CaveCarverConfiguration(
					0.08f,
					UniformHeight.of(
						VerticalAnchor.absolute(DepthsDimConstants.MIN_Y + 8),
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP - 8)
					),
					UniformFloat.of(0.1f, 0.75f),
					VerticalAnchor.absolute(DepthsDimConstants.MIN_Y + 8),
					CarverDebugSettings.DEFAULT,
					blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
					UniformFloat.of(0.65f, 1.0f),
					UniformFloat.of(0.65f, 1.0f),
					UniformFloat.of(-1.0f, -0.7f)
				)
			)
		)
	}

	private fun rk(path: String): ResourceKey<ConfiguredWorldCarver<*>> {
		return ResourceKey.create(Registries.CONFIGURED_CARVER, ExcessiveUtilities.modResource(path))
	}

}