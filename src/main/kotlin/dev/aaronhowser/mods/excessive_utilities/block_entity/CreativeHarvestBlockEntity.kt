package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class CreativeHarvestBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SyncingBlockEntity(ModBlockEntityTypes.CREATIVE_HARVEST.get(), pos, blockState) {

	override val syncImmediately: Boolean = true

	var mimicStack: ItemStack = ItemStack.EMPTY
		private set

	var mimicBlockState: BlockState = Blocks.STONE.defaultBlockState()
		private set

	var visibleDirections: List<Direction> = Direction.Plane.HORIZONTAL.toList()
		private set

	fun updateVisibleDirections(level: LevelAccessor) {
		val newVisibleDirections = mutableListOf<Direction>()

		for (dir in Direction.Plane.HORIZONTAL) {
			val posThere = blockPos.relative(dir)
			val stateThere = level.getBlockState(posThere)
			val blocksView = stateThere.isFaceSturdy(level, posThere, dir.opposite)

			if (!blocksView) {
				newVisibleDirections.add(dir)
			}
		}

		visibleDirections = newVisibleDirections
	}

	fun setMimic(stack: ItemStack): Boolean {
		val item = stack.item
		val itemBlockState = (item as? BlockItem)?.block?.defaultBlockState()

		if (itemBlockState != null && !itemBlockState.isBlock(ModBlockTagsProvider.CREATIVE_HARVEST_BLACKLIST)) {
			mimicStack = stack.copy()
			mimicBlockState = itemBlockState
			setChanged()
			return true
		}

		return level?.isClientSide.isTrue()
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val blockStateTag = NbtUtils.writeBlockState(mimicBlockState)
		tag.put(MIMIC_BLOCK_NBT, blockStateTag)

		val stackTag = mimicStack.saveOptional(registries)
		tag.put(MIMIC_STACK_NBT, stackTag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		val blockStateTag = tag.getCompound(MIMIC_BLOCK_NBT)
		val readBLockState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), blockStateTag)
		mimicBlockState = readBLockState

		val stackTag = tag.getCompound(MIMIC_STACK_NBT)
		mimicStack = ItemStack.parseOptional(registries, stackTag)
	}

	companion object {
		const val MIMIC_STACK_NBT = "MimicStack"
		const val MIMIC_BLOCK_NBT = "MimicBlock"
	}

}