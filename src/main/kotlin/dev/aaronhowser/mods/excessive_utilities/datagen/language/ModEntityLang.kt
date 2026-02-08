package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes

object ModEntityLang {

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			addEntityType(ModEntityTypes.FLAT_TRANSFER_NODE, "Flat Transfer Node")
		}
	}

}