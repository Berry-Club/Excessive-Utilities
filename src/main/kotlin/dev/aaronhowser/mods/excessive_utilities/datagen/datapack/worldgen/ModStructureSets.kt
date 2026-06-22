package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.feature.DepthsAncientCityPlacement
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.structure.StructureSet
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType

object ModStructureSets {

	val ANCIENT_CITIES = rk("ancient_cities")

	fun bootstrap(context: BootstrapContext<StructureSet>) {
		val structures = context.lookup(Registries.STRUCTURE)

		context.register(
			ANCIENT_CITIES,
			StructureSet(
				structures.getOrThrow(ModStructures.ANCIENT_CITY),
				RandomSpreadStructurePlacement(
					DepthsAncientCityPlacement.SPACING,
					DepthsAncientCityPlacement.SEPARATION,
					RandomSpreadType.LINEAR,
					DepthsAncientCityPlacement.SALT
				)
			)
		)
	}

	private fun rk(name: String): ResourceKey<StructureSet> {
		return ResourceKey.create(Registries.STRUCTURE_SET, ExcessiveUtilities.modResource(name))
	}

}
