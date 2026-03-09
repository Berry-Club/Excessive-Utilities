package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.MagicalSnowGlobeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class MagicalSnowGlobeBlock : Block(Properties.ofFullCopy(Blocks.OAK_PLANKS)), EntityBlock {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return MagicalSnowGlobeBlockEntity(pos, state)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val component = stack.get(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS) ?: return
		val blockEntity = level.getBlockEntity(pos) as? MagicalSnowGlobeBlockEntity ?: return

		blockEntity.progressComponent = component
	}

	companion object {
		val SHAPE: VoxelShape = box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0)
	}

}