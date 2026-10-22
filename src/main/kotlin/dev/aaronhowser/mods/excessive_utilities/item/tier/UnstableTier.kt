package dev.aaronhowser.mods.excessive_utilities.item.tier

import net.minecraft.world.item.ToolMaterial

val UnstableTier: ToolMaterial = ToolMaterial(
	ToolMaterial.DIAMOND.incorrectBlocksForDrops(),
	Int.MAX_VALUE,
	ToolMaterial.DIAMOND.speed(),
	ToolMaterial.DIAMOND.attackDamageBonus(),
	ToolMaterial.DIAMOND.enchantmentValue() * 3 / 2,
	ToolMaterial.DIAMOND.repairItems()
)




