package dev.aaronhowser.mods.excessive_utilities.datamap

import net.minecraft.util.StringRepresentable

enum class GeneratorType(
	private val id: String
) : StringRepresentable {
	FURNACE("furnace"),
	MAGMATIC("magmatic"),
	ENDER("ender"),
	HEATED_REDSTONE("heated_redstone"),
	CULINARY("culinary"),
	POTIONS("potions"),
	EXPLOSIVE("explosive"),
	PINK("pink"),
	NETHER_STAR("nether_star"),
	FROSTY("frosty"),
	HALITOSIS("halitosis"),
	SLIMY("slimy"),
	DEATH("death")

	;

	override fun getSerializedName(): String = id

	companion object {
		val CODEC: StringRepresentable.EnumCodec<GeneratorType> =
			StringRepresentable.fromEnum { entries.toTypedArray() }
	}

}