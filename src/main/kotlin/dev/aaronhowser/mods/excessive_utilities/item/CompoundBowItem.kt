package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class CompoundBowItem(properties: Properties) : BowItem(properties) {

	override fun createProjectile(level: Level, shooter: LivingEntity, weapon: ItemStack, ammo: ItemStack, isCrit: Boolean): Projectile {
		val projectile = super.createProjectile(level, shooter, weapon, ammo, isCrit)
		projectile.isNoGravity = true
		return projectile
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}