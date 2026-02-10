package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class CreativeHarvestBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CREATIVE_HARVEST.get(), pos, blockState) {

	var mimicBlockState: BlockState = Blocks.STONE.defaultBlockState()
		set(value) {
			field = value
			setChanged()
		}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val blockStateTag = NbtUtils.writeBlockState(mimicBlockState)
		tag.put(MIMIC_BLOCK_NBT, blockStateTag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		val blockStateTag = tag.getCompound(MIMIC_BLOCK_NBT)
		val readBLockState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), blockStateTag)
		mimicBlockState = readBLockState
	}

	companion object {
		const val MIMIC_BLOCK_NBT = "MimicBlock"
	}

}