package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator.RainbowGeneratorHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.items.IItemHandlerModifiable

class RainbowGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GeneratorBlockEntity(ModBlockEntityTypes.RAINBOW_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.RAINBOW

	override val energyStorage: EnergyStorage = EnergyStorage(1_000_000_000)

	override val container: GeneratorContainer? = null
	override fun getItemHandler(direction: Direction?): IItemHandlerModifiable? = null
	override fun isValidInput(itemStack: ItemStack): Boolean = false
	override fun isValidUpgrade(itemStack: ItemStack): Boolean = false
	override fun isValidSecondaryInput(itemStack: ItemStack): Boolean = false

	override fun tryStartBurning(level: ServerLevel): Boolean {
		val owner = ownerUuid ?: return false
		val network = RainbowGeneratorHandler.get(level).getGeneratorNetwork(owner)
		if (network.rainbowGeneratedThisTick) return false

		val shouldGenerate = network.allTypesActive()

		if (shouldGenerate) {
			fePerTick = ServerConfig.CONFIG.rainbowGeneratorFePerTick.get()
			burnTimeRemaining = 1
			network.rainbowGeneratedThisTick = true
			setChanged()
		}

		return shouldGenerate
	}
}