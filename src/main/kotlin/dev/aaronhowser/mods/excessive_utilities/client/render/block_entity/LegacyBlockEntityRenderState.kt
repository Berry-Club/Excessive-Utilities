package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.core.Vec3i

class LegacyBlockEntityRenderState<T : BlockEntity> : BlockEntityRenderState() {
    var radius = 0.0
    var currentOffset = Vec3i.ZERO
    var minimumOffset = Vec3i.ZERO
    var maximumOffset = Vec3i.ZERO
}
