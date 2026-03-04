package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Tiers
import net.neoforged.neoforge.event.level.BlockDropsEvent

class GlassCutterItem(properties: Properties) : DiggerItem(Tiers.IRON, ModBlockTagsProvider.GLASS_CUTTER_MINEABLE, properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.attributes(createAttributes(Tiers.IRON, 1.5f, -3f))

		fun handleDropEvent(event: BlockDropsEvent) {
			if (event.isCanceled) return

			val tool = event.tool
			if (!tool.isItem(ModItems.GLASS_CUTTER)) return

			val minedBlock = event.state
			if (!minedBlock.isBlock(ModBlockTagsProvider.GLASS_CUTTER_MINEABLE)) return

			val pos = event.pos.center

			val blockItem = minedBlock.block.getDefaultInstance()
			val itemEntity = ItemEntity(event.level, pos.x, pos.y, pos.z, blockItem)

			event.drops.clear()
			event.drops.add(itemEntity)
		}
	}

}