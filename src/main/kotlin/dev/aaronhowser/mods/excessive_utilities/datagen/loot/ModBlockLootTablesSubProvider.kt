package dev.aaronhowser.mods.excessive_utilities.datagen.loot

import dev.aaronhowser.mods.aaron.datagen.AaronLootTableDsl
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.advancements.critereon.BlockPredicate
import net.minecraft.advancements.critereon.LocationPredicate
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.storage.loot.IntRange
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.predicates.TimeCheck
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
			ModBlocks.MOON_STONE_ORE.get(),
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get(),
			ModBlocks.CURSED_EARTH.get(),
			ModBlocks.MAGICAL_SNOW_GLOBE.get(),
			*drums.toTypedArray(),
			ModBlocks.ENDER_LILY.get(),
			ModBlocks.RED_ORCHID.get(),
			ModBlocks.RESTURBED_MOB_SPAWNER.get()
		)

		val dropSelfBlocks = knownBlocks - noDropSelfBlocks

		for (block in dropSelfBlocks) {
			dropSelf(block)
		}

		val enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT)
		val fortune = enchantments.getOrThrow(Enchantments.FORTUNE)

		add(
			ModBlocks.MOON_STONE_ORE.get(),
			AaronLootTableDsl.table {
				pool {
					rolls(1f)
					item(ModItems.MOON_STONE)
					condition(TimeCheck.time(IntRange.range(13000, 23000)))
				}
				pool {
					rolls(1f)
					item(Items.COBBLESTONE)
				}
			}
		)

		add(
			ModBlocks.DEEPSLATE_MOON_STONE_ORE.get(),
			AaronLootTableDsl.table {
				pool {
					rolls(1f)
					item(ModItems.MOON_STONE)
					condition(TimeCheck.time(IntRange.range(13000, 23000)))
				}
				pool {
					rolls(1f)
					item(Items.COBBLED_DEEPSLATE)
				}
			}
		)

		add(ModBlocks.CURSED_EARTH.get()) { block -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT) }

		add(
			ModBlocks.RESTURBED_MOB_SPAWNER.get(),
			AaronLootTableDsl.table {
				pool {
					rolls(1f)
					item(ModBlocks.RESTURBED_MOB_SPAWNER.asItem()) {
						apply(
							CopyComponentsFunction.copyComponents(
								CopyComponentsFunction.Source.BLOCK_ENTITY
							)
								.include(DataComponents.BLOCK_ENTITY_DATA)
						)
					}
				}
			}
		)

		add(
			ModBlocks.MAGICAL_SNOW_GLOBE.get(),
			AaronLootTableDsl.table {
				pool {
					rolls(1f)
					item(ModBlocks.MAGICAL_SNOW_GLOBE.asItem()) {
						apply(
							CopyComponentsFunction.copyComponents(
								CopyComponentsFunction.Source.BLOCK_ENTITY
							)
								.include(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS.get())
						)
					}
				}
			}
		)

		for (drum in drums) {
			add(
				drum,
				AaronLootTableDsl.table {
					pool {
						rolls(1f)
						item(drum) {
							apply(
								CopyComponentsFunction.copyComponents(
									CopyComponentsFunction.Source.BLOCK_ENTITY
								)
									.include(ModDataComponents.TANK.get())
							)
						}
					}
				}
			)
		}

		add(
			ModBlocks.ENDER_LILY.get(),
			AaronLootTableDsl.table {

				// Always drop the seeds
				pool {
					rolls(1f)
					item(ModItems.ENDER_LILY)
				}

				// Drop one Ender Pearl if it's fully grown
				pool {
					rolls(1f)
					item(Items.ENDER_PEARL)
					condition(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_LILY.get())
								.setProperties(
									StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE, 7)
								)
					)
				}

				// Drop another if on End Stone and fully grown
				pool {
					rolls(1f)
					item(Items.ENDER_PEARL)
					condition(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_LILY.get())
								.setProperties(
									StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE, 7)
								)
					)
					condition(
							LocationCheck.checkLocation(
								LocationPredicate.Builder.location()
									.setBlock(
										BlockPredicate.Builder.block()
											.of(Tags.Blocks.END_STONES)
									),
								BlockPos(0, -1, 0)
							)
					)
				}

				// Drop another for each level of Fortune, if fully grown
				pool(createFortuneBonusPool(ModBlocks.ENDER_LILY.get(), Items.ENDER_PEARL, fortune, 1))
				pool(createFortuneBonusPool(ModBlocks.ENDER_LILY.get(), Items.ENDER_PEARL, fortune, 2))
				pool(createFortuneBonusPool(ModBlocks.ENDER_LILY.get(), Items.ENDER_PEARL, fortune, 3))
				pool(createFortuneBonusPool(ModBlocks.ENDER_LILY.get(), Items.ENDER_PEARL, fortune, 4))
				pool(createFortuneBonusPool(ModBlocks.ENDER_LILY.get(), Items.ENDER_PEARL, fortune, 5))

			}
		)

		add(
			ModBlocks.RED_ORCHID.get(),
			AaronLootTableDsl.table {

				// Always drop the seeds
				pool {
					rolls(1f)
					item(ModItems.RED_ORCHID)
				}

				// Drop one Redstone Dust if it's fully grown
				pool {
					rolls(1f)
					item(Items.REDSTONE)
					condition(
							LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RED_ORCHID.get())
								.setProperties(
									StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE, 7)
								)
					)
				}

				// Drop another for each level of Fortune, if fully grown
				pool(createFortuneBonusPool(ModBlocks.RED_ORCHID.get(), Items.REDSTONE, fortune, 1))
				pool(createFortuneBonusPool(ModBlocks.RED_ORCHID.get(), Items.REDSTONE, fortune, 2))
				pool(createFortuneBonusPool(ModBlocks.RED_ORCHID.get(), Items.REDSTONE, fortune, 3))
				pool(createFortuneBonusPool(ModBlocks.RED_ORCHID.get(), Items.REDSTONE, fortune, 4))
				pool(createFortuneBonusPool(ModBlocks.RED_ORCHID.get(), Items.REDSTONE, fortune, 5))

			}
		)

	}

	private fun createFortuneBonusPool(
		crop: Block,
		drop: ItemLike,
		fortune: Holder<Enchantment>,
		minFortuneLevel: Int
	): LootPool.Builder {
		val chances = FloatArray(minFortuneLevel + 1) { index ->
			if (index < minFortuneLevel) 0f else 1f
		}

		return AaronLootTableDsl.pool {
			rolls(1f)
			item(drop)
			condition(
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
					.setProperties(
						StatePropertiesPredicate.Builder.properties()
							.hasProperty(CropBlock.AGE, 7)
					)
			)
			condition(BonusLevelTableCondition.bonusLevelFlatChance(fortune, *chances))
		}
	}

}
