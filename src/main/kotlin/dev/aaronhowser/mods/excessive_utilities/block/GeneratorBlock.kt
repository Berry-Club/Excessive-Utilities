package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.block.base.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.generator.RainbowGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator.RainbowGeneratorHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.*
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
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
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidUtil

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

	override fun <T : BlockEntity> getTicker(
		level: Level,
		state: BlockState,
		blockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			beTypeGetter(),
			GeneratorBlockEntity.Companion::tick
		)
	}

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		val blockEntity = level.getBlockEntity(pos)

		if (blockEntity is RainbowGeneratorBlockEntity) {
			if (level is ServerLevel) {
				val handler = RainbowGeneratorHandler.get(level).getGeneratorNetwork(player)

				val allTypes = GeneratorType.NON_RAINBOW
				val activeTypes = handler.getActiveGeneratorTypes()

				val yesComponent = Component.literal("Active: ")
				val noComponent = Component.literal("Inactive: ")

				var anyActive = false
				var anyInactive = false

				for (type in allTypes) {
					if (type in activeTypes) {
						anyActive = true
						yesComponent.append("${type.name}, ")
					} else {
						anyInactive = true
						noComponent.append("${type.name}, ")
					}
				}

				player.tell(Component.literal("Rainbow Generator Status: "))
				if (anyActive) player.tell(yesComponent)
				if (anyInactive) player.tell(noComponent)
			}

			return InteractionResult.sidedSuccess(level.isClientSide)
		} else if (blockEntity is MenuProvider) {
			player.openMenu(blockEntity)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	override fun useItemOn(
		stack: ItemStack,
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hand: InteractionHand,
		hitResult: BlockHitResult
	): ItemInteractionResult {
		val fluidTransferred = FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction)

		return if (fluidTransferred) {
			ItemInteractionResult.sidedSuccess(level.isClientSide)
		} else {
			ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
		}
	}

	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
		if (!state.isBlock(newState.block)) {
			val be = level.getBlockEntity(pos)
			if (be is ContainerContainer) {
				val container = be.getContainer()
				if (container != null) Containers.dropContents(level, pos, container)
			}
		}

		super.onRemove(state, level, pos, newState, movedByPiston)
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
		val LIT: BooleanProperty = BlockStateProperties.LIT

		fun getColors(): Map<Block, Int> {
			return mapOf(
				ModBlocks.FURNACE_GENERATOR.get() to 0xFFFFFF,
				ModBlocks.SURVIVAL_GENERATOR.get() to 0xFFFFFF,
				ModBlocks.CULINARY_GENERATOR.get() to 0xFFFFFF,
				ModBlocks.POTION_GENERATOR.get() to 0x5411B1,
				ModBlocks.EXPLOSIVE_GENERATOR.get() to 14369818,
				ModBlocks.MAGMATIC_GENERATOR.get() to 0xDB441A,
				ModBlocks.PINK_GENERATOR.get() to 0xFF4550,
				ModBlocks.NETHER_STAR_GENERATOR.get() to 0xFFFFFF,
				ModBlocks.ENDER_GENERATOR.get() to 0x258474,
				ModBlocks.HEATED_REDSTONE_GENERATOR.get() to 0xAA4E03,
				ModBlocks.HIGH_TEMPERATURE_FURNACE_GENERATOR.get() to 0x1B0FB0,
				ModBlocks.HALITOSIS_GENERATOR.get() to 0xA77AA7,
				ModBlocks.FROSTY_GENERATOR.get() to 0x4E6C9F,
				ModBlocks.DEATH_GENERATOR.get() to 0xD8CD9C,
				ModBlocks.DISENCHANTMENT_GENERATOR.get() to 0x3C3056,
				ModBlocks.SLIMY_GENERATOR.get() to 0xFFFFFF
			)
		}
	}

}