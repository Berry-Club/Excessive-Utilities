package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.*
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.loot.ModGlobalLootModifierProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.loot.ModLootTableProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModBlockStateProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.recipe.ModRecipeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.*
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.minecraft.data.advancements.AdvancementProvider
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries

@EventBusSubscriber(modid = ExcessiveUtilities.MOD_ID)
object ModDataGen {

	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent.Client) {
		val generator = event.generator
		val output = generator.packOutput
		val lookupProvider = event.lookupProvider

		val datapackRegistrySets = generator.addProvider(
			true,
			DatapackBuiltinEntriesProvider(
				output,
				lookupProvider,
				RegistrySetBuilder()
					.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
					.add(Registries.ENCHANTMENT, ModEnchantmentProvider::bootstrap)
					.add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap)
					.add(Registries.LEVEL_STEM, ModLevelStems::bootstrap)
					.add(Registries.DIMENSION_TYPE, ModDimensionTypes::bootstrap)
					.add(Registries.BIOME, ModBiomes::bootstrap)
					.add(Registries.STRUCTURE, ModStructures::bootstrap)
					.add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
					.add(Registries.CONFIGURED_CARVER, ModConfiguredCarvers::bootstrap)
					.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
					.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
					.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap),
				setOf(ExcessiveUtilities.MOD_ID)
			)
		)

		val lookupWithDatapack = datapackRegistrySets.registryProvider

		generator.addProvider(
			true,
			ModItemModelProvider(output)
		)
		generator.addProvider(
			true,
			ModBlockStateProvider(output)
		)

		generator.addProvider(
			true,
			ModRecipeProvider(output, lookupProvider)
		)

		generator.addProvider(
			true,
			AdvancementProvider(
				output,
				lookupProvider,
				listOf(ModAdvancementSubProvider(lookupProvider))
			)
		)

		generator.addProvider(
			true,
			ModDataMapProvider(output, lookupProvider)
		)

		generator.addProvider(
			true,
			ModLootTableProvider(output, lookupProvider)
		)

		generator.addProvider(
			true,
			ModGlobalLootModifierProvider(output, lookupProvider)
		)

//		generator.addProvider(
//			true,
//			ModSoundDefinitionsProvider(output)
//		)

		val blockTagProvider = generator.addProvider(
			true,
			ModBlockTagsProvider(output, lookupProvider)
		)
		generator.addProvider(
			true,
			ModItemTagsProvider(output, lookupProvider)
		)
//		generator.addProvider(
//			true,
//			ModFluidTagsProvider(output, lookupProvider, existingFileHelper)
//		)
		generator.addProvider(
			true,
			ModEntityTypeTagsProvider(output, lookupProvider)
		)
		generator.addProvider(
			true,
			ModMobEffectTagsProvider(output, lookupProvider)
		)
		generator.addProvider(
			true,
			ModDamageTypeTagsProvider(output, lookupWithDatapack)
		)
		generator.addProvider(
			true,
			ModEnchantmentTagsProvider(output, lookupWithDatapack)
		)
		generator.addProvider(
			true,
			ModBiomeTagsProvider(output, lookupWithDatapack)
		)

		generator.addProvider(
			true,
			ModCurioProvider(output, lookupProvider)
		)

		val languageProvider = ModLanguageProvider(output)

//		generator.addProvider(
//			true,
//			NeoBookProvider.of(
//				event, lookupProvider, ModModonomiconProvider(languageProvider::add)
//			)
//		)

		generator.addProvider(true, languageProvider)

		generator.addProvider(
			true,
			ModParticleDescriptionProvider(output)
		)

//		generator.addProvider(
//			true,
//			ModPatchouliBookProvider(
//				generator,
//				"guide",
//				lookupProvider
//			)
//		)

	}

}
