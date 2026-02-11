package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext

//TODO: Open a menu instead
class PortableScanner(properties: Properties) : Item(properties) {

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		val entityTag = CompoundTag()
		interactionTarget.save(entityTag)

		for (key in entityTag.allKeys) {
			val value = entityTag.get(key)

			player.tell("$key: $value")
		}

		return InteractionResult.SUCCESS
	}

	override fun useOn(context: UseOnContext): InteractionResult {
		val player = context.player ?: return InteractionResult.PASS

		val blockPos = context.clickedPos
		val level = context.level
		val blockState = level.getBlockState(blockPos)

		player.tell(blockState.toString())

		val blockEntity = level.getBlockEntity(blockPos)
		if (blockEntity != null) {
			val tag = blockEntity.saveWithoutMetadata(level.registryAccess())
			for (key in tag.allKeys) {
				val value = tag.get(key)

				player.tell("$key: $value")
			}
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}