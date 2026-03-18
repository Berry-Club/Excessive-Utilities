package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.EnchanterBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class EnchanterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.ENCHANTER.get(), pos, blockState), ContainerContainer, MenuProvider {

	private val energyStorage = EnergyStorage(100_000)
	fun getEnergyCapability(direction: Direction?): IEnergyStorage = energyStorage

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)
	override fun getContainers(): List<Container> = listOf(container)

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

	fun getItemHandler(direction: Direction?): IItemHandlerModifiable = itemHandler

	override fun getGpUsage(): Double {
		val isCrafting = recipeCache != null
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

		val recipe = getRecipe()?.value
		if (recipe == null) {
			progress = 0
			updateBlockState(isCrafting = false)
			return
		}

		val fePerTick = recipe.fePerTick
		val speedUpgrades = container.getItem(UPGRADE_SLOT).count
		val duration = recipe.ticks

		var success = false

		for (i in 0 until speedUpgrades + 1) {
			if (energyStorage.energyStored < fePerTick) break

			progress++
			energyStorage.extractEnergy(fePerTick, false)

			while (progress >= duration) {
				craftRecipe(level, recipe)
				progress -= duration
			}

			success = true
		}

		updateBlockState(isCrafting = success)
	}

	private fun updateBlockState(isCrafting: Boolean) {
		val wasEnabled = blockState.getValue(EnchanterBlock.ENABLED)
		if (wasEnabled != isCrafting) {
			val newState = blockState.setValue(EnchanterBlock.ENABLED, isCrafting)
			level?.setBlockAndUpdate(blockPos, newState)
		}
	}

	private fun craftRecipe(level: ServerLevel, recipe: EnchanterRecipe) {
		val leftStack = container.getItem(LEFT_INPUT_SLOT)
		val rightStack = container.getItem(RIGHT_INPUT_SLOT)

		leftStack.shrink(recipe.leftCount)
		rightStack.shrink(recipe.rightCount)

		val outputStack = container.getItem(OUTPUT_SLOT)
		val resultStack = recipe.getResultItem(level.registryAccess()).copy()

		if (outputStack.isNotEmpty()) {
			outputStack.grow(resultStack.count)
		} else {
			container.setItem(OUTPUT_SLOT, resultStack)
		}

		setChanged()
	}

	private var recipeCache: RecipeHolder<EnchanterRecipe>? = null
	private fun getRecipe(): RecipeHolder<EnchanterRecipe>? {
		val level = level ?: return null

		val leftStack = container.getItem(LEFT_INPUT_SLOT)
		val rightStack = container.getItem(RIGHT_INPUT_SLOT)

		val cache = recipeCache
		if (cache != null) {
			if (cache.value
					.matches(
						EnchanterRecipe.Input(leftStack, rightStack),
						level
					)
			) {
				return cache
			}
		}

		var recipe = EnchanterRecipe.getRecipe(level, leftStack, rightStack)

		if (recipe != null) {
			val stackInOutput = container.getItem(OUTPUT_SLOT)
			val recipeOutput = recipe.value.getResultItem(level.registryAccess())

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


	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return EnchanterMenu(containerId, playerInventory, container)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.saveEnergy(ENERGY_NBT, energyStorage, registries)
		tag.saveItems(container, registries)
		tag.putInt(PROGRESS_NBT, progress)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		tag.loadEnergy(ENERGY_NBT, energyStorage, registries)
		tag.loadItems(container, registries)
		progress = tag.getInt(PROGRESS_NBT)
	}

	companion object {
		const val PROGRESS_NBT = "Progress"
		const val ENERGY_NBT = "Energy"

		const val CONTAINER_SIZE = 4
		const val LEFT_INPUT_SLOT = 0
		const val RIGHT_INPUT_SLOT = 1
		const val OUTPUT_SLOT = 2
		const val UPGRADE_SLOT = 3

		const val CONTAINER_DATA_SIZE = 0
	}
}