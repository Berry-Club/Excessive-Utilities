package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.attribute.BedRule
import net.minecraft.world.attribute.EnvironmentAttributeMap
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.clock.WorldClocks
import net.minecraft.world.level.CardinalLighting
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.timeline.Timeline
import net.neoforged.neoforge.common.world.NeoForgeEnvironmentAttributes
import java.util.Optional

object ModDimensionTypes {
	val THE_LAST_MILLENNIUM: ResourceKey<DimensionType> = rk("the_last_millennium")
	val THE_DEPTHS: ResourceKey<DimensionType> = rk(DepthsDimConstants.NAME)

	fun bootstrap(context: BootstrapContext<DimensionType>) {
		val clocks = context.lookup(Registries.WORLD_CLOCK)
		val timelines = HolderSet.empty<Timeline>()
		val lastMillenniumAttributes = EnvironmentAttributeMap.builder()
            .set(EnvironmentAttributes.SUN_ANGLE, 180f)
            .set(EnvironmentAttributes.MOON_ANGLE, 360f)
            .set(EnvironmentAttributes.STAR_ANGLE, 180f)
            .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.5f)
            .set(EnvironmentAttributes.SKY_LIGHT_LEVEL, 4f)
            .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0.24f)
			.set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
			.set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
			.set(NeoForgeEnvironmentAttributes.CUSTOM_SKYBOX, ExcessiveUtilities.modResource("the_last_millennium"))
			.build()
		val depthsAttributes = EnvironmentAttributeMap.builder()
            .set(EnvironmentAttributes.SUN_ANGLE, 180f)
            .set(EnvironmentAttributes.MOON_ANGLE, 360f)
            .set(EnvironmentAttributes.STAR_ANGLE, 180f)
            .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.5f)
            .set(EnvironmentAttributes.SKY_LIGHT_LEVEL, 4f)
            .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, 0.24f)
			.set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
			.set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, true)
			.build()

		context.register(
			THE_LAST_MILLENNIUM,
			DimensionType(
				true, true, false, false, 1.0,
				-64, 384, 319, BlockTags.INFINIBURN_OVERWORLD, 1f,
				DimensionType.MonsterSettings(ConstantInt.of(0), 0),
				DimensionType.Skybox.OVERWORLD, CardinalLighting.Type.DEFAULT,
				lastMillenniumAttributes, timelines, Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))
			)
		)

		context.register(
			THE_DEPTHS,
			DimensionType(
				true, false, true, false, 1.0,
				DepthsDimConstants.MIN_Y, DepthsDimConstants.HEIGHT, DepthsDimConstants.HEIGHT,
				BlockTags.INFINIBURN_OVERWORLD, 0f,
				DimensionType.MonsterSettings(ConstantInt.of(0), 0),
				DimensionType.Skybox.OVERWORLD, CardinalLighting.Type.DEFAULT,
				depthsAttributes, timelines, Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))
			)
		)
	}

	private fun rk(path: String): ResourceKey<DimensionType> =
		ResourceKey.create(Registries.DIMENSION_TYPE, ExcessiveUtilities.modResource(path))
}
