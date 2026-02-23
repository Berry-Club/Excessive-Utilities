package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.asIngredient
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

object UnstableTier : Tier {
	override fun getUses(): Int = Int.MAX_VALUE
	override fun getSpeed(): Float = Tiers.DIAMOND.speed
	override fun getAttackDamageBonus(): Float = Tiers.DIAMOND.attackDamageBonus
	override fun getIncorrectBlocksForDrops(): TagKey<Block> = Tiers.DIAMOND.incorrectBlocksForDrops
	override fun getEnchantmentValue(): Int = Mth.ceil(Tiers.DIAMOND.enchantmentValue * 1.5)
	override fun getRepairIngredient(): Ingredient = ModItems.UNSTABLE_INGOT.asIngredient()
}