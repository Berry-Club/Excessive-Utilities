package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min
import kotlin.math.sqrt

class DisenchantmentGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.DISENCHANTMENT_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.DISENCHANTMENT

	override fun isValidInput(itemStack: ItemStack): Boolean {
		if (itemStack.isEmpty) return false
		val level = level ?: return false
		return getPowerFromStack(level, itemStack) > 0
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val totalPower = getPowerFromStack(level, inputStack)
		if (totalPower <= 0) return false

		fePerTick = 40
		burnTimeRemaining = Mth.ceil(totalPower / fePerTick.toDouble())

		inputStack.shrink(1)
		setChanged()

		return true
	}

	companion object {
		fun getPowerFromStack(level: Level, itemStack: ItemStack): Int {
			val lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
			val enchantments = itemStack.getAllEnchantments(lookup)

			var totalPower = 0

			for ((enchantment, enchantLevel) in enchantments.entrySet()) {
				totalPower += getPowerFromEnchantment(enchantment.value(), enchantLevel)
			}

			return totalPower
		}

		fun getPowerFromEnchantment(enchantment: Enchantment, level: Int): Int {
			val minLevel = enchantment.minLevel
			val maxLevel = enchantment.maxLevel
			val weight = enchantment.weight

			val a = sqrt(min(level + 1, maxLevel) / maxLevel.toDouble())
			val b = (maxLevel * maxLevel) * (level + 1)
			val c = minLevel / sqrt(weight.toDouble())

			return Mth.ceil(a * b * c) * 400
		}
	}

}