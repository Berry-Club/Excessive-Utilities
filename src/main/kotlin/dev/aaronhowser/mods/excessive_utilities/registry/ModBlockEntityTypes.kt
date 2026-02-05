package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockEntityTypeRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.ChandelierBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.DrumBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.GpPanelBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.MagnumTorchBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.PeacefulTableBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlockEntityTypes : AaronBlockEntityTypeRegistry() {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,
			ExcessiveUtilities.MOD_ID
		)

	override fun getBlockEntityRegistry(): DeferredRegister<BlockEntityType<*>> = BLOCK_ENTITY_REGISTRY

	val MAGNUM_TORCH: DeferredHolder<BlockEntityType<*>, BlockEntityType<MagnumTorchBlockEntity>> =
		register("magnum_torch", ::MagnumTorchBlockEntity, ModBlocks.MAGNUM_TORCH)
	val CHANDELIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<ChandelierBlockEntity>> =
		register("chandelier", ::ChandelierBlockEntity, ModBlocks.CHANDELIER)
	val PEACEFUL_TABLE: DeferredHolder<BlockEntityType<*>, BlockEntityType<PeacefulTableBlockEntity>> =
		register("peaceful_table", ::PeacefulTableBlockEntity, ModBlocks.PEACEFUL_TABLE)
	val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<DrumBlockEntity>> =
		register(
			"drum",
			::DrumBlockEntity,
			ModBlocks.STONE_DRUM, ModBlocks.IRON_DRUM, ModBlocks.REINFORCED_LARGE_DRUM, ModBlocks.DEMONICALLY_GARGANTUAN_DRUM, ModBlocks.CREATIVE_DRUM
		)
	val GP_PANEL: DeferredHolder<BlockEntityType<*>, BlockEntityType<GpPanelBlockEntity>> =
		register("solar_panel", {pos, state -> GpPanelBlockEntity(pos, state)}, ModBlocks.SOLAR_PANEL)

}