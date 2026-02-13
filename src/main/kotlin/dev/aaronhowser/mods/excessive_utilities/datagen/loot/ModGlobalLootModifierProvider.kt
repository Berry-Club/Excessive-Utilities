package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.EntityTypePredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import java.util.concurrent.CompletableFuture

class ModGlobalLootModifierProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : GlobalLootModifierProvider(output, lookupProvider, ExcessiveUtilities.MOD_ID) {

	override fun start() {

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
				OtherLootTableSubProvider.RESONATING_REDSTONE_CRYSTAL
			)
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
								.entityType(EntityTypePredicate.of(EntityType.WITHER_SKELETON))
								.build()
						)
						.build()
				),
				OtherLootTableSubProvider.DROP_OF_EVIL
			)
		)

	}

}