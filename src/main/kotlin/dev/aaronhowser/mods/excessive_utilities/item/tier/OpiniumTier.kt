package dev.aaronhowser.mods.excessive_utilities.item.tier

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.asIngredient
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

object OpiniumTier : Tier {
	override fun getUses(): Int = Int.MAX_VALUE
	override fun getSpeed(): Float = Tiers.NETHERITE.speed
	override fun getAttackDamageBonus(): Float = Tiers.NETHERITE.attackDamageBonus
	override fun getIncorrectBlocksForDrops(): TagKey<Block> = Tiers.NETHERITE.incorrectBlocksForDrops
	override fun getEnchantmentValue(): Int = Mth.ceil(Tiers.NETHERITE.enchantmentValue * 1.5)
	override fun getRepairIngredient(): Ingredient = OpiniumCoreContentsComponent.getDefaultTiers().last().getStack().asIngredient()
}