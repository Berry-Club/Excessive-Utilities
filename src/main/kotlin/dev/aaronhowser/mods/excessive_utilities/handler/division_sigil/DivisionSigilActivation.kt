package dev.aaronhowser.mods.excessive_utilities.handler.division_sigil

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.allItemStacksSequence
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

		val killer = event.source.entity
		if (killer !is Player) return

		activateSigil(killer, victim)
	}

	private fun activateSigil(
		killer: Player,
		victim: LivingEntity
	) {
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
		val allStacks = player.allItemStacksSequence()

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
		val hRad = 10
		val vRad = 3

		val searchArea = BlockPos.betweenClosed(
			entityPos.offset(-hRad, -vRad, -hRad),
			entityPos.offset(hRad, vRad, hRad)
		)

		for (checkPos in searchArea) {
			val result = isValidSetup(level, checkPos)
			if (result is DivisionSigilSetupResult.Ready) {
				return checkPos.immutable()
			}
		}

		return null
	}

	fun isValidSetup(
		level: ServerLevel,
		enchantingTablePos: BlockPos
	): DivisionSigilSetupResult {
		if (!level.isLoaded(enchantingTablePos)) {
			return DivisionSigilSetupResult.Invalid(emptyList())
		}

		if (!level.getBlockState(enchantingTablePos).isBlock(Blocks.ENCHANTING_TABLE)) {
			return DivisionSigilSetupResult.Invalid(emptyList())
		}

		val messages: MutableList<Component> = mutableListOf()

		checkActivationBiome(level, enchantingTablePos, messages)
		checkActivationSkyAccess(level, enchantingTablePos, messages)
		if (messages.isNotEmpty()) {
			return DivisionSigilSetupResult.Invalid(messages.toList())
		}

		checkActivationRedstoneRing(level, enchantingTablePos, messages)
		if (messages.isNotEmpty()) {
			return DivisionSigilSetupResult.Invalid(messages.toList())
		}

		checkActivationDirtBase(level, enchantingTablePos, messages)
		if (messages.isNotEmpty()) {
			return DivisionSigilSetupResult.Invalid(messages.toList())
		}

		checkActivationTime(level, messages)
		checkActivationDarkness(level, enchantingTablePos, messages)
		if (messages.isNotEmpty()) {
			return DivisionSigilSetupResult.Invalid(messages.toList())
		}

		return DivisionSigilSetupResult.Ready(
			listOf(
				ModMessageLang.DIVISION_READY_ONE.toComponent(),
				ModMessageLang.DIVISION_READY_TWO.toComponent()
			)
		)
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
		enchantingTablePos: BlockPos,
		messages: MutableList<Component>
	) {
		val radius = 1

		for (dx in -radius..radius) {
			for (dz in -radius..radius) {
				if (dx == 0 && dz == 0) continue

				val checkPos = enchantingTablePos.offset(dx, 0, dz)
				if (!level.getBlockState(checkPos).isBlock(Blocks.REDSTONE_WIRE)) {
					messages += listOf(
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
		messages: MutableList<Component>
	) {
		val dirtRadius = 5
		val dirtArea = BlockPos.betweenClosed(
			enchantingTablePos.offset(-dirtRadius, -1, -dirtRadius),
			enchantingTablePos.offset(dirtRadius, -1, dirtRadius)
		)

		for (checkPos in dirtArea) {
			if (!level.getBlockState(checkPos).isBlock(BlockTags.DIRT)) {
				messages += listOf(
					ModMessageLang.DIVISION_DIRT.toComponent(),
					ModMessageLang.DIVISION_DIRT_AT.toComponent(checkPos.x, checkPos.y, checkPos.z)
				)
			}
		}
	}

	private fun checkActivationTime(
		level: ServerLevel,
		messages: MutableList<Component>
	) {
		val minTime = 17_500L
		val maxTime = 18_500L
		val timeOfDay = level.overworldClockTime % 24_000L
		if (timeOfDay !in minTime..maxTime) {
			messages += ModMessageLang.DIVISION_MIDNIGHT.toComponent()
		}
	}

	private fun checkActivationDarkness(
		level: ServerLevel,
		enchantingTablePos: BlockPos,
		messages: MutableList<Component>
	) {
		val lightAbove = level.getBrightness(LightLayer.BLOCK, enchantingTablePos.above())
		val maxLight = 7
		if (lightAbove > maxLight) {
			messages += ModMessageLang.DIVISION_DARKNESS.toComponent()
		}
	}

	//endregion

}