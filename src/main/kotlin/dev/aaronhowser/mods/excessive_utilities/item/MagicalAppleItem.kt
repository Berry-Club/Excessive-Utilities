package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class MagicalAppleItem(properties: Properties) : Item(properties) {

	override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
		if (livingEntity is Player) {
			livingEntity.enchantmentSeed = level.random.nextInt()
			livingEntity.tell(ModMessageLang.EAT_MAGICAL_APPLE.toComponent())
		}

		return super.finishUsingItem(stack, level, livingEntity)
	}

	companion object {
		val PROPERTIES: Properties =
			Properties()
				.food(
					FoodProperties.Builder()
						.nutrition(4)
						.saturationModifier(1.2f)
						.alwaysEdible()
						.build()
				)
	}

}