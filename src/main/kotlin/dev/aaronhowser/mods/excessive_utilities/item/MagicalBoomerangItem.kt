package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.entity.MagicalBoomerangEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class MagicalBoomerangItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val boomerang = MagicalBoomerangEntity(level, player)
			boomerang.shootFromRotation(player, player.xRot, player.yRot, 0f, 1.5f, 0f)
			level.addFreshEntity(boomerang)
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}