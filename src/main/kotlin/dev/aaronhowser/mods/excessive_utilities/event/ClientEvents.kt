package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.client.render.GridPowerGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.item.ItemProperties
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ModelEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

@EventBusSubscriber(
	modid = ExcessiveUtilities.MOD_ID,
	value = [Dist.CLIENT]
)
object ClientEvents {

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

}