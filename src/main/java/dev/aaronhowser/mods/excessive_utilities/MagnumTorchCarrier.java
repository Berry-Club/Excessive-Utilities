package dev.aaronhowser.mods.excessive_utilities;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public interface MagnumTorchCarrier {

	default LongOpenHashSet eu$getMagnumTorchBlockPositions() {
		throw new IllegalStateException();
	}

}
