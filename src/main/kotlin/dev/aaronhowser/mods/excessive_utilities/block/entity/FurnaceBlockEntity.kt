package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

class FurnaceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.FURNACE.get(), pos, blockState) {

	private val container =
		object : ImprovedSimpleContainer(this, 3) {
			override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
				val level = level ?: return false

				return when (slot) {
					INPUT_SLOT ->
						level.recipeManager
							.getRecipeFor(RecipeType.SMELTING, SingleRecipeInput(stack), level)
							.isPresent

					UPGRADE_SLOT ->
						stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)

					else ->
						false
				}
			}
		}

	private fun serverTick(level: ServerLevel) {

	}

	private fun getRecipe(level: Level): RecipeHolder<SmeltingRecipe?>? {
		val input = SingleRecipeInput(container.getItem(INPUT_SLOT))
		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, input, level)
			.getOrNull()
	}

	companion object {
		const val CONTAINER_SIZE = 3
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1
		const val UPGRADE_SLOT = 2
	}

}