package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.aaron.datagen.AaronAdvancementSubProvider
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModAdvancementLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
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
			.addCriterion("has_wood", hasItems(ItemPredicate.Builder.item().of(ItemTags.LOGS)))
			.save(ROOT)

		advancement()
			.parent(root)
			.display(
				ModBlocks.MANUAL_MILL,
				ModAdvancementLang.GP_PRODUCERS_TITLE.toComponent(),
				ModAdvancementLang.GP_PRODUCERS_DESC.toComponent(),
			)
			.addCriterion("has_gp_producer", hasItems(ItemPredicate.Builder.item().of(ModItemTagsProvider.GP_PRODUCER)))
			.save(GP_PRODUCERS)

	}

	companion object {
		private fun guide(path: String): ResourceLocation = ExcessiveUtilities.modResource("guide/$path")

		val ROOT = guide("root")
		val GP_PRODUCERS = guide("gp_producers")
	}

}