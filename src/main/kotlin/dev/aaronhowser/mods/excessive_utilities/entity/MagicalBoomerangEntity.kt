package dev.aaronhowser.mods.excessive_utilities.entity

import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class MagicalBoomerangEntity(
	entityType: EntityType<out Projectile>,
	level: Level
) : Projectile(entityType, level) {

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		TODO("Not yet implemented")
	}

}