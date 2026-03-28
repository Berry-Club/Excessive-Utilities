package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.CrusherBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
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
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class CrusherBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.CRUSHER.get(), pos, blockState), ContainerContainer, MenuProvider {

	private val energyStorage = EnergyStorage(100_000)
	fun getEnergyCapability(direction: Direction?): IEnergyStorage = energyStorage

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)
	override fun getContainers(): List<Container> = listOf(container)

	private val itemHandler: IItemHandlerModifiable =
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = when (slot) {
				INPUT_SLOT -> true
				UPGRADE_SLOT -> stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
				else -> false
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (slot != INPUT_SLOT) return stack
				return super.insertItem(slot, stack, simulate)
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				if (slot != PRIMARY_OUTPUT_SLOT && slot != SECONDARY_OUTPUT_SLOT) return ItemStack.EMPTY
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

		val fePerTick = 20 // TODO: Configurable
		val speedUpgrades = container.getItem(UPGRADE_SLOT).count

		val maxProgress = 200 // TODO: Configurable

		var success = false

		for (i in 0 until speedUpgrades + 1) {
			if (energyStorage.energyStored < fePerTick) break

			progress++
			energyStorage.extractEnergy(fePerTick, false)

			while (progress >= maxProgress) {
				craftRecipe(level, recipe)
				progress -= maxProgress
			}

			success = true
		}

		updateBlockState(isCrafting = success)
	}

	private fun updateBlockState(isCrafting: Boolean) {
		val wasEnabled = blockState.getValue(CrusherBlock.ENABLED)
		if (wasEnabled != isCrafting) {
			val newState = blockState.setValue(CrusherBlock.ENABLED, isCrafting)
			level?.setBlockAndUpdate(blockPos, newState)
		}
	}

	private fun craftRecipe(level: ServerLevel, recipe: CrusherRecipe) {
		val inputStack = container.getItem(INPUT_SLOT)
		inputStack.shrink(1)

		val primaryOutputStack = recipe.getPrimaryOutput()
		val stackInOutput = container.getItem(PRIMARY_OUTPUT_SLOT)

		if (stackInOutput.isNotEmpty()) {
			stackInOutput.grow(primaryOutputStack.count)
		} else {
			container.setItem(PRIMARY_OUTPUT_SLOT, primaryOutputStack)
		}

		val secondaryOutputStack = recipe.getSecondaryOutput()
		if (secondaryOutputStack.isNotEmpty() && level.random.chance(recipe.secondaryChance)) {
			val stackInSecondaryOutput = container.getItem(SECONDARY_OUTPUT_SLOT)

			if (stackInSecondaryOutput.isEmpty) {
				container.setItem(SECONDARY_OUTPUT_SLOT, secondaryOutputStack)
			} else if (ItemStack.isSameItemSameComponents(stackInSecondaryOutput, secondaryOutputStack)) {
				val amountCanFit = stackInSecondaryOutput.maxStackSize - stackInSecondaryOutput.count
				if (amountCanFit > 0) {
					val amountToAdd = secondaryOutputStack.count.coerceAtMost(amountCanFit)
					stackInSecondaryOutput.grow(amountToAdd)
				}
			}
		}

		setChanged()
	}

	private var recipeCache: RecipeHolder<CrusherRecipe>? = null
	private fun getRecipe(): RecipeHolder<CrusherRecipe>? {
		val level = level ?: return null

		val input = container.getItem(INPUT_SLOT)

		val cache = recipeCache
		if (cache != null) {
			if (cache.value
					.matches(
						SingleRecipeInput(input),
						level
					)
			) {
				return cache
			}
		}

		var recipe = CrusherRecipe.getRecipe(level, input)

		if (recipe != null) {
			val stackInPrimaryOutput = container.getItem(PRIMARY_OUTPUT_SLOT)
			val recipeOutput = recipe.value.getPrimaryOutput()

			if (stackInPrimaryOutput.isNotEmpty() && ItemStack.isSameItemSameComponents(stackInPrimaryOutput, recipeOutput)) {
				val canFit = stackInPrimaryOutput.count + recipeOutput.count <= stackInPrimaryOutput.maxStackSize
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

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return null
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
		const val INPUT_SLOT = 0
		const val PRIMARY_OUTPUT_SLOT = 1
		const val SECONDARY_OUTPUT_SLOT = 2
		const val UPGRADE_SLOT = 3

		const val CONTAINER_DATA_SIZE = 0
	}
}