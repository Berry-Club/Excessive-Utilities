package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
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
		fun calculateBrewingSteps(level: Level, itemStack: ItemStack): Int {
			val brewing = level.potionBrewing()
			val actualRecipes = brewing.recipes
			val potMixes = brewing.potionMixes
			val containerMixes = brewing.containerMixes

			val firstMix = potMixes.first()
			val output = firstMix.to()

			return 1
		}

	}

}