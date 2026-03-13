package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.util.FurnaceFuelGeneratorType
import dev.aaronhowser.mods.excessive_utilities.util.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.util.GeneratorType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class FurnaceFuelGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val furnaceFuelGeneratorType: FurnaceFuelGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override val generatorType: GeneratorType = furnaceFuelGeneratorType.baseGeneratorType

	override fun isValidInput(itemStack: ItemStack): Boolean {
		return itemStack.getBurnTime(RecipeType.SMELTING) > 0
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0 || container == null) return false

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val burnTime = inputStack.getBurnTime(RecipeType.SMELTING)
		if (burnTime <= 0) return false

		fePerTick = furnaceFuelGeneratorType.fePerTick
		burnTimeRemaining = Mth.ceil(burnTime * furnaceFuelGeneratorType.burnTimeMultiplier)

		if (inputStack.isItem(ModItems.HEATING_COIL)) {
			HeatingCoilItem.burnInFuelSlot(inputStack)
		} else {
			inputStack.shrink(1)
		}

		setChanged()

		return true
	}

	companion object {
		fun furnace(pos: BlockPos, state: BlockState) = FurnaceFuelGeneratorBlockEntity(
			ModBlockEntityTypes.FURNACE_GENERATOR.get(),
			FurnaceFuelGeneratorType.FURNACE,
			pos,
			state
		)

		fun survival(pos: BlockPos, state: BlockState) = FurnaceFuelGeneratorBlockEntity(
			ModBlockEntityTypes.SURVIVAL_GENERATOR.get(),
			FurnaceFuelGeneratorType.SURVIVAL,
			pos,
			state
		)
	}

}