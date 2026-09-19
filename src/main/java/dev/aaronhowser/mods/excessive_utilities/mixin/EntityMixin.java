package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig;
import dev.aaronhowser.mods.excessive_utilities.handler.CurseHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@ModifyVariable(
			method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V",
			at = @At("HEAD"),
			argsOnly = true,
			ordinal = 1
	)
	private float eu$lowerCursedMobSoundPitch(float pitch) {
		Entity entity = (Entity) (Object) this;
		if (!CurseHandler.INSTANCE.isCursed(entity)) return pitch;

		float pitchReduction = ServerConfig.CONFIG
				.getCursedMobSoundPitchReduction()
				.get()
				.floatValue();

		return pitch - pitchReduction;
	}

}