package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem;
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

	@Redirect(
			method = "serverTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
			)
	)
	private static void eu$dontBurnCoil(ItemStack instance, int decrement) {
		if (instance.is(ModItems.HEATING_COIL)) {
			HeatingCoilItem.burnInFuelSlot(instance);
		} else {
			instance.shrink(decrement);
		}
	}

}
