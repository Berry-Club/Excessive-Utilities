package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler
import net.neoforged.neoforge.transfer.transaction.Transaction
import java.util.function.IntSupplier

/** One transactional fluid tank with a capacity that follows configuration changes. */
open class ConfigurableFluidTank(private val capacityGetter: IntSupplier) : FluidStacksResourceHandler(1, 0) {
    constructor(capacity: Int) : this(IntSupplier { capacity })
    val fluid: FluidStack get() = getResource(0).toStack(getAmountAsInt(0))
    val fluidAmount: Int get() = getAmountAsInt(0)
    val tankCapacity: Int get() = capacityGetter.asInt.also { require(it >= 0) }
    val isEmpty: Boolean get() = getAmountAsLong(0) == 0L
    override fun getCapacity(index: Int, resource: FluidResource): Int = tankCapacity
    open fun isFluidValid(stack: FluidStack): Boolean = true
    override fun isValid(index: Int, resource: FluidResource): Boolean = isFluidValid(resource.toStack(Int.MAX_VALUE))
    override fun onContentsChanged(index: Int, previousContents: FluidStack) = onContentsChanged()
    protected open fun onContentsChanged() {}
    fun copy(): FluidStack = fluid
    fun setFluid(stack: FluidStack) = set(0, FluidResource.of(stack), stack.amount)
    fun setFromFluidContent(content: SimpleFluidContent) = setFluid(content.copy())
    fun fill(stack: FluidStack): Int {
        if (stack.isEmpty) return 0
        return Transaction.openRoot().use { transaction ->
            val inserted = insert(0, FluidResource.of(stack), stack.amount, transaction)
            transaction.commit()
            inserted
        }
    }
    fun drain(amount: Int): FluidStack {
        val resource = getResource(0)
        if (resource.isEmpty || amount == 0) return FluidStack.EMPTY
        return Transaction.openRoot().use { transaction ->
            val extracted = extract(0, resource, amount, transaction)
            transaction.commit()
            resource.toStack(extracted)
        }
    }
}
