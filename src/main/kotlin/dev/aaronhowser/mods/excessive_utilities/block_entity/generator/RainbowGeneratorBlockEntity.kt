package dev.aaronhowser.mods.excessive_utilities.block_entity.generator

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator.RainbowGeneratorHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource

class RainbowGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GeneratorBlockEntity(ModBlockEntityTypes.RAINBOW_GENERATOR.get(), pos, blockState) {

	override val generatorType: GeneratorType = GeneratorType.RAINBOW

	override val energyStorage: SimpleEnergyHandler = SimpleEnergyHandler(1_000_000_000)

	override fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource>? = null
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
