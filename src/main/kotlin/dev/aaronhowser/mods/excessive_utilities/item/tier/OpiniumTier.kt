package dev.aaronhowser.mods.excessive_utilities.item.tier

import net.minecraft.world.item.ToolMaterial

val OpiniumTier: ToolMaterial = ToolMaterial(
	ToolMaterial.NETHERITE.incorrectBlocksForDrops(),
	Int.MAX_VALUE,
	ToolMaterial.NETHERITE.speed(),
	ToolMaterial.NETHERITE.attackDamageBonus(),
	ToolMaterial.NETHERITE.enchantmentValue() * 3 / 2,
	ToolMaterial.NETHERITE.repairItems()
)




