package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import mezz.jei.api.registration.IRecipeRegistration

object JeiInfoRecipes {

	fun addRecipes(registration: IRecipeRegistration) {

		registration.addItemStackInfo(
			listOf(
				ModItems.DEMON_NUGGET.getDefaultInstance(),
				ModItems.DEMON_INGOT.getDefaultInstance(),
				ModBlocks.BLOCK_OF_DEMON_METAL.getDefaultInstance()
			),
			ModMenuLang.DEMON_CRAFTING.toComponent()
		)

	}

}