package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDirectionName
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent

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

		private class ResultWithMessage(
			val isReady: Boolean,
			val messages: List<Component> = emptyList()
		)

		private fun checkActivationReady(
			player: Player,
			pos: BlockPos
		): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			val state = level.getBlockState(pos)

			if (!state.isBlock(Blocks.ENCHANTING_TABLE)) return false

			val result = isActivationReady(level, pos)

			for (component in result.messages) {
				player.tell(component)
			}

			return result.isReady
		}

		private fun isActivationReady(
			level: ServerLevel,
			enchantingTablePos: BlockPos
		): ResultWithMessage {
			if (!level.getBlockState(enchantingTablePos).isBlock(Blocks.ENCHANTING_TABLE)) {
				return ResultWithMessage(false)
			}

			val messages = mutableListOf<Component>()

			if (!level.getBiome(enchantingTablePos).isHolder(Tags.Biomes.IS_OVERWORLD)) {
				messages += Component.literal("You can only activate the Division Sigil in the Overworld!")
			}

			if (!level.canSeeSky(enchantingTablePos)) {
				messages += Component.literal("The Enchanting Table must be able to see the sky.")
			}

			for (dx in -1..1) for (dz in -1..1) {
				if (dx == 0 && dz == 0) continue

				val checkPos = enchantingTablePos.offset(dx, 0, dz)
				val checkState = level.getBlockState(checkPos)

				if (!checkState.isBlock(Blocks.REDSTONE_WIRE)) {
					messages += Component.literal("You must have Redstone surrounding the Enchanting Table.")
					messages += Component.literal("It's missing at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}.")
					return ResultWithMessage(false, messages)
				}
			}

			for (dx in -5..5) for (dz in -5..5) {
				val checkPos = enchantingTablePos.offset(dx, -1, dz)
				val checkState = level.getBlockState(checkPos)

				if (!checkState.isBlock(BlockTags.DIRT)) {
					messages += Component.literal("You must have a 5x5 layer of Dirt under the Enchanting Table.")
					messages += Component.literal("It's missing at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}.")
					return ResultWithMessage(false, messages)
				}
			}

			if (level.dayTime !in 17500..18500) {
				messages += Component.literal("You can only activate the Division Sigil at midnight.")
			}

			if (level.getBrightness(LightLayer.BLOCK, enchantingTablePos.above()) > 7) {
				messages += Component.literal("The Enchanting Table must be in darkness.")
			}

			if (messages.isEmpty()) {
				messages += Component.literal("The Division Sigil is ready to be activated!")
				messages += Component.literal("Kill a mob nearby the Enchanting Table.")
			}

			return ResultWithMessage(true, messages)
		}

		private fun checkInversionReady(
			player: Player,
			pos: BlockPos
		): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			val state = level.getBlockState(pos)

			if (!state.isBlock(Blocks.BEACON)) return false
			val result = isInversionReady(level, pos)

			for (component in result.messages) {
				player.tell(component)
			}

			return result.isReady
		}

		private fun isInversionReady(
			level: ServerLevel,
			catalystPos: BlockPos
		): ResultWithMessage {
			if (!level.getBlockState(catalystPos).isBlock(Blocks.BEACON)) {
				return ResultWithMessage(false)
			}

			val messages = mutableListOf<Component>()

			if (!level.getBiome(catalystPos).isHolder(Tags.Biomes.IS_END)) {
				messages += Component.literal("You can only invert the Division Sigil in the End!")
			}

			val directions = Direction.Plane.HORIZONTAL

			for (dir in directions) {
				val offset = dir.normal.multiply(5)
				val checkPos = catalystPos.offset(offset)

				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null)
				@Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
				if (itemHandler == null) {
					messages += Component.literal("Yuo must have a Chest 5 blocks to the ${dir.getDirectionName()}.")
				}
			}

			if (messages.isNotEmpty()) {
				return ResultWithMessage(false, messages)
			}

			val stringRedstonePositions = """
				◼◻◻◻◻◻◻◻◻
				◼◻◼◼◼◼◼◼◼
				◼◻◼◻◻◻◻◻◼
				◼◻◼◻◼◼◼◻◼
				◼◻◼◻B◻◼◻◼
				◼◻◼◼◼◻◼◻◼
				◼◻◻◻◻◻◼◻◼
				◼◼◼◼◼◼◼◻◼
				◻◻◻◻◻◻◻◻◼
			""".trimIndent()
				.lines()

			for ((row, line) in stringRedstonePositions.withIndex()) {
				val north = row - 4
				for ((column, char) in line.withIndex()) {
					val west = column - 4
					val checkPos = catalystPos.north(north).west(west)
					val checkState = level.getBlockState(checkPos)

					if (char == '◼' && !checkState.isBlock(Blocks.REDSTONE_WIRE)) {
						messages += Component.literal("You are missing a Redstone at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}.")
					}

					if (char == '◻' && !checkState.isBlock(Blocks.TRIPWIRE)) {
						messages += Component.literal("You are missing a String at ${checkPos.x}, ${checkPos.y}, ${checkPos.z}.")
					}

					if (messages.isNotEmpty()) {
						return ResultWithMessage(false, messages)
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
				val checkPos = catalystPos.offset(offset)

				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null) ?: continue

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
					messages += Component.literal("You need at least $amountNeeded items from the tag #${tag.location()} in the Chest to the ${dir.getDirectionName()}, but you only have $count.")
				}
			}

			if (messages.isEmpty()) {
				messages += Component.literal("The Division Sigil is ready to be inverted!")
				messages += Component.literal("Kill an Iron Golem near the Beacon to begin the ritual.")
			}

			return ResultWithMessage(true, messages)
		}

		fun handleEntityDeath(event: LivingDeathEvent) {
			if (event.isCanceled) return
			val entity = event.entity
			if (entity.isClientSide) return

			if (entity is Mob) {
				activateSigils(entity)
			}

		}

		private fun activateSigils(entity: Mob): Boolean {
			val level = entity.level() as? ServerLevel ?: return false
			val entityPos = entity.blockPosition()

			val radius = 10

			val area = BlockPos.betweenClosed(
				entityPos.offset(-radius, -radius, -radius),
				entityPos.offset(radius, radius, radius)
			)

			val enchantingTablePos = area
				.firstOrNull { checkPos ->
					isActivationReady(level, checkPos).isReady
				}
				?: return false

			val aabb = AABB(enchantingTablePos).inflate(20.0)
			val players = level.getEntitiesOfClass(Player::class.java, aabb)

			val divisionSigils = mutableListOf<ItemStack>()

			for (player in players) {
				val allStacks = player.inventory.items + player.inventory.offhand
				val minChargeSigil = allStacks
					.asSequence()
					.filter { it.isItem(ModItems.DIVISION_SIGIL) }
					.filter { it.getOrDefault(ModDataComponents.REMAINING_USES, 0) >= 0 }
					.sortedBy { it.getOrDefault(ModDataComponents.REMAINING_USES, 0) }
					.firstOrNull()

				if (minChargeSigil != null) {
					divisionSigils += minChargeSigil
				}
			}

			if (divisionSigils.isEmpty()) return false

			for (sigil in divisionSigils) {
				sigil.set(ModDataComponents.REMAINING_USES, USES_AFTER_ACTIVATION)
			}

			for (dx in -7..7) for (dz in -7..7) {
				val checkPos = enchantingTablePos.offset(dx, -1, dz)
				val checkState = level.getBlockState(checkPos)

				if (checkState.isBlock(BlockTags.DIRT)) {
					level.setBlockAndUpdate(
						checkPos,
						ModBlocks.CURSED_EARTH.defaultBlockState()
					)
				}
			}

			return true
		}
	}

}