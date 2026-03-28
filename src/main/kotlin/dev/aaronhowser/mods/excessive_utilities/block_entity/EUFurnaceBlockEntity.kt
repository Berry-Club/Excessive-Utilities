package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.block.EUFurnaceBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.SimpleMachineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import kotlin.jvm.optionals.getOrNull

class EUFurnaceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SimpleMachineBlockEntity<SmeltingRecipe>(ModBlockEntityTypes.FURNACE.get(), pos, blockState) {

	override val litProperty: Property<Boolean> = EUFurnaceBlock.LIT

	override fun isValidInput(stack: ItemStack): Boolean {
		val level = level ?: return false

		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, SingleRecipeInput(stack), level)
			.isPresent
	}

	override fun getFePerTick(recipe: RecipeHolder<SmeltingRecipe>): Int {
		return ServerConfig.CONFIG.furnaceFePerTick.get()
	}

	override fun getRecipeDuration(recipe: RecipeHolder<SmeltingRecipe>): Int {
		return ServerConfig.CONFIG.furnaceTicksPerRecipe.get()
	}

	private var recipeCache: RecipeHolder<SmeltingRecipe>? = null
	override fun getRecipe(level: Level): RecipeHolder<SmeltingRecipe>? {
		val input = SingleRecipeInput(container.getItem(INPUT_SLOT))

		val cache = recipeCache
		if (cache != null) {
			if (cache.value.matches(input, level)) {
				return cache
			} else {
				recipeCache = null
			}
		}

		val recipe = level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, input, level)
			.getOrNull()

		if (recipe == null) {
			return null
		}

		recipeCache = recipe
		return recipe
	}

}