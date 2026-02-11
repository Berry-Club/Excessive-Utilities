package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable

class RainbowGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GeneratorBlockEntity(ModBlockEntityTypes.RAINBOW_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.RAINBOW

	override val container: GeneratorContainer? = null
	override fun getItemHandler(direction: Direction?): IItemHandlerModifiable? = null
	override fun isValidInput(itemStack: ItemStack): Boolean = false
	override fun isValidUpgrade(itemStack: ItemStack): Boolean = false
	override fun isValidSecondaryInput(itemStack: ItemStack): Boolean = false

	override fun tryStartBurning(level: ServerLevel): Boolean {
		return true
	}
}