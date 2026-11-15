package dev.aaronhowser.mods.excessive_utilities.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.aaronhowser.mods.excessive_utilities.handler.CurseHandler;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

	@ModifyArg(
			method = "submit*",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
			),
			index = 6
	)
	private int excessiveUtilities$tintCursedEntity(
			int color,
			@Local(argsOnly = true, name = "state") LivingEntityRenderState state
	) {
		return state.getRenderDataOrDefault(CurseHandler.IS_CURSED, false)
				? CurseHandler.darkenColor(color)
				: color;
	}
}