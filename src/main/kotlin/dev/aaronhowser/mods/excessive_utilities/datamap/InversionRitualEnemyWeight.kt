package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.neoforged.neoforge.registries.datamaps.DataMapType

class InversionRitualEnemyWeight(
	val weight: Double
) {

	companion object {
		val CODEC: Codec<InversionRitualEnemyWeight> =
			Codec.DOUBLE.xmap(::InversionRitualEnemyWeight, InversionRitualEnemyWeight::weight)

		val DATA_MAP: DataMapType<EntityType<*>, InversionRitualEnemyWeight> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("inversion_ritual_enemy_weight"),
					Registries.ENTITY_TYPE,
					CODEC
				)
				.synced(CODEC, true)
				.build()

		fun getRandomType(random: RandomSource): EntityType<out Mob>? {
			val weights = getWeightedTypes()

			val totalWeight = weights.values.sum()
			var roll = random.nextDouble() * totalWeight

			for ((type, weight) in weights) {
				roll -= weight
				if (roll < 0.0) {
					return type
				}
			}

			return null
		}

		fun getWeightedTypes(): Map<EntityType<out Mob>, Double> {
			val weights = mutableMapOf<EntityType<out Mob>, Double>()

			val registry = BuiltInRegistries.ENTITY_TYPE
				.filterIsInstance<EntityType<out Mob>>()

			for (type in registry) {
				val weight = type.builtInRegistryHolder().getData(DATA_MAP)
				if (weight != null) {
					weights[type] = weight.weight
				}
			}

			return weights
		}
	}

}