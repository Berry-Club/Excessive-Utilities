package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.client.render.GridPowerGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.OpiniumCoreBEWLR
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.item.WateringCanItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.world.item.BlockItem
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.client.model.generators.ItemModelProvider

@EventBusSubscriber(
	modid = ExcessiveUtilities.MOD_ID,
	value = [Dist.CLIENT]
)
object ClientEvents {

	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {

		// Doing it here instead of in the model because Athena doesn't even LOAD the model
		val cutout = listOf(
			ModBlocks.INEFFABLE_GLASS.get(),
			ModBlocks.INVERTED_ETHEREAL_GLASS.get()
		)

		for (block in cutout) {
			ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout())
		}

		val translucent = listOf(
			ModBlocks.DARK_INEFFABLE_GLASS.get()
		)

		for (block in translucent) {
			ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent())
		}

	}

	@SubscribeEvent
	fun onModelRegistry(event: ModelEvent.RegisterAdditional) {
		ItemProperties.register(
			ModItems.GOLDEN_LASSO.get(),
			EntityLassoItem.HAS_ENTITY,
			EntityLassoItem::hasEntityPredicate
		)

		ItemProperties.register(
			ModItems.CURSED_LASSO.get(),
			EntityLassoItem.HAS_ENTITY,
			EntityLassoItem::hasEntityPredicate
		)

		ItemProperties.register(
			ModItems.ENDER_SHARD.get(),
			ModItemModelProvider.ENDER_SHARD_COUNT
		) { stack, level, entity, seed -> stack.count.toFloat() }

		ItemProperties.register(
			ModItems.WATERING_CAN.get(),
			WateringCanItem.IS_BROKEN_PREDICATE,
			WateringCanItem::isBroken
		)
	}

	@SubscribeEvent
	fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
		event.register(HeatingCoilItem::getItemColor, ModItems.HEATING_COIL.get())

		val generatorColors = GeneratorBlock.getColors()

		event.register(
			{ stack, tintIndex ->
				val item = stack.item
				if (item is BlockItem && tintIndex == 0) {
					val block = item.block
					generatorColors.getOrDefault(block, 0xFFFFFF)
				} else {
					0xFFFFFF
				}
			},
			ModBlocks.FURNACE_GENERATOR.get(),
			ModBlocks.SURVIVAL_GENERATOR.get(),
			ModBlocks.CULINARY_GENERATOR.get(),
			ModBlocks.POTION_GENERATOR.get(),
			ModBlocks.EXPLOSIVE_GENERATOR.get(),
			ModBlocks.MAGMATIC_GENERATOR.get(),
			ModBlocks.PINK_GENERATOR.get(),
			ModBlocks.NETHER_STAR_GENERATOR.get(),
			ModBlocks.ENDER_GENERATOR.get(),
			ModBlocks.HEATED_REDSTONE_GENERATOR.get(),
			ModBlocks.HIGH_TEMPERATURE_FURNACE_GENERATOR.get(),
			ModBlocks.HALITOSIS_GENERATOR.get(),
			ModBlocks.FROSTY_GENERATOR.get(),
			ModBlocks.DEATH_GENERATOR.get(),
			ModBlocks.DISENCHANTMENT_GENERATOR.get(),
			ModBlocks.SLIMY_GENERATOR.get(),
		)
	}

	@SubscribeEvent
	fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {

		val generatorColors = GeneratorBlock.getColors()

		event.register(
			{ state, level, pos, tintIndex ->
				if (tintIndex == 0) {
					generatorColors.getOrDefault(state.block, 0xFFFFFF)
				} else {
					0xFFFFFF
				}
			},
			ModBlocks.FURNACE_GENERATOR.get(),
			ModBlocks.SURVIVAL_GENERATOR.get(),
			ModBlocks.CULINARY_GENERATOR.get(),
			ModBlocks.POTION_GENERATOR.get(),
			ModBlocks.EXPLOSIVE_GENERATOR.get(),
			ModBlocks.MAGMATIC_GENERATOR.get(),
			ModBlocks.PINK_GENERATOR.get(),
			ModBlocks.NETHER_STAR_GENERATOR.get(),
			ModBlocks.ENDER_GENERATOR.get(),
			ModBlocks.HEATED_REDSTONE_GENERATOR.get(),
			ModBlocks.HIGH_TEMPERATURE_FURNACE_GENERATOR.get(),
			ModBlocks.HALITOSIS_GENERATOR.get(),
			ModBlocks.FROSTY_GENERATOR.get(),
			ModBlocks.DEATH_GENERATOR.get(),
			ModBlocks.DISENCHANTMENT_GENERATOR.get(),
			ModBlocks.SLIMY_GENERATOR.get(),
		)

	}

	@SubscribeEvent
	fun registerGuiLayers(event: RegisterGuiLayersEvent) {
		event.registerAbove(
			VanillaGuiLayers.CROSSHAIR,
			GridPowerGuiRenderer.LAYER_NAME,
			GridPowerGuiRenderer::renderGridPower
		)
	}

	@SubscribeEvent
	fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
		event.registerItem(
			OpiniumCoreBEWLR.ClientItemExtensions,
			ModItems.OPINIUM_CORE.get()
		)
	}

	@SubscribeEvent
	fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
		event.registerEntityRenderer(ModEntityTypes.FLAT_TRANSFER_NODE.get(), ::NoopRenderer)
	}

	@SubscribeEvent
	fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
		ModMenuTypes.registerScreens(event)
	}

}