package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
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
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.item.ItemStack
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.core.component.DataComponentGetter
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
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

			else -> error("Unsupported drum block: ${blockState.block}")
		}

	val tank: ConfigurableFluidTank =
		object : ConfigurableFluidTank(capacityGetter) {
			override fun onContentsChanged() {
				setChanged()
			}
		}

	override fun applyImplicitComponents(componentInput: DataComponentGetter) {
		val tankComponent = componentInput.get(ModDataComponents.TANK)
		if (tankComponent != null) {
			tank.setFromFluidContent(tankComponent)
		}
	}

	override fun collectImplicitComponents(components: DataComponentMap.Builder) {
		components.set(ModDataComponents.TANK, SimpleFluidContent.copyOf(tank.copy()))
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.store("Fluid", FluidStack.OPTIONAL_CODEC, tank.fluid)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		tag.read("Fluid", FluidStack.OPTIONAL_CODEC).ifPresent(tank::setFluid)
	}

	companion object {
		fun getFluidCapability(blockEntity: DrumBlockEntity, direction: Direction?): ResourceHandler<FluidResource> {
			return blockEntity.tank
		}

		fun getBlockColor(
			state: BlockState,
			level: BlockAndTintGetter?,
			pos: BlockPos?,
			tintIndex: Int
		): Int {
			if (tintIndex != 1 || level == null || pos == null) return 0xFFFFFFFF.toInt()

			val blockEntity = level.getBlockEntity(pos)
			if (blockEntity !is DrumBlockEntity) return 0xFFFFFFFF.toInt()

			val fluid = blockEntity.tank.fluid
			if (fluid.isEmpty) return 0xFFFFFFFF.toInt()
			return AaronClientUtil.getFluidColor(fluid.fluid)
		}

		fun getItemStackColor(
			itemStack: ItemStack,
			tintIndex: Int
		): Int {
			val content = itemStack.get(ModDataComponents.TANK) ?: return 0xFFFFFFFF.toInt()
			if (content.isEmpty) return 0xFFFFFFFF.toInt()

			return AaronClientUtil.getFluidColor(content.fluid)
		}
	}

}
