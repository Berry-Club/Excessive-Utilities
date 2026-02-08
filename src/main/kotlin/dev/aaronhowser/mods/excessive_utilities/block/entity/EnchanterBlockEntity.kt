package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.recipe.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class EnchanterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.ENCHANTER.get(), pos, blockState) {

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)

	private val itemHandler: IItemHandlerModifiable =
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
				LEFT_INPUT_SLOT -> true
				RIGHT_INPUT_SLOT -> true
				UPGRADE_SLOT -> stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
				OUTPUT_SLOT -> false
				else -> false
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (slot != LEFT_INPUT_SLOT && slot != RIGHT_INPUT_SLOT) return stack
				return super.insertItem(slot, stack, simulate)
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				if (slot != OUTPUT_SLOT) return ItemStack.EMPTY
				return super.extractItem(slot, amount, simulate)
			}
		}

	override fun getGpUsage(): Double {
		val isCrafting = true
		if (!isCrafting) return 0.0

		val amountUpgrades = container.getItem(UPGRADE_SLOT).count
		return SpeedUpgradeItem.getGpCost(amountUpgrades)
	}

	private var progress: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		val recipe = getRecipe()
		if (recipe == null) {
			progress = 0
			return
		}

		progress++

		if (progress >= recipe.value.ticks) {
			craftRecipe(level, recipe.value)
			progress = 0
		}
	}

	private fun craftRecipe(level: ServerLevel, recipe: EnchanterRecipe) {
		val leftStack = container.getItem(LEFT_INPUT_SLOT)
		val rightStack = container.getItem(RIGHT_INPUT_SLOT)

		leftStack.shrink(recipe.leftCount)
		rightStack.shrink(recipe.rightCount)

	}

	private var recipeCache: RecipeHolder<EnchanterRecipe>? = null
	private fun getRecipe(): RecipeHolder<EnchanterRecipe>? {
		val level = level ?: return null

		val leftStack = container.getItem(LEFT_INPUT_SLOT)
		val rightStack = container.getItem(RIGHT_INPUT_SLOT)

		var recipe = EnchanterRecipe.getRecipe(level, leftStack, rightStack)

		if (recipe != null) {
			val stackInOutput = container.getItem(ResonatorBlockEntity.OUTPUT_SLOT)
			val recipeOutput = recipe.value.getResultItem(level.registryAccess()).copy()

			if (stackInOutput.isNotEmpty() && ItemStack.isSameItemSameComponents(stackInOutput, recipeOutput)) {
				val canFit = stackInOutput.count + recipeOutput.count <= stackInOutput.maxStackSize
				if (!canFit) recipe = null
			}
		}

		if (recipe == null) {
			recipeCache = null
			return null
		}

		if (recipe.id != recipeCache?.id) {
			recipeCache = recipe
			progress = 0
		}

		return recipe
	}

	companion object {
		const val CONTAINER_SIZE = 4
		const val LEFT_INPUT_SLOT = 0
		const val RIGHT_INPUT_SLOT = 1
		const val OUTPUT_SLOT = 2
		const val UPGRADE_SLOT = 3
	}
}