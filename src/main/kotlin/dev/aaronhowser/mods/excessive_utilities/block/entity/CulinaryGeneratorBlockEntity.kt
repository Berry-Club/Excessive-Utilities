package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

class CulinaryGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
) : GeneratorBlockEntity(ModBlockEntityTypes.CULINARY_GENERATOR.get(), pos, blockState) {

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

	}

}