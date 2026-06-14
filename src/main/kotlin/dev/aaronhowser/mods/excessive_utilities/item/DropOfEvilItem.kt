package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.CursedEarthBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SpawnerBlockEntity

class DropOfEvilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level as? ServerLevel ?: return InteractionResult.SUCCESS
		val pos = context.clickedPos
		val blockState = level.getBlockState(pos)

		if (blockState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
			CursedEarthBlock.placeAndSpread(level, pos)
			return InteractionResult.SUCCESS
		}

		if (blockState.isBlock(Blocks.SPAWNER)) {
			makeResturbedSpawner(level, pos)
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

	companion object {
		private fun makeResturbedSpawner(
			level: Level,
			pos: BlockPos
		) {
			val blockEntity = level.getBlockEntity(pos)
			if (blockEntity !is SpawnerBlockEntity) return

			val stack = ModBlocks.RESTURBED_MOB_SPAWNER.toStack()
			blockEntity.saveToItem(stack, level.registryAccess())

			level.removeBlock(pos, false)

			val itemEntity = ItemEntity(level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, stack)
			level.addFreshEntity(itemEntity)
		}
	}

}