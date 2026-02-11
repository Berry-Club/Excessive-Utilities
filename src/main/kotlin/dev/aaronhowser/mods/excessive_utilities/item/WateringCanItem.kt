package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.util.Unit
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent

class WateringCanItem(
	val isReinforced: Boolean,
	properties: Properties
) : Item(properties) {

	override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = Int.MAX_VALUE
	override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.BOW

	override fun shouldCauseReequipAnimation(
		oldStack: ItemStack,
		newStack: ItemStack,
		slotChanged: Boolean
	): Boolean {
		return slotChanged
	}

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (player.isFakePlayer) {
			stack.set(ModDataComponents.IS_BROKEN, Unit.INSTANCE)
		}

		if (stack.has(ModDataComponents.IS_BROKEN)) {
			return InteractionResultHolder.fail(stack)
		}

		if (tryCollectWater(player, stack)) {
			return InteractionResultHolder.consume(stack)
		}

		if (!player.hasInfiniteMaterials() && needsToBeFilled(stack)) {
			return InteractionResultHolder.fail(stack)
		}

		player.startUsingItem(usedHand)
		return InteractionResultHolder.consume(stack)
	}

	override fun onUseTick(
		level: Level,
		livingEntity: LivingEntity,
		stack: ItemStack,
		remainingUseDuration: Int
	) {
		if (level !is ServerLevel || livingEntity !is Player) return
		if (livingEntity.isFakePlayer) {
			stack.set(ModDataComponents.IS_BROKEN, Unit.INSTANCE)
			return
		}

		if (stack.has(ModDataComponents.IS_BROKEN)) return

		if (!livingEntity.hasInfiniteMaterials() && needsToBeFilled(stack)) {
			livingEntity.stopUsingItem()
			return
		}

		val lookingAt = getPlayerPOVHitResult(level, livingEntity, ClipContext.Fluid.NONE)
		if (lookingAt.type != HitResult.Type.BLOCK) return

		val radius = if (isReinforced) {
			ServerConfig.CONFIG.reinforcedWateringCanRadius.get()
		} else {
			ServerConfig.CONFIG.wateringCanRadius.get()
		}

		val blocks = BlockPos.betweenClosed(
			lookingAt.blockPos.offset(-radius, -radius, -radius),
			lookingAt.blockPos.offset(radius, radius, radius)
		)

		val chancePerBlock = if (isReinforced) {
			ServerConfig.CONFIG.reinforcedWateringCanTickChance.get()
		} else {
			ServerConfig.CONFIG.wateringCanTickChance.get()
		}

		for (pos in blocks) {
			if (!level.random.chance(chancePerBlock)) continue
			if (!level.mayInteract(livingEntity, pos)) continue

			val stateThere = level.getBlockState(pos)
			stateThere.randomTick(level, pos, level.random)

			if (stateThere.isBlock(Blocks.FIRE)) {
				level.removeBlock(pos, false)
			}

			spawnParticles(level, pos)
		}

		drainWater(livingEntity, stack)
	}

	private fun spawnParticles(level: ServerLevel, pos: BlockPos) {
		if (level.isEmptyBlock(pos)) return
		val stateThere = level.getBlockState(pos)
		if (stateThere.isCollisionShapeFullBlock(level, pos)) return

		val y = pos.y + 0.1
		val particleCount = 10

		for (i in 0 until particleCount) {
			val x = pos.x + level.random.nextDouble()
			val z = pos.z + level.random.nextDouble()

			level.sendParticles(
				ParticleTypes.SPLASH,
				x, y, z,
				1,
				0.0, 0.0, 0.0,
				0.0
			)
		}
	}

	private fun drainWater(player: Player, stack: ItemStack) {
		if (player.hasInfiniteMaterials()) return
		val heldWater = stack.get(ModDataComponents.TANK) ?: return
		val newAmount = maxOf(heldWater.amount - getWaterPerTick(), 0)
		val newFluidStack = FluidStack(Fluids.WATER, newAmount)
		stack.set(ModDataComponents.TANK, SimpleFluidContent.copyOf(newFluidStack))
	}

	private fun getWaterPerTick(): Int {
		return if (isReinforced) {
			ServerConfig.CONFIG.reinforcedWateringCanWaterUsagePerTick.get()
		} else {
			ServerConfig.CONFIG.wateringCanWaterUsagePerTick.get()
		}
	}

	private fun usesWater(): Boolean = getWaterPerTick() > 0

	private fun needsToBeFilled(stack: ItemStack): Boolean {
		if (!usesWater()) return false

		val heldWater = stack.get(ModDataComponents.TANK) ?: return true
		return heldWater.amount <= getWaterPerTick()
	}

	private fun isFull(stack: ItemStack): Boolean {
		if (!usesWater()) return true

		val heldWater = stack.get(ModDataComponents.TANK) ?: return false
		return heldWater.amount >= MAX_WATER
	}

	private fun tryCollectWater(player: Player, stack: ItemStack): Boolean {
		if (isFull(stack)) return false
		val level = player.level()

		val blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.WATER)
		if (blockHitResult.type != HitResult.Type.BLOCK) return false

		val pos = blockHitResult.blockPos
		if (!level.mayInteract(player, pos)) return false

		if (!level.getFluidState(pos).isFluid(FluidTags.WATER)) return false

		val heldWater = stack.get(ModDataComponents.TANK) ?: return false
		val newAmount = minOf(heldWater.amount + 1000, MAX_WATER)
		val newFluidStack = FluidStack(Fluids.WATER, newAmount)
		stack.set(ModDataComponents.TANK, SimpleFluidContent.copyOf(newFluidStack))

		return true
	}

	companion object {
		const val MAX_WATER = 10_000

		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.TANK, SimpleFluidContent.EMPTY)
		}

		val DEFAULT_REINFORCED_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.TANK, SimpleFluidContent.EMPTY)
		}
	}

}