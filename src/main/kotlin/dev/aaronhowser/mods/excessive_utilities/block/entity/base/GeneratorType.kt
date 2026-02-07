package dev.aaronhowser.mods.excessive_utilities.block.entity.base

import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorItemFuel
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.datamaps.DataMapType

enum class GeneratorType(
	private val id: String,
	val fuelDataMap: DataMapType<Item, GeneratorItemFuel>?
) : StringRepresentable {
	ENDER("ender", GeneratorItemFuel.ENDER),
	HEATED_REDSTONE("heated_redstone", null),
	POTIONS("potions", null),
	EXPLOSIVE("explosive", GeneratorItemFuel.EXPLOSIVE),
	PINK("pink", GeneratorItemFuel.PINK),
	NETHER_STAR("nether_star", GeneratorItemFuel.NETHER_STAR),
	FROSTY("frosty", GeneratorItemFuel.FROSTY),
	HALITOSIS("halitosis", GeneratorItemFuel.HALITOSIS),
	SLIMY("slimy", null),
	DEATH("death", GeneratorItemFuel.DEATH)

	/**
	 * Left out intentionally:
	 * - Survival, Furnace, High-Temperature Furnace: Factor of burn time
	 * - Culinary: Factor of food value
	 * - Magmatic: Uses a fluid
	 */

	;

	override fun getSerializedName(): String = id

	companion object {
		val CODEC: StringRepresentable.EnumCodec<GeneratorType> =
			StringRepresentable.fromEnum { entries.toTypedArray() }
	}

}