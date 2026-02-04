package dev.aaronhowser.mods.excessive_utilities;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public interface ChandelierCarrier {

	default LongOpenHashSet eu$getChandelierBlockPositions() {
		throw new IllegalStateException();
	}

}
