package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.block_entity.extractEnergy
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.simple_machine.SimpleMachineMenu
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
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.Property
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.RangedResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.transaction.TransactionContext

// I thought the Crusher was also single input single output but it isn't
// so all the time I spent making this abstract was pointless
// Keeping it though because it's good.
abstract class SimpleMachineBlockEntity<T : Recipe<SingleRecipeInput>>(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer, MenuProvider {

	protected val energyStorage = SimpleEnergyHandler(MAX_ENERGY)

	protected open val container: ImprovedSimpleContainer = SimpleMachineContainer()

	protected val containerData: ContainerData =
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
				// Unused
			}
		}
	private val itemHandler: ResourceHandler<ItemResource> = SimpleMachineItemHandler()

	override fun getContainers(): List<Container> = listOf(container)
	fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource> = itemHandler

	override fun getGpUsage(): Double {
		val level = level ?: return 0.0
		val hasRecipe = getRecipe(level) != null
		if (!hasRecipe) return 0.0

		val amountUpgrades = container.getItem(UPGRADE_SLOT).count
		return SpeedUpgradeItem.getGpCost(amountUpgrades)
	}

	abstract val litProperty: Property<Boolean>
	abstract fun isValidInput(stack: ItemStack): Boolean
	protected abstract fun getRecipe(level: Level): RecipeHolder<T>?
	protected abstract fun getFePerTick(recipe: RecipeHolder<T>): Int
	protected abstract fun getRecipeDuration(recipe: RecipeHolder<T>): Int

	protected var progress = 0
		set(value) {
			if (field != value) {
				field = value
				setChanged()
			}
		}

	protected var maxProgress: Int = 0
		set(value) {
			if (field != value) {
				field = value
				setChanged()
			}
		}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)
		if (isOverloaded()) {
			didWorkLastTick = false
			updateBlockState(level)
			return
		}

		val amountSpeedUpgrades = container.getItem(UPGRADE_SLOT).count
		for (i in 0..amountSpeedUpgrades) {
			stepCraft(level)
		}

		updateBlockState(level)
	}

	protected var didWorkLastTick = false

	protected open fun stepCraft(level: ServerLevel) {
		didWorkLastTick = false

		val recipe = getRecipe(level)
		if (recipe == null) {
			progress = 0
			maxProgress = 0
			return
		}

		val feRequired = getFePerTick(recipe)
		if (energyStorage.getAmountAsInt() < feRequired) {
			return
		}

		val canFit = canOutputFit(level, recipe)
		if (!canFit) return

		didWorkLastTick = true

		val recipeDuration = getRecipeDuration(recipe)
		maxProgress = recipeDuration

		progress++

		while (progress >= recipeDuration) {
			progress -= recipeDuration

			val stackInInput = container.getItem(INPUT_SLOT)
			val newOutput = recipe.value.assemble(SingleRecipeInput(stackInInput))

			val stackInOutput = container.getItem(OUTPUT_SLOT)
			if (stackInOutput.isEmpty) {
				container.setItem(OUTPUT_SLOT, newOutput)
			} else {
				if (!ItemStack.isSameItemSameComponents(stackInOutput, newOutput)) {
					break
				}

				val amountCanBeAdded = stackInOutput.maxStackSize - stackInOutput.count
				if (amountCanBeAdded <= 0) {
					break
				}

				val toAdd = newOutput.count.coerceAtMost(amountCanBeAdded)
				stackInOutput.grow(toAdd)
			}

			stackInInput.shrink(1)
			energyStorage.extractEnergy(feRequired, false)
		}
	}

	protected fun canOutputFit(level: ServerLevel, recipe: RecipeHolder<T>): Boolean {
		val stackInOutput = container.getItem(OUTPUT_SLOT)
		if (stackInOutput.isEmpty) return true

		val amountCanBeAdded = stackInOutput.maxStackSize - stackInOutput.count
		if (amountCanBeAdded <= 0) return false

		val newOutput = recipe.value.assemble(SingleRecipeInput(container.getItem(INPUT_SLOT)))
		if (!ItemStack.isSameItemSameComponents(stackInOutput, newOutput)) return false

		return newOutput.count <= amountCanBeAdded
	}

	protected fun updateBlockState(level: ServerLevel) {
		val wasLit = blockState.getValue(litProperty)
		val shouldBeLit = didWorkLastTick

		if (wasLit != shouldBeLit) {
			val newState = blockState.setValue(litProperty, shouldBeLit)
			level.setBlockAndUpdate(blockPos, newState)
		}
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return SimpleMachineMenu(containerId, playerInventory, container, containerData)
	}


	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.putInt(PROGRESS_NBT, progress)
		tag.saveItems(container)
		tag.saveEnergy(ENERGY_NBT, energyStorage)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		progress = tag.getIntOr(PROGRESS_NBT, 0)
		tag.loadItems(container)
		tag.loadEnergy(ENERGY_NBT, energyStorage)
	}

	companion object {
		const val CONTAINER_SIZE = 3
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1
		const val UPGRADE_SLOT = 2

		const val CONTAINER_DATA_SIZE = 4
		const val CURRENT_ENERGY_DATA_INDEX = 0
		const val MAX_ENERGY_DATA_INDEX = 1
		const val PROGRESS_DATA_INDEX = 2
		const val MAX_PROGRESS_DATA_INDEX = 3

		const val PROGRESS_NBT = "Progress"
		const val ENERGY_NBT = "Energy"

		const val MAX_ENERGY = 128_000

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: SimpleMachineBlockEntity<*>
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}

		fun getEnergyCapability(machine: SimpleMachineBlockEntity<*>, direction: Direction?): EnergyHandler {
			return machine.energyStorage
		}
	}

	private inner class SimpleMachineItemHandler : DelegatingResourceHandler<ItemResource>(
		RangedResourceHandler.of(VanillaContainerWrapper.of(container), INPUT_SLOT, OUTPUT_SLOT + 1)
	) {
		override fun extract(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
			if (index != OUTPUT_SLOT - INPUT_SLOT) return 0
			return getDelegate().extract(index, resource, amount, transaction)
		}

	}

	private inner class SimpleMachineContainer : ImprovedSimpleContainer(this, CONTAINER_SIZE) {

		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			val level = level ?: return false

			return when (slot) {
				INPUT_SLOT -> isValidInput(stack)
				UPGRADE_SLOT -> stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
				else -> false
			}
		}

	}

}
