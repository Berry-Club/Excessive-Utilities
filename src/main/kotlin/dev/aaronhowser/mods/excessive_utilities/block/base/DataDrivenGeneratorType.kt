package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorItemFuel
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.datamaps.DataMapType

enum class DataDrivenGeneratorType(
	private val id: String
) : StringRepresentable {
	ENDER("ender"),
	EXPLOSIVE("explosive"),
	PINK("pink"),
	NETHER_STAR("nether_star"),
	FROSTY("frosty"),
	HALITOSIS("halitosis"),
	DEATH("death")

	/**
	 * Left out intentionally:
	 * - Survival, Furnace, High-Temperature Furnace: Factor of burn time
	 * - Culinary: Factor of food value
	 * - Magmatic, Heated Redstone: Uses a fluid
	 * - Potions: Uses a potion
	 * - Slimy: Uses multiple items together
	 */

	;

	val fuelDataMap: DataMapType<Item, GeneratorItemFuel>
		get() = when (this) {
			ENDER -> GeneratorItemFuel.ENDER
			EXPLOSIVE -> GeneratorItemFuel.EXPLOSIVE
			PINK -> GeneratorItemFuel.PINK
			NETHER_STAR -> GeneratorItemFuel.NETHER_STAR
			FROSTY -> GeneratorItemFuel.FROSTY
			HALITOSIS -> GeneratorItemFuel.HALITOSIS
			DEATH -> GeneratorItemFuel.DEATH
		}

	override fun getSerializedName(): String = id

	companion object {
		val CODEC: StringRepresentable.EnumCodec<DataDrivenGeneratorType> =
			StringRepresentable.fromEnum { entries.toTypedArray() }
	}

}