package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
	output: PackOutput
) : LanguageProvider(output, ExcessiveUtilities.MOD_ID, "en_us") {

	override fun addTranslations() {
		ModItemLang.add(this)
		ModBlockLang.add(this)
		ModEntityLang.add(this)
		ModMessageLang.add(this)
		ModEffectLang.add(this)
		ModConfigLang.add(this)
		ModMenuLang.add(this)
		ModEnchantmentLang.add(this)
		ModAdvancementLang.add(this)
	}

}
