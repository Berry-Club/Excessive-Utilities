package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDirectionName
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.Tags

class DivisionSigilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		if (level.isClientSide) return InteractionResult.SUCCESS

		val player = context.player ?: return InteractionResult.SUCCESS

		val pos = context.clickedPos
		val stack = context.itemInHand

		// If inverted already, don't do anything
		if (stack.getOrDefault(ModDataComponents.REMAINING_USES, 0) < 0) {
			return InteractionResult.SUCCESS
		}

		if (checkActivationReady(player, pos)) {
			return InteractionResult.SUCCESS
		}

		if (checkInversionReady(player, pos)) {
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

	override fun getName(stack: ItemStack): Component {
		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)

		return if (remainingUses < 0) {
			ModItemLang.PSEUDO_INVERSION_SIGIL.toComponent()
		} else {
			super.getName(stack)
		}
	}

	override fun isFoil(stack: ItemStack): Boolean {
		return stack.getOrDefault(ModDataComponents.REMAINING_USES, 0) < 0
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)

		val component = if (remainingUses < 0) {
			Component.literal("Infinite Uses")
		} else {
			Component.literal("$remainingUses Uses Remaining")
		}

		tooltipComponents.add(component)
	}

	companion object {
		const val USES_AFTER_ACTIVATION = 256
		const val USES_AFTER_INVERSION = -1

		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.fireResistant()
					.component(ModDataComponents.REMAINING_USES, 0)
			}

		private fun checkActivationReady(
			player: Player,
			pos: BlockPos
		): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			val state = level.getBlockState(pos)

			if (!state.isBlock(Blocks.ENCHANTING_TABLE)) return false

			if (!level.getBiome(pos).isHolder(Tags.Biomes.IS_OVERWORLD)) {
				player.tell(Component.literal("You can only activate the Division Sigil in the Overworld!"))
				return true
			}

			if (!level.canSeeSky(pos)) {
				player.tell(Component.literal("The Enchanting Table must be able to see the sky."))
				return true
			}

			for (dx in -1..1) for (dz in -1..1) {
				if (dx == 0 && dz == 0) continue

				val checkPos = pos.offset(dx, 0, dz)
				val checkState = level.getBlockState(checkPos)

				if (!checkState.isBlock(Blocks.REDSTONE_WIRE)) {
					player.tell(Component.literal("You must have Redstone surrounding the Enchanting Table."))
					player.tell(Component.literal("It's missing at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}."))
					return true
				}
			}

			for (dx in -5..5) for (dz in -5..5) {
				val checkPos = pos.offset(dx, -1, dz)
				val checkState = level.getBlockState(checkPos)

				if (!checkState.isBlock(BlockTags.DIRT)) {
					player.tell(Component.literal("You must have a 5x5 layer of Dirt under the Enchanting Table."))
					player.tell(Component.literal("It's missing at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}."))
					return true
				}
			}

			if (level.dayTime !in 17500..18500) {
				player.tell(Component.literal("You can only activate the Division Sigil at midnight."))
				return true
			}

			if (level.getBrightness(LightLayer.BLOCK, pos.above()) > 7) {
				player.tell(Component.literal("The Enchanting Table must be in darkness."))
				return true
			}

			player.tell(Component.literal("The Division Sigil is ready to be activated!"))
			player.tell(Component.literal("Kill a mob nearby the Enchanting Table."))

			return true
		}

		private fun checkInversionReady(
			player: Player,
			pos: BlockPos
		): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			val state = level.getBlockState(pos)

			if (!state.isBlock(Blocks.BEACON)) return false

			if (!level.getBiome(pos).isHolder(Tags.Biomes.IS_END)) {
				player.tell(Component.literal("You can only invert the Division Sigil in the End!"))
				return true
			}

			val directions = Direction.Plane.HORIZONTAL

			for (dir in directions) {
				val offset = dir.normal.multiply(5)
				val checkPos = pos.offset(offset)

				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null)
				if (itemHandler == null) {
					player.tell(Component.literal("You must have Chests 5 blocks from the Beacon in each direction."))
					player.tell(Component.literal("One is missing to the ${dir.getDirectionName()}."))
					return true
				}
			}

			val stringRedstonePositions = """
				RSSSSSSSS
				RSRRRRRRR
				RSRSSSSSR
				RSRSRRRSR
				RSRSBSRSR
				RSRRRSRSR
				RSSSSSRSR
				RRRRRRRSR
				SSSSSSSSR
			""".trimIndent()
				.lines()

			for ((row, line) in stringRedstonePositions.withIndex()) {
				val north = row - 4
				for ((column, char) in line.withIndex()) {
					val west = column - 4
					val checkPos = pos.north(north).west(west)
					val checkState = level.getBlockState(checkPos)

					if (char == 'R' && !checkState.isBlock(Blocks.REDSTONE_WIRE)) {
						player.tell(Component.literal("You are missing a Redstone at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}."))
						return true
					}

					if (char == 'S' && !checkState.isBlock(Blocks.TRIPWIRE)) {
						player.tell(Component.literal("You are missing a String at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}."))
						return true
					}
				}
			}

			val contentRequirements = mapOf(
				Direction.NORTH to ModItemTagsProvider.CHILDREN_OF_FIRE,
				Direction.SOUTH to ModItemTagsProvider.GIFTS_OF_EARTH,
				Direction.EAST to ModItemTagsProvider.DESCENDANTS_OF_WATER,
				Direction.WEST to ModItemTagsProvider.SPICES_OF_AIR,
			)

			for ((dir, tag) in contentRequirements) {
				val offset = dir.normal.multiply(5)
				val checkPos = pos.offset(offset)

				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null) ?: return false

				var count = 0

				// Special handling because potions are all the same Item class
				if (tag == ModItemTagsProvider.DESCENDANTS_OF_WATER) {
					for (slot in 0 until itemHandler.slots) {
						val stack = itemHandler.getStackInSlot(slot)
						if (stack.isItem(tag)) {
							count += stack.count
						}
					}
				} else {
					val uniqueItems = mutableSetOf<Item>()
					for (slot in 0 until itemHandler.slots) {
						val stack = itemHandler.getStackInSlot(slot)
						if (stack.isItem(tag)) {
							uniqueItems.add(stack.item)
						}
					}

					count = uniqueItems.size
				}

				val amountNeeded = 12
				if (count < amountNeeded) {
					player.tell(Component.literal("You need at least $amountNeeded items from the tag #${tag.location()} in the Chest to the ${dir.getDirectionName()}, but you only have $count."))
					return true
				}
			}

			player.tell(Component.literal("The Division Sigil is ready to be inverted!"))
			player.tell(Component.literal("Kill an Iron Golem near the Beacon to begin the ritual."))

			return true
		}
	}

}