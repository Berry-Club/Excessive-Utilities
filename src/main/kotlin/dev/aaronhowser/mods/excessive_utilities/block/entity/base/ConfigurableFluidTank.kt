package dev.aaronhowser.mods.excessive_utilities.block.entity.base

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
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
	override fun getFluidAmount(): Int = fluid.amount
	override fun getCapacity(): Int = capacityGetter.asInt
	override fun isFluidValid(stack: FluidStack): Boolean = true

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
		var drained = maxDrain

		if (fluid.amount < drained) {
			drained = fluid.amount
		}

		val stack = fluid.copyWithAmount(drained)
		if (action.execute() && drained > 0) {
			fluid.shrink(drained)
			onContentsChanged()
		}

		return stack
	}

	override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
		if (resource.isEmpty || !FluidStack.isSameFluidSameComponents(resource, fluid)) {
			return FluidStack.EMPTY
		}

		return drain(resource.amount, action)
	}

	protected open fun onContentsChanged() {}

	override fun getTanks(): Int = 1
	override fun getTankCapacity(tank: Int): Int = capacity
	override fun getFluidInTank(tank: Int): FluidStack = fluid
	override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = isFluidValid(stack)

	fun addToTag(lookupProvider: HolderLookup.Provider, nbt: CompoundTag): CompoundTag {
		if (!fluid.isEmpty) {
			nbt.put("Fluid", fluid.save(lookupProvider))
		}

		return nbt
	}

	fun loadFromTag(lookupProvider: HolderLookup.Provider, nbt: CompoundTag): ConfigurableFluidTank {
		fluid = FluidStack.parseOptional(lookupProvider, nbt.getCompound("Fluid"))
		return this
	}

}