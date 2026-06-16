package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimplePreparableReloadListener
import net.minecraft.util.RandomSource
import net.minecraft.util.profiling.ProfilerFiller
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

		private var cachedWeights: Map<EntityType<out Mob>, Double>? = null
		fun getWeightedTypes(): Map<EntityType<out Mob>, Double> {
			if (cachedWeights == null) {
				resetCache()
			}

			return cachedWeights ?: emptyMap()
		}

		fun resetCache() {
			val weights = mutableMapOf<EntityType<out Mob>, Double>()

			val registry = BuiltInRegistries.ENTITY_TYPE
				.filterIsInstance<EntityType<out Mob>>()

			for (type in registry) {
				val weight = type.builtInRegistryHolder().getData(DATA_MAP)
				if (weight != null) {
					weights[type] = weight.weight
				}
			}

			cachedWeights = weights
		}
	}

	@Suppress("WRONG_TYPE_FOR_JAVA_OVERRIDE")
	class ReloadListener : SimplePreparableReloadListener<Any?>() {
		override fun prepare(resourceManager: ResourceManager, profiler: ProfilerFiller): Any? {
			return null
		}

		override fun apply(`object`: Any?, resourceManager: ResourceManager, profiler: ProfilerFiller) {
			resetCache()
		}
	}

}