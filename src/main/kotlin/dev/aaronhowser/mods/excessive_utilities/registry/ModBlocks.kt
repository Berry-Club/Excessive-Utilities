package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronBlockRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks : AaronBlockRegistry() {

	val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(ExcessiveUtilities.MOD_ID)
	override fun getBlockRegistry(): DeferredRegister.Blocks = BLOCK_REGISTRY
	override fun getItemRegistry(): DeferredRegister.Items = ModItems.ITEM_REGISTRY

	val COMPRESSED_BLOCK =
		basicBlock("compressed_block")
	val SLIGHTLY_LARGER_CHEST =
		basicBlock("slightly_larger_chest")

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