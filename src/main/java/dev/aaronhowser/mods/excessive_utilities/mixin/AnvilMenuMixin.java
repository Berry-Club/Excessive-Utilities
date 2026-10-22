package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.objectweb.asm.Opcodes;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

	@Redirect(
			method = "createResult",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z",
					ordinal = 1
			)
	)
	private boolean eu$disableTooExpensiveForTaggedItems(Player player) {
		AnvilMenu anvilMenu = (AnvilMenu) (Object) this;
		ItemStack inputStack = anvilMenu
				.getSlot(AnvilMenu.INPUT_SLOT)
				.getItem();

		return player.hasInfiniteMaterials()
				|| inputStack.is(ModItemTagsProvider.Companion.getINFINITE_ENCHANT_STACKING());
	}

}