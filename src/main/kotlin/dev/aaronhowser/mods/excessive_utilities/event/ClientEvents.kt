package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.client.render.GridPowerGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.OpiniumCoreBEWLR
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

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
	}

	@SubscribeEvent
	fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
		event.register(HeatingCoilItem::getItemColor, ModItems.HEATING_COIL.get())
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