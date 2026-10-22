package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.menu.single_fluid_generator.SingleFluidGeneratorMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.MagmaticFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
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
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ConfigurableFluidTank

class MagmaticGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.MAGMATIC_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.MAGMATIC

	val tank: ConfigurableFluidTank =
		object : ConfigurableFluidTank(1_000_000) {
			override fun isFluidValid(stack: FluidStack): Boolean {
				val level = level ?: return false
				val recipe = MagmaticFuelRecipe.getRecipe(level, stack)
				return recipe != null
			}

			override fun onContentsChanged() {
				setChanged()
				val level = level ?: return
				if (!level.isClientSide) {
					level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS)
				}
			}
		}

	fun getRecipe(): MagmaticFuelRecipe? {
		val level = level ?: return null
		return MagmaticFuelRecipe.getRecipe(level, tank.fluid)
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val fluidInTank = tank.fluid
		if (fluidInTank.isEmpty) return false

		val recipe = getRecipe() ?: return false

		fePerTick = recipe.fePerTick
		burnTimeRemaining = recipe.duration

		tank.drain(recipe.fluidIngredient.amount())
		setChanged()

		return true
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return SingleFluidGeneratorMenu(containerId, playerInventory, container, containerData, this)
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
		fun getFluidCapability(blockEntity: MagmaticGeneratorBlockEntity, direction: Direction?): ResourceHandler<FluidResource> {
			return blockEntity.tank
		}
	}

}
