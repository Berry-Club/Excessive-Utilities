package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.WalkType
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.tier.OpiniumTier
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class FireAxeItem(properties: Properties) : AxeItem(OpiniumTier, properties) {

	override fun mineBlock(
		stack: ItemStack,
		level: Level,
		state: BlockState,
		pos: BlockPos,
		miningEntity: LivingEntity
	): Boolean {
		if (level is ServerLevel && state.isBlock(ModBlockTagsProvider.FIRE_AXE_MINEABLE)) {
			val blockWalker = BlockWalker.Builder(level)
				.walkType(WalkType.ALL_CARDINAL)
				.startPos(pos)
				.filter { l, p, s -> s.isBlock(ModBlockTagsProvider.FIRE_AXE_MINEABLE) }
				.maxTotalBlocks(10000)
				.maxDistance(100)
				.build()

			val found = blockWalker.locateAllImmediately()

			for (log in found) {
				if (log.block.state.isBlock(ModBlockTagsProvider.FIRE_AXE_MINEABLE)) {
					level.destroyBlock(log.block.pos, true, miningEntity)
				}
			}
		}

		return true
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(createAttributes(UnstableTier, 6f, -3.5f))
	}

}