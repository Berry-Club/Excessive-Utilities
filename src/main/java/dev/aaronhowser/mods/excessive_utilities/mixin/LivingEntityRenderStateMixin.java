package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.client.render.CursedRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements CursedRenderState {
	@Unique
	private boolean excessive_utilities$isCursed;

	@Override
	public boolean excessiveUtilities$isCursed() {
		return excessive_utilities$isCursed;
	}

	@Override
	public void excessiveUtilities$setCursed(boolean cursed) {
		excessive_utilities$isCursed = cursed;
	}
}
