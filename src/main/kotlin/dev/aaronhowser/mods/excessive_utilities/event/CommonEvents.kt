package dev.aaronhowser.mods.excessive_utilities.event

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import dev.aaronhowser.mods.excessive_utilities.block.AngelBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.*
import dev.aaronhowser.mods.excessive_utilities.block.entity.generator.MagmaticGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.trash.EnergyTrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.trash.FluidTrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.trash.TrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorItemFuel
import dev.aaronhowser.mods.excessive_utilities.datamap.MagmaticGeneratorFuel
import dev.aaronhowser.mods.excessive_utilities.datamap.NetherLavaDunkConversion
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.KeyHandler
import dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator.RainbowGeneratorHandler
import dev.aaronhowser.mods.excessive_utilities.item.DestructionPickaxeItem
import dev.aaronhowser.mods.excessive_utilities.item.ErosionShovelItem
import dev.aaronhowser.mods.excessive_utilities.item.GlassCutterItem
import dev.aaronhowser.mods.excessive_utilities.item.HeatingCoilItem
import dev.aaronhowser.mods.excessive_utilities.packet.ModPacketHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.AnvilUpdateEvent
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.level.BlockDropsEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent


@EventBusSubscriber(
	modid = ExcessiveUtilities.MOD_ID
)
object CommonEvents {

	@SubscribeEvent
	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		ModPacketHandler.registerPayloads(event)
	}

	@SubscribeEvent
	fun onMobSpawn(event: MobSpawnEvent.SpawnPlacementCheck) {
		MagnumTorchBlockEntity.handleSpawnEvent(event)
		ChandelierBlockEntity.handleSpawnEvent(event)
	}

	@SubscribeEvent
	fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {

		event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			ModBlockEntityTypes.DRUM.get(),
			DrumBlockEntity::getFluidCapability
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.RESONATOR.get(),
			ResonatorBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.ENCHANTER.get(),
			EnchanterBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(),
			WirelessFeBatteryBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.WIRELESS_FE_TRANSMITTER.get(),
			WirelessFeTransmitterBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.SLIGHTLY_LARGER_CHEST.get(),
			SlightlyLargerChestBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.TRASH_CAN.get(),
			TrashCanBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.CREATIVE_CHEST.get(),
			CreativeChestBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			ModBlockEntityTypes.FLUID_TRASH_CAN.get(),
			FluidTrashCanBlockEntity::getFluidHandler
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.ENERGY_TRASH_CAN.get(),
			EnergyTrashCanBlockEntity::getEnergyStorage
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.MINI_CHEST.get(),
			MiniChestBlockEntity::getItemHandler
		)

		event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			ModBlockEntityTypes.FILING_CABINET.get(),
			FilingCabinetBlockEntity::getItemHandler
		)

		val generators = listOf(
			ModBlockEntityTypes.ENDER_GENERATOR.get(),
			ModBlockEntityTypes.EXPLOSIVE_GENERATOR.get(),
			ModBlockEntityTypes.PINK_GENERATOR.get(),
			ModBlockEntityTypes.NETHER_STAR_GENERATOR.get(),
			ModBlockEntityTypes.FROSTY_GENERATOR.get(),
			ModBlockEntityTypes.HALITOSIS_GENERATOR.get(),
			ModBlockEntityTypes.DEATH_GENERATOR.get(),
			ModBlockEntityTypes.CULINARY_GENERATOR.get(),
			ModBlockEntityTypes.FURNACE_GENERATOR.get(),
			ModBlockEntityTypes.SURVIVAL_GENERATOR.get(),
			ModBlockEntityTypes.MAGMATIC_GENERATOR.get()
		)

		for (beType in generators) {
			event.registerBlockEntity(
				Capabilities.EnergyStorage.BLOCK,
				beType,
				GeneratorBlockEntity::getEnergyCapability
			)

			event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				beType,
				GeneratorBlockEntity::getItemHandler
			)
		}

		event.registerItem(
			Capabilities.EnergyStorage.ITEM,
			HeatingCoilItem::getEnergyCapability,
			ModItems.HEATING_COIL.get()
		)

		event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			ModBlockEntityTypes.MAGMATIC_GENERATOR.get(),
			MagmaticGeneratorBlockEntity::getFluidCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.ENDER_QUARRY.get(),
			EnderQuarryBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.QUANTUM_QUARRY.get(),
			QuantumQuarryBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.QUANTUM_QUARRY_ACTUATOR.get(),
			QuantumQuarryActuatorBlockEntity::getEnergyCapability
		)

		event.registerBlockEntity(
			Capabilities.EnergyStorage.BLOCK,
			ModBlockEntityTypes.CREATIVE_ENERGY_SOURCE.get(),
			CreativeEnergySourceBlockEntity::getEnergyCapability
		)
	}

	@SubscribeEvent
	fun afterServerTick(event: ServerTickEvent.Post) {
		val overworld = event.server.overworld()
		GridPowerHandler.get(overworld).tick(overworld)
		RainbowGeneratorHandler.get(overworld).tick(overworld)
	}

	@SubscribeEvent
	fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {
		event.register(GeneratorItemFuel.ENDER)
		event.register(GeneratorItemFuel.EXPLOSIVE)
		event.register(GeneratorItemFuel.PINK)
		event.register(GeneratorItemFuel.NETHER_STAR)
		event.register(GeneratorItemFuel.FROSTY)
		event.register(GeneratorItemFuel.HALITOSIS)
		event.register(GeneratorItemFuel.DEATH)
		event.register(MagmaticGeneratorFuel.DATA_MAP)
		event.register(NetherLavaDunkConversion.DATA_MAP)
	}

	@SubscribeEvent
	fun onRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
		FlatTransferNodeEntity.handleRightClickBlock(event)
	}

	@SubscribeEvent
	fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
		FlatTransferNodeEntity.handleLeftClickBlock(event)
	}

	@SubscribeEvent
	fun onBlockDrops(event: BlockDropsEvent) {
		AngelBlock.handleDropEvent(event)
		DestructionPickaxeItem.handleDropEvent(event)
		ErosionShovelItem.handleDropEvent(event)
		GlassCutterItem.handleDropEvent(event)
	}

	@SubscribeEvent
	fun onPlayerLogout(event: PlayerEvent.PlayerLoggedOutEvent) {
		KeyHandler.remove(event.entity)
	}

	@SubscribeEvent
	fun onPlayerChangeDimension(event: PlayerEvent.PlayerChangedDimensionEvent) {
		KeyHandler.remove(event.entity)
	}

	@SubscribeEvent
	fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
		SoulDebt.onRespawn(event)
	}

	@SubscribeEvent
	fun afterEntityTick(event: EntityTickEvent.Post) {
		val entity = event.entity
		if (entity is ItemEntity) {
			makeDemonMetal(entity)
		}
	}

	private fun makeDemonMetal(itemEntity: ItemEntity) {
		val level = itemEntity.level() as? ServerLevel ?: return
		if (level.dimension() != Level.NETHER) return

		val item = itemEntity.item
		val output = item.item
			.builtInRegistryHolder()
			.getData(NetherLavaDunkConversion.DATA_MAP)
			?.output
			?: return

		val pos = itemEntity.blockPosition()
		val fluidState = level.getFluidState(pos)
		if (fluidState.isFluid(FluidTags.LAVA)) {
			val count = item.count
			val demonStack = output.copyWithCount(count)

			itemEntity.item = demonStack
			itemEntity.deltaMovement = Vec3.ZERO
		}
	}

	@SubscribeEvent
	fun onAnvilUpdate(event: AnvilUpdateEvent) {
		val leftStack = event.left
		val rightStack = event.right
		val player = event.player

		if (leftStack.isItem(ModItemTagsProvider.DOUBLE_ANVIL_ENCHANTMENTS) && rightStack.isItem(Items.ENCHANTED_BOOK)) {
			val currentEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(leftStack)
			val bookEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(rightStack)

			val newEnchantments = ItemEnchantments.Mutable(currentEnchantments)

			for ((enchantment, level) in bookEnchantments.entrySet()) {
				val noIncompatibleEnchantments = newEnchantments.keySet().all { Enchantment.areCompatible(enchantment, it) }
				val valid = player.hasInfiniteMaterials() || (noIncompatibleEnchantments && leftStack.supportsEnchantment(enchantment))
				if (!valid) continue

				val currentLevel = newEnchantments.getLevel(enchantment)
				if (currentLevel < level * 2) {
					newEnchantments.set(enchantment, level * 2)
				}
			}

			val output = leftStack.copy()
			EnchantmentHelper.setEnchantments(output, newEnchantments.toImmutable())

			event.output = output
			event.cost = 40
		}

	}

}