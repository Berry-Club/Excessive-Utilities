package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.ChandelierCarrier;
import dev.aaronhowser.mods.excessive_utilities.MagnumTorchCarrier;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Level.class)
public abstract class LevelMixin implements ChandelierCarrier, MagnumTorchCarrier {

	@Unique
	LongOpenHashSet eu$chandelierBlockPositions = new LongOpenHashSet();
	@Unique
	LongOpenHashSet eu$magnumTorchBlockPositions = new LongOpenHashSet();

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

}
