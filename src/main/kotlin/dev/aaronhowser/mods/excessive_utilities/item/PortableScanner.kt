package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import net.minecraft.util.ProblemReporter
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.storage.TagValueOutput

class PortableScanner(properties: Properties) : Item(properties) {
	override fun interactLivingEntity(stack: ItemStack, player: Player, interactionTarget: LivingEntity, usedHand: InteractionHand): InteractionResult {
		val reporter = ProblemReporter.Collector()
		val output = TagValueOutput.createWithContext(reporter, interactionTarget.registryAccess())
		interactionTarget.save(output)
		val tag: CompoundTag = output.buildResult()
		for (key in tag.keySet()) player.tell("$key: ${tag.get(key)}")
		return InteractionResult.SUCCESS
	}

	override fun useOn(context: UseOnContext): InteractionResult {
		val player = context.player ?: return InteractionResult.PASS
		val level = context.level
		if (level.isClientSide) return InteractionResult.SUCCESS
		val blockPos = context.clickedPos
		player.tell(level.getBlockState(blockPos).toString())
		level.getBlockEntity(blockPos)?.let { blockEntity ->
			val reporter = ProblemReporter.Collector()
			val output = TagValueOutput.createWithContext(reporter, level.registryAccess())
			blockEntity.saveWithoutMetadata(output)
			val tag: CompoundTag = output.buildResult()
			for (key in tag.keySet()) player.tell("$key: ${tag.get(key)}")
		}
		return InteractionResult.SUCCESS
	}
}
