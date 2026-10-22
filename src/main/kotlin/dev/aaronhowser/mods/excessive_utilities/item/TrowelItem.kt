package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Items
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.neoforged.neoforge.event.level.BlockDropsEvent

class TrowelItem(properties: Properties) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.durability(ToolMaterial.IRON.durability())

		fun handleDropEvent(event: BlockDropsEvent) {
			if (event.isCanceled) return

			val tool = event.tool
			if (!tool.isItem(ModItems.TROWEL)) return

			val minedBlock = event.state
			if (!minedBlock.isBlock(ModBlockTagsProvider.TROWEL_MINEABLE)) return

			val pos = event.pos.center
			val level = event.level

			val fakeTool = Items.NETHERITE_SHOVEL.defaultInstance
			val enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)

			fakeTool.enchant(
				enchantments.getOrThrow(Enchantments.SILK_TOUCH),
				1
			)

			val lootParams = LootParams.Builder(level)
				.withParameter(LootContextParams.ORIGIN, pos)
				.withParameter(LootContextParams.TOOL, fakeTool)
				.withParameter(LootContextParams.BLOCK_STATE, minedBlock)
				.withOptionalParameter(LootContextParams.THIS_ENTITY, event.breaker)
				.withOptionalParameter(LootContextParams.BLOCK_ENTITY, event.blockEntity)

			val drops = minedBlock.getDrops(lootParams)

			event.drops.clear()

			for (drop in drops) {
				val itemEntity = ItemEntity(level, pos.x, pos.y, pos.z, drop)
				event.drops.add(itemEntity)
			}
		}
	}

}





