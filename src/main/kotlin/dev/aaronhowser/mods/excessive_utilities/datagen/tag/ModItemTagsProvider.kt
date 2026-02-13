package dev.aaronhowser.mods.excessive_utilities.datagen.tag

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
				ModItems.SPEED_UPGRADE.get(),
				ModItems.POWER_MANAGER.get(),
				ModItems.RESONATING_REDSTONE_CRYSTAL.get(),
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
				ModItems.RING_OF_THE_FLYING_SQUID.get(),
				ModItems.ANGEL_RING.get(),
				ModItems.CHICKEN_WING_RING.get()
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
				ModItems.SPEED_UPGRADE.get(),
				ModItems.SPEED_UPGRADE_MAGICAL.get(),
				ModItems.SPEED_UPGRADE_ULTIMATE.get()
			)

		tag(INTERACT_WITH_FLAT_TRANSFER_NODES)
			.add(
				ModItems.WRENCH.get(),
				ModItems.FLAT_TRANSFER_NODE_ITEMS.get(),
				ModItems.FLAT_TRANSFER_NODE_FLUIDS.get()
			)
			.addTags(
				Tags.Items.TOOLS_WRENCH
			)

		tag(FILTERS)
			.add(
				ModItems.ITEM_FILTER.get(),
				ModItems.ADVANCED_ITEM_FILTER.get(),
				ModItems.FLUID_FILTER.get(),
			)

		tag(Tags.Items.TOOLS_WRENCH)
			.add(
				ModItems.WRENCH.get()
			)
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
	}

}