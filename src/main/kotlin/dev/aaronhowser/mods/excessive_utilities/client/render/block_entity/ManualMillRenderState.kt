package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState

class ManualMillRenderState : BlockEntityRenderState() {
    val gear = ItemStackRenderState()
    var turnDegrees = 0f
}
