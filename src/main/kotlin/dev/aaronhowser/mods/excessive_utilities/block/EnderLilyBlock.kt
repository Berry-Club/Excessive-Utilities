package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock

class EnderLilyBlock : CropBlock(Properties.ofFullCopy(Blocks.WHEAT)) {

	override fun getBaseSeedId(): ItemLike = ModItems.ENDER_LILY.get()

}