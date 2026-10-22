package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.nbt.NbtOps
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

class MagicalSnowGlobeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.MAGICAL_SNOW_GLOBE.get(), pos, blockState) {

	var progressComponent = MagicalSnowGlobeProgressComponent.DEFAULT
		set(value) {
			field = value
			setChanged()
		}

	override fun collectImplicitComponents(components: DataComponentMap.Builder) {
		super.collectImplicitComponents(components)
		components.set(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS, progressComponent)
	}

	override fun applyImplicitComponents(componentInput: DataComponentGetter) {
		progressComponent = componentInput.getOrDefault(
			ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS,
			MagicalSnowGlobeProgressComponent.DEFAULT
		)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.store(PROGRESS_NBT, MagicalSnowGlobeProgressComponent.CODEC, progressComponent)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		progressComponent = tag.read(PROGRESS_NBT, MagicalSnowGlobeProgressComponent.CODEC)
			.orElse(MagicalSnowGlobeProgressComponent.DEFAULT)
	}

	companion object {
		const val PROGRESS_NBT = "Progress"
	}

}
