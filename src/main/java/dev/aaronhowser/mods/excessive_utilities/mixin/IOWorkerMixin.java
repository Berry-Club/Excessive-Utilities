package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry.QuantumQuarryDimension;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.IOWorker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(IOWorker.class)
public class IOWorkerMixin {

	@Inject(
			method = "store*",
			at = @At("HEAD"),
			cancellable = true
	)
	private void eu$discardQuantumQuarryData(
			ChunkPos pos,
			@Coerce Object o,
			CallbackInfoReturnable<CompletableFuture<Void>> cir
	) {
		IOWorker worker = (IOWorker) (Object) this;
		if (!QuantumQuarryDimension.isTransientDimension(worker.storageInfo().dimension())) return;

		cir.setReturnValue(CompletableFuture.completedFuture(null));
	}

}