package dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModBiomeSources
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.QuartPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Climate
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

class QuantumQuarryBiomeSource(
	val dimension: QuantumQuarryDimension,
	private val allowedBiomes: HolderSet<Biome>,
	private val fallbackBiome: Holder<Biome>
) : BiomeSource() {

	override fun codec(): MapCodec<out BiomeSource> = ModBiomeSources.QUANTUM_QUARRY.get()

	override fun collectPossibleBiomes(): Stream<Holder<Biome>> = allowedBiomes.stream()

	override fun getNoiseBiome(x: Int, y: Int, z: Int, sampler: Climate.Sampler): Holder<Biome> {
		val chunkPosLong = ChunkPos.asLong(x shr 2, z shr 2)
		val assignment = assignments.getValue(dimension)[chunkPosLong]

		return assignment?.biome ?: fallbackBiome
	}

	companion object {
		val CODEC: MapCodec<QuantumQuarryBiomeSource> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					QuantumQuarryDimension.CODEC
						.fieldOf("dimension")
						.forGetter(QuantumQuarryBiomeSource::dimension),
					Biome.LIST_CODEC
						.fieldOf("biomes")
						.forGetter(QuantumQuarryBiomeSource::allowedBiomes),
					Biome.CODEC
						.fieldOf("fallback_biome")
						.forGetter(QuantumQuarryBiomeSource::fallbackBiome)
				).apply(instance, ::QuantumQuarryBiomeSource)
			}

		private val assignments: EnumMap<QuantumQuarryDimension, ConcurrentHashMap<Long, Assignment>> =
			EnumMap(QuantumQuarryDimension::class.java)

		init {
			for (dimension in QuantumQuarryDimension.entries) {
				assignments[dimension] = ConcurrentHashMap()
			}
		}

		fun claim(
			dimension: QuantumQuarryDimension,
			chunkPos: ChunkPos,
			owner: UUID,
			biome: Holder<Biome>
		): Boolean {
			val dimensionAssignments = assignments.getValue(dimension)
			val assignment = Assignment(owner, biome)
			val existing = dimensionAssignments.putIfAbsent(chunkPos.toLong(), assignment) ?: return true

			if (existing.owner != owner) return false

			dimensionAssignments[chunkPos.toLong()] = assignment
			return true
		}

		fun release(dimension: QuantumQuarryDimension, chunkPos: ChunkPos, owner: UUID) {
			val dimensionAssignments = assignments.getValue(dimension)
			val existing = dimensionAssignments[chunkPos.toLong()] ?: return
			if (existing.owner != owner) return

			dimensionAssignments.remove(chunkPos.toLong(), existing)
		}
	}

	private data class Assignment(
		val owner: UUID,
		val biome: Holder<Biome>
	)

}