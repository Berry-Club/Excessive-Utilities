package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronMenuTypesRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding.BagOfHoldingMenu
import dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding.BagOfHoldingScreen
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.mini_chest.MiniChestMenu
import dev.aaronhowser.mods.excessive_utilities.menu.mini_chest.MiniChestScreen
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
	val MINI_CHEST: DeferredHolder<MenuType<*>, MenuType<MiniChestMenu>> =
		register("mini_chest", ::MiniChestMenu)
	val BAG_OF_HOLDING: DeferredHolder<MenuType<*>, MenuType<BagOfHoldingMenu>> =
		register("bag_of_holding", ::BagOfHoldingMenu)

	override fun registerScreens(event: RegisterMenuScreensEvent) {
		event.register(FLAT_TRANSFER_NODE.get(), ::FlatTransferNodeScreen)
		event.register(MINI_CHEST.get(), ::MiniChestScreen)
		event.register(BAG_OF_HOLDING.get(), ::BagOfHoldingScreen)
	}

}