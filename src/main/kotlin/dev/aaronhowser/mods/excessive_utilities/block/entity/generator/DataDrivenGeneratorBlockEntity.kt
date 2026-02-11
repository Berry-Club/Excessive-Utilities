package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class DataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	val dataDrivenGeneratorType: DataDrivenGeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(type, pos, blockState) {

	override val generatorType: GeneratorType = dataDrivenGeneratorType.baseGeneratorType

	override fun isValidInput(itemStack: ItemStack): Boolean {
		val fuelMap = dataDrivenGeneratorType.fuelDataMap
		val itemFuel = itemStack.item.builtInRegistryHolder().getData(fuelMap)

		return itemFuel != null
	}

	override fun tryStartBurning(level: ServerLevel): Boolean {
		if (burnTimeRemaining > 0) return false

		val fuelMap = dataDrivenGeneratorType.fuelDataMap

		val inputStack = container.getItem(GeneratorContainer.INPUT_SLOT)
		if (inputStack.isEmpty) return false

		val itemFuel = inputStack.item.builtInRegistryHolder().getData(fuelMap) ?: return false

		fePerTick = itemFuel.fePerTick
		burnTimeRemaining = itemFuel.burnTime

		inputStack.shrink(1)
		setChanged()

		return true
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		ContainerHelper.saveAllItems(tag, container.items, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		ContainerHelper.loadAllItems(tag, container.items, registries)
	}

	companion object {
		const val CONTAINER_SIZE = 1
		const val INPUT_SLOT = 0

		fun ender(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
			dataDrivenGeneratorType = DataDrivenGeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun explosive(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get(),
			dataDrivenGeneratorType = DataDrivenGeneratorType.EXPLOSIVE,
			pos = pos,
			blockState = state
		)

		fun pink(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.PINK_GENERATOR.get(),
			dataDrivenGeneratorType = DataDrivenGeneratorType.PINK,
			pos = pos,
			blockState = state
		)

		fun frosty(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.FROSTY_GENERATOR.get(),
			dataDrivenGeneratorType = DataDrivenGeneratorType.FROSTY,
			pos = pos,
			blockState = state
		)

		fun halitosis(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HALITOSIS_GENERATOR.get(),
			dataDrivenGeneratorType = DataDrivenGeneratorType.HALITOSIS,
			pos = pos,
			blockState = state
		)

	}

}