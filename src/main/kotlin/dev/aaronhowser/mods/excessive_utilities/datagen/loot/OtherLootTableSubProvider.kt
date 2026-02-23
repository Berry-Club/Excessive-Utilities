package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.aaron.datagen.LootItemStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModTooltipLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import java.util.function.BiConsumer

class OtherLootTableSubProvider(
	val registries: HolderLookup.Provider
) : LootTableSubProvider {

	override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder?>) {

		output.accept(
			DROP_OF_EVIL,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.DROP_OF_EVIL.get(), 10)
				)
		)


		output.accept(
			RESONATING_REDSTONE_CRYSTAL,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.RESONATING_REDSTONE_CRYSTAL.get(), 30)
				)
		)

		val forgottenSoul = ModItems.SOUL_FRAGMENT.withComponent(
			DataComponents.LORE,
			ItemLore(
				listOf(ModTooltipLang.SOUL_OF_A_FORGOTTEN_DEITY.toComponent())
			)
		)

		output.accept(
			SOUL_FRAGMENT,
			LootTable
				.lootTable()
				.withPool(
					LootPool.lootPool()
						.add(EmptyLootItem.emptyItem().setWeight(43046721))
						.add(LootItemStack.lootTableStack(forgottenSoul).setWeight(1))
				)
		)

		output.accept(
			SOUL_FRAGMENT_KIKOKU,
			LootTable
				.lootTable()
				.withPool(
					LootPool.lootPool()
						.add(EmptyLootItem.emptyItem().setWeight(43046721))
						.add(LootItemStack.lootTableStack(forgottenSoul).setWeight(10))
				)
		)
	}

	companion object {
		private fun createPoolRk(name: String): ResourceKey<LootTable> {
			return ResourceKey.create(
				Registries.LOOT_TABLE,
				ExcessiveUtilities.modResource(name)
			)
		}

		private fun singleItemPool(item: Item, chance: Int) = LootPool.lootPool()
			.add(EmptyLootItem.emptyItem().setWeight(100 - chance))
			.add(LootItem.lootTableItem(item).setWeight(chance))

		val DROP_OF_EVIL = createPoolRk("entity/drop_of_evil")
		val RESONATING_REDSTONE_CRYSTAL = createPoolRk("block/resonating_redstone_crystal")
		val SOUL_FRAGMENT = createPoolRk("entity/soul_fragment")
		val SOUL_FRAGMENT_KIKOKU = createPoolRk("entity/soul_fragment_kikoku")
	}

}