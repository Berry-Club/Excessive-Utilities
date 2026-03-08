package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.SoundMufflerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.GridPowerGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.RingRechargeGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.OpiniumCoreBEWLR
import dev.aaronhowser.mods.excessive_utilities.client.render.block_entity.EnderQuarryBER
import dev.aaronhowser.mods.excessive_utilities.client.render.entity.FlatTransferNodeEntityRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.entity.MagicalBoomerangEntityRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.layer.AngelRingWingsLayer
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.ClientKeyHandler
import dev.aaronhowser.mods.excessive_utilities.item.EntityLassoItem
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.item.MagicalBoomerangItem
import dev.aaronhowser.mods.excessive_utilities.item.WateringCanItem
import dev.aaronhowser.mods.excessive_utilities.registry.*
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.player.PlayerRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent
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

		for (color in DyeColor.entries) {
			event.register(
				{ stack, tintIndex -> color.textureDiffuseColor },
				ModBlocks.getColoredStone(color).get(),
				ModBlocks.getColoredCobblestone(color).get(),
				ModBlocks.getColoredStoneBricks(color).get(),
				ModBlocks.getColoredBricks(color).get(),
				ModBlocks.getColoredPlanks(color).get(),
				ModBlocks.getColoredCoalBlock(color).get(),
				ModBlocks.getColoredRedstoneBlock(color).get(),
				ModBlocks.getColoredLapisBlock(color).get(),
				ModBlocks.getColoredObsidian(color).get(),
				ModBlocks.getColoredQuartz(color).get(),
				ModBlocks.getColoredSoulSand(color).get(),
			)
		}
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

		for (color in DyeColor.entries) {
			event.register(
				{ state, level, pos, tintIndex -> color.textureDiffuseColor },
				ModBlocks.getColoredStone(color).get(),
				ModBlocks.getColoredCobblestone(color).get(),
				ModBlocks.getColoredStoneBricks(color).get(),
				ModBlocks.getColoredBricks(color).get(),
				ModBlocks.getColoredPlanks(color).get(),
				ModBlocks.getColoredCoalBlock(color).get(),
				ModBlocks.getColoredRedstoneBlock(color).get(),
				ModBlocks.getColoredLapisBlock(color).get(),
				ModBlocks.getColoredObsidian(color).get(),
				ModBlocks.getColoredQuartz(color).get(),
				ModBlocks.getColoredSoulSand(color).get(),
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

}