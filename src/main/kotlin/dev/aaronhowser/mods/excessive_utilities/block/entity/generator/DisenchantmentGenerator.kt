package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min
import kotlin.math.sqrt

class DisenchantmentGenerator(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.DISENCHANTMENT_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.DISENCHANTMENT

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (!inputStack.isItem(Items.ENCHANTED_BOOK)) return false

		val totalPower = getPowerFromEnchantment(level, inputStack)
		if (totalPower <= 0) return false

		fePerTick = 40
		burnTimeRemaining = Mth.ceil(totalPower / fePerTick.toDouble())

		inputStack.shrink(1)
		setChanged()

		return true
	}

	companion object {
		fun getPowerFromEnchantment(level: Level, itemStack: ItemStack): Int {
			val enchantments = itemStack.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT))

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