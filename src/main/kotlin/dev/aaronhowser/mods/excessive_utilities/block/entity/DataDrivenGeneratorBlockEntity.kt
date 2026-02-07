package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.CompressibleFeGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.entity.ResonatorBlockEntity.Companion.OUTPUT_SLOT
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

open class DataDrivenGeneratorBlockEntity(
	type: BlockEntityType<*>,
	override val tier: Int,
	val generatorType: GeneratorType,
	pos: BlockPos,
	blockState: BlockState,
) : CompressibleFeGeneratorBlockEntity(type, pos, blockState) {

	private val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, CONTAINER_SIZE)

	private val itemHandler: IItemHandlerModifiable =
		object : InvWrapper(container) {

			override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
				val fuelMap = generatorType.fuelDataMap
				val itemFuel = stack.item.builtInRegistryHolder().getData(fuelMap)

				return slot == INPUT_SLOT && itemFuel != null
			}

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

	override fun tryStartBurning(level: ServerLevel) {
		if (burnTimeRemaining > 0) return

		val fuelMap = generatorType.fuelDataMap

		val inputStack = container.getItem(INPUT_SLOT)
		if (inputStack.isEmpty) return

		val itemFuel = inputStack.item.builtInRegistryHolder().getData(fuelMap) ?: return

		fePerTick = itemFuel.fePerTick
		burnTimeRemaining = itemFuel.burnTime

		inputStack.shrink(1)
		setChanged()
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

		fun enderMk1(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun enderMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun enderMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.ENDER_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.ENDER,
			pos = pos,
			blockState = state
		)

		fun explosiveMk1(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.EXPLOSIVE,
			pos = pos,
			blockState = state
		)

		fun explosiveMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.EXPLOSIVE_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.EXPLOSIVE,
			pos = pos,
			blockState = state
		)

		fun explosiveMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.EXPLOSIVE_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.EXPLOSIVE,
			pos = pos,
			blockState = state
		)

		fun pink(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.PINK_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.PINK,
			pos = pos,
			blockState = state
		)

		fun pinkMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.PINK_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.PINK,
			pos = pos,
			blockState = state
		)

		fun pinkMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.PINK_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.PINK,
			pos = pos,
			blockState = state
		)

		fun netherStar(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.NETHER_STAR_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.NETHER_STAR,
			pos = pos,
			blockState = state
		)

		fun netherStarMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.NETHER_STAR_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.NETHER_STAR,
			pos = pos,
			blockState = state
		)

		fun netherStarMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.NETHER_STAR_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.NETHER_STAR,
			pos = pos,
			blockState = state
		)

		fun frosty(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.FROSTY_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.FROSTY,
			pos = pos,
			blockState = state
		)

		fun frostyMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.FROSTY_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.FROSTY,
			pos = pos,
			blockState = state
		)

		fun frostyMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.FROSTY_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.FROSTY,
			pos = pos,
			blockState = state
		)

		fun halitosis(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HALITOSIS_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.HALITOSIS,
			pos = pos,
			blockState = state
		)

		fun halitosisMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HALITOSIS_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.HALITOSIS,
			pos = pos,
			blockState = state
		)

		fun halitosisMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.HALITOSIS_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.HALITOSIS,
			pos = pos,
			blockState = state
		)

		fun death(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.DEATH_GENERATOR.get(),
			tier = 1,
			generatorType = GeneratorType.DEATH,
			pos = pos,
			blockState = state
		)

		fun deathMk2(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.DEATH_GENERATOR_MK2.get(),
			tier = 2,
			generatorType = GeneratorType.DEATH,
			pos = pos,
			blockState = state
		)

		fun deathMk3(pos: BlockPos, state: BlockState) = DataDrivenGeneratorBlockEntity(
			type = ModBlockEntityTypes.DEATH_GENERATOR_MK3.get(),
			tier = 3,
			generatorType = GeneratorType.DEATH,
			pos = pos,
			blockState = state
		)

	}

}