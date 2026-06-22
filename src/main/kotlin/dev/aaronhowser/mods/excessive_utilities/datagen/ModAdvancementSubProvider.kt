package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.aaron.datagen.AaronAdvancementSubProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModAdvancementLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.handler.LastMillenniumHandler
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.ChangeDimensionTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPredicate
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class ModAdvancementSubProvider(
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronAdvancementSubProvider(ExcessiveUtilities.MOD_ID, lookupProvider) {

	override fun generate(
		registries: HolderLookup.Provider,
		saver: Consumer<AdvancementHolder>,
		existingFileHelper: ExistingFileHelper
	) {
		fun Advancement.Builder.save(id: ResourceLocation) = save(saver, id, existingFileHelper)

		val root = advancement()
			.display(
				ModItems.ANGEL_BLOCK.get(),
				ModAdvancementLang.ROOT_TITLE.toComponent(),
				ModAdvancementLang.ROOT_DESC.toComponent(),
				ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
				AdvancementType.TASK,
				false,
				false,
				false
			)
			.addCriterion(
				"has_wood",
				hasItems(
					ItemPredicate.Builder.item()
						.of(ItemTags.LOGS)
				)
			)
			.save(ROOT)

		val gpProducers = advancement()
			.parent(root)
			.display(
				ModBlocks.MANUAL_MILL,
				ModAdvancementLang.GP_PRODUCERS_TITLE.toComponent(),
				ModAdvancementLang.GP_PRODUCERS_DESC.toComponent(),
			)
			.addCriterion(
				"has_gp_producer",
				hasItems(
					ItemPredicate.Builder.item()
						.of(ModItemTagsProvider.GP_PRODUCER)
				)
			)
			.save(GP_PRODUCERS)

		val resonator = advancement()
			.parent(gpProducers)
			.display(
				ModBlocks.RESONATOR,
				ModAdvancementLang.RESONATOR_TITLE.toComponent(),
				ModAdvancementLang.RESONATOR_DESC.toComponent(),
			)
			.has(ModBlocks.RESONATOR)
			.save(RESONATOR)

		val tlm = advancement()
			.parent(resonator)
			.display(
				ModBlocks.LAST_MILLENNIUM_PORTAL,
				ModAdvancementLang.TLM_TITLE.toComponent(),
				ModAdvancementLang.TLM_DESC.toComponent(),
			)
			.addCriterion(
				"entered_tlm",
				ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(LastMillenniumHandler.LEVEL_KEY)
			)
			.save(TLM)

		val qq = advancement()
			.parent(resonator)
			.display(
				ModBlocks.QUANTUM_QUARRY_ACTUATOR,
				ModAdvancementLang.QUANTUM_QUARRY_TITLE.toComponent(),
				ModAdvancementLang.QUANTUM_QUARRY_DESC.toComponent(),
			)
			.has(ModBlocks.QUANTUM_QUARRY)
			.save(QUANTUM_QUARRY)

		val qed = advancement()
			.parent(resonator)
			.display(
				ModBlocks.QED,
				ModAdvancementLang.QED_TITLE.toComponent(),
				ModAdvancementLang.QED_DESC.toComponent(),
			)
			.has(ModBlocks.QED)
			.save(QED)

		val enderQuarry = advancement()
			.parent(qed)
			.display(
				ModBlocks.ENDER_QUARRY,
				ModAdvancementLang.ENDER_QUARRY_TITLE.toComponent(),
				ModAdvancementLang.ENDER_QUARRY_DESC.toComponent(),
			)
			.has(ModBlocks.ENDER_QUARRY)
			.save(ENDER_QUARRY)

		val enderQuarryUpgrades = advancement()
			.parent(enderQuarry)
			.display(
				ModBlocks.ENDER_QUARRY_SPEED_TWO_UPGRADE,
				ModAdvancementLang.ENDER_QUARRY_UPGRADE_TITLE.toComponent(),
				ModAdvancementLang.ENDER_QUARRY_UPGRADE_DESC.toComponent()
			)
			.addCriterion(
				"has_ender_quarry_upgrade",
				hasItems(
					ItemPredicate.Builder.item()
						.of(ModItemTagsProvider.ENDER_QUARRY_UPGRADE)
				)
			)
			.save(ENDER_QUARRY_UPGRADE)

		val perfect = advancement()
			.parent(root)
			.display(
				OpiniumCoreContentsComponent.perfected().getStack(),
				ModAdvancementLang.PERFECT_OPINIUM_TITLE.toComponent(),
				ModAdvancementLang.PERFECT_OPINIUM_DESC.toComponent()
			)
			.addCriterion(
				"has_perfected_opinium_core",
				hasItems(
					ItemPredicate.Builder.item()
						.of(ModItems.OPINIUM_CORE)
						.hasComponents(
							DataComponentPredicate.builder()
								.expect(
									ModDataComponents.OPINIUM_CORE_CONTENTS.get(),
									OpiniumCoreContentsComponent.perfected()
								)
								.build()
						)
				)
			)
			.save(PERFECT_OPINIUM)

		val kikoku = advancement()
			.parent(perfect)
			.display(
				ModItems.KIKOKU.get(),
				ModAdvancementLang.KIKOKU_TITLE.toComponent(),
				ModAdvancementLang.KIKOKU_DESC.toComponent(),
			)
			.has(ModItems.KIKOKU)
			.save(KIKOKU)

		val anyGenerator = advancement()
			.parent(root)
			.display(
				ModBlocks.SURVIVALIST_GENERATOR.asItem(),
				ModAdvancementLang.ANY_GENERATOR_TITLE.toComponent(),
				ModAdvancementLang.ANY_GENERATOR_DESC.toComponent(),
			)
			.hasAny(
				ModBlocks.SURVIVALIST_GENERATOR.asItem(),
				ModBlocks.FURNACE_GENERATOR.asItem(),
				ModBlocks.MAGMATIC_GENERATOR.asItem(),
				ModBlocks.ENDER_GENERATOR.asItem(),
				ModBlocks.HEATED_REDSTONE_GENERATOR.asItem(),
				ModBlocks.CULINARY_GENERATOR.asItem(),
				ModBlocks.POTION_GENERATOR.asItem(),
				ModBlocks.EXPLOSIVE_GENERATOR.asItem(),
				ModBlocks.PINK_GENERATOR.asItem(),
				ModBlocks.OVERCLOCKED_GENERATOR.asItem(),
				ModBlocks.NETHER_STAR_GENERATOR.asItem(),
				ModBlocks.DISENCHANTMENT_GENERATOR.asItem(),
				ModBlocks.FROSTY_GENERATOR.asItem(),
				ModBlocks.HALITOSIS_GENERATOR.asItem(),
				ModBlocks.SLIMY_GENERATOR.asItem(),
				ModBlocks.DEATH_GENERATOR.asItem(),
			)
			.save(ANY_GENERATOR)

		val everyGenerator = advancement()
			.parent(anyGenerator)
			.display(
				ModBlocks.NETHER_STAR_GENERATOR.asItem(),
				ModAdvancementLang.ANY_GENERATOR_TITLE.toComponent(),
				ModAdvancementLang.ANY_GENERATOR_DESC.toComponent(),
			)
			.hasAll(
				ModBlocks.SURVIVALIST_GENERATOR.asItem(),
				ModBlocks.FURNACE_GENERATOR.asItem(),
				ModBlocks.MAGMATIC_GENERATOR.asItem(),
				ModBlocks.ENDER_GENERATOR.asItem(),
				ModBlocks.HEATED_REDSTONE_GENERATOR.asItem(),
				ModBlocks.CULINARY_GENERATOR.asItem(),
				ModBlocks.POTION_GENERATOR.asItem(),
				ModBlocks.EXPLOSIVE_GENERATOR.asItem(),
				ModBlocks.PINK_GENERATOR.asItem(),
				ModBlocks.OVERCLOCKED_GENERATOR.asItem(),
				ModBlocks.NETHER_STAR_GENERATOR.asItem(),
				ModBlocks.DISENCHANTMENT_GENERATOR.asItem(),
				ModBlocks.FROSTY_GENERATOR.asItem(),
				ModBlocks.HALITOSIS_GENERATOR.asItem(),
				ModBlocks.SLIMY_GENERATOR.asItem(),
				ModBlocks.DEATH_GENERATOR.asItem(),
			)
			.save(EVERY_GENERATOR)

		val rainbowGenerator = advancement()
			.parent(everyGenerator)
			.display(
				ModBlocks.RAINBOW_GENERATOR.asItem(),
				ModAdvancementLang.RAINBOW_GENERATOR_TITLE.toComponent(),
				ModAdvancementLang.RAINBOW_GENERATOR_DESC.toComponent(),
			)
			.has(ModBlocks.RAINBOW_GENERATOR)
			.save(RAINBOW_GENERATOR)
	}

	companion object {
		private fun guide(path: String): ResourceLocation = ExcessiveUtilities.modResource("guide/$path")

		val ROOT = guide("root")
		val GP_PRODUCERS = guide("gp_producers")
		val RESONATOR = guide("resonator")
		val TLM = guide("tlm")
		val QUANTUM_QUARRY = guide("quarry")
		val QED = guide("qed")
		val ENDER_QUARRY = guide("ender_quarry")
		val ENDER_QUARRY_UPGRADE = guide("ender_quarry_upgrade")

		val PERFECT_OPINIUM = guide("perfect_opinium")
		val KIKOKU = guide("kikoku")

		val ANY_GENERATOR = guide("any_generator")
		val EVERY_GENERATOR = guide("every_generator")
		val RAINBOW_GENERATOR = guide("rainbow_generator")
	}

}