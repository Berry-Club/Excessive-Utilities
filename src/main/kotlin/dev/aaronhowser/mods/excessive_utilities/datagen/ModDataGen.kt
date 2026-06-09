package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.loot.ModGlobalLootModifierProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.loot.ModLootTableProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModBlockStateProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.ModRecipeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.*
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModBiomeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModDensityFunctionsProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModDimensionTypeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModLevelStemProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModNoiseSettingsProvider
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = ExcessiveUtilities.MOD_ID)
object ModDataGen {

	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent) {
		val generator = event.generator
		val output = generator.packOutput
		val existingFileHelper = event.existingFileHelper
		val lookupProvider = event.lookupProvider

		val withDensity = generator.addProvider(
			event.includeServer(),
			DatapackBuiltinEntriesProvider(
				output,
				lookupProvider,
				RegistrySetBuilder()
					.add(Registries.DENSITY_FUNCTION, ModDensityFunctionsProvider::bootstrap)
				,
				setOf(ExcessiveUtilities.MOD_ID)
			)
		)

		val datapackRegistrySets = generator.addProvider(
			event.includeServer(),
			DatapackBuiltinEntriesProvider(
				output,
				withDensity.registryProvider,
				RegistrySetBuilder()
					.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
					.add(Registries.ENCHANTMENT, ModEnchantmentProvider::bootstrap)
					.add(Registries.NOISE_SETTINGS, ModNoiseSettingsProvider::bootstrap)
					.add(Registries.LEVEL_STEM, ModLevelStemProvider::bootstrap)
					.add(Registries.DIMENSION_TYPE, ModDimensionTypeProvider::bootstrap)
					.add(Registries.BIOME, ModBiomeProvider::bootstrap)
				,
				setOf(ExcessiveUtilities.MOD_ID)
			)
		)

		val lookupWithDatapack = datapackRegistrySets.registryProvider

		generator.addProvider(
			event.includeClient(),
			ModItemModelProvider(output, existingFileHelper)
		)
		generator.addProvider(
			event.includeClient(),
			ModBlockStateProvider(output, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModRecipeProvider(output, lookupProvider)
		)

		generator.addProvider(
			event.includeServer(),
			ModDataMapProvider(output, lookupProvider)
		)

		generator.addProvider(
			event.includeServer(),
			ModLootTableProvider(output, lookupProvider)
		)

		generator.addProvider(
			event.includeServer(),
			ModGlobalLootModifierProvider(output, lookupProvider)
		)

//		generator.addProvider(
//			event.includeClient(),
//			ModSoundDefinitionsProvider(output, existingFileHelper)
//		)

		val blockTagProvider = generator.addProvider(
			event.includeServer(),
			ModBlockTagsProvider(output, lookupProvider, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModItemTagsProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper)
		)
//		generator.addProvider(
//			event.includeServer(),
//			ModFluidTagsProvider(output, lookupProvider, existingFileHelper)
//		)
		generator.addProvider(
			event.includeServer(),
			ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModDamageTypeTagsProvider(output, lookupWithDatapack, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModEnchantmentTagsProvider(output, lookupWithDatapack, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModBiomeTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModCurioProvider(output, existingFileHelper, lookupProvider)
		)

		val languageProvider = ModLanguageProvider(output)

//		generator.addProvider(
//			event.includeClient(),
//			NeoBookProvider.of(
//				event, lookupProvider, ModModonomiconProvider(languageProvider::add)
//			)
//		)

		generator.addProvider(event.includeClient(), languageProvider)

//		generator.addProvider(
//			event.includeClient(),
//			ModParticleDescriptionProvider(output, existingFileHelper)
//		)
//
//		generator.addProvider(
//			event.includeClient(),
//			ModPatchouliBookProvider(
//				generator,
//				"guide",
//				lookupProvider
//			)
//		)

	}

}