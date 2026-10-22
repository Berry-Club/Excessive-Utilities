package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.EnchanterBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class EnchanterBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.ENCHANTER.get(), pos, blockState), ContainerContainer, MenuProvider {

	private val energyStorage = SimpleEnergyHandler(100_000)
	fun getEnergyCapability(direction: Direction?): EnergyHandler = energyStorage

	private val container = EnchanterContainer()
	override fun getContainers(): List<Container> = listOf(container)

	private val itemHandler: ResourceHandler<ItemResource> = EnchanterItemHandler()

	fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource> = itemHandler

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

	private var maxProgress: Int = 0

	private var currentEnchantingPower: Float = 0f

	override fun clientTick(level: Level) {
		if (level.gameTime % 20 == 0L) {
			currentEnchantingPower = getCurrentEnchantingLevel(level)
		}
	}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)
		if (isOverloaded()) {
			updateBlockState(isCrafting = false)
			return
		}

		if (level.gameTime % 20 == 0L) {
			currentEnchantingPower = getCurrentEnchantingLevel(level)
		}

		val recipe = getRecipe()?.value
		if (recipe == null) {
			progress = 0
			maxProgress = 0
			updateBlockState(isCrafting = false)
			return
		}

		val fePerTick = recipe.fePerTick
		val speedUpgrades = container.getItem(UPGRADE_SLOT).count
		val duration = recipe.ticks

		var success = false

		if (currentEnchantingPower >= recipe.enchantingPower) {
			for (i in 0 until speedUpgrades + 1) {
				if (energyStorage.getAmountAsInt() < fePerTick) break

				progress++
				energyStorage.extractEnergy(fePerTick, false)

				while (progress >= duration) {
					craftRecipe(level, recipe)
					progress -= duration
				}

				success = true
			}
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

		leftStack.shrink(recipe.leftIngredient.count())
		rightStack.shrink(recipe.rightIngredient.count())

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
			maxProgress = recipe.value.ticks
		}

		return recipe
	}

	fun getCurrentEnchantingLevel(level: Level): Float {
		var i = 0f

		for (offset in EnchantingTableBlock.BOOKSHELF_OFFSETS) {
			val valid = EnchantingTableBlock.isValidBookShelf(level, blockPos, offset)
			if (!valid) continue

			val offsetPos = blockPos.offset(offset)
			val state = level.getBlockState(offsetPos)
			val power = state.getEnchantPowerBonus(level, offsetPos)

			i += power
		}

		return i
	}

	private val containerData: ContainerData =
		object : ContainerData {
			override fun getCount(): Int = CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					CURRENT_ENERGY_DATA_INDEX -> energyStorage.getAmountAsInt()
					MAX_ENERGY_DATA_INDEX -> energyStorage.getCapacityAsInt()
					PROGRESS_DATA_INDEX -> progress
					MAX_PROGRESS_DATA_INDEX -> maxProgress
					else -> -1
				}
			}

			override fun set(index: Int, value: Int) {
				// Cannot set from container data
			}
		}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return EnchanterMenu(containerId, playerInventory, container, containerData)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.saveEnergy(ENERGY_NBT, energyStorage)
		tag.saveItems(container)
		tag.putInt(PROGRESS_NBT, progress)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		tag.loadEnergy(ENERGY_NBT, energyStorage)
		tag.loadItems(container)
		progress = tag.getIntOr(PROGRESS_NBT, 0)
	}

	companion object {
		const val PROGRESS_NBT = "Progress"
		const val ENERGY_NBT = "Energy"

		const val CONTAINER_SIZE = 4
		const val LEFT_INPUT_SLOT = 0
		const val RIGHT_INPUT_SLOT = 1
		const val OUTPUT_SLOT = 2
		const val UPGRADE_SLOT = 3

		const val CONTAINER_DATA_SIZE = 4
		const val CURRENT_ENERGY_DATA_INDEX = 0
		const val MAX_ENERGY_DATA_INDEX = 1
		const val PROGRESS_DATA_INDEX = 2
		const val MAX_PROGRESS_DATA_INDEX = 3
	}

	private inner class EnchanterItemHandler : DelegatingResourceHandler<ItemResource>(VanillaContainerWrapper.of(container)) {
		override fun extract(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
			if (index != OUTPUT_SLOT) return 0
			return getDelegate().extract(index, resource, amount, transaction)
		}

	}

	private inner class EnchanterContainer : ImprovedSimpleContainer(this, CONTAINER_SIZE) {

		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				LEFT_INPUT_SLOT -> true
				RIGHT_INPUT_SLOT -> true
				UPGRADE_SLOT -> stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
				else -> false
			}
		}

	}

}
