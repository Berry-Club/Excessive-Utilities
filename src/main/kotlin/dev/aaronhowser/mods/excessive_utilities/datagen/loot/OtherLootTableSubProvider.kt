package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.aaron.datagen.AaronLootTableDsl
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.level.storage.loot.LootTable
import java.util.function.BiConsumer

class OtherLootTableSubProvider(
	val registries: HolderLookup.Provider
) : LootTableSubProvider {

	override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder?>) {

		output.accept(
			DROP_OF_EVIL,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.DROP_OF_EVIL.get(), .1f))
			}
		)


		output.accept(
			RESONATING_REDSTONE_CRYSTAL,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.RESONATING_REDSTONE_CRYSTAL.get(), .3f))
			}
		)

		val forgottenSoul = ModItems.SOUL_FRAGMENT.withComponent(
			DataComponents.LORE,
			ItemLore(
				listOf(ModMenuLang.SOUL_OF_A_FORGOTTEN_DEITY.toComponent())
			)
		)

		output.accept(
			SOUL_FRAGMENT,
			AaronLootTableDsl.table {
				pool {
					empty(43_046_721)
					stack(forgottenSoul, 1)
				}
			}
		)

		output.accept(
			SOUL_FRAGMENT_KIKOKU,
			AaronLootTableDsl.table {
				pool {
					empty(43_046_721)
					stack(forgottenSoul, 10)
				}
			}
		)

		output.accept(
			DIVISION_SIGIL_STRONGHOLD_CORRIDOR,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.DIVISION_SIGIL.get(), 0.011f))
			}
		)

		output.accept(
			DIVISION_SIGIL_STRONGHOLD_CROSSING,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.DIVISION_SIGIL.get(), 0.013f))
			}
		)

		output.accept(
			DIVISION_SIGIL_MINESHAFT,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.DIVISION_SIGIL.get(), 0.045f))
			}
		)

		output.accept(
			DIVISION_SIGIL_DUNGEON,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.DIVISION_SIGIL.get(), 0.01f))
			}
		)

		output.accept(
			DIVISION_SIGIL_DESERT_TEMPLE,
			AaronLootTableDsl.table {
				pool(AaronLootTableDsl.singleItemChancePool(ModItems.DIVISION_SIGIL.get(), 0.021f))
			}
		)
	}

	companion object {
		private fun key(name: String): ResourceKey<LootTable> {
			return ResourceKey.create(
				Registries.LOOT_TABLE,
				ExcessiveUtilities.modResource(name)
			)
		}

		val DROP_OF_EVIL = key("entity/drop_of_evil")
		val RESONATING_REDSTONE_CRYSTAL = key("block/resonating_redstone_crystal")
		val SOUL_FRAGMENT = key("entity/soul_fragment")
		val SOUL_FRAGMENT_KIKOKU = key("entity/soul_fragment_kikoku")

		val DIVISION_SIGIL_STRONGHOLD_CORRIDOR = key("structure/division_sigils/stronghold_corridor")
		val DIVISION_SIGIL_STRONGHOLD_CROSSING = key("structure/division_sigils/stronghold_crossing")
		val DIVISION_SIGIL_MINESHAFT = key("structure/division_sigils/mineshaft")
		val DIVISION_SIGIL_DUNGEON = key("structure/division_sigils/dungeon")
		val DIVISION_SIGIL_DESERT_TEMPLE = key("structure/division_sigils/desert_temple")
	}

}
