package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.DrumBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.SoundMufflerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.GridPowerGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.RingRechargeGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.WandRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.OpiniumCoreBEWLR
import dev.aaronhowser.mods.excessive_utilities.client.render.block_entity.*
import dev.aaronhowser.mods.excessive_utilities.client.render.entity.FlatTransferNodeEntityRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.entity.MagicalBoomerangEntityRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.layer.AngelRingWingsLayer
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.ClientKeyHandler
import dev.aaronhowser.mods.excessive_utilities.item.*
import dev.aaronhowser.mods.excessive_utilities.registry.*
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.DyeColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

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
			ModBlocks.INVERTED_ETHEREAL_GLASS.get(),
			ModBlocks.HEART_GLASS.get(),
			ModBlocks.EDGED_GLASS.get(),
			ModBlocks.SWIRLING_GLASS.get(),
			ModBlocks.THICKENED_GLASS.get(),
			ModBlocks.PATTERNED_GLASS.get(),
			ModBlocks.GLASS_BRICKS.get(),
			ModBlocks.GOLDEN_EDGED_GLASS.get(),
			ModBlocks.GLOWING_GLASS.get(),
			ModBlocks.BLOCK_OF_UNSTABLE_INGOT.get()
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
			ModItems.ANGEL_RING.get(),
			AngelRingItem.RING_TYPE,
			AngelRingItem::hasEntityPredicate
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

		ItemProperties.register(
			ModItems.MAGICAL_BOOMERANG.get(),
			MagicalBoomerangItem.THROWN_PREDICATE,
			MagicalBoomerangItem::isThrown
		)

		ItemProperties.register(
			ModItems.COMPOUND_BOW.get(),
			ResourceLocation.withDefaultNamespace("pull")
		) { stack, level, entity, seed ->
			if (entity == null || entity.useItem != stack) {
				return@register 0f
			}

			return@register (stack.getUseDuration(entity) - entity.useItemRemainingTicks) / 20f
		}

		ItemProperties.register(
			ModItems.COMPOUND_BOW.get(),
			ResourceLocation.withDefaultNamespace("pulling")
		) { stack, level, entity, seed ->
			if (entity != null && entity.isUsingItem && entity.getUseItem() == stack) 1.0f else 0.0f
		}
	}

	@SubscribeEvent
	fun addEntityRenderLayers(event: EntityRenderersEvent.AddLayers) {

		for (skin in event.skins) {
			val renderer = event.getSkin(skin) as? PlayerRenderer
			if (renderer != null) {
				val layer = AngelRingWingsLayer(renderer)
				renderer.addLayer(layer)
			}
		}

	}

	@SubscribeEvent
	fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
		event.register(HeatingCoilItem::getItemColor, ModItems.HEATING_COIL.get())
		event.register(SunCrystalItem::getItemColor, ModItems.SUN_CRYSTAL.get())

		event.register(
			UnstableIngotItem::getColor,
			ModItems.UNSTABLE_INGOT
		)

		event.register(PaintbrushItem::getItemColor, ModItems.PAINTBRUSH.get())

		event.register(
			DrumBlockEntity::getColor,
			ModBlocks.STONE_DRUM.get(),
			ModBlocks.IRON_DRUM.get(),
			ModBlocks.REINFORCED_LARGE_DRUM.get(),
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(),
			ModBlocks.BEDROCKIUM_DRUM.get(),
			ModBlocks.CREATIVE_DRUM.get()
		)


		for (color in DyeColor.entries) {
			event.register(
				{ stack, tintIndex -> color.textureDiffuseColor },
				ModBlocks.getLapisCaelestis(color).get()
			)
		}
	}

	@SubscribeEvent
	fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {

		event.register(
			DrumBlockEntity::getColor,
			ModBlocks.STONE_DRUM.get(),
			ModBlocks.IRON_DRUM.get(),
			ModBlocks.REINFORCED_LARGE_DRUM.get(),
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(),
			ModBlocks.BEDROCKIUM_DRUM.get(),
			ModBlocks.CREATIVE_DRUM.get()
		)

		for (color in DyeColor.entries) {
			event.register(
				{ state, level, pos, tintIndex -> color.textureDiffuseColor },
				ModBlocks.getLapisCaelestis(color).get()
			)
		}

	}

	@SubscribeEvent
	fun registerGuiLayers(event: RegisterGuiLayersEvent) {
		event.registerAbove(
			VanillaGuiLayers.CROSSHAIR,
			GridPowerGuiRenderer.LAYER_NAME,
			GridPowerGuiRenderer::render
		)

		event.registerAbove(
			VanillaGuiLayers.CROSSHAIR,
			RingRechargeGuiRenderer.LAYER_NAME,
			RingRechargeGuiRenderer::render
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
		event.registerEntityRenderer(ModEntityTypes.FLAT_TRANSFER_NODE.get(), ::FlatTransferNodeEntityRenderer)
		event.registerEntityRenderer(ModEntityTypes.MAGICAL_BOOMERANG.get(), ::MagicalBoomerangEntityRenderer)

		event.registerBlockEntityRenderer(ModBlockEntityTypes.ENDER_QUARRY.get(), ::EnderQuarryBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.MANUAL_MILL.get(), ::ManualMillBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.RESTURBED_MOB_SPAWNER.get(), ::ResturbedMobSpawnerBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.RAINBOW_GENERATOR.get(), ::RainbowGeneratorBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.ENDER_PORCUPINE.get(), ::EnderPorcupineBER)
	}

	@SubscribeEvent
	fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
		ModMenuTypes.registerScreens(event)
	}

	@SubscribeEvent
	fun beforeClientTick(event: ClientTickEvent.Pre) {
		ClientKeyHandler.updateControls()
	}

	@SubscribeEvent
	fun onPlaySound(event: PlaySoundEvent) {
		SoundMufflerBlockEntity.handleSoundSourceEvent(event)
	}

	@SubscribeEvent
	fun onRenderBlockHighlight(event: RenderHighlightEvent.Block) {
		WandRenderer.renderTargetBlocks(event)
	}

	@SubscribeEvent
	fun onItemTooltip(event: ItemTooltipEvent) {
		UnstableIngotItem.handleTooltip(event)
	}

}