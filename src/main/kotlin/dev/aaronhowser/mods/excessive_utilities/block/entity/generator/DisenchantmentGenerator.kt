package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

class DisenchantmentGenerator(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.DISENCHANTMENT_GENERATOR.get(), pos, blockState) {

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val potion = inputStack.get(DataComponents.POTION_CONTENTS)?.potion?.getOrNull() ?: return false

		if (potion.isHolder(Potions.WATER)) {
			burnTimeRemaining = 10
			fePerTick = 1
		} else {
			val totalPower = getPowerFromEnchantment(level, inputStack)
			if (totalPower <= 0) return false

			val duration = 200

			fePerTick = Mth.ceil(totalPower / duration.toDouble())
			burnTimeRemaining = duration
		}

		inputStack.shrink(1)
		setChanged()

		return true
	}

	companion object {
		fun getPowerFromEnchantment(level: Level, itemStack: ItemStack): Int {
			val enchantments = itemStack.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT))

			var totalPower = 0

			for ((enchantment, enchantLevel) in enchantments.entrySet()) {
				totalPower += getPowerFromEnchantment(level, enchantment, enchantLevel)
			}

			return totalPower
		}

		fun getPowerFromEnchantment(level: Level, enchantment: Holder<Enchantment>, enchantLevel: Int): Int {


		}
	}

}