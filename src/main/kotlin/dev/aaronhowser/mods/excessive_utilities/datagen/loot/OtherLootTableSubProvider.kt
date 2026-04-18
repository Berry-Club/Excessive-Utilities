package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.aaron.datagen.LootItemStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Mth
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
					singleItemPool(ModItems.DROP_OF_EVIL.get(), .1f)
				)
		)


		output.accept(
			RESONATING_REDSTONE_CRYSTAL,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.RESONATING_REDSTONE_CRYSTAL.get(), .3f)
				)
		)

		val forgottenSoul = ModItems.SOUL_FRAGMENT.withComponent(
			DataComponents.LORE,
			ItemLore(
				listOf(ModMenuLang.SOUL_OF_A_FORGOTTEN_DEITY.toComponent())
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

		output.accept(
			DIVISION_SIGIL_STRONGHOLD_CORRIDOR,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.DIVISION_SIGIL.get(), 0.011f)
				)
		)

		output.accept(
			DIVISION_SIGIL_STRONGHOLD_CROSSING,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.DIVISION_SIGIL.get(), 0.013f)
				)
		)

		output.accept(
			DIVISION_SIGIL_MINESHAFT,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.DIVISION_SIGIL.get(), 0.045f)
				)
		)

		output.accept(
			DIVISION_SIGIL_DUNGEON,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.DIVISION_SIGIL.get(), 0.01f)
				)
		)

		output.accept(
			DIVISION_SIGIL_DESERT_TEMPLE,
			LootTable
				.lootTable()
				.withPool(
					singleItemPool(ModItems.DIVISION_SIGIL.get(), 0.021f)
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

		private fun singleItemPool(item: Item, chance: Float): LootPool.Builder {
			val itemWeight = Mth.ceil(chance * 10_000)
			val emptyWeight = 10_000 - itemWeight

			return LootPool.lootPool()
				.add(EmptyLootItem.emptyItem().setWeight(emptyWeight))
				.add(LootItem.lootTableItem(item).setWeight(itemWeight))
		}

		val DROP_OF_EVIL = createPoolRk("entity/drop_of_evil")
		val RESONATING_REDSTONE_CRYSTAL = createPoolRk("block/resonating_redstone_crystal")
		val SOUL_FRAGMENT = createPoolRk("entity/soul_fragment")
		val SOUL_FRAGMENT_KIKOKU = createPoolRk("entity/soul_fragment_kikoku")

		val DIVISION_SIGIL_STRONGHOLD_CORRIDOR = createPoolRk("structure/division_sigils/stronghold_corridor")
		val DIVISION_SIGIL_STRONGHOLD_CROSSING = createPoolRk("structure/division_sigils/stronghold_crossing")
		val DIVISION_SIGIL_MINESHAFT = createPoolRk("structure/division_sigils/mineshaft")
		val DIVISION_SIGIL_DUNGEON = createPoolRk("structure/division_sigils/dungeon")
		val DIVISION_SIGIL_DESERT_TEMPLE = createPoolRk("structure/division_sigils/desert_temple")
	}

}