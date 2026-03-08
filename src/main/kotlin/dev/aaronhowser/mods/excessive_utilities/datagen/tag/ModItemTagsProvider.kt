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
				ModItems.SPEED_UPGRADE,
				ModItems.SPEED_UPGRADE_MAGICAL,
				ModItems.SPEED_UPGRADE_ULTIMATE,
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
				ModItems.WRENCH,
				ModItems.FLAT_TRANSFER_NODE_ITEMS,
				ModItems.FLAT_TRANSFER_NODE_FLUIDS,
			)
			.addTags(
				Tags.Items.TOOLS_WRENCH
			)

		tag(FILTERS)
			.add(
				ModItems.ITEM_FILTER,
				ModItems.ADVANCED_ITEM_FILTER,
				ModItems.FLUID_FILTER,
			)

		tag(Tags.Items.TOOLS_WRENCH)
			.add(
				ModItems.WRENCH
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
				ModItems.EROSION_SHOVEL
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
	}

	companion object {
		private fun create(id: String): TagKey<Item> = ItemTags.create(ExcessiveUtilities.modResource(id))
		private fun common(id: String): TagKey<Item> = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", id))

		val RENDER_GP_WHILE_HOLDING = create("render_gp_while_holding")
		val PISTONS = common("pistons")
		val CORPSE_PARTS = create("corpse_parts")
		val SPEED_UPGRADES = create("speed_upgrades")
		val INTERACT_WITH_FLAT_TRANSFER_NODES = create("interact_with_flat_transfer_nodes")
		val REMOVE_FLAT_TRANSFER_NODES = create("remove_flat_transfer_nodes")
		val FILTERS = create("filters")
		val SICKLES = common("sickles")
		val MAGICAL_BOOMERANG_ENCHANTABLE = create("magical_boomerang_enchantable")
		val DOUBLE_ANVIL_ENCHANTMENTS = create("double_anvil_enchantments")
		val QUARTZ_STORAGE_BLOCKS = common("storage_blocks/quartz")
		val SOUL_SANDS = common("soul_sands")
		val BRICK_BLOCKS = common("brick_blocks")
		val GLOWSTONES = common("glowstones")
	}

}