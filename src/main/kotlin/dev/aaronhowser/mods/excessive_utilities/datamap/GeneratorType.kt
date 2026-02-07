package dev.aaronhowser.mods.excessive_utilities.datamap

import net.minecraft.util.StringRepresentable

enum class GeneratorType(
	private val id: String
) : StringRepresentable {
	ENDER("ender"),
	HEATED_REDSTONE("heated_redstone"),
	POTIONS("potions"),
	EXPLOSIVE("explosive"),
	PINK("pink"),
	NETHER_STAR("nether_star"),
	FROSTY("frosty"),
	HALITOSIS("halitosis"),
	SLIMY("slimy"),
	DEATH("death")

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