package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.common.Tags

class RedOrchidBlock(properties: BlockBehaviour.Properties) : CropBlock(properties) {

	override fun getBaseSeedId(): ItemLike = ModItems.RED_ORCHID.get()

	override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
		return state.isBlock(Tags.Blocks.ORES_REDSTONE)
	}

}










