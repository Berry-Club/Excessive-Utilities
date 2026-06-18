package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.AncientCityStructurePieces
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.random.WeightedRandomList
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure
import java.util.*

object ModStructures {

	val ANCIENT_CITY = rk("ancient_city")

	fun bootstrap(context: BootstrapContext<Structure>) {
		val biomes = context.lookup(Registries.BIOME)
		val templatePools = context.lookup(Registries.TEMPLATE_POOL)

		context.register(
			ANCIENT_CITY,
			JigsawStructure(
				Structure.StructureSettings.Builder(HolderSet.direct(biomes.getOrThrow(ModBiomes.DEEP_DARK)))
					.spawnOverrides(emptySpawnOverrides())
					.generationStep(GenerationStep.Decoration.UNDERGROUND_DECORATION)
					.terrainAdapation(TerrainAdjustment.BEARD_BOX)
					.build(),
				templatePools.getOrThrow(AncientCityStructurePieces.START),
				Optional.of(ResourceLocation.withDefaultNamespace("city_anchor")),
				7,
				ConstantHeight.of(VerticalAnchor.absolute(DeepDarkConstants.FLOOR_TOP + CITY_ANCHOR_Y_OFFSET + FLOOR_CLEARANCE)),
				false,
				Optional.empty(),
				116,
				emptyList(),
				JigsawStructure.DEFAULT_DIMENSION_PADDING,
				JigsawStructure.DEFAULT_LIQUID_SETTINGS
			)
		)
	}

	private fun emptySpawnOverrides(): Map<MobCategory, StructureSpawnOverride> {
		return MobCategory.entries.associateWith {
			StructureSpawnOverride(
				StructureSpawnOverride.BoundingBoxType.STRUCTURE,
				WeightedRandomList.create()
			)
		}
	}

	private fun rk(name: String): ResourceKey<Structure> {
		return ResourceKey.create(Registries.STRUCTURE, ExcessiveUtilities.modResource(name))
	}

	private const val CITY_ANCHOR_Y_OFFSET = 24
	private const val FLOOR_CLEARANCE = 6

}
