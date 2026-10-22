package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.advancements.criterion.EntityEquipmentPredicate
import net.minecraft.advancements.criterion.EntityPredicate
import net.minecraft.advancements.criterion.EntityTypePredicate
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition
import java.util.concurrent.CompletableFuture

class ModGlobalLootModifierProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : GlobalLootModifierProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID) {

	override fun start() {
		val noConditions = emptyArray<ICondition>()

		add(
			"resonating_crystal_from_redstone_ore",
			AddTableLootModifier(
				arrayOf(
					LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(Blocks.REDSTONE_ORE)
						.build(),
					LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(Blocks.DEEPSLATE_REDSTONE_ORE)
						.build()
				),
				1,
				OtherLootTableSubProvider.RESONATING_REDSTONE_CRYSTAL
			)
			, *noConditions
		)

		add(
			"drop_of_evil_from_wither_skeleton",
			AddTableLootModifier(
				arrayOf(
					LootItemEntityPropertyCondition
						.hasProperties(
							LootContext.EntityTarget.THIS,
							EntityPredicate.Builder
								.entity()
								.entityType(EntityTypePredicate.of(registries.lookupOrThrow(Registries.ENTITY_TYPE), EntityType.WITHER_SKELETON))
								.build()
						)
						.build()
				),
				1,
				OtherLootTableSubProvider.DROP_OF_EVIL
			)
			, *noConditions
		)

		add(
			"soul_fragment",
			AddTableLootModifier(
				arrayOf(
					LootItemEntityPropertyCondition
						.hasProperties(
							LootContext.EntityTarget.THIS,
							EntityPredicate.Builder.entity()
						)
						.build()
				),
				1,
				OtherLootTableSubProvider.SOUL_FRAGMENT
			)
			, *noConditions
		)

		add(
			"soul_fragment_kikoku_bonus",
			AddTableLootModifier(
				arrayOf(
					LootItemEntityPropertyCondition
						.hasProperties(
							LootContext.EntityTarget.ATTACKER,
							EntityPredicate.Builder
								.entity()
								.equipment(
									EntityEquipmentPredicate.Builder
										.equipment()
										.mainhand(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModItems.KIKOKU.get()))
										.build()
								)
								.build()
						)
						.build()
				),
				1,
				OtherLootTableSubProvider.SOUL_FRAGMENT_KIKOKU
			)
			, *noConditions
		)

		add(
			"division_sigil_stronghold_crossing",
			AddTableLootModifier(
				arrayOf(
					LootTableIdCondition.Builder(BuiltInLootTables.STRONGHOLD_CORRIDOR.identifier()).build()
				),
				1,
				OtherLootTableSubProvider.DIVISION_SIGIL_STRONGHOLD_CROSSING
			)
			, *noConditions
		)

		add(
			"division_sigil_stronghold_corridor",
			AddTableLootModifier(
				arrayOf(
					LootTableIdCondition.Builder(BuiltInLootTables.STRONGHOLD_CROSSING.identifier()).build()
				),
				1,
				OtherLootTableSubProvider.DIVISION_SIGIL_STRONGHOLD_CORRIDOR
			)
			, *noConditions
		)

		add(
			"division_sigil_mineshaft",
			AddTableLootModifier(
				arrayOf(
					LootTableIdCondition.Builder(BuiltInLootTables.ABANDONED_MINESHAFT.identifier()).build()
				),
				1,
				OtherLootTableSubProvider.DIVISION_SIGIL_MINESHAFT
			)
			, *noConditions
		)

		add(
			"division_sigil_dungeon",
			AddTableLootModifier(
				arrayOf(
					LootTableIdCondition.Builder(BuiltInLootTables.SIMPLE_DUNGEON.identifier()).build()
				),
				1,
				OtherLootTableSubProvider.DIVISION_SIGIL_DUNGEON
			)
			, *noConditions
		)

		add(
			"division_sigil_desert_temple",
			AddTableLootModifier(
				arrayOf(
					LootTableIdCondition.Builder(BuiltInLootTables.DESERT_PYRAMID.identifier()).build()
				),
				1,
				OtherLootTableSubProvider.DIVISION_SIGIL_DESERT_TEMPLE
			)
			, *noConditions
		)

	}

}
