package dev.aaronhowser.mods.excessive_utilities.mixin;

import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {

	@Redirect(
			method = "extractLabels",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z"
			)
	)
	private boolean eu$disableTooExpensiveForTaggedItems(Player player) {
		AnvilScreen anvilScreen = (AnvilScreen) (Object) this;
		ItemStack inputStack = anvilScreen
				.getMenu()
				.getSlot(AnvilMenu.INPUT_SLOT)
				.getItem();

		return player.hasInfiniteMaterials()
				|| inputStack.is(ModItemTagsProvider.Companion.getINFINITE_ENCHANT_STACKING());
	}

}
