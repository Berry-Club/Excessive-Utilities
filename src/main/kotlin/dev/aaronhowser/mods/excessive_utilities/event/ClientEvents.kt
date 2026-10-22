package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.DrumBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.SoundMufflerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.particle.VomitParticle
import dev.aaronhowser.mods.excessive_utilities.client.render.AngelWingRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.GridPowerGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.RingRechargeGuiRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.WandRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.ManualMillBEWLR
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.OpiniumCoreBEWLR
import dev.aaronhowser.mods.excessive_utilities.client.render.bewlr.TesseractBEWLR
import dev.aaronhowser.mods.excessive_utilities.client.render.block_entity.*
import dev.aaronhowser.mods.excessive_utilities.client.render.entity.FlatTransferNodeEntityRenderer
import dev.aaronhowser.mods.excessive_utilities.client.render.entity.MagicalBoomerangEntityRenderer
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.ClientKeyHandler
import dev.aaronhowser.mods.excessive_utilities.item.*
import dev.aaronhowser.mods.excessive_utilities.registry.*
import dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes
import net.minecraft.ChatFormatting
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.DyeColor
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.client.color.block.BlockTintSources
import net.minecraft.client.renderer.block.BlockAndTintGetter
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import top.theillusivec4.curios.api.client.ICurioRenderer

@EventBusSubscriber(
	modid = ExcessiveUtilities.MOD_ID,
	value = [Dist.CLIENT]
)
object ClientEvents {

	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {

	}

    @SubscribeEvent
    fun registerItemProperties(event: RegisterRangeSelectItemModelPropertyEvent) {
        event.register(ExcessiveUtilities.modResource("item_predicate"), dev.aaronhowser.mods.excessive_utilities.client.item.ItemVariantProperty.CODEC)
    }

    @SubscribeEvent
    fun registerItemColors(event: RegisterColorHandlersEvent.ItemTintSources) {
        event.register(ExcessiveUtilities.modResource("item_color"), dev.aaronhowser.mods.excessive_utilities.client.item.ItemColorSource.CODEC)
    }

    @SubscribeEvent
    fun registerBlockColors(event: RegisterColorHandlersEvent.BlockTintSources) {
        val fluidTint = object : BlockTintSource {
            override fun color(state: BlockState): Int = -1
            override fun colorInWorld(state: BlockState, level: BlockAndTintGetter, pos: BlockPos): Int = DrumBlockEntity.getBlockColor(state, level, pos, 1)
        }
        event.register(listOf(BlockTintSources.constant(-1), fluidTint),
            ModBlocks.STONE_DRUM.get(), ModBlocks.IRON_DRUM.get(), ModBlocks.REINFORCED_LARGE_DRUM.get(),
            ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(), ModBlocks.BEDROCKIUM_DRUM.get(), ModBlocks.CREATIVE_DRUM.get())
        for (color in DyeColor.entries) {
            event.register(listOf(BlockTintSources.constant(color.textureDiffuseColor)), ModBlocks.getLapisCaelestis(color).get())
        }
    }

	@SubscribeEvent
	fun recipesReceived(event: RecipesReceivedEvent) {
		ModRecipes.clientRecipes = event.recipeMap
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
    fun registerSpecialModels(event: RegisterSpecialModelRendererEvent) {
        event.register(ExcessiveUtilities.modResource("manual_mill"), ManualMillBEWLR.Unbaked.CODEC)
        event.register(ExcessiveUtilities.modResource("opinium_core"), OpiniumCoreBEWLR.Unbaked.CODEC)
        event.register(ExcessiveUtilities.modResource("tesseract"), TesseractBEWLR.Unbaked.CODEC)
    }

	@SubscribeEvent
	fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
		event.registerEntityRenderer(ModEntityTypes.FLAT_TRANSFER_NODE.get(), ::FlatTransferNodeEntityRenderer)
		event.registerEntityRenderer(ModEntityTypes.MAGICAL_BOOMERANG.get(), ::MagicalBoomerangEntityRenderer)

		event.registerBlockEntityRenderer(ModBlockEntityTypes.ENDER_QUARRY.get()) { _ -> EnderQuarryBER() } 
		event.registerBlockEntityRenderer(ModBlockEntityTypes.MANUAL_MILL.get(), ::ManualMillBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.RESTURBED_MOB_SPAWNER.get(), ::ResturbedMobSpawnerBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.RAINBOW_GENERATOR.get(), ::RainbowGeneratorBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.ENDER_PORCUPINE.get(), ::EnderPorcupineBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.ENDER_COLLECTOR.get(), ::EnderCollectorBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.QED.get(), ::QedBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CREATIVE_HARVEST.get(), ::CreativeHarvestBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.MAGNUM_TORCH.get(), ::MagnumTorchBER)
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CHANDELIER.get(), ::ChandelierBER)
	}

	@SubscribeEvent
	fun registerParticleProviders(event: RegisterParticleProvidersEvent) {
		event.registerSpriteSet(ModParticleTypes.VOMIT.get()) { sprites ->
			ParticleProvider<SimpleParticleType> { type, level, x, y, z, xSpeed, ySpeed, zSpeed, random ->
				VomitParticle.Provider(sprites).createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, random)
			}
		}
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
	fun onRenderBlockHighlight(event: ExtractBlockOutlineRenderStateEvent) {
		WandRenderer.renderTargetBlocks(event)
	}

	@SubscribeEvent
	fun onItemTooltip(event: ItemTooltipEvent) {
		UnstableIngotItem.handleTooltip(event)

		val stack = event.itemStack

		if (
			!ExcessiveUtilities.IS_ATHENA_LOADED
			&& ClientConfig.CONFIG.athenaTooltip.get()
			&& stack.isItem(ModItemTagsProvider.ATHENA_COMPATIBLE)
		) {
			event.toolTip += ModMenuLang.ATHENA_COMPATIBLE.toComponent().withStyle(ChatFormatting.DARK_GRAY)
		}

		if (stack.isItem(ModItemTagsProvider.NOT_YET_IMPLEMENTED)) {
			event.toolTip += ModMenuLang.NOT_YET_IMPLEMENTED.toComponent().withStyle(ChatFormatting.DARK_RED)
		}
	}

	@SubscribeEvent
	fun addEntityRenderLayers(event: EntityRenderersEvent.AddLayers) {
		ICurioRenderer.register(ModItems.ANGEL_RING.get(), ::AngelWingRenderer)
	}

}
