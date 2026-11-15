package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.entity.MagicalBoomerangEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class MagicalBoomerangItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResult {
		val stack = player.getItemInHand(usedHand)

		if (stack.has(ModDataComponents.THROWN_BOOMERANG)) return InteractionResult.FAIL

		if (level.isServerSide) {
			val boomerang = MagicalBoomerangEntity(level, player, stack)
			boomerang.shootFromRotation(player, player.xRot, player.yRot, 0f, 1.5f, 0f)
			level.addFreshEntity(boomerang)
			stack.set(ModDataComponents.THROWN_BOOMERANG, boomerang.uuid)
		}

		return InteractionResult.SUCCESS
	}

	override fun inventoryTick(
		stack: ItemStack,
		level: ServerLevel,
		entity: Entity,
		slot: EquipmentSlot?
	) {
		val boomerangId = stack.get(ModDataComponents.THROWN_BOOMERANG) ?: return
		val boomerangEntity = level.getEntity(boomerangId)

		if (boomerangEntity == null) {
			stack.remove(ModDataComponents.THROWN_BOOMERANG)
		}
	}

	companion object {
		val THROWN_PREDICATE = ExcessiveUtilities.modResource("magical_boomerang_thrown")
		fun isThrown(
			stack: ItemStack,
			localLevel: Level?,
			holdingEntity: LivingEntity?,
			int: Int
		): Float {
			return if (stack.has(ModDataComponents.THROWN_BOOMERANG)) 1f else 0f
		}

	}

}





