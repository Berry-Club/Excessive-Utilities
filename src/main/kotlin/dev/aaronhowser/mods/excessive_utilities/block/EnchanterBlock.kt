package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.excessive_utilities.block.base.GpDrainBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty

class EnchanterBlock : GpDrainBlock(Properties.ofFullCopy(Blocks.IRON_BLOCK)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(ENABLED, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(FACING, ENABLED)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(FACING, context.horizontalDirection.opposite)
	}

	override fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity> {
		return ModBlockEntityTypes.ENCHANTER.get()
	}

	override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
		if (!state.getValue(ENABLED)) return

		for (offset in EnchantingTableBlock.BOOKSHELF_OFFSETS) {
			if (random.nextInt(16) != 0
				|| !EnchantingTableBlock.isValidBookShelf(level, pos, offset)
			) continue

			val targetX = pos.x.toDouble() + random.nextRange(0.2, 0.8)
			val targetZ = pos.z.toDouble() + random.nextRange(0.2, 0.8)

			level.addParticle(
				ParticleTypes.ENCHANT,
				targetX,
				pos.y.toDouble() + 2.0,
				targetZ,
				(offset.x.toFloat() + random.nextFloat()).toDouble() - 0.5,
				(offset.y.toFloat() - random.nextFloat() - 1.0f).toDouble(),
				(offset.z.toFloat() + random.nextFloat()).toDouble() - 0.5
			)
		}
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
		val ENABLED: BooleanProperty = BlockStateProperties.ENABLED
	}

}