package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.ItemAndFluidInputDataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.menu.item_fluid_generator.ItemFluidGeneratorMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ConfigurableFluidTank

open class ItemAndFluidInputDataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val specificType: ItemAndFluidInputDataDrivenGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override val generatorType: GeneratorType = specificType.baseGeneratorType

	val tank: ConfigurableFluidTank =
		object : ConfigurableFluidTank(4_000) {
			override fun isFluidValid(stack: FluidStack): Boolean {
				val level = level ?: return false
				return ItemAndFluidFuelRecipe.isValidFluid(stack, specificType.fuelRecipeType, ModRecipes.recipes(level))
			}

			override fun onContentsChanged() {
				setChanged()
				val level = level ?: return
				if (!level.isClientSide) {
					level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS)
				}
			}
		}

	protected fun getRecipe(itemStack: ItemStack, fluidStack: FluidStack): ItemAndFluidFuelRecipe? {
		val level = level ?: return null
		return ItemAndFluidFuelRecipe.getRecipe(level, specificType.fuelRecipeType, itemStack, fluidStack)
	}

	override fun isValidInput(itemStack: ItemStack): Boolean {
		val level = level ?: return false
		return ItemAndFluidFuelRecipe.isValidItem(itemStack, specificType.fuelRecipeType, ModRecipes.recipes(level))
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val inputItemStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		val inputFluidStack = tank.fluid
		val recipe = getRecipe(inputItemStack, inputFluidStack) ?: return false

		fePerTick = recipe.fePerTick
		burnTimeRemaining = recipe.duration

		inputItemStack.shrink(1)
		tank.drain(recipe.fluidIngredient.amount())
		setChanged()

		return true
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return ItemFluidGeneratorMenu(containerId, playerInventory, container, containerData, this)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.store(TANK_NBT, FluidStack.OPTIONAL_CODEC, tank.fluid)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		tag.read(TANK_NBT, FluidStack.OPTIONAL_CODEC).ifPresent(tank::setFluid)
	}

	override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(registries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	companion object {
		const val TANK_NBT = "Tank"
		fun slimy(pos: BlockPos, state: BlockState) = ItemAndFluidInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.SLIMY_GENERATOR.get(),
			specificType = ItemAndFluidInputDataDrivenGeneratorType.SLIMY,
			pos = pos,
			blockState = state
		)

		fun heatedRedstone(pos: BlockPos, state: BlockState) = ItemAndFluidInputDataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get(),
			specificType = ItemAndFluidInputDataDrivenGeneratorType.HEATED_REDSTONE,
			pos = pos,
			blockState = state
		)

		fun getFluidCapability(blockEntity: ItemAndFluidInputDataDrivenGeneratorBlockEntity, direction: Direction?): ResourceHandler<FluidResource> {
			return blockEntity.tank
		}
	}

}
