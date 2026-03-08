package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronMenuTypesRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.qed.QedMenu
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorScreen
import dev.aaronhowser.mods.excessive_utilities.menu.single_item_generator.SingleItemGeneratorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.single_item_generator.SingleItemGeneratorScreen
import dev.aaronhowser.mods.excessive_utilities.menu.single_slot.SingleSlotMenu
import dev.aaronhowser.mods.excessive_utilities.menu.single_slot.SingleSlotScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModMenuTypes : AaronMenuTypesRegistry() {

	val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
		DeferredRegister.create(BuiltInRegistries.MENU, ExcessiveUtilities.MOD_ID)

	override fun getMenuTypeRegistry(): DeferredRegister<MenuType<*>> = MENU_TYPE_REGISTRY

	val FLAT_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<FlatTransferNodeMenu>> =
		register("flat_transfer_node", ::FlatTransferNodeMenu)
	val SINGLE_SLOT: DeferredHolder<MenuType<*>, MenuType<SingleSlotMenu>> =
		register("single_slot", ::SingleSlotMenu)
	val RESONATOR: DeferredHolder<MenuType<*>, MenuType<ResonatorMenu>> =
		register("resonator", ::ResonatorMenu)
	val SINGLE_ITEM_GENERATOR: DeferredHolder<MenuType<*>, MenuType<SingleItemGeneratorMenu>> =
		register("single_item_generator", ::SingleItemGeneratorMenu)
	val QED: DeferredHolder<MenuType<*>, MenuType<QedMenu>> =
		register("qed", ::QedMenu)

	override fun registerScreens(event: RegisterMenuScreensEvent) {
		event.register(FLAT_TRANSFER_NODE.get(), ::FlatTransferNodeScreen)
		event.register(SINGLE_SLOT.get(), ::SingleSlotScreen)
		event.register(RESONATOR.get(), ::ResonatorScreen)
		event.register(SINGLE_ITEM_GENERATOR.get(), ::SingleItemGeneratorScreen)
	}

}