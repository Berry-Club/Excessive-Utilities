package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDirectionName
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.block.CursedEarthBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
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
import net.neoforged.neoforge.items.IItemHandler

class DivisionSigilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		if (level.isClientSide) return InteractionResult.PASS

		val player = context.player ?: return InteractionResult.SUCCESS
		val pos = context.clickedPos
		val stack = context.itemInHand

		if (isInverted(stack)) return InteractionResult.PASS

		if (tryActivate(player, pos)) return InteractionResult.SUCCESS
		if (tryInvert(player, pos)) return InteractionResult.SUCCESS

		return InteractionResult.PASS
	}

	override fun getName(stack: ItemStack): Component {
		return if (isInverted(stack)) {
			ModItemLang.PSEUDO_INVERSION_SIGIL.toComponent()
		} else {
			super.getName(stack)
		}
	}

	override fun isFoil(stack: ItemStack): Boolean {
		return isInverted(stack)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		if (isInverted(stack)) {
			tooltipComponents += ModMenuLang.INFINITE_USES.toComponent()
			return
		}

		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
		tooltipComponents += ModMenuLang.REMAINING_USES.toComponent(remainingUses)
	}

	override fun isBarVisible(stack: ItemStack): Boolean {
		return !isInverted(stack)
	}

	override fun getBarWidth(stack: ItemStack): Int {
		if (isInverted(stack)) return MAX_BAR_WIDTH

		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
		return (remainingUses * MAX_BAR_WIDTH) / USES_AFTER_ACTIVATION
	}

	companion object {
		const val USES_AFTER_ACTIVATION = 256
		private const val MAX_BAR_WIDTH = 13
		private const val ACTIVATION_SEARCH_RADIUS = 10
		private const val SIGIL_SEARCH_RADIUS = 20.0
		private const val REQUIRED_UNIQUE_ITEM_COUNT = 12
		private const val MIDNIGHT_DAY_TIME_MIN = 17_500
		private const val MIDNIGHT_DAY_TIME_MAX = 18_500
		private const val MAX_BLOCK_LIGHT_FOR_DARKNESS = 7
		private const val CHEST_HORIZONTAL_OFFSET = 5
		private const val REDSTONE_RING_RADIUS = 1
		private const val DIRT_RADIUS = 5

		fun defaultProperties(): Properties {
			return Properties()
				.stacksTo(1)
				.fireResistant()
				.component(ModDataComponents.REMAINING_USES, USES_AFTER_ACTIVATION)
		}

		fun isInverted(stack: ItemStack): Boolean {
			return !stack.has(ModDataComponents.REMAINING_USES)
		}

		private fun tryActivate(player: Player, pos: BlockPos): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			if (!level.getBlockState(pos).isBlock(Blocks.ENCHANTING_TABLE)) return false

			val result = getActivationResult(level, pos)
			sendMessages(player, result.messages)
			return result.isReady
		}

		private fun tryInvert(player: Player, pos: BlockPos): Boolean {
			val level = player.level() as? ServerLevel ?: return false
			if (!level.getBlockState(pos).isBlock(Blocks.BEACON)) return false

			val result = getInversionResult(level, pos)
			sendMessages(player, result.messages)
			return result.isReady
		}

		private fun sendMessages(player: Player, messages: List<Component>) {
			for (message in messages) {
				player.tell(message)
			}
		}

		private fun getActivationResult(level: ServerLevel, enchantingTablePos: BlockPos): ActivationResult {
			if (!level.getBlockState(enchantingTablePos).isBlock(Blocks.ENCHANTING_TABLE)) {
				return ActivationResult(false)
			}

			val messages = mutableListOf<Component>()

			checkActivationBiome(level, enchantingTablePos, messages)
			checkActivationSkyAccess(level, enchantingTablePos, messages)

			val redstoneResult = checkActivationRedstoneRing(level, enchantingTablePos)
			if (!redstoneResult.isReady) return ActivationResult(false, messages + redstoneResult.messages)
			messages += redstoneResult.messages

			val dirtResult = checkActivationDirtBase(level, enchantingTablePos)
			if (!dirtResult.isReady) return ActivationResult(false, messages + dirtResult.messages)
			messages += dirtResult.messages

			checkActivationTime(level, messages)
			checkActivationDarkness(level, enchantingTablePos, messages)

			if (messages.isEmpty()) {
				messages += ModMessageLang.DIVISION_READY_ONE.toComponent()
				messages += ModMessageLang.DIVISION_READY_TWO.toComponent()

				return ActivationResult(true, messages)
			} else {
				return ActivationResult(false, messages)
			}
		}

		private fun checkActivationBiome(
			level: ServerLevel,
			pos: BlockPos,
			messages: MutableList<Component>
		) {
			if (!level.getBiome(pos).isHolder(Tags.Biomes.IS_OVERWORLD)) {
				messages += ModMessageLang.DIVISION_OVERWORLD_ONLY.toComponent()
			}
		}

		private fun checkActivationSkyAccess(
			level: ServerLevel,
			pos: BlockPos,
			messages: MutableList<Component>
		) {
			if (!level.canSeeSky(pos)) {
				messages += ModMessageLang.DIVISION_SEE_SKY.toComponent()
			}
		}

		private fun checkActivationRedstoneRing(
			level: ServerLevel,
			enchantingTablePos: BlockPos
		): ActivationResult {
			val messages = mutableListOf<Component>()

			for (dx in -REDSTONE_RING_RADIUS..REDSTONE_RING_RADIUS) {
				for (dz in -REDSTONE_RING_RADIUS..REDSTONE_RING_RADIUS) {
					if (dx == 0 && dz == 0) continue

					val checkPos = enchantingTablePos.offset(dx, 0, dz)
					if (!level.getBlockState(checkPos).isBlock(Blocks.REDSTONE_WIRE)) {
						messages += ModMessageLang.DIVISION_REDSTONE.toComponent()
						messages += ModMessageLang.DIVISION_REDSTONE_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
						return ActivationResult(false, messages)
					}
				}
			}

			return ActivationResult(true, messages)
		}

		private fun checkActivationDirtBase(
			level: ServerLevel,
			enchantingTablePos: BlockPos
		): ActivationResult {
			val messages = mutableListOf<Component>()

			for (dx in -DIRT_RADIUS..DIRT_RADIUS) {
				for (dz in -DIRT_RADIUS..DIRT_RADIUS) {
					val checkPos = enchantingTablePos.offset(dx, -1, dz)
					if (!level.getBlockState(checkPos).isBlock(BlockTags.DIRT)) {
						messages += ModMessageLang.DIVISION_DIRT.toComponent()
						messages += ModMessageLang.DIVISION_DIRT_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
						return ActivationResult(false, messages)
					}
				}
			}

			return ActivationResult(true, messages)
		}

		private fun checkActivationTime(level: ServerLevel, messages: MutableList<Component>) {
			if (level.dayTime !in MIDNIGHT_DAY_TIME_MIN..MIDNIGHT_DAY_TIME_MAX) {
				messages += ModMessageLang.DIVISION_MIDNIGHT.toComponent()
			}
		}

		private fun checkActivationDarkness(
			level: ServerLevel,
			enchantingTablePos: BlockPos,
			messages: MutableList<Component>
		) {
			if (level.getBrightness(LightLayer.BLOCK, enchantingTablePos.above()) > MAX_BLOCK_LIGHT_FOR_DARKNESS) {
				messages += ModMessageLang.DIVISION_DARKNESS.toComponent()
			}
		}

		private fun getInversionResult(level: ServerLevel, catalystPos: BlockPos): ActivationResult {
			if (!level.getBlockState(catalystPos).isBlock(Blocks.BEACON)) {
				return ActivationResult(false)
			}

			val messages = mutableListOf<Component>()

			checkInversionBiome(level, catalystPos, messages)

			val chestResult = checkInversionChests(level, catalystPos)
			if (!chestResult.isReady) return ActivationResult(false, messages + chestResult.messages)

			val patternResult = checkInversionPattern(level, catalystPos)
			if (!patternResult.isReady) return ActivationResult(false, messages + patternResult.messages)

			checkInversionItemContents(level, catalystPos, messages)

			if (messages.isEmpty()) {
				messages += ModMessageLang.INVERSION_READY_ONE.toComponent()
				messages += ModMessageLang.INVERSION_READY_TWO.toComponent()
			}

			return ActivationResult(true, messages)
		}

		private fun checkInversionBiome(
			level: ServerLevel,
			pos: BlockPos,
			messages: MutableList<Component>
		) {
			if (!level.getBiome(pos).isHolder(Tags.Biomes.IS_END)) {
				messages += ModMessageLang.INVERSION_END_ONLY.toComponent()
			}
		}

		private fun checkInversionChests(level: ServerLevel, catalystPos: BlockPos): ActivationResult {
			val messages = mutableListOf<Component>()

			for (direction in Direction.Plane.HORIZONTAL) {
				val checkPos = catalystPos.offset(direction.normal.multiply(CHEST_HORIZONTAL_OFFSET))
				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null)
				if (itemHandler == null) {
					messages += ModMessageLang.INVERSION_MISSING_CHEST.toComponent(direction.getDirectionName())
				}
			}

			return ActivationResult(messages.isEmpty(), messages)
		}

		private fun checkInversionPattern(level: ServerLevel, catalystPos: BlockPos): ActivationResult {
			// ◼ = redstone wire, ◻ = tripwire (string), B = beacon (center, skipped)
			val patternRows = listOf(
				"◼◻◻◻◻◻◻◻◻",
				"◼◻◼◼◼◼◼◼◼",
				"◼◻◼◻◻◻◻◻◼",
				"◼◻◼◻◼◼◼◻◼",
				"◼◻◼◻B◻◼◻◼",
				"◼◻◼◼◼◻◼◻◼",
				"◼◻◻◻◻◻◼◻◼",
				"◼◼◼◼◼◼◼◻◼",
				"◻◻◻◻◻◻◻◻◼",
			)

			val centerRow = 4
			val centerColumn = 4

			for ((rowIndex, rowString) in patternRows.withIndex()) {
				val northOffset = rowIndex - centerRow
				for ((columnIndex, character) in rowString.withIndex()) {
					val westOffset = columnIndex - centerColumn
					val checkPos = catalystPos.north(northOffset).west(westOffset)
					val checkState = level.getBlockState(checkPos)

					if (character == '◼' && !checkState.isBlock(Blocks.REDSTONE_WIRE)) {
						return ActivationResult(
							false,
							listOf(ModMessageLang.INVERSION_MISSING_REDSTONE.toComponent(checkPos.x, checkPos.y, checkPos.z))
						)
					}

					if (character == '◻' && !checkState.isBlock(Blocks.TRIPWIRE)) {
						return ActivationResult(
							false,
							listOf(ModMessageLang.INVERSION_MISSING_STRING.toComponent(checkPos.x, checkPos.y, checkPos.z))
						)
					}
				}
			}

			return ActivationResult(true)
		}

		private fun checkInversionItemContents(
			level: ServerLevel,
			catalystPos: BlockPos,
			messages: MutableList<Component>
		) {
			val contentRequirements = mapOf(
				Direction.NORTH to ModItemTagsProvider.CHILDREN_OF_FIRE,
				Direction.SOUTH to ModItemTagsProvider.GIFTS_OF_EARTH,
				Direction.EAST to ModItemTagsProvider.DESCENDANTS_OF_WATER,
				Direction.WEST to ModItemTagsProvider.SPICES_OF_AIR,
			)

			for ((direction, itemTag) in contentRequirements) {
				val checkPos = catalystPos.offset(direction.normal.multiply(CHEST_HORIZONTAL_OFFSET))
				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null) ?: continue

				val count = if (itemTag == ModItemTagsProvider.DESCENDANTS_OF_WATER) {
					// Potions share the same Item class, so count stacks rather than unique items
					countMatchingStacks(itemHandler, itemTag)
				} else {
					countUniqueMatchingItems(itemHandler, itemTag)
				}

				if (count < REQUIRED_UNIQUE_ITEM_COUNT) {
					messages += ModMessageLang.INVERSION_MISSING_ITEMS.toComponent(
						REQUIRED_UNIQUE_ITEM_COUNT,
						itemTag.location.toString(),
						direction.getDirectionName(),
						count
					)
				}
			}
		}

		private fun countMatchingStacks(itemHandler: IItemHandler, itemTag: TagKey<Item>): Int {
			var count = 0
			for (slot in 0 until itemHandler.slots) {
				val stack = itemHandler.getStackInSlot(slot)
				if (stack.isItem(itemTag)) {
					count += stack.count
				}
			}
			return count
		}

		private fun countUniqueMatchingItems(itemHandler: IItemHandler, itemTag: TagKey<Item>): Int {
			val uniqueItems = mutableSetOf<Item>()
			for (slot in 0 until itemHandler.slots) {
				val stack = itemHandler.getStackInSlot(slot)
				if (stack.isItem(itemTag)) {
					uniqueItems.add(stack.item)
				}
			}
			return uniqueItems.size
		}

		fun handleEntityDeath(event: LivingDeathEvent) {
			if (event.isCanceled) return

			val entity = event.entity
			if (entity.isClientSide) return
			if (entity !is Mob) return

			activateSigilsNear(entity)
		}

		private fun activateSigilsNear(entity: Mob) {
			val level = entity.level() as? ServerLevel ?: return
			val entityPos = entity.blockPosition()

			val enchantingTablePos = findNearbyActivatableTable(level, entityPos) ?: return
			val sigils = collectSigilsNearTable(level, enchantingTablePos)

			if (sigils.isEmpty()) return

			rechargeSigils(sigils)
			tryPlaceCursedEarth(level, enchantingTablePos)
		}

		private fun findNearbyActivatableTable(level: ServerLevel, entityPos: BlockPos): BlockPos? {
			val searchArea = BlockPos.betweenClosed(
				entityPos.offset(-ACTIVATION_SEARCH_RADIUS, -ACTIVATION_SEARCH_RADIUS, -ACTIVATION_SEARCH_RADIUS),
				entityPos.offset(ACTIVATION_SEARCH_RADIUS, ACTIVATION_SEARCH_RADIUS, ACTIVATION_SEARCH_RADIUS)
			)

			for (checkPos in searchArea) {
				if (getActivationResult(level, checkPos).isReady) {
					return checkPos.immutable()
				}
			}

			return null
		}

		private fun collectSigilsNearTable(level: ServerLevel, enchantingTablePos: BlockPos): List<ItemStack> {
			val searchBox = AABB(enchantingTablePos).inflate(SIGIL_SEARCH_RADIUS)
			val nearbyPlayers = level.getEntitiesOfClass(Player::class.java, searchBox)

			val sigils = mutableListOf<ItemStack>()

			for (player in nearbyPlayers) {
				val lowestChargeSigil = findLowestChargeSigil(player) ?: continue
				sigils.add(lowestChargeSigil)
			}

			return sigils
		}

		private fun findLowestChargeSigil(player: Player): ItemStack? {
			val allStacks = player.inventory.items + player.inventory.offhand

			var lowestSigil: ItemStack? = null
			var lowestCharges = Int.MAX_VALUE

			for (stack in allStacks) {
				if (!stack.isItem(ModItems.DIVISION_SIGIL)) continue

				val charges = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
				if (charges >= 0 && charges < lowestCharges) {
					lowestCharges = charges
					lowestSigil = stack
				}
			}

			return lowestSigil
		}

		private fun rechargeSigils(sigils: List<ItemStack>) {
			for (sigil in sigils) {
				sigil.set(ModDataComponents.REMAINING_USES, USES_AFTER_ACTIVATION)
			}
		}

		private fun tryPlaceCursedEarth(level: ServerLevel, enchantingTablePos: BlockPos) {
			val posBelow = enchantingTablePos.below()
			if (level.getBlockState(posBelow).isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
				CursedEarthBlock.placeAndSpread(level, posBelow)
			}
		}

	}

	private class ActivationResult(
		val isReady: Boolean,
		val messages: List<Component> = emptyList()
	)

}