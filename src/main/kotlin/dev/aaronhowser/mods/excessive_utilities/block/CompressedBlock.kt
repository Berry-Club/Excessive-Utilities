package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.world.level.block.state.BlockBehaviour.Properties

import net.minecraft.world.level.block.Block
import java.util.*
import kotlin.math.pow

class CompressedBlock(
	compressionLevel: Int,
	properties: Properties
) : Block(properties) {

	val amount: Long = 9.0.pow(compressionLevel.toDouble()).toLong()
	private val formattedString = "%,d".format(Locale.US, amount)


}














