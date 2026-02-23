package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class UnstableIngotItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		val isStable = !stack.has(ModDataComponents.COUNTDOWN)
		if (isStable) return

		val countdown = stack.get(ModDataComponents.COUNTDOWN) ?: return
		if (countdown > 0) {
			stack.set(ModDataComponents.COUNTDOWN, countdown - 1)
			return
		}

		level.explode(
			entity,
			entity.x, entity.y, entity.z,
			4f,
			false,
			Level.ExplosionInteraction.MOB
		)

		stack.count = 0
	}

	override fun getName(stack: ItemStack): Component {
		if (stack.has(ModDataComponents.COUNTDOWN)) return super.getName(stack)
		return ModItemLang.MOBIUS_INGOT.toComponent()
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.COUNTDOWN, 20 * 10)
		}
	}

}