package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.ChatFormatting
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
	output: PackOutput
) : LanguageProvider(output, ExcessiveUtilities.MOD_ID, "en_us") {

	override fun addTranslations() {
		ModItemLang.add(this)
		ModBlockLang.add(this)
	}

	companion object {
		fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)
		fun String.toGrayComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args).withStyle(ChatFormatting.GRAY)
	}

}