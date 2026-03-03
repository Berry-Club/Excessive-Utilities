package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.event.EventHooks

class CompoundBowItem(properties: Properties) : BowItem(properties) {

	override fun releaseUsing(stack: ItemStack, level: Level, entityLiving: LivingEntity, timeLeft: Int) {
		if (entityLiving is Player) {
			val projectileStack = entityLiving.getProjectile(stack)
			if (projectileStack.isEmpty) return

			var time = getUseDuration(stack, entityLiving) - timeLeft
			time = EventHooks.onArrowLoose(stack, level, entityLiving, time, !projectileStack.isEmpty)

			if (time < 0) return
			val power = getPowerForTime(time)
			if (power < 0.1) return

			val draw = draw(stack, projectileStack, entityLiving)
			if (level is ServerLevel && draw.isNotEmpty()) {
				shoot(
					level,
					entityLiving,
					entityLiving.usedItemHand,
					stack,
					draw,
					power * 3f,
					1f,
					power == 1f,
					null
				)
			}

			level.playSound(
				null,
				entityLiving.x,
				entityLiving.y,
				entityLiving.z,
				SoundEvents.ARROW_SHOOT,
				SoundSource.PLAYERS,
				1.0f,
				1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f
			)

			entityLiving.awardStat(Stats.ITEM_USED.get(this))
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}