package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrowableProjectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

class MagicalBoomerangEntity(
	entityType: EntityType<MagicalBoomerangEntity>,
	level: Level
) : ThrowableProjectile(entityType, level) {

	constructor(
		level: Level,
		entity: LivingEntity,
		boomerangStack: ItemStack
	) : this(ModEntityTypes.MAGICAL_BOOMERANG.get(), level) {
		owner = entity
		enchantments = boomerangStack.enchantments
		setPos(entity.x, entity.eyeY - 0.1, entity.z)
	}

	private var enchantments: ItemEnchantments = ItemEnchantments.EMPTY
	private var isReturning: Boolean = false

	private fun getEnchantmentLevel(enchantment: ResourceKey<Enchantment>): Int {
		val enchantmentHolder = level()
			.registryAccess()
			.registryOrThrow(Registries.ENCHANTMENT)
			.getHolder(enchantment)
			.orElse(null)
			?: return 0

		return enchantments.getLevel(enchantmentHolder)
	}

	private fun getSpeed(): Double {
		val zoomerangLevel = getEnchantmentLevel(ModEnchantmentProvider.ZOOMERANG)

		val baseSpeed = 1.5
		val speedMultiplier = 1.0 + (zoomerangLevel * 0.3)
		return baseSpeed * speedMultiplier
	}

	private fun getDamage(): Float {
		val bladerangLevel = getEnchantmentLevel(ModEnchantmentProvider.BLADERANG)

		return 4f + (bladerangLevel * 4f)
	}

	override fun onHitBlock(result: BlockHitResult) {
		super.onHitBlock(result)
		isReturning = true
		explode()
	}

	private fun explode() {
		val kaboomerangLevel = getEnchantmentLevel(ModEnchantmentProvider.KABOOMERANG)
		if (kaboomerangLevel <= 0) return

		//TODO
	}

	override fun onHitEntity(result: EntityHitResult) {
		super.onHitEntity(result)
		if (tickCount < 2) return

		isReturning = true

		val hitEntity = result.entity
		if (hitEntity == owner && hitEntity is Player) {
			enterInventory(hitEntity)
		} else if (hitEntity is LivingEntity) {
			hurtEntity(hitEntity)
		}
	}

	private fun enterInventory(owner: Player) {
		val boomerangStack = owner.inventory
			.items
			.firstOrNull { it.isItem(ModItems.MAGICAL_BOOMERANG) && it.get(ModDataComponents.THROWN_BOOMERANG) == uuid }

		boomerangStack?.remove(ModDataComponents.THROWN_BOOMERANG)
		discard()
	}

	private fun hurtEntity(target: LivingEntity) {
		target.hurt(
			damageSources().thrown(this, owner),
			getDamage()
		)

		val burnerangLevel = getEnchantmentLevel(ModEnchantmentProvider.BURNERANG)
		if (burnerangLevel > 0) {
			target.remainingFireTicks = maxOf(
				target.remainingFireTicks,
				20 * 5 * burnerangLevel
			)
		}
	}

	override fun tick() {
		super.tick()

		returnToOwner()
		breakPlants()
		carryItems()
	}

	private fun breakPlants() {
		val boomereaperangLevel = getEnchantmentLevel(ModEnchantmentProvider.BOOMEREAPERANG)
		if (boomereaperangLevel <= 0) return

		val boomereaperangRadius = ServerConfig.CONFIG.boomereaperangRadius.get()
		val aabb = boundingBox.inflate(boomereaperangRadius)
		val blockPosList = BlockPos.betweenClosed(
			BlockPos.containing(aabb.minX, aabb.minY, aabb.minZ),
			BlockPos.containing(aabb.maxX, aabb.maxY, aabb.maxZ)
		)

		val owner = owner
		val level = level()

		for (pos in blockPosList) {
			if (owner is Player && !level.mayInteract(owner, pos)) continue

			val state = level().getBlockState(pos)
			if (state.isBlock(ModBlockTagsProvider.COLLECTABLE_BY_BOOMEREAPERANG)) {
				level.destroyBlock(pos, true, owner)
			}
		}
	}

	private fun returnToOwner() {
		if (!isReturning) return
		val owner = owner ?: return
		val targetPos = Vec3(owner.x, owner.eyeY - 0.4, owner.z)

		val vecTo = position().vectorTo(targetPos)

		if (vecTo.lengthSqr() < 0.1) {
			discard()
			return
		}

		val distanceToTravel = getSpeed().coerceAtMost(vecTo.length())
		deltaMovement = vecTo.normalize().scale(distanceToTravel)
	}

	private fun carryItems() {
		val itemPickupRadius = ServerConfig.CONFIG.boomerangItemPickupRadius.get()
		val aabb = boundingBox.inflate(itemPickupRadius)
		val pickupEntities = level().getEntities(this, aabb).filter { it.isEntity(ModEntityTypeTagsProvider.BOOMERANG_PICKUP) }

		for (itemEntity in pickupEntities) {
			if (itemEntity.isAlive) {
				itemEntity.setPos(position())
			}
		}
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {}

	override fun canUsePortal(allowPassengers: Boolean): Boolean = false
	override fun isInvulnerable(): Boolean = true
	override fun isInvulnerableTo(source: DamageSource): Boolean = true

	override fun isNoGravity(): Boolean = true
	override fun getDefaultGravity(): Double = 0.0


}