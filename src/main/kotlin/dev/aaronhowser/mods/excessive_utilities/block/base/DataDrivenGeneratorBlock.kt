package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.block.base.entity.CompressibleFeGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.DataDrivenGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class DataDrivenGeneratorBlock(
	val beTypeGetter: () -> BlockEntityType<out DataDrivenGeneratorBlockEntity>
) : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is DataDrivenGeneratorBlockEntity && placer != null) {
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
			CompressibleFeGeneratorBlockEntity::tick
		)
	}

	companion object {
		fun ender(tier: Int) = DataDrivenGeneratorBlock {
			when (tier) {
				1 -> ModBlockEntityTypes.ENDER_GENERATOR.get()
				2 -> ModBlockEntityTypes.ENDER_GENERATOR_MK2.get()
				3 -> ModBlockEntityTypes.ENDER_GENERATOR_MK3.get()
				else -> error("Invalid tier: $tier")
			}
		}
	}

}