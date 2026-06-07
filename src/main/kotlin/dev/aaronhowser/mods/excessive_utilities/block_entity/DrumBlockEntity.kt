package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ConfigurableFluidTank
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
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
		private val FLUID_COLORS: MutableMap<Fluid, Int> = mutableMapOf()

		fun getFluidCapability(blockEntity: DrumBlockEntity, direction: Direction?): IFluidHandler {
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
			return getFluidColor(fluid.fluid)
		}

		private fun getFluidColor(fluid: Fluid): Int {
			val existing = FLUID_COLORS[fluid]
			if (existing != null) return existing

			val newColor = computeFluidColor(fluid)
			FLUID_COLORS[fluid] = newColor
			return newColor
		}

		private fun computeFluidColor(fluid: Fluid): Int {
			val ext = IClientFluidTypeExtensions.of(fluid)
			val tintColor = ext.tintColor
			if (tintColor != 0xFFFFFFFF.toInt()) {
				return tintColor
			}

			val textureLocation = ext.stillTexture
			val atlasTexture = Minecraft.getInstance()
				.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
				.apply(textureLocation)

			if (atlasTexture == null) {
				return 0xFFFFFFFF.toInt()
			}

			val pixels = buildList {
				val contents = atlasTexture.contents()
				val width = contents.width()
				val height = contents.height()

				val nativeImage = contents.originalImage

				for (x in 0 until width) {
					for (y in 0 until height) {
						val rgba = nativeImage.getPixelRGBA(x, y)
						val a = rgba ushr 24 and 0xFF
						if (a > 10) {
							add(rgba)
						}
					}
				}
			}

			if (pixels.isEmpty()) {
				return 0xFFFFFFFF.toInt()
			}

			var totalRed = 0L
			var totalGreen = 0L
			var totalBlue = 0L

			for (rgba in pixels) {
				val r = rgba ushr 16 and 0xFF
				val g = rgba ushr 8 and 0xFF
				val b = rgba and 0xFF

				totalRed += r
				totalGreen += g
				totalBlue += b
			}

			val count = pixels.size
			val averageRed = (totalRed / count).toInt()
			val averageGreen = (totalGreen / count).toInt()
			val averageBlue = (totalBlue / count).toInt()

			val argb = (0xFF shl 24) or (averageRed shl 16) or (averageGreen shl 8) or averageBlue
			return argb

//			val rgba = (averageRed shl 24) or (averageGreen shl 16) or (averageBlue shl 8) or 0xFF
//			return rgba
		}

		fun getItemStackColor(
			itemStack: ItemStack,
			tintIndex: Int
		): Int {
			val content = itemStack.get(ModDataComponents.TANK) ?: return 0xFFFFFFFF.toInt()
			if (content.isEmpty) return 0xFFFFFFFF.toInt()

			return getFluidColor(content.fluid)
		}
	}

}