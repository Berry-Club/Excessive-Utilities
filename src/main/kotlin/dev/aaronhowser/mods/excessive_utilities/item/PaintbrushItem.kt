package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDyeName
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class PaintbrushItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val player = context.player ?: return InteractionResult.PASS

		val level = context.level
		if (level.isClientSide || !level.mayInteract(player, context.clickedPos)) return InteractionResult.PASS

		val pos = context.clickedPos

		val stack = context.itemInHand

		if (player.isSecondaryUseActive) {
			if (setBrushColor(level.getBlockState(pos), stack)) {
				return InteractionResult.SUCCESS
			}
		} else {
			if (setBlockColor(level, pos, stack)) {
				return InteractionResult.SUCCESS
			}
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.COLOR, DyeColor.WHITE)
			}

		fun getItemColor(stack: ItemStack, tintIndex: Int): Int {
			if (tintIndex != 1) return 0xFFFFFFFF.toInt()
			return stack.getOrDefault(ModDataComponents.COLOR, DyeColor.WHITE).textureDiffuseColor
		}

		private fun setBrushColor(blockState: BlockState, brushStack: ItemStack): Boolean {
			for (dyeColor in DyeColor.entries) {
				val tag = TagKey.create(
					Registries.BLOCK,
					Identifier.fromNamespaceAndPath(
						"c",
						"dyed/${dyeColor.getDyeName()}"
					)
				)

				if (blockState.isBlock(tag)) {
					brushStack.set(ModDataComponents.COLOR, dyeColor)
					return true
				}
			}

			return false
		}

		private fun setBlockColor(level: Level, pos: BlockPos, brushStack: ItemStack): Boolean {
			val blockState = level.getBlockState(pos)

			if (blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_BLACKLIST)) return false
			val color = brushStack.get(ModDataComponents.COLOR) ?: return false

			if (blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_WOOLS)) {
				val newState = when (color) {
					DyeColor.WHITE -> Blocks.WHITE_WOOL
					DyeColor.ORANGE -> Blocks.ORANGE_WOOL
					DyeColor.MAGENTA -> Blocks.MAGENTA_WOOL
					DyeColor.LIGHT_BLUE -> Blocks.LIGHT_BLUE_WOOL
					DyeColor.YELLOW -> Blocks.YELLOW_WOOL
					DyeColor.LIME -> Blocks.LIME_WOOL
					DyeColor.PINK -> Blocks.PINK_WOOL
					DyeColor.GRAY -> Blocks.GRAY_WOOL
					DyeColor.LIGHT_GRAY -> Blocks.LIGHT_GRAY_WOOL
					DyeColor.CYAN -> Blocks.CYAN_WOOL
					DyeColor.PURPLE -> Blocks.PURPLE_WOOL
					DyeColor.BLUE -> Blocks.BLUE_WOOL
					DyeColor.BROWN -> Blocks.BROWN_WOOL
					DyeColor.GREEN -> Blocks.GREEN_WOOL
					DyeColor.RED -> Blocks.RED_WOOL
					DyeColor.BLACK -> Blocks.BLACK_WOOL
				}.defaultBlockState()

				level.setBlockAndUpdate(pos, newState)
				return true
			}

			val newState = when {
				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_STONES)
					-> ModBlocks.getColoredStone(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_COBBLESTONES) ->
					ModBlocks.getColoredCobblestone(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_STONE_BRICKS) ->
					ModBlocks.getColoredStoneBricks(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_BRICKS) ->
					ModBlocks.getColoredBricks(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_COAL_BLOCKS) ->
					ModBlocks.getColoredCoalBlock(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_LAPIS_BLOCKS) ->
					ModBlocks.getColoredLapisBlock(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_PLANKS) ->
					ModBlocks.getColoredPlanks(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_OBSIDIANS) ->
					ModBlocks.getColoredObsidian(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_QUARTZES) ->
					ModBlocks.getColoredQuartz(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_GLOWSTONES) ->
					ModBlocks.getColoredGlowstone(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_REDSTONE_BLOCKS) ->
					ModBlocks.getColoredRedstoneBlock(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_REDSTONE_LAMPS) ->
					ModBlocks.getColoredRedstoneLamp(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_SOUL_SANDS) ->
					ModBlocks.getColoredSoulSand(color).defaultBlockState()

				blockState.isBlock(ModBlockTagsProvider.PAINTBRUSH_LAPIS_CAELESTI) ->
					ModBlocks.getLapisCaelestis(color).defaultBlockState()

				else -> null
			}

			if (newState == null) return false

			level.setBlockAndUpdate(pos, newState)
			return false
		}
	}

}




