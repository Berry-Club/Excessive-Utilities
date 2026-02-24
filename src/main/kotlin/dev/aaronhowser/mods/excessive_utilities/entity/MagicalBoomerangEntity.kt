package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrowableProjectile
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

class MagicalBoomerangEntity(
	entityType: EntityType<MagicalBoomerangEntity>,
	level: Level
) : ThrowableProjectile(entityType, level) {

	constructor(level: Level, entity: LivingEntity) : this(ModEntityTypes.MAGICAL_BOOMERANG.get(), level) {
		owner = entity
		setPos(entity.x, entity.eyeY - 0.1, entity.z)
	}

	private var isReturning: Boolean = false

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {}

	override fun canUsePortal(allowPassengers: Boolean): Boolean = false
	override fun isInvulnerable(): Boolean = true
	override fun isInvulnerableTo(source: DamageSource): Boolean = true

	override fun isNoGravity(): Boolean = true
	override fun getDefaultGravity(): Double = 0.0

	override fun onHitBlock(result: BlockHitResult) {
		super.onHitBlock(result)
		isReturning = true
	}

	override fun onHitEntity(result: EntityHitResult) {
		super.onHitEntity(result)
		if (tickCount < 10) return

		isReturning = true

		val hitEntity = result.entity
		if (hitEntity == owner && hitEntity is Player) {
			val boomerangStack = hitEntity.inventory
				.items
				.firstOrNull { it.isItem(ModItems.MAGICAL_BOOMERANG) && it.get(ModDataComponents.THROWN_BOOMERANG) == uuid }

			boomerangStack?.remove(ModDataComponents.THROWN_BOOMERANG)
			discard()
		}
	}

	override fun tick() {
		super.tick()

		carryItems()
		returnToOwner()
	}

	private fun returnToOwner() {
		if (!isReturning) return
		val owner = owner ?: return
		val targetPos = Vec3(owner.x, owner.eyeY - 0.4, owner.z)

		val currentSpeed = deltaMovement.length()
		val newDirection = position().vectorTo(targetPos).normalize()

		deltaMovement = newDirection.scale(currentSpeed.coerceAtLeast(0.1))
	}

	private fun carryItems() {
		val aabb = boundingBox.inflate(3.0)
		val itemEntities = level().getEntitiesOfClass(ItemEntity::class.java, aabb)

		for (itemEntity in itemEntities) {
			if (itemEntity.isAlive) {
				itemEntity.setPos(position())
			}
		}
	}

}