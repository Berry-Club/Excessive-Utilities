package dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine

import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource

/** Resolves the remote endpoint on every access so moved targets never retain stale inventories. */
class EnderPorcupineFluidHandler(porcupine: EnderPorcupineBlockEntity, direction: Direction?) :
    DelegatingResourceHandler<FluidResource>({
        requireNotNull(porcupine.findTargetFluidHandler(direction)) { "Ender Porcupine fluid endpoint is unavailable" }
    })
