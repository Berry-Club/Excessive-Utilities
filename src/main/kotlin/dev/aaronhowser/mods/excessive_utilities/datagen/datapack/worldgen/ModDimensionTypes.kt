package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.DimensionType
import java.util.*

object ModDimensionTypes {

	val THE_LAST_MILLENNIUM: ResourceKey<DimensionType> =
		rk("the_last_millennium")
	val DEEP_DARK: ResourceKey<DimensionType> =
		rk("deep_dark")

	fun bootstrap(context: BootstrapContext<DimensionType>) {
		context.register(
			THE_LAST_MILLENNIUM,
			DimensionType(
				OptionalLong.of(18000L),
				true,
				false,
				false,
				false,
				1.0,
				true,
				true,
				-64,
				384,
				319,
				BlockTags.INFINIBURN_OVERWORLD,
				ExcessiveUtilities.modResource("the_last_millennium"),
				1f,
				DimensionType.MonsterSettings(
					false,
					false,
					ConstantInt.of(0),
					0
				)
			)
		)

		context.register(
			DEEP_DARK,
			DimensionType(
				OptionalLong.of(18000L),
				false,
				true,
				false,
				false,
				1.0,
				false,
				true,
				DeepDarkConstants.MIN_Y,
				DeepDarkConstants.HEIGHT,
				319,
				BlockTags.INFINIBURN_OVERWORLD,
				BuiltinDimensionTypes.OVERWORLD_EFFECTS,
				0f,
				DimensionType.MonsterSettings(
					false,
					false,
					ConstantInt.of(0),
					0
				)
			)
		)
	}

	private fun rk(path: String): ResourceKey<DimensionType> {
		return ResourceKey.create(Registries.DIMENSION_TYPE, ExcessiveUtilities.modResource(path))
	}


}