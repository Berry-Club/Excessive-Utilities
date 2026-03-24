package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronMenuTypesRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterMenu
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterScreen
import dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine.EnderPorcupineMenu
import dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine.EnderPorcupineScreen
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu.FluidFilterMenu
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu.FluidFilterScreen
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node.FluidTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node.FluidTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.furnace.EUFurnaceMenu
import dev.aaronhowser.mods.excessive_utilities.menu.furnace.EUFurnaceScreen
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterMenu
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterScreen
import dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node.ItemTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node.ItemTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.qed.QedMenu
import dev.aaronhowser.mods.excessive_utilities.menu.qed.QedScreen
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorScreen
import dev.aaronhowser.mods.excessive_utilities.menu.single_item_generator.SingleItemGeneratorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.single_item_generator.SingleItemGeneratorScreen
import dev.aaronhowser.mods.excessive_utilities.menu.single_slot.SingleSlotMenu
import dev.aaronhowser.mods.excessive_utilities.menu.single_slot.SingleSlotScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModMenuTypes : AaronMenuTypesRegistry() {

	val MENU_TYPE_REGISTRY: DeferredRegister<MenuType<*>> =
		DeferredRegister.create(BuiltInRegistries.MENU, ExcessiveUtilities.MOD_ID)

	override fun getMenuTypeRegistry(): DeferredRegister<MenuType<*>> = MENU_TYPE_REGISTRY

	val FLAT_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<FlatTransferNodeMenu>> =
		register("flat_transfer_node", ::FlatTransferNodeMenu)
	val ITEM_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<ItemTransferNodeMenu>> =
		register("item_transfer_node", ::ItemTransferNodeMenu)
	val FLUID_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<FluidTransferNodeMenu>> =
		register("fluid_transfer_node") { IMenuTypeExtension.create(FluidTransferNodeMenu::fromNetwork) }
	val SINGLE_SLOT: DeferredHolder<MenuType<*>, MenuType<SingleSlotMenu>> =
		register("single_slot", ::SingleSlotMenu)
	val RESONATOR: DeferredHolder<MenuType<*>, MenuType<ResonatorMenu>> =
		register("resonator", ::ResonatorMenu)
	val SINGLE_ITEM_GENERATOR: DeferredHolder<MenuType<*>, MenuType<SingleItemGeneratorMenu>> =
		register("single_item_generator", ::SingleItemGeneratorMenu)
	val QED: DeferredHolder<MenuType<*>, MenuType<QedMenu>> =
		register("qed", ::QedMenu)
	val FURNACE: DeferredHolder<MenuType<*>, MenuType<EUFurnaceMenu>> =
		register("furnace", ::EUFurnaceMenu)
	val ITEM_FILTER: DeferredHolder<MenuType<*>, MenuType<ItemFilterMenu>> =
		register("item_filter") { IMenuTypeExtension.create(::ItemFilterMenu) }
	val FLUID_FILTER: DeferredHolder<MenuType<*>, MenuType<FluidFilterMenu>> =
		register("fluid_filter") { IMenuTypeExtension.create(::FluidFilterMenu) }
	val ENDER_PORCUPINE: DeferredHolder<MenuType<*>, MenuType<EnderPorcupineMenu>> =
		register("ender_porcupine", ::EnderPorcupineMenu)
	val ENCHANTER: DeferredHolder<MenuType<*>, MenuType<EnchanterMenu>> =
		register("enchanter", ::EnchanterMenu)

	override fun registerScreens(event: RegisterMenuScreensEvent) {
		event.register(FLAT_TRANSFER_NODE.get(), ::FlatTransferNodeScreen)
		event.register(ITEM_TRANSFER_NODE.get(), ::ItemTransferNodeScreen)
		event.register(FLUID_TRANSFER_NODE.get(), ::FluidTransferNodeScreen)
		event.register(SINGLE_SLOT.get(), ::SingleSlotScreen)
		event.register(RESONATOR.get(), ::ResonatorScreen)
		event.register(SINGLE_ITEM_GENERATOR.get(), ::SingleItemGeneratorScreen)
		event.register(QED.get(), ::QedScreen)
		event.register(FURNACE.get(), ::EUFurnaceScreen)
		event.register(ITEM_FILTER.get(), ::ItemFilterScreen)
		event.register(FLUID_FILTER.get(), ::FluidFilterScreen)
		event.register(ENDER_PORCUPINE.get(), ::EnderPorcupineScreen)
		event.register(ENCHANTER.get(), ::EnchanterScreen)
	}

}