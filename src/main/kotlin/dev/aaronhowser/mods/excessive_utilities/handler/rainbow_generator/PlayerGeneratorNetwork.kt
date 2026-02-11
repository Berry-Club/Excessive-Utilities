package dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator

import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import net.minecraft.server.level.ServerLevel
import java.util.*

class PlayerGeneratorNetwork(
	val playerUuid: UUID
) {

	private val generatorBlockEntities: MutableSet<GeneratorBlockEntity> = mutableSetOf()
	var rainbowGeneratedThisTick = false

	fun getActiveGeneratorTypes(): Set<GeneratorType> {
		return generatorBlockEntities
			.asSequence()
			.filter(GeneratorBlockEntity::isContributingToRainbowGen)
			.map(GeneratorBlockEntity::generatorType)
			.toSet()
	}

	fun allTypesActive(): Boolean {
		val activeTypes = getActiveGeneratorTypes()
		return GeneratorType.NON_RAINBOW.all { it in activeTypes }
	}

	fun addGenerator(generator: GeneratorBlockEntity) = generatorBlockEntities.add(generator)
	fun removeGenerator(generator: GeneratorBlockEntity) = generatorBlockEntities.remove(generator)
	fun isEmpty(): Boolean = generatorBlockEntities.isEmpty()

	fun tick(level: ServerLevel) {
		generatorBlockEntities.removeIf { it.isRemoved }
		rainbowGeneratedThisTick = false
	}

}