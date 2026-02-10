package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpSourceBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.mill.GpPanelBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlock(
	val requiresDay: Boolean
) : GpSourceBlock(Properties.ofFullCopy(Blocks.DAYLIGHT_DETECTOR)) {

	override fun getBlockEntityType(): BlockEntityType<out GpSourceBlockEntity> = ModBlockEntityTypes.GP_PANEL.get()

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return GpPanelBlockEntity(pos, state)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		super.setPlacedBy(level, pos, state, placer, stack)
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is GpPanelBlockEntity) {
			blockEntity.requiresDay = requiresDay
		}
	}

}