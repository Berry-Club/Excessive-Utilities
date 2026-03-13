package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ExtractOnlyInvWrapper
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.menu.qed.QedMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.util.ContainerContainer
import net.minecraft.core.BlockPos
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
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class QedBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.QED.get(), pos, blockState), ContainerContainer, MenuProvider {

	private val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, CONTAINER_SIZE)
	override fun getContainers(): List<Container> {
		return listOf(container)
	}

	private val itemHandler: ExtractOnlyInvWrapper =
		object : ExtractOnlyInvWrapper(container) {
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				return if (slot == OUTPUT_SLOT) {
					super.extractItem(slot, amount, simulate)
				} else {
					ItemStack.EMPTY
				}
			}
		}

	private var amountNearbyCrystals = 0
	private var progress = 0

	private fun serverTick(level: ServerLevel) {
		if (level.gameTime % 60 == 0L) {
			updateNearbyCrystals(level)
		}

		val recipe = getRecipe(level) ?: return
		progress += amountNearbyCrystals

		while (progress >= recipe.crystalTicks) {
			val input = CraftingInput.of(3, 3, container.items.subList(0, 9))
			if (!recipe.matches(input, level)) {
				progress = 0
				return
			}

			progress -= recipe.crystalTicks

			val output = recipe.assemble(input, level.registryAccess())
			val currentOutput = container.getItem(OUTPUT_SLOT)
			if (currentOutput.isEmpty) {
				container.setItem(OUTPUT_SLOT, output)
			} else {
				currentOutput.grow(output.count)
			}

			for (i in 0 until 9) {
				container.removeItem(i, 1)
			}
		}
	}

	private fun getRecipe(level: ServerLevel): QedRecipe? {
		val recipeInput = CraftingInput.of(3, 3, container.items.subList(0, 9))
		val recipe = QedRecipe.getRecipe(level, recipeInput) ?: return null

		val currentOutput = container.getItem(OUTPUT_SLOT)
		if (currentOutput.isEmpty) return recipe

		val newOutput = recipe.assemble(recipeInput, level.registryAccess())
		if (!ItemStack.isSameItemSameComponents(newOutput, currentOutput)) return null

		val newCount = currentOutput.count + newOutput.count
		if (newCount > newOutput.maxStackSize) return null

		return recipe
	}

	fun updateNearbyCrystals(level: ServerLevel) {
		val radius = 9
		val blocks = BlockPos.betweenClosed(
			blockPos.offset(-radius, -radius, -radius),
			blockPos.offset(radius, radius, radius)
		)

		val count = blocks.count { pos ->
			val stateThere = level.getBlockState(pos)
			stateThere.isBlock(ModBlocks.ENDER_FLUX_CRYSTAL)
		}

		this.amountNearbyCrystals = count
	}

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return QedMenu(containerId, playerInventory, container)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(PROGRESS_NBT, progress)
		tag.saveItems(container, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		progress = tag.getInt(PROGRESS_NBT)
		tag.loadItems(container, registries)
	}

	companion object {
		const val OUTPUT_SLOT = 9
		const val CONTAINER_SIZE = 10

		const val PROGRESS_NBT = "Progress"

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: QedBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}
	}

}