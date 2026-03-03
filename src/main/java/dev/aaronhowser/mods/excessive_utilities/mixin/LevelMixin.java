package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.ChandelierCarrier;
import dev.aaronhowser.mods.excessive_utilities.MagnumTorchCarrier;
import dev.aaronhowser.mods.excessive_utilities.SoundMufflerCarrier;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin implements ChandelierCarrier, MagnumTorchCarrier, SoundMufflerCarrier {

	@Unique
	LongOpenHashSet eu$chandelierBlockPositions = new LongOpenHashSet();
	@Unique
	LongOpenHashSet eu$magnumTorchBlockPositions = new LongOpenHashSet();
	@Unique
	LongOpenHashSet eu$soundMufflerBlockPositions = new LongOpenHashSet();

	@Unique
	@Override
	public LongOpenHashSet eu$getChandelierBlockPositions() {
		return this.eu$chandelierBlockPositions;
	}

	@Unique
	@Override
	public LongOpenHashSet eu$getMagnumTorchBlockPositions() {
		return this.eu$magnumTorchBlockPositions;
	}

	@Unique
	@Override
	public LongOpenHashSet eu$getSoundMufflerBlockPositions() {
		return this.eu$soundMufflerBlockPositions;
	}

	@Inject(
			method = "tickBlockEntities",
			at = @At("HEAD")
	)
	private void eu$tickBlockEntities(CallbackInfo ci) {
		this.eu$chandelierBlockPositions.clear();
		this.eu$magnumTorchBlockPositions.clear();
		this.eu$soundMufflerBlockPositions.clear();
	}

}
