package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.EUFurnaceBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.furnace.EUFurnaceMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ContainerContainer
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
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper
import net.neoforged.neoforge.items.wrapper.RangedWrapper
import kotlin.jvm.optionals.getOrNull

class EUFurnaceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.FURNACE.get(), pos, blockState), ContainerContainer, MenuProvider {

	private val energyStorage = EnergyStorage(MAX_ENERGY)

	private val container: ImprovedSimpleContainer =
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

	private val containerData =
		object : ContainerData {
			override fun getCount(): Int = CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					CURRENT_ENERGY_DATA_INDEX -> energyStorage.energyStored
					PROGRESS_DATA_INDEX -> progress
					else -> -1
				}
			}

			override fun set(index: Int, value: Int) {
				// Unused
			}
		}

	override fun getContainers(): List<Container> = listOf(container)
	fun getItemHandler(direction: Direction?): IItemHandlerModifiable =
		object : RangedWrapper(InvWrapper(container), INPUT_SLOT, OUTPUT_SLOT + 1) {
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				return if (slot == OUTPUT_SLOT) {
					super.extractItem(slot, amount, simulate)
				} else {
					ItemStack.EMPTY
				}
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				return if (slot == OUTPUT_SLOT) {
					stack
				} else {
					super.insertItem(slot, stack, simulate)
				}
			}
		}

	override fun getGpUsage(): Double {
		val level = level ?: return 0.0
		val hasRecipe = getRecipe(level) != null
		if (!hasRecipe) return 0.0

		val amountUpgrades = container.getItem(UPGRADE_SLOT).count
		return SpeedUpgradeItem.getGpCost(amountUpgrades)
	}

	private var progress = 0

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		tryCraft(level)

		val wasLit = blockState.getValue(EUFurnaceBlock.LIT)
		val shouldBeLit = progress > 0

		if (wasLit != shouldBeLit) {
			val newState = blockState.setValue(EUFurnaceBlock.LIT, shouldBeLit)
			level.setBlockAndUpdate(blockPos, newState)
		}
	}

	private fun tryCraft(level: ServerLevel) {
		val recipe = getRecipe(level)
		if (recipe == null) {
			progress = 0
			level.setBlockAndUpdate(blockPos, blockState.setValue(EUFurnaceBlock.LIT, false))
			return
		}

		val feRequired = ServerConfig.CONFIG.furnaceGeneratorFePerTick.get()
		if (energyStorage.energyStored < feRequired) {
			level.setBlockAndUpdate(blockPos, blockState.setValue(EUFurnaceBlock.LIT, false))
			return
		}

		val amountUpgrades = container.getItem(UPGRADE_SLOT).count
		progress += amountUpgrades

		val maxProgress = ServerConfig.CONFIG.furnaceTicksPerRecipe.get()

		if (progress >= maxProgress) {
			val inputStack = container.getItem(INPUT_SLOT)
			val newOutput = recipe.value.assemble(SingleRecipeInput(inputStack), level.registryAccess())

			val stackInOutput = container.getItem(OUTPUT_SLOT)
			if (stackInOutput.isEmpty) {
				container.setItem(OUTPUT_SLOT, newOutput)
			} else if (stackInOutput.isItem(newOutput.item)) {
				stackInOutput.grow(newOutput.count)
			}

			inputStack.shrink(1)
			energyStorage.extractEnergy(feRequired, false)

			progress = 0
		}
	}

	private fun getRecipe(level: Level): RecipeHolder<SmeltingRecipe>? {
		val input = SingleRecipeInput(container.getItem(INPUT_SLOT))
		return level.recipeManager
			.getRecipeFor(RecipeType.SMELTING, input, level)
			.getOrNull()
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return EUFurnaceMenu(containerId, playerInventory, container, containerData)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(PROGRESS_NBT, progress)
		tag.saveItems(container, registries)
		tag.saveEnergy(ENERGY_NBT, energyStorage, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		progress = tag.getInt(PROGRESS_NBT)
		tag.loadItems(container, registries)
		tag.loadEnergy(ENERGY_NBT, energyStorage, registries)
	}

	companion object {
		const val CONTAINER_SIZE = 3
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1
		const val UPGRADE_SLOT = 2

		const val CONTAINER_DATA_SIZE = 2
		const val CURRENT_ENERGY_DATA_INDEX = 0
		const val PROGRESS_DATA_INDEX = 1

		const val PROGRESS_NBT = "Progress"
		const val ENERGY_NBT = "Energy"

		const val MAX_ENERGY = 128_000

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: EUFurnaceBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}

		fun getEnergyCapability(transmitter: EUFurnaceBlockEntity, direction: Direction?): IEnergyStorage {
			return transmitter.energyStorage
		}
	}

}