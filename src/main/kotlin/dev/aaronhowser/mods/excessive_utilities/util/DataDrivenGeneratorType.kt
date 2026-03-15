package dev.aaronhowser.mods.excessive_utilities.util

import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleItemFuelRecipe
import net.minecraft.util.StringRepresentable

enum class DataDrivenGeneratorType(
	private val id: String,
	val baseGeneratorType: GeneratorType
) : StringRepresentable {
	ENDER("ender", GeneratorType.ENDER),
	EXPLOSIVE("explosive", GeneratorType.EXPLOSIVE),
	PINK("pink", GeneratorType.PINK),
	NETHER_STAR("nether_star", GeneratorType.NETHER_STAR),
	FROSTY("frosty", GeneratorType.FROSTY),
	HALITOSIS("halitosis", GeneratorType.HALITOSIS),
	DEATH("death", GeneratorType.DEATH)

	/**
	 * Left out intentionally:
	 * - Survival, Furnace, High-Temperature Furnace: Factor of burn time
	 * - Culinary: Factor of food value
	 * - Magmatic, Heated Redstone: Uses a fluid
	 * - Potions: Uses a potion
	 * - Slimy: Uses multiple items together
	 */

	;

	val fuelRecipeType: SingleItemFuelRecipe.GeneratorType by lazy {
		when (this) {
			ENDER -> SingleItemFuelRecipe.GeneratorType.ENDER
			EXPLOSIVE -> SingleItemFuelRecipe.GeneratorType.EXPLOSIVE
			PINK -> SingleItemFuelRecipe.GeneratorType.PINK
			NETHER_STAR -> SingleItemFuelRecipe.GeneratorType.NETHER_STAR
			FROSTY -> SingleItemFuelRecipe.GeneratorType.FROSTY
			HALITOSIS -> SingleItemFuelRecipe.GeneratorType.HALITOSIS
			DEATH -> SingleItemFuelRecipe.GeneratorType.DEATH
		}
	}

	override fun getSerializedName(): String = id

	companion object {
		val CODEC: StringRepresentable.EnumCodec<DataDrivenGeneratorType> =
			StringRepresentable.fromEnum { entries.toTypedArray() }
	}

}