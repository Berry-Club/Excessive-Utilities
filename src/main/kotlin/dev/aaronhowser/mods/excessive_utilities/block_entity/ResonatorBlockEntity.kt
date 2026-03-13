package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.putUuidIfNotNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.util.ContainerContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class ResonatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.RESONATOR.get(), pos, blockState), ContainerContainer, MenuProvider {

	override fun getGpUsage(): Double {
		val level = level ?: return 0.0
		val recipe = getRecipe(level) ?: return 0.0

		return recipe.gpCost
	}

	private val container = ImprovedSimpleContainer(this, CONTAINER_SIZE)
	override fun getContainers(): List<Container> = listOf(container)

	private val itemHandler: IItemHandlerModifiable =
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = slot == INPUT_SLOT

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (slot != INPUT_SLOT) return stack
				return super.insertItem(slot, stack, simulate)
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				if (slot != OUTPUT_SLOT) return ItemStack.EMPTY
				return super.extractItem(slot, amount, simulate)
			}
		}

	fun getItemHandler(direction: Direction?): IItemHandlerModifiable = itemHandler

	private val containerData: ContainerData =
		object : ContainerData {
			override fun getCount(): Int = CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					PROGRESS_DATA_INDEX -> progress
					MAX_PROGRESS_DATA_INDEX -> CRAFT_TIME
					GP_HUNDREDTHS_COST_DATA_INDEX -> Mth.ceil(getGpUsage() * 100)
					else -> 0
				}
			}

			override fun set(index: Int, value: Int) {
				// No setting from the client
			}
		}

	private var progress: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		val recipe = getRecipe(level)

		if (recipe == null) {
			progress = 0
			return
		}

		if (isOverloaded()) return

		progress++

		if (progress >= CRAFT_TIME) {
			craftItem(level, recipe)
			progress = 0
		}
	}

	private fun craftItem(level: ServerLevel, recipe: ResonatorRecipe) {
		val inputStack = container.getItem(INPUT_SLOT)

		val recipeOutput = recipe.getResultItem(level.registryAccess()).copy()
		val stackInOutput = container.getItem(OUTPUT_SLOT)
		if (stackInOutput.isNotEmpty()) {
			stackInOutput.grow(recipeOutput.count)
		} else {
			container.setItem(OUTPUT_SLOT, recipeOutput)
		}

		inputStack.shrink(1)
	}

	fun getRecipe(level: Level): ResonatorRecipe? {
		val inputStack = container.getItem(INPUT_SLOT)
		val recipe = ResonatorRecipe.getRecipe(level, inputStack) ?: return null

		val stackInOutput = container.getItem(OUTPUT_SLOT)
		if (stackInOutput.isEmpty) return recipe

		val recipeOutput = recipe.getResultItem(level.registryAccess()).copy()
		val outputCanFit = ItemStack.isSameItemSameComponents(stackInOutput, recipeOutput)
				&& stackInOutput.count + recipeOutput.count <= stackInOutput.maxStackSize

		return if (outputCanFit) recipe else null
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return ResonatorMenu(containerId, playerInventory, container, containerData)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.saveItems(container, registries)
		tag.putInt(PROGRESS_NBT, progress)
		tag.putUuidIfNotNull(OWNER_UUID_NBT, ownerUuid)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		tag.loadItems(container, registries)
		progress = tag.getInt(PROGRESS_NBT)
	}

	companion object {
		const val PROGRESS_NBT = "Progress"

		const val CONTAINER_SIZE = 3
		const val INPUT_SLOT = 0
		const val OUTPUT_SLOT = 1
		const val UPGRADE_SLOT = 2

		const val CONTAINER_DATA_SIZE = 3
		const val PROGRESS_DATA_INDEX = 0
		const val MAX_PROGRESS_DATA_INDEX = 1
		const val GP_HUNDREDTHS_COST_DATA_INDEX = 2 // GP cost multiplied by 100, because container data only supports ints

		const val CRAFT_TIME = 20 * 10
	}

}