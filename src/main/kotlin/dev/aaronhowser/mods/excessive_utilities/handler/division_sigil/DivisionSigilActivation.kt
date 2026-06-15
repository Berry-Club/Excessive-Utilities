package dev.aaronhowser.mods.excessive_utilities.handler.division_sigil

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.block.CursedEarthBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.DivisionSigilItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent

object DivisionSigilActivation {

	fun handleEntityDeath(event: LivingDeathEvent) {
		if (event.isCanceled) return

		val victim = event.entity
		if (victim.isClientSide) return
		if (victim !is LivingEntity) return

		val killer = event.source.entity
		if (killer !is Player) return

		activateSigil(killer, victim)
	}

	private fun activateSigil(killer: Player, victim: LivingEntity) {
		val level = victim.level() as? ServerLevel ?: return

		val sigil = findLowestChargeSigil(killer) ?: return
		val enchantingTablePos = findValidEnchantingTable(level, victim.blockPosition()) ?: return

		sigil.set(ModDataComponents.REMAINING_USES, DivisionSigilItem.USES_AFTER_ACTIVATION)

		val posBelow = enchantingTablePos.below()
		val stateBelow = level.getBlockState(posBelow)
		if (stateBelow.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
			CursedEarthBlock.placeAndSpread(level, posBelow)
		}
	}

	private fun findLowestChargeSigil(player: Player): ItemStack? {
		val allStacks = player.inventory.items + player.inventory.offhand

		var lowestSigil: ItemStack? = null
		var lowestCharges = Int.MAX_VALUE

		for (stack in allStacks) {
			if (!stack.isItem(ModItems.DIVISION_SIGIL)) continue

			val charges = stack.getOrDefault(ModDataComponents.REMAINING_USES, 0)
			if (charges in 0 until lowestCharges) {
				lowestCharges = charges
				lowestSigil = stack
			}
		}

		return lowestSigil
	}

	// region Structure

	fun findValidEnchantingTable(
		level: ServerLevel,
		entityPos: BlockPos
	): BlockPos? {
		val radius = 10

		val searchArea = BlockPos.betweenClosed(
			entityPos.offset(-radius, -radius, -radius),
			entityPos.offset(radius, radius, radius)
		)

		for (checkPos in searchArea) {
			val result = isValidSetup(level, checkPos)
			if (result.isValid) {
				return checkPos.immutable()
			}
		}

		return null
	}

	fun isValidSetup(
		level: ServerLevel,
		enchantingTablePos: BlockPos
	): ActivationResult {
		val result = ActivationResult(isValid = true)

		if (!level.isLoaded(enchantingTablePos)) {
			result.isValid = false
			return result
		}

		if (!level.getBlockState(enchantingTablePos).isBlock(Blocks.ENCHANTING_TABLE)) {
			result.isValid = false
			return result
		}

		checkActivationBiome(level, enchantingTablePos, result)
		checkActivationSkyAccess(level, enchantingTablePos, result)
		if (!result.isValid) {
			return result
		}

		checkActivationRedstoneRing(level, enchantingTablePos, result)
		if (!result.isValid) {
			return result
		}

		checkActivationDirtBase(level, enchantingTablePos, result)
		if (!result.isValid) {
			return result
		}

		checkActivationTime(level, result)
		checkActivationDarkness(level, enchantingTablePos, result)
		if (!result.isValid) {
			return result
		}

		result.addMessages(
			ModMessageLang.DIVISION_READY_ONE.toComponent(),
			ModMessageLang.DIVISION_READY_TWO.toComponent()
		)

		return result
	}

	private fun checkActivationBiome(
		level: ServerLevel,
		pos: BlockPos,
		result: ActivationResult
	) {
		if (!level.getBiome(pos).isHolder(Tags.Biomes.IS_OVERWORLD)) {
			result.failWithMessages(ModMessageLang.DIVISION_OVERWORLD_ONLY.toComponent())
		}
	}

	private fun checkActivationSkyAccess(
		level: ServerLevel,
		pos: BlockPos,
		result: ActivationResult
	) {
		if (!level.canSeeSky(pos)) {
			result.failWithMessages(ModMessageLang.DIVISION_SEE_SKY.toComponent())
		}
	}

	private fun checkActivationRedstoneRing(
		level: ServerLevel,
		enchantingTablePos: BlockPos,
		result: ActivationResult
	) {
		val radius = 1

		for (dx in -radius..radius) {
			for (dz in -radius..radius) {
				if (dx == 0 && dz == 0) continue

				val checkPos = enchantingTablePos.offset(dx, 0, dz)
				if (!level.getBlockState(checkPos).isBlock(Blocks.REDSTONE_WIRE)) {
					result.failWithMessages(
						ModMessageLang.DIVISION_REDSTONE.toComponent(),
						ModMessageLang.DIVISION_REDSTONE_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
					)
				}
			}
		}
	}

	private fun checkActivationDirtBase(
		level: ServerLevel,
		enchantingTablePos: BlockPos,
		result: ActivationResult
	) {
		val dirtRadius = 5
		val dirtArea = BlockPos.betweenClosed(
			enchantingTablePos.offset(-dirtRadius, -1, -dirtRadius),
			enchantingTablePos.offset(dirtRadius, -1, dirtRadius)
		)

		for (checkPos in dirtArea) {
			if (!level.getBlockState(checkPos).isBlock(BlockTags.DIRT)) {
				result.failWithMessages(
					ModMessageLang.DIVISION_DIRT.toComponent(),
					ModMessageLang.DIVISION_DIRT_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
				)
			}
		}
	}

	private fun checkActivationTime(
		level: ServerLevel,
		result: ActivationResult
	) {
		val minTime = 17_500
		val maxTime = 18_500
		if (level.dayTime !in minTime..maxTime) {
			result.failWithMessages(ModMessageLang.DIVISION_MIDNIGHT.toComponent())
		}
	}

	private fun checkActivationDarkness(
		level: ServerLevel,
		enchantingTablePos: BlockPos,
		result: ActivationResult
	) {
		val lightAbove = level.getBrightness(LightLayer.BLOCK, enchantingTablePos.above())
		val maxLight = 7
		if (lightAbove > maxLight) {
			result.failWithMessages(ModMessageLang.DIVISION_DARKNESS.toComponent())
		}
	}

	//endregion

	class ActivationResult(
		var isValid: Boolean
	) {
		val messages: MutableList<Component> = mutableListOf()

		fun failWithMessages(vararg message: Component) {
			messages += message
			isValid = false
		}

		fun addMessages(vararg message: Component) {
			messages += message
		}
	}


}