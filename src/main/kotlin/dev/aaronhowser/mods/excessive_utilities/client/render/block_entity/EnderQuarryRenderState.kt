package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.core.BlockPos

class EnderQuarryRenderState : BlockEntityRenderState() {
    var dx = 0
    var dy = 0
    var dz = 0
    var gameTime = 0L
    var partialTick = 0f
    var active = false
}
