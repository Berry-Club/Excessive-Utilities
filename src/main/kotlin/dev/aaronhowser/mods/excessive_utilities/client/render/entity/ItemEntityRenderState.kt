package dev.aaronhowser.mods.excessive_utilities.client.render.entity

import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.core.Direction

class ItemEntityRenderState : EntityRenderState() {
	val item = ItemStackRenderState()
	var direction = Direction.NORTH
	var rotation = 0f
	var yaw = 0f
}
