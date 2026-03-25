package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ConfigurableFluidTank
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.IntSupplier

class DrumBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : SyncingBlockEntity(ModBlockEntityTypes.DRUM.get(), pos, blockState) {

	override val syncImmediately: Boolean = true

	private val capacityGetter: IntSupplier =
		when (blockState.block) {
			ModBlocks.STONE_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.stoneDrumCapacity.get() }
			ModBlocks.IRON_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.ironDrumCapacity.get() }
			ModBlocks.REINFORCED_LARGE_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.reinforcedLargeDrumCapacity.get() }
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.demonicallyGargantuanDrumCapacity.get() }
			ModBlocks.BEDROCKIUM_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.bedrockDrumCapacity.get() }
			ModBlocks.CREATIVE_DRUM.get() -> IntSupplier { Int.MAX_VALUE }

			else -> IntSupplier { 0 }
		}

	val tank: ConfigurableFluidTank =
		object : ConfigurableFluidTank(capacityGetter) {
			override fun onContentsChanged() {
				setChanged()
			}
		}

	override fun applyImplicitComponents(componentInput: DataComponentInput) {
		val tankComponent = componentInput.get(ModDataComponents.TANK)
		if (tankComponent != null) {
			tank.setFromFluidContent(tankComponent)
		}
	}

	override fun collectImplicitComponents(components: DataComponentMap.Builder) {
		components.set(ModDataComponents.TANK, SimpleFluidContent.copyOf(tank.copy()))
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tank.addToTag(registries, tag)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		tank.loadFromTag(registries, tag)
	}

	companion object {
		fun getFluidCapability(blockEntity: DrumBlockEntity, direction: Direction?): IFluidHandler {
			return blockEntity.tank
		}

		fun getColor(
			state: BlockState,
			level: BlockAndTintGetter?,
			pos: BlockPos?,
			tintIndex: Int
		): Int {
			if (tintIndex != 1 || level == null || pos == null) return 0xFFFFFFFF.toInt()
			val blockEntity = level.getBlockEntity(pos) as? DrumBlockEntity ?: return 0xFFFFFFFF.toInt()

			val fluid = blockEntity.tank.fluid
			if (fluid.isEmpty) return 0xFFFFFFFF.toInt()
			return IClientFluidTypeExtensions.of(fluid.fluid).getTintColor(fluid)
		}

		fun getColor(
			itemStack: ItemStack,
			tintIndex: Int
		): Int {
			val content = itemStack.get(ModDataComponents.TANK) ?: return 0xFFFFFFFF.toInt()
			if (content.isEmpty) return 0xFFFFFFFF.toInt()
			return IClientFluidTypeExtensions.of(content.fluid).getTintColor(content.copy())
		}
	}

}