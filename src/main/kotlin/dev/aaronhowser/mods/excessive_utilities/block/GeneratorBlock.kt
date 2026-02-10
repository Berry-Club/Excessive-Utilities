package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty

class GeneratorBlock(
	val beTypeGetter: () -> BlockEntityType<out GeneratorBlockEntity>
) : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)), EntityBlock {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(FACING, LIT)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(FACING, context.horizontalDirection.opposite)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is GeneratorBlockEntity && placer != null) {
			blockEntity.ownerUuid = placer.uuid
		}
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		return beTypeGetter().create(pos, state)
	}

	override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			beTypeGetter(),
			GeneratorBlockEntity.Companion::tick
		)
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
		val LIT: BooleanProperty = BlockStateProperties.LIT

		val COLORS: Map<Block, Int> = mapOf(
			ModBlocks.FURNACE_GENERATOR.get() to 0xFFFFFF,
			ModBlocks.SURVIVAL_GENERATOR.get() to 0xFFFFFF,
			ModBlocks.CULINARY_GENERATOR.get() to 0xFFFFFF,
			ModBlocks.POTIONS_GENERATOR.get() to 5509553,
			ModBlocks.EXPLOSIVE_GENERATOR.get() to 14369818,
			ModBlocks.MAGMATIC_GENERATOR.get() to 10032418,
			ModBlocks.PINK_GENERATOR.get() to 16729424,
			ModBlocks.NETHER_STAR_GENERATOR.get() to 0xFFFFFF,
			ModBlocks.ENDER_GENERATOR.get() to 2458740,
			ModBlocks.HEATED_REDSTONE_GENERATOR.get() to 11161091,
			ModBlocks.HIGH_TEMPERATURE_FURNACE_GENERATOR.get() to 1773488,
			ModBlocks.HALITOSIS_GENERATOR.get() to 0xA77AA7,
			ModBlocks.FROSTY_GENERATOR.get() to 5139615,
			ModBlocks.DEATH_GENERATOR.get() to 14208412,
			ModBlocks.DISENCHANTMENT_GENERATOR.get() to 3944534,
			ModBlocks.SLIMY_GENERATOR.get() to 0xFFFFFF
		)
	}

}