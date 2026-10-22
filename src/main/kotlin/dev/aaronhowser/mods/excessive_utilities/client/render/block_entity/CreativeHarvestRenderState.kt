package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.core.Direction

class CreativeHarvestRenderState : BlockEntityRenderState() {
    val item = ItemStackRenderState()
    val directions = mutableListOf<Pair<Direction, Int>>()
}
