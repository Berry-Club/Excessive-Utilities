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
		if (level.isClientSide) return InteractionResult.PASS

		val player = context.player ?: return InteractionResult.SUCCESS

		val pos = context.clickedPos
		val stack = context.itemInHand

		// If inverted already, don't do anything
		if (isInverted(stack)) {
			return InteractionResult.PASS
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
		if (isInverted(stack)) return 13

		val remainingUses = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
		return (remainingUses * 13) / USES_AFTER_ACTIVATION
	}

	companion object {
		const val USES_AFTER_ACTIVATION = 256

		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.fireResistant()
					.component(ModDataComponents.REMAINING_USES, USES_AFTER_ACTIVATION)
			}

		fun isInverted(stack: ItemStack): Boolean {
			return !stack.has(ModDataComponents.REMAINING_USES)
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
				messages += ModMessageLang.DIVISION_OVERWORLD_ONLY.toComponent()
			}

			if (!level.canSeeSky(enchantingTablePos)) {
				messages += ModMessageLang.DIVISION_SEE_SKY.toComponent()
			}

			for (dx in -1..1) for (dz in -1..1) {
				if (dx == 0 && dz == 0) continue

				val checkPos = enchantingTablePos.offset(dx, 0, dz)
				val checkState = level.getBlockState(checkPos)

				if (!checkState.isBlock(Blocks.REDSTONE_WIRE)) {
					messages += ModMessageLang.DIVISION_REDSTONE.toComponent()
					messages += ModMessageLang.DIVISION_REDSTONE_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
					return ResultWithMessage(false, messages)
				}
			}

			for (dx in -5..5) for (dz in -5..5) {
				val checkPos = enchantingTablePos.offset(dx, -1, dz)
				val checkState = level.getBlockState(checkPos)

				if (!checkState.isBlock(BlockTags.DIRT)) {
					messages += ModMessageLang.DIVISION_DIRT.toComponent()
					messages += ModMessageLang.DIVISION_DIRT_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
					return ResultWithMessage(false, messages)
				}
			}

			if (level.dayTime !in 17500..18500) {
				messages += ModMessageLang.DIVISION_MIDNIGHT.toComponent()
			}

			if (level.getBrightness(LightLayer.BLOCK, enchantingTablePos.above()) > 7) {
				messages += ModMessageLang.DIVISION_DARKNESS.toComponent()
			}

			if (messages.isEmpty()) {
				messages += ModMessageLang.DIVISION_READY_ONE.toComponent()
				messages += ModMessageLang.DIVISION_READY_TWO.toComponent()
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
				messages += ModMessageLang.INVERSION_END_ONLY.toComponent()
			}

			val directions = Direction.Plane.HORIZONTAL

			for (dir in directions) {
				val offset = dir.normal.multiply(5)
				val checkPos = catalystPos.offset(offset)

				val itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, checkPos, null)
				@Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
				if (itemHandler == null) {
					messages += ModMessageLang.INVERSION_MISSING_CHEST.toComponent(dir.getDirectionName())
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
						messages += ModMessageLang.INVERSION_MISSING_REDSTONE.toComponent(checkPos.x, checkPos.y, checkPos.z)
					}

					if (char == '◻' && !checkState.isBlock(Blocks.TRIPWIRE)) {
						messages += ModMessageLang.INVERSION_MISSING_STRING.toComponent(checkPos.x, checkPos.y, checkPos.z)
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
					messages += ModMessageLang.INVERSION_MISSING_ITEMS.toComponent(amountNeeded, tag.location.toString(), dir.getDirectionName(), count)
				}
			}

			if (messages.isEmpty()) {
				messages += ModMessageLang.INVERSION_READY_ONE.toComponent()
				messages += ModMessageLang.INVERSION_READY_TWO.toComponent()
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

			val posBelow = enchantingTablePos.below()
			val stateBelow = level.getBlockState(posBelow)
			if (stateBelow.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
				CursedEarthBlock.placeAndSpread(level, posBelow)
			}

			return true
		}
	}

}