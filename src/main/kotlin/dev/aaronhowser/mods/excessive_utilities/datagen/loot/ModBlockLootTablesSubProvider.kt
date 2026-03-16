package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.storage.loot.IntRange
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.TimeCheck
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredHolder

class ModBlockLootTablesSubProvider(
	provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

	override fun getKnownBlocks(): Iterable<Block> {
		return ModBlocks.BLOCK_REGISTRY.entries.map(DeferredHolder<Block, out Block>::get)
	}

	override fun generate() {
		val drums = listOf(
			ModBlocks.STONE_DRUM.get(),
			ModBlocks.IRON_DRUM.get(),
			ModBlocks.REINFORCED_LARGE_DRUM.get(),
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(),
			ModBlocks.BEDROCKIUM_DRUM.get(),
			ModBlocks.CREATIVE_DRUM.get()
		)

		val noDropSelfBlocks = setOf(
			ModBlocks.MOON_STORE_ORE.get(),
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get(),
			ModBlocks.CURSED_EARTH.get(),
			ModBlocks.MAGICAL_SNOW_GLOBE.get(),
			*drums.toTypedArray(),
			ModBlocks.ENDER_LILY.get()
		)

		val dropSelfBlocks = knownBlocks - noDropSelfBlocks

		for (block in dropSelfBlocks) {
			dropSelf(block)
		}

		val enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT)
		val fortune = enchantments.getOrThrow(Enchantments.FORTUNE)

		add(
			ModBlocks.MOON_STORE_ORE.get(),
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(ModItems.MOON_STONE))
						.`when`(TimeCheck.time(IntRange.range(13000, 23000)))
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.COBBLESTONE))
				)
		)

		add(
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get(),
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(ModItems.MOON_STONE))
						.`when`(TimeCheck.time(IntRange.range(13000, 23000)))
				)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.COBBLED_DEEPSLATE))
				)
		)

		add(ModBlocks.CURSED_EARTH.get()) { block -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT) }

		add(
			ModBlocks.MAGICAL_SNOW_GLOBE.get(),
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(
							LootItem.lootTableItem(ModBlocks.MAGICAL_SNOW_GLOBE.asItem())
								.apply(
									CopyComponentsFunction.copyComponents(
										CopyComponentsFunction.Source.BLOCK_ENTITY
									)
										.include(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS.get())
								)
						)
				)
		)

		for (drum in drums) {
			add(
				drum,
				LootTable.lootTable()
					.withPool(
						LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1f))
							.add(
								LootItem.lootTableItem(drum)
									.apply(
										CopyComponentsFunction.copyComponents(
											CopyComponentsFunction.Source.BLOCK_ENTITY
										)
											.include(ModDataComponents.TANK.get())
									)
							)
					)
			)
		}

		add(
			ModBlocks.ENDER_LILY.get(),
			LootTable.lootTable()

				// Always drop the seeds
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(ModItems.ENDER_LILY))
				)

				// Drop one Ender Pearl if it's fully grown
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.ENDER_PEARL))
						.`when`(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_LILY.get())
								.setProperties(
									StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE, 7)
								)
						)
				)

				// Drop another if on End Stone and fully grown
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.ENDER_PEARL))
						.`when`(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_LILY.get())
								.setProperties(
									StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE, 7)
								)
						)
						.`when`(
							LocationCheck.checkLocation(
								LocationPredicate.Builder.location()
									.setBlock(
										BlockPredicate.Builder.block()
											.of(Tags.Blocks.END_STONES)
									),
								BlockPos(0, -1, 0)
							)
						)
				)

				// Drop another for each level of Fortune, if fully grown
				// FIXME: This shit ain't WORK
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(LootItem.lootTableItem(Items.ENDER_PEARL))
						.`when`(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_LILY.get())
								.setProperties(
									StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE, 7)
								)
						)
						.`when`(
							BonusLevelTableCondition.bonusLevelFlatChance(
								fortune,
								0f,
								1f,
								2f,
								3f,
								4f,
								5f
							)
						)
				)

		)

	}

}