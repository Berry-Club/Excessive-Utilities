package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.add
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
	pOutput: PackOutput,
	pLookupProvider: CompletableFuture<HolderLookup.Provider>,
	pBlockTags: CompletableFuture<TagLookup<Block>>,
	existingFileHelper: ExistingFileHelper
) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(RENDER_GP_WHILE_HOLDING)
			.add(
				ModItems.POWER_MANAGER,
				ModItems.RESONATING_REDSTONE_CRYSTAL,
				ModItems.RING_OF_THE_FLYING_SQUID,
				ModItems.ANGEL_RING,
				ModItems.CHICKEN_WING_RING,
				ModItems.RED_COAL,
			)
			.add(
				ModBlocks.MANUAL_MILL.asItem(),
				ModBlocks.WATER_MILL.asItem(),
				ModBlocks.WIND_MILL.asItem(),
				ModBlocks.FIRE_MILL.asItem(),
				ModBlocks.LAVA_MILL.asItem(),
				ModBlocks.SOLAR_PANEL.asItem(),
				ModBlocks.LUNAR_PANEL.asItem(),
				ModBlocks.DRAGON_EGG_MILL.asItem(),
				ModBlocks.CREATIVE_MILL.asItem(),
				ModBlocks.RESONATOR.asItem(),
				ModBlocks.WIRELESS_FE_TRANSMITTER.asItem(),
			)
			.addTags(
				SPEED_UPGRADES
			)

		tag(PISTONS)
			.add(
				Items.PISTON,
				Items.STICKY_PISTON,
			)

		tag(CORPSE_PARTS)
			.add(
				Items.BONE,
				Items.ROTTEN_FLESH
			)

		tag(SPEED_UPGRADES)
			.add(
				ModItems.SPEED_UPGRADE,
				ModItems.SPEED_UPGRADE_MAGICAL,
				ModItems.SPEED_UPGRADE_ULTIMATE,
			)

		tag(INTERACT_WITH_FLAT_TRANSFER_NODES)
			.add(
				ModItems.PIPE_WRENCH,
				ModItems.FLAT_TRANSFER_NODE_ITEMS,
				ModItems.FLAT_TRANSFER_NODE_FLUIDS,
			)
			.addTags(
				Tags.Items.TOOLS_WRENCH
			)

		tag(FILTERS)
			.add(
				ModItems.ITEM_FILTER,
				ModItems.FLUID_FILTER,
			)

		tag(Tags.Items.TOOLS_WRENCH)
			.add(
				ModItems.PIPE_WRENCH
			)

		tag(Tags.Items.DYES_MAGENTA)
			.add(
				ModItems.LUNAR_REACTIVE_DUST
			)

		tag(ItemTags.PICKAXES)
			.add(
				ModItems.DESTRUCTION_PICKAXE
			)

		tag(ItemTags.SHOVELS)
			.add(
				ModItems.EROSION_SHOVEL,
				ModItems.TROWEL
			)

		tag(ItemTags.AXES)
			.add(
				ModItems.HEALING_AXE,
				ModItems.FIRE_AXE
			)

		tag(ItemTags.HOES)
			.add(
				ModItems.REVERSING_HOE
			)

		tag(Tags.Items.TOOLS_SHEAR)
			.add(
				ModItems.PRECISION_SHEARS
			)

		tag(ItemTags.SWORDS)
			.add(
				ModItems.ETHERIC_SWORD,
				ModItems.KIKOKU,
				ModItems.LUX_SABER
			)

		tag(Tags.Items.MINING_TOOL_TOOLS)
			.add(
				ModItems.DESTRUCTION_PICKAXE,
				ModItems.EROSION_SHOVEL,
				ModItems.HEALING_AXE,
				ModItems.FIRE_AXE
			)
			.addTag(
				SICKLES
			)

		tag(ItemTags.DURABILITY_ENCHANTABLE)
			.addTag(
				SICKLES
			)
			.remove(
				ModItems.DESTRUCTION_PICKAXE.get(),
				ModItems.EROSION_SHOVEL.get(),
				ModItems.HEALING_AXE.get(),
				ModItems.ETHERIC_SWORD.get(),
				ModItems.REVERSING_HOE.get(),
				ModItems.PRECISION_SHEARS.get()
			)

		tag(SICKLES)
			.add(
				ModItems.WOODEN_SICKLE,
				ModItems.STONE_SICKLE,
				ModItems.IRON_SICKLE,
				ModItems.GOLDEN_SICKLE,
				ModItems.DIAMOND_SICKLE,
				ModItems.NETHERITE_SICKLE,
			)

		tag(ItemTags.MINING_ENCHANTABLE)
			.addTag(
				SICKLES
			)

		tag(ItemTags.MINING_LOOT_ENCHANTABLE)
			.remove(
				ModItems.TROWEL.get(),
				ModItems.EROSION_SHOVEL.get(),
				ModItems.DESTRUCTION_PICKAXE.get()
			)

		tag(MAGICAL_BOOMERANG_ENCHANTABLE)
			.add(
				ModItems.MAGICAL_BOOMERANG
			)

		tag(DOUBLE_ANVIL_ENCHANTMENTS)
			.add(
				ModItems.KIKOKU,
				ModItems.COMPOUND_BOW,
				ModItems.FIRE_AXE
			)

		tag(Tags.Items.TOOLS_BOW)
			.add(
				ModItems.COMPOUND_BOW
			)

		tag(ItemTags.BOW_ENCHANTABLE)
			.add(
				ModItems.COMPOUND_BOW
			)

		tag(Tags.Items.RANGED_WEAPON_TOOLS)
			.add(
				ModItems.COMPOUND_BOW
			)

		tag(ItemTags.LOGS_THAT_BURN)
			.add(ModBlocks.MAGICAL_WOOD.asItem())

		tag(ItemTags.PLANKS)
			.add(ModBlocks.MAGICAL_WOOD.asItem())
			.add(*ModBlocks.COLORED_PLANKS.map { it.value.asItem() }.toTypedArray())

		tag(ItemTags.STONE_BRICKS)
			.add(*ModBlocks.COLORED_STONE_BRICKS.map { it.value.asItem() }.toTypedArray())

		tag(Tags.Items.COBBLESTONES_NORMAL)
			.add(*ModBlocks.COLORED_COBBLESTONES.map { it.value.asItem() }.toTypedArray())

		tag(Tags.Items.STONES)
			.add(*ModBlocks.COLORED_STONES.map { it.value.asItem() }.toTypedArray())

		tag(Tags.Items.STORAGE_BLOCKS_COAL)
			.add(*ModBlocks.COLORED_COAL_BLOCKS.map { it.value.asItem() }.toTypedArray())

		tag(Tags.Items.STORAGE_BLOCKS_LAPIS)
			.add(*ModBlocks.COLORED_LAPIS_BLOCKS.map { it.value.asItem() }.toTypedArray())

		tag(Tags.Items.OBSIDIANS_NORMAL)
			.add(*ModBlocks.COLORED_OBSIDIANS.map { it.value.asItem() }.toTypedArray())

		tag(Tags.Items.STORAGE_BLOCKS_REDSTONE)
			.add(*ModBlocks.COLORED_REDSTONE_BLOCKS.map { it.value.asItem() }.toTypedArray())

		tag(QUARTZ_STORAGE_BLOCKS)
			.add(Items.QUARTZ_BLOCK)
			.add(*ModBlocks.COLORED_QUARTZES.map { it.value.asItem() }.toTypedArray())

		tag(SOUL_SANDS)
			.add(Items.SOUL_SAND)
			.add(*ModBlocks.COLORED_SOUL_SANDS.map { it.value.asItem() }.toTypedArray())

		tag(BRICK_BLOCKS)
			.add(Items.BRICKS)
			.add(*ModBlocks.COLORED_BRICKS.map { it.value.asItem() }.toTypedArray())

		tag(GLOWSTONES)
			.add(Items.GLOWSTONE)
			.add(*ModBlocks.COLORED_GLOWSTONES.map { it.value.asItem() }.toTypedArray())

		tag(REDSTONE_LAMPS)
			.add(Items.REDSTONE_LAMP)
			.add(*ModBlocks.COLORED_REDSTONE_LAMPS.map { it.value.asItem() }.toTypedArray())

		tag(TRANSFER_NODE_UPGRADES)
			.add(
				ModItems.STACK_UPGRADE,
				ModItems.WORLD_INTERACTION_UPGRADE,
				ModItems.CREATIVE_UPGRADE,
				ModItems.BREADTH_FIRST_SEARCH_UPGRADE,
				ModItems.DEPTH_FIRST_SEARCH_UPGRADE,
				ModItems.PSEUDO_ROUND_ROBIN_UPGRADE,
				ModItems.ENDER_TRANSMITTER,
			)
			.addTags(
				SPEED_UPGRADES
			)

		tag(RETRIEVAL_NODE_UPGRADES)
			.add(
				ModItems.STACK_UPGRADE,
				ModItems.CREATIVE_UPGRADE,
				ModItems.BREADTH_FIRST_SEARCH_UPGRADE,
				ModItems.DEPTH_FIRST_SEARCH_UPGRADE,
				ModItems.PSEUDO_ROUND_ROBIN_UPGRADE,
				ModItems.ENDER_RECEIVER,
			)
			.addTags(
				SPEED_UPGRADES
			)

		tag(NOT_YET_IMPLEMENTED)
			.add(
				ModBlocks.TERRAFORMER.asItem(),
				ModBlocks.CLIMOGRAPH_BASE_UNIT.asItem(),
				ModBlocks.ANTENNA.asItem(),
				ModBlocks.COOLER.asItem(),
				ModBlocks.DEHOSTILIFIER.asItem(),
				ModBlocks.DEHUMIDIFIER.asItem(),
				ModBlocks.HEATER.asItem(),
				ModBlocks.HUMIDIFIER.asItem(),
				ModBlocks.MAGIC_ABSORPTION.asItem(),
				ModBlocks.MAGIC_INFUSER.asItem(),
				ModBlocks.REDSTONE_GLASS.asItem(),
				ModBlocks.DEEP_DARK_PORTAL.asItem(),
				ModBlocks.LAST_MILLENNIUM_PORTAL.asItem(),
				ModBlocks.TRADING_POST.asItem(),
				ModBlocks.INDEXER.asItem(),
				ModBlocks.TRANSFER_FILTER.asItem(),
				ModBlocks.TRANSFER_PIPE_FILTER.asItem(),
				ModBlocks.ENDER_THERMIC_PUMP.asItem(),
				ModBlocks.PLAYER_CHEST.asItem(),
				ModBlocks.ENERGY_TRANSFER_NODE.asItem(),
				ModBlocks.ENERGY_RETRIEVAL_NODE.asItem()
			)
			.add(
				ModItems.BREADTH_FIRST_SEARCH_UPGRADE,
				ModItems.DEPTH_FIRST_SEARCH_UPGRADE,
				ModItems.PSEUDO_ROUND_ROBIN_UPGRADE,
				ModItems.ENDER_TRANSMITTER,
				ModItems.ENDER_RECEIVER,
				ModItems.PRECISION_SHEARS,
				ModItems.SONAR_GOGGLES,
				ModItems.KIKOKU,
				ModItems.LUX_SABER
			)

		tag(RENDER_ENDER_PORCUPINE_WHILE_HOLDING)
			.add(
				ModBlocks.ENDER_PORCUPINE.asItem(),
				ModItems.PIPE_WRENCH.get()
			)

		tag(CHILDREN_OF_FIRE)
			.addTags(
				Tags.Items.STONES,
				Tags.Items.GLASS_BLOCKS,
				ItemTags.TERRACOTTA,
				ItemTags.COALS,
				Tags.Items.INGOTS_IRON,
				Tags.Items.INGOTS_GOLD,
				Tags.Items.FOODS_COOKED_FISH,
				Tags.Items.FOODS_COOKED_MEAT,
				Tags.Items.BRICKS_NORMAL,
				Tags.Items.BRICKS_NETHER,
				Tags.Items.FOODS_BREAD
			)
			.add(
				Items.GREEN_DYE
			)

		tag(GIFTS_OF_EARTH)
			.addTags(
				ItemTags.DIRT,
				ItemTags.SAND,
				Tags.Items.GRAVELS,
				Tags.Items.ORES_GOLD,
				Tags.Items.ORES_IRON,
				Tags.Items.ORES_COAL,
				Tags.Items.ORES_LAPIS,
				Tags.Items.OBSIDIANS_NORMAL,
				Tags.Items.ORES_DIAMOND,
				Tags.Items.ORES_REDSTONE,
				Tags.Items.ORES_EMERALD
			)
			.add(
				Items.GRASS_BLOCK,
				Items.CLAY
			)

		tag(DESCENDANTS_OF_WATER)
			.add(Items.POTION)

		tag(SPICES_OF_AIR)
			.addTags(
				Tags.Items.MUSIC_DISCS
			)

		tag(RING_CURIO)
			.add(
				ModItems.ANGEL_RING,
				ModItems.CHICKEN_WING_RING,
				ModItems.RING_OF_THE_FLYING_SQUID
			)
	}

	companion object {
		private fun create(id: String): TagKey<Item> = ItemTags.create(ExcessiveUtilities.modResource(id))
		private fun common(id: String): TagKey<Item> = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", id))
		private fun curio(id: String): TagKey<Item> = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", id))

		val TRANSFER_NODE_UPGRADES = create("transfer_node_upgrades")
		val RETRIEVAL_NODE_UPGRADES = create("retrieval_node_upgrades")
		val SPEED_UPGRADES = create("speed_upgrades")
		val FILTERS = create("filters")
		val SICKLES = common("sickles")

		val RENDER_GP_WHILE_HOLDING = create("render_gp_while_holding")
		val RENDER_ENDER_PORCUPINE_WHILE_HOLDING = create("render_ender_porcupine_while_holding")

		val INTERACT_WITH_FLAT_TRANSFER_NODES = create("interact_with_flat_transfer_nodes")

		val PISTONS = common("pistons")
		val CORPSE_PARTS = create("corpse_parts")
		val SOUL_SANDS = common("soul_sands")
		val BRICK_BLOCKS = common("brick_blocks")
		val GLOWSTONES = common("glowstones")
		val REDSTONE_LAMPS = common("redstone_lamps")
		val QUARTZ_STORAGE_BLOCKS = common("storage_blocks/quartz")

		val MAGICAL_BOOMERANG_ENCHANTABLE = create("magical_boomerang_enchantable")
		val DOUBLE_ANVIL_ENCHANTMENTS = create("double_anvil_enchantments")

		val NOT_YET_IMPLEMENTED = create("not_yet_implemented")

		val CHILDREN_OF_FIRE = create("children_of_fire")
		val GIFTS_OF_EARTH = create("gifts_of_earth")
		val DESCENDANTS_OF_WATER = create("descendants_of_water")
		val SPICES_OF_AIR = create("spices_of_air")

		val RING_CURIO = curio("ring")
	}

}