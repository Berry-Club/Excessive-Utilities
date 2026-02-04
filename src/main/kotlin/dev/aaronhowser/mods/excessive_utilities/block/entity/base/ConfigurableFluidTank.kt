package dev.aaronhowser.mods.excessive_utilities.block.entity.base

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.IntSupplier
import kotlin.math.min

open class ConfigurableFluidTank(
	private val capacityGetter: IntSupplier
) : IFluidTank, IFluidHandler {

	private var fluid: FluidStack = FluidStack.EMPTY

	override fun getFluid(): FluidStack = fluid

	override fun getFluidAmount(): Int {
		TODO("Not yet implemented")
	}

	override fun getCapacity(): Int = capacityGetter.asInt

	override fun isFluidValid(stack: FluidStack): Boolean {
		TODO("Not yet implemented")
	}

	override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
		if (resource.isEmpty || !isFluidValid(resource)) return 0

		if (action.simulate()) {
			if (fluid.isEmpty) return min(capacity, resource.amount)
			if (!FluidStack.isSameFluidSameComponents(fluid, resource)) return 0
			return min(capacity - fluid.amount, resource.amount)
		}

		if (fluid.isEmpty) {
			fluid = resource.copyWithAmount(min(capacity, resource.amount))
			onContentsChanged()
			return fluid.amount
		}

		if (!FluidStack.isSameFluidSameComponents(fluid, resource)) return 0

		var filled = capacity - fluid.amount

		if (resource.amount < filled) {
			fluid.grow(resource.amount)
			filled = resource.amount
		} else {
			fluid.amount = capacity
		}

		if (filled > 0) onContentsChanged()

		return filled
	}

	override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
		TODO("Not yet implemented")
	}

	override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
		TODO("Not yet implemented")
	}

	protected fun onContentsChanged() {}

	override fun getTanks(): Int = 1
	override fun getTankCapacity(tank: Int): Int = capacity
	override fun getFluidInTank(tank: Int): FluidStack = fluid
	override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = isFluidValid(stack)
}