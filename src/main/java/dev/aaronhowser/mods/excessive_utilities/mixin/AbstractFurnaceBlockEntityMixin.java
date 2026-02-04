package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem;
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

	@Inject(
			method = "getBurnDuration",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void eu$heating_coil_fuel(
			ItemStack fuel,
			CallbackInfoReturnable<Integer> cir
	) {
//		if (fuel.is(ModItems.HEATING_COIL.get())) {
//			cir.setReturnValue(HeatingCoilItem.getFuelBurnTime(fuel));
//		}
	}

}
