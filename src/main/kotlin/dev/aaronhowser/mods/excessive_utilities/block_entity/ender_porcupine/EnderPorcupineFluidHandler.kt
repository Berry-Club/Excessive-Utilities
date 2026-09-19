package dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine

import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class EnderPorcupineFluidHandler(
	private val porcupine: EnderPorcupineBlockEntity,
	private val direction: Direction?
) : IFluidHandler {

	private fun getTarget(): IFluidHandler? = porcupine.findTargetFluidHandler(direction)

	override fun getTanks(): Int = getTarget()?.tanks ?: 0

	override fun getFluidInTank(tank: Int): FluidStack = getTarget()?.getFluidInTank(tank) ?: FluidStack.EMPTY

	override fun getTankCapacity(tank: Int): Int = getTarget()?.getTankCapacity(tank) ?: 0

	override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
		return getTarget()?.isFluidValid(tank, stack) ?: false
	}

	override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
		return getTarget()?.fill(resource, action) ?: 0
	}

	override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
		return getTarget()?.drain(resource, action) ?: FluidStack.EMPTY
	}

	override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
		return getTarget()?.drain(maxDrain, action) ?: FluidStack.EMPTY
	}

}