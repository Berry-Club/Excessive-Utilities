package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(ExcessiveUtilities.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	// Functional

	val COMPRESSED_BLOCK =
		basicBlock("compressed_block")
	val SLIGHTLY_LARGER_CHEST =
		basicBlock("slightly_larger_chest")
	val MINI_CHEST =
		basicBlock("mini_chest")
	val ANGEL_BLOCK =
		basicBlock("angel_block")
	val BLACKOUT_CURTAIN =
		basicBlock("blackout_curtain")
	val ADVANCED_OBSERVER =
		basicBlock("advanced_observer")
	val CONVEYOR_BELT =
		basicBlock("conveyor_belt")
	val CURSED_EARTH =
		basicBlock("cursed_earth")
	val ENDER_COLLECTOR =
		basicBlock("ender_collector")
	val ENDER_CORE =
		basicBlock("ender_core")
	val ENDER_QUARRY =
		basicBlock("ender_quarry")
	val ENDER_FLUX_CRYSTAL =
		basicBlock("ender_flux_crystal")
	val ENDER_INFUSED_OBSIDIAN =
		basicBlock("ender_infused_obsidian")
	val ENDER_MARKER =
		basicBlock("ender_marker")
	val ENDER_THERMIC_PUMP =
		basicBlock("ender_thermic_pump")
	val MAGICAL_WOOD =
		basicBlock("magical_wood")
	val MAGNUM_TORCH =
		basicBlock("magnum_torch")
	val PEACEFUL_TABLE =
		basicBlock("peaceful_table")
	val DEEP_DARK_PORTAL =
		basicBlock("deep_dark_portal")
	val LAST_MILLENNIUM_PORTAL =
		basicBlock("last_millennium_portal")
	val QED =
		basicBlock("qed")
	val RAIN_MUFFLER =
		basicBlock("rain_muffler")
	val REDSTONE_CLOCK =
		basicBlock("redstone_clock")
	val SOUND_MUFFLER =
		basicBlock("sound_muffler")
	val TRADING_POST =
		basicBlock("trading_post")
	val TRASH_CAN =
		basicBlock("trash_can")
	val TRASH_CAN_ENERGY =
		basicBlock("trash_can_energy")
	val TRASH_CAN_FLUID =
		basicBlock("trash_can_fluid")

	// Upgrades

	val UPGRADE_BASE =
		basicBlock("upgrade_base")
	val FORTUNE_UPGRADE =
		basicBlock("fortune_upgrade")
	val FORTUNE_TWO_UPGRADE =
		basicBlock("fortune_two_upgrade")
	val FORTUNE_THREE_UPGRADE =
		basicBlock("fortune_three_upgrade")
	val PUMP_UPGRADE =
		basicBlock("pump_upgrade")
	val SILK_TOUCH_UPGRADE =
		basicBlock("silk_touch_upgrade")
	val SPEED_UPGRADE =
		basicBlock("speed_upgrade")
	val SPEED_TWO_UPGRADE =
		basicBlock("speed_two_upgrade")
	val SPEED_THREE_UPGRADE =
		basicBlock("speed_three_upgrade")
	val WORLD_HOLE_UPGRADE =
		basicBlock("world_hole_upgrade")

	// Decor

	val BLOCK_OF_BEDROCKIUM =
		basicBlock("block_of_bedrockium")
	val UNSTABLE_INGOT_BLOCK =
		basicBlock("unstable_ingot_block")
	val BORDER_STONE =
		basicBlock("border_stone")
	val BORDER_STONE_ALT =
		basicBlock("border_stone_alt")
	val BURNT_QUARTZ =
		basicBlock("burnt_quartz")
	val EMINENCE_STONE =
		basicBlock("eminence_stone")
	val DIAMOND_ETCHED_COMPUTATIONAL_MATRIX =
		basicBlock("diamond_etched_computational_matrix")
	val EDGED_STONE_BRICKS =
		basicBlock("edged_stone_bricks")
	val ENDER_SAND_ALLOY =
		basicBlock("ender_sand_alloy")
	val FROSTED_STONE =
		basicBlock("frosted_stone")
	val GLASS_BRICKS =
		basicBlock("glass_bricks")
	val GRAVEL_BRICKS =
		basicBlock("gravel_bricks")
	val GRAVEL_ROAD =
		basicBlock("gravel_road")
	val LAPIS_CAELESTIS =
		basicBlock("lapis_caelestis")
	val CHANDELIER =
		basicBlock("chandelier")

	// Glass

	val CARVED_GLASS =
		basicBlock("carved_glass")
	val EDGED_GLASS =
		basicBlock("edged_glass")
	val GOLDEN_EDGED_GLASS =
		basicBlock("golden_edged_glass")
	val GLOWSTONE_GLASS =
		basicBlock("glowstone_glass")
	val HEART_GLASS =
		basicBlock("heart_glass")
	val OBSIDIAN_GLASS =
		basicBlock("obsidian_glass")
	val REINFORCED_DARK_GLASS =
		basicBlock("reinforced_dark_glass")
	val SANDY_GLASS =
		basicBlock("sandy_glass")
	val SQUARE_GLASS =
		basicBlock("square_glass")
	val SWIRLING_GLASS =
		basicBlock("swirling_glass")
	val THICKENED_GLASS =
		basicBlock("thickened_glass")
	val ETHEREAL_GLASS =
		basicBlock("ethereal_glass")
	val INVERTED_ETHEREAL_GLASS =
		basicBlock("inverted_ethereal_glass")
	val DARK_ETHEREAL_GLASS =
		basicBlock("dark_ethereal_glass")
	val INVERTED_DARK_ETHEREAL_GLASS =
		basicBlock("inverted_dark_ethereal_glass")
	val INEFFABLE_GLASS =
		basicBlock("ineffable_glass")
	val INVERTED_INEFFABLE_GLASS =
		basicBlock("inverted_ineffable_glass")

}