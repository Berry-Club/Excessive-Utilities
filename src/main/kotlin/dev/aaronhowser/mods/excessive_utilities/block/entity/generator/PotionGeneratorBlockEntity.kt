package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull
import kotlin.math.pow

class PotionGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.POTION_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.POTION

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0 || container == null) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val potion = inputStack.get(DataComponents.POTION_CONTENTS)?.potion?.getOrNull() ?: return false

		if (potion.isHolder(Potions.WATER)) {
			burnTimeRemaining = 10
			fePerTick = 1
		} else {
			val totalPower = getPowerFromPotion(level, inputStack)
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
		fun getPowerFromPotion(level: Level, itemStack: ItemStack): Int {
			val potion = itemStack.get(DataComponents.POTION_CONTENTS)
				?.potion
				?.getOrNull()
				?: return -1

			val steps = calculateBrewingSteps(level, potion)
			return 100 * Mth.ceil(4.0.pow(steps))
		}

		tailrec fun calculateBrewingSteps(
			level: Level,
			potion: Holder<Potion>,
			runningTotal: Int = 0
		): Int {
			if (runningTotal >= 100) return runningTotal

			val brewing = level.potionBrewing()
			val potMixes = brewing.potionMixes

			for (potMix in potMixes) {
				if (potMix.to.isHolder(potion)) {
					return calculateBrewingSteps(level, potMix.from, runningTotal + 1)
				}
			}

			return runningTotal
		}

	}

}