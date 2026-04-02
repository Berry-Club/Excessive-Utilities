package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.common.Tags

object ModMenuLang {

	const val SOUL_OF_A_FORGOTTEN_DEITY = "tooltip.excessive_utilities.soul_of_a_forgotten_deity"
	const val SOUL_DEBT = "tooltip.excessive_utilities.soul_debt"
	const val SOUL_SURPLUS = "tooltip.excessive_utilities.soul_surplus"
	const val SOUL_HEALTH_MODIFIER = "tooltip.excessive_utilities.soul_health_modifier"
	const val FE_WITH_CAPACITY = "tooltip.geneticsresequenced.fe_with_capacity"
	const val FE_PER_TICK = "message.excessive_utilities.fe_per_tick"
	const val FE = "tooltip.geneticsresequenced.fe"
	const val UNSTABLE_INGOT_CHEESED_1 = "tooltip.excessive_utilities.unstable_ingot.cheesed_1"
	const val UNSTABLE_INGOT_CHEESED_2 = "tooltip.excessive_utilities.unstable_ingot.cheesed_2"
	const val UNSTABLE_INGOT_CHEESED_3 = "tooltip.excessive_utilities.unstable_ingot.cheesed_3"
	const val TICKS = "message.excessive_utilities.ticks"
	const val GP = "tooltip.excessive_utilities.gp"
	const val GP_COST = "tooltip.excessive_utilities.gp_cost"
	const val GP_WITH_CAPACITY = "tooltip.excessive_utilities.gp_with_capacity"
	const val INFINITE_USES = "tooltip.excessive_utilities.infinite_uses"
	const val REMAINING_USES = "tooltip.excessive_utilities.remaining_uses"
	const val BROKEN = "tooltip.excessive_utilities.broken"
	const val HELD_WATER = "tooltip.excessive_utilities.held_water"
	const val SOLAR_POWER = "tooltip.excessive_utilities.solar_power"
	const val CURRENT_OFFSET = "tooltip.excessive_utilities.current_offset"
	const val MAX_OFFSET = "tooltip.excessive_utilities.max_offset"
	const val MIN_OFFSET = "tooltip.excessive_utilities.min_offset"
	const val GP_AT_POS = "tooltip.excessive_utilities.gp_at_pos"
	const val BREWING_STEPS = "message.excessive_utilities.brewing_steps"
	const val SECONDS_UNTIL_EXPLOSION = "message.excessive_utilities.seconds_until_explosion"
	const val INVERTED_ON = "tooltip.excessive_utilities.item_filter.inverted_on"
	const val INVERTED_OFF = "tooltip.excessive_utilities.item_filter.inverted_off"
	const val USE_TAGS_ON = "tooltip.excessive_utilities.item_filter.use_tags_on"
	const val USE_TAGS_OFF = "tooltip.excessive_utilities.item_filter.use_tags_off"
	const val IGNORE_DAMAGE_ON = "tooltip.excessive_utilities.item_filter.ignore_damage_on"
	const val IGNORE_DAMAGE_OFF = "tooltip.excessive_utilities.item_filter.ignore_damage_off"
	const val IGNORE_ALL_COMPONENTS_ON = "tooltip.excessive_utilities.item_filter.ignore_all_components_on"
	const val IGNORE_ALL_COMPONENTS_OFF = "tooltip.excessive_utilities.item_filter.ignore_all_components_off"

	const val SNOW_GLOBE_TOOLTIP = "tooltip.excessive_utilities.magical_snow_globe"
	const val SNOW_GLOBE_INSTRUCTIONS = "tooltip.excessive_utilities.magical_snow_globe_instructions"

	fun add(provider: ModLanguageProvider) {
		provider.apply {
			add(SOUL_OF_A_FORGOTTEN_DEITY, "Soul of a Forgotten Deity")
			add(SOUL_DEBT, "Soul Debt: %d")
			add(SOUL_SURPLUS, "Soul Surplus: %d")
			add(SOUL_HEALTH_MODIFIER, "Current Health Modifier: %s")
			add(FE_WITH_CAPACITY, "%1\$s / %2\$s FE")
			add(FE_PER_TICK, "%s FE/t")
			add(TICKS, "%s ticks")
			add(FE, "%s FE")
			add(GP, "%s GP")
			add(GP_COST, "GP Cost: %s")
			add(GP_WITH_CAPACITY, "%1\$s / %2\$s GP")
			add(INFINITE_USES, "Infinite Uses")
			add(REMAINING_USES, "Remaining Uses: %d")
			add(BROKEN, "Broken")
			add(HELD_WATER, "Water: %s/%s")
			add(SOLAR_POWER, "Solar Power: %d%%")
			add(CURRENT_OFFSET, "Current Offset: (%d, %d, %d)")
			add(MAX_OFFSET, "Max Offset: (%d, %d, %d)")
			add(MIN_OFFSET, "Min Offset: (%d, %d, %d)")
			add(GP_AT_POS, "%s at (%d, %d, %d)")
			add(BREWING_STEPS, "%d Brewing Steps")
			add(SECONDS_UNTIL_EXPLOSION, "Seconds Until Explosion: %s")
			add(INVERTED_ON, "Inverted: ON")
			add(INVERTED_OFF, "Inverted: OFF")
			add(USE_TAGS_ON, "Use Tags: ON")
			add(USE_TAGS_OFF, "Use Tags: OFF")
			add(IGNORE_DAMAGE_ON, "Ignore Damage: ON")
			add(IGNORE_DAMAGE_OFF, "Ignore Damage: OFF")
			add(IGNORE_ALL_COMPONENTS_ON, "Ignore All Components: ON")
			add(IGNORE_ALL_COMPONENTS_OFF, "Ignore All Components: OFF")

			add(AngelRingItem.Type.INVISIBLE.langKey, "Invisible")
			add(AngelRingItem.Type.FEATHER.langKey, "Feather")
			add(AngelRingItem.Type.BUTTERFLY.langKey, "Butterfly")
			add(AngelRingItem.Type.DEMON.langKey, "Demon")
			add(AngelRingItem.Type.GOLD.langKey, "Gold")
			add(AngelRingItem.Type.BAT.langKey, "Bat")

			add(UNSTABLE_INGOT_CHEESED_1, "Naughty naughty!")
			add(UNSTABLE_INGOT_CHEESED_2, "Unstable Ingots must be crafted manually, by you.")
			add(UNSTABLE_INGOT_CHEESED_3, "If this one WAS crafted manually, try using a vanilla Crafting Table instead.")

			add(biomeTagLangKey(BiomeTags.IS_FOREST), "Forest")
			add(biomeTagLangKey(BiomeTags.IS_HILL), "Hills")
			add(biomeTagLangKey(BiomeTags.IS_JUNGLE), "Jungle")
			add(biomeTagLangKey(Tags.Biomes.IS_MAGICAL), "Magical")
			add(biomeTagLangKey(BiomeTags.IS_MOUNTAIN), "Mountain")
			add(biomeTagLangKey(BiomeTags.IS_OCEAN), "Ocean")
			add(biomeTagLangKey(Tags.Biomes.IS_PLAINS), "Plains")
			add(biomeTagLangKey(Tags.Biomes.IS_SANDY), "Sandy")
			add(biomeTagLangKey(Tags.Biomes.IS_SNOWY), "Snowy")
			add(biomeTagLangKey(Tags.Biomes.IS_SWAMP), "Swamp")
			add(biomeTagLangKey(BiomeTags.IS_END), "End")
			add(biomeTagLangKey(BiomeTags.IS_NETHER), "Nether")
		}
	}

	fun biomeTagLangKey(tag: TagKey<Biome>): String {
		val location = tag.location
		return "biome.${location.namespace}.${location.path}"
	}

}