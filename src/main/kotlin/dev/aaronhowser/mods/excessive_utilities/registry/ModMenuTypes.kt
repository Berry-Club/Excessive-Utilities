package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronMenuTypesRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterMenu
import dev.aaronhowser.mods.excessive_utilities.menu.enchanter.EnchanterScreen
import dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine.EnderPorcupineMenu
import dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine.EnderPorcupineScreen
import dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node.EnergyTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node.EnergyTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu.FluidFilterMenu
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu.FluidFilterScreen
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node.FluidTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node.FluidTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterMenu
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterScreen
import dev.aaronhowser.mods.excessive_utilities.menu.item_fluid_generator.ItemFluidGeneratorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.item_fluid_generator.ItemFluidGeneratorScreen
import dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node.ItemTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node.ItemTransferNodeScreen
import dev.aaronhowser.mods.excessive_utilities.menu.qed.QedMenu
import dev.aaronhowser.mods.excessive_utilities.menu.qed.QedScreen
import dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry.QuantumQuarryMenu
import dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry.QuantumQuarryScreen
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.resonator.ResonatorScreen
import dev.aaronhowser.mods.excessive_utilities.menu.simple_machine.SimpleMachineMenu
import dev.aaronhowser.mods.excessive_utilities.menu.simple_machine.SimpleMachineScreen
import dev.aaronhowser.mods.excessive_utilities.menu.single_fluid_generator.SingleFluidGeneratorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.single_fluid_generator.SingleFluidGeneratorScreen
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
		register("flat_transfer_node") { IMenuTypeExtension.create(FlatTransferNodeMenu::fromNetwork) }
	val ITEM_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<ItemTransferNodeMenu>> =
		register("item_transfer_node", ::ItemTransferNodeMenu)
	val FLUID_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<FluidTransferNodeMenu>> =
		register("fluid_transfer_node") { IMenuTypeExtension.create(FluidTransferNodeMenu::fromNetwork) }
	val ENERGY_TRANSFER_NODE: DeferredHolder<MenuType<*>, MenuType<EnergyTransferNodeMenu>> =
		register("energy_transfer_node", ::EnergyTransferNodeMenu)
	val SINGLE_SLOT: DeferredHolder<MenuType<*>, MenuType<SingleSlotMenu>> =
		register("single_slot", ::SingleSlotMenu)
	val RESONATOR: DeferredHolder<MenuType<*>, MenuType<ResonatorMenu>> =
		register("resonator", ::ResonatorMenu)
	val SINGLE_ITEM_GENERATOR: DeferredHolder<MenuType<*>, MenuType<SingleItemGeneratorMenu>> =
		register("single_item_generator", ::SingleItemGeneratorMenu)
	val SINGLE_FLUID_GENERATOR: DeferredHolder<MenuType<*>, MenuType<SingleFluidGeneratorMenu>> =
		register("single_fluid_generator", ::SingleFluidGeneratorMenu)
	val ITEM_FLUID_GENERATOR: DeferredHolder<MenuType<*>, MenuType<ItemFluidGeneratorMenu>> =
		register("item_fluid_generator", ::ItemFluidGeneratorMenu)
	val QED: DeferredHolder<MenuType<*>, MenuType<QedMenu>> =
		register("qed", ::QedMenu)
	val SIMPLE_MACHINE: DeferredHolder<MenuType<*>, MenuType<SimpleMachineMenu>> =
		register("simple_machine", ::SimpleMachineMenu)
	val ITEM_FILTER: DeferredHolder<MenuType<*>, MenuType<ItemFilterMenu>> =
		register("item_filter") { IMenuTypeExtension.create(::ItemFilterMenu) }
	val FLUID_FILTER: DeferredHolder<MenuType<*>, MenuType<FluidFilterMenu>> =
		register("fluid_filter") { IMenuTypeExtension.create(::FluidFilterMenu) }
	val ENDER_PORCUPINE: DeferredHolder<MenuType<*>, MenuType<EnderPorcupineMenu>> =
		register("ender_porcupine", ::EnderPorcupineMenu)
	val ENCHANTER: DeferredHolder<MenuType<*>, MenuType<EnchanterMenu>> =
		register("enchanter", ::EnchanterMenu)
	val QUANTUM_QUARRY: DeferredHolder<MenuType<*>, MenuType<QuantumQuarryMenu>> =
		register("quantum_quarry", ::QuantumQuarryMenu)

	override fun registerScreens(event: RegisterMenuScreensEvent) {
		event.register(FLAT_TRANSFER_NODE.get(), ::FlatTransferNodeScreen)
		event.register(ITEM_TRANSFER_NODE.get(), ::ItemTransferNodeScreen)
		event.register(FLUID_TRANSFER_NODE.get(), ::FluidTransferNodeScreen)
		event.register(ENERGY_TRANSFER_NODE.get(), ::EnergyTransferNodeScreen)
		event.register(SINGLE_SLOT.get(), ::SingleSlotScreen)
		event.register(RESONATOR.get(), ::ResonatorScreen)
		event.register(SINGLE_ITEM_GENERATOR.get(), ::SingleItemGeneratorScreen)
		event.register(SINGLE_FLUID_GENERATOR.get(), ::SingleFluidGeneratorScreen)
		event.register(ITEM_FLUID_GENERATOR.get(), ::ItemFluidGeneratorScreen)
		event.register(QED.get(), ::QedScreen)
		event.register(SIMPLE_MACHINE.get(), ::SimpleMachineScreen)
		event.register(ITEM_FILTER.get(), ::ItemFilterScreen)
		event.register(FLUID_FILTER.get(), ::FluidFilterScreen)
		event.register(ENDER_PORCUPINE.get(), ::EnderPorcupineScreen)
		event.register(ENCHANTER.get(), ::EnchanterScreen)
		event.register(QUANTUM_QUARRY.get(), ::QuantumQuarryScreen)
	}

}