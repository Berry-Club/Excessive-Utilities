package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class PotionGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.CULINARY_GENERATOR.get(), pos, blockState) {

	override fun tryStartBurning(level: ServerLevel): Boolean {
		return false
	}

	companion object {
		tailrec fun calculateBrewingSteps(
			level: Level,
			potion: Holder<Potion>,
			runningTotal: Int = 0
		): Int {
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