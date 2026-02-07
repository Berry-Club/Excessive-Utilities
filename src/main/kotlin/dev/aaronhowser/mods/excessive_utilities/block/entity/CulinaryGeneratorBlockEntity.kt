package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.TieredGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class CulinaryGeneratorBlockEntity(
	type: BlockEntityType<*>,
	override val tier: Int,
	pos: BlockPos,
	blockState: BlockState,
) : TieredGeneratorBlockEntity(type, pos, blockState) {

	private val container: ImprovedSimpleContainer =
		object : ImprovedSimpleContainer(this, CONTAINER_SIZE) {
			override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
				val foodValue = stack.getFoodProperties(null)
				return foodValue != null && (foodValue.nutrition > 0 || foodValue.saturation > 0f)
			}
		}

	private val itemHandler: IItemHandlerModifiable = InvWrapper(container)

	fun getItemHandler(direction: Direction?): IItemHandlerModifiable = itemHandler

	override fun tryStartBurning(level: ServerLevel) {
		if (burnTimeRemaining > 0) return

		val inputStack = container.getItem(INPUT_SLOT)
		if (inputStack.isEmpty) return

		val foodProperties = inputStack.getFoodProperties(null) ?: return

		val foodValue = foodProperties.nutrition
		val saturationValue = foodProperties.saturation

		val fePerFoodValue = ServerConfig.CONFIG.culinaryFePerFoodValue.get()
		val ticksPerSaturationValue = ServerConfig.CONFIG.culinaryTicksPerSaturationValue.get()

		fePerTick = (foodValue * fePerFoodValue).toInt()
		burnTimeRemaining = (saturationValue * ticksPerSaturationValue).toInt()

		inputStack.shrink(1)
		setChanged()
	}

	companion object {
		const val CONTAINER_SIZE = 1
		const val INPUT_SLOT = 0

		fun mk1(pos: BlockPos, state: BlockState) = CulinaryGeneratorBlockEntity(
			type = ModBlockEntityTypes.CULINARY_GENERATOR.get(),
			tier = 1,
			pos = pos,
			blockState = state
		)

		fun mk2(pos: BlockPos, state: BlockState) = CulinaryGeneratorBlockEntity(
			type = ModBlockEntityTypes.CULINARY_GENERATOR_MK2.get(),
			tier = 2,
			pos = pos,
			blockState = state
		)

		fun mk3(pos: BlockPos, state: BlockState) = CulinaryGeneratorBlockEntity(
			type = ModBlockEntityTypes.CULINARY_GENERATOR_MK3.get(),
			tier = 3,
			pos = pos,
			blockState = state
		)
	}

}