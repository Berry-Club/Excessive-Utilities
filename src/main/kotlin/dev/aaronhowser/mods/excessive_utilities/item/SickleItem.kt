package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SickleItem(properties: Properties) : Item(properties) {

	override fun mineBlock(
		stack: ItemStack,
		level: Level,
		state: BlockState,
		pos: BlockPos,
		miningEntity: LivingEntity
	): Boolean {
		if (!isCorrectToolForDrops(stack, state)) return false

		if (level.isServerSide) {
			val radius = stack.getOrDefault(ModDataComponents.RADIUS.get(), 1)
			val blocks = BlockPos.betweenClosed(
				pos.offset(-radius, -radius, -radius),
				pos.offset(radius, radius, radius)
			)

			for (block in blocks) {
				val stateThere = level.getBlockState(block)
				if (isCorrectToolForDrops(stack, stateThere)) {
					level.destroyBlock(block, true, miningEntity)

					stack.hurtAndBreak(1, miningEntity, EquipmentSlot.MAINHAND)

					if (stack.isEmpty) {
						return true
					}
				}
			}
		}

		return true
	}

	companion object {
		fun getDefaultProperties(tier: ToolMaterial): Properties {
			return tier.applyToolProperties(Properties(), ModBlockTagsProvider.SICKLE_MINEABLE, 0f, -3f, 0f)
				.component(
					ModDataComponents.RADIUS.get(),
					when (tier) {
						ToolMaterial.WOOD -> 1
						ToolMaterial.STONE -> 2
						ToolMaterial.GOLD -> 1
						ToolMaterial.IRON -> 3
						ToolMaterial.DIAMOND -> 4
						ToolMaterial.NETHERITE -> 5
						else -> 1
					}
				)
				.durability(
					when (tier) {
						ToolMaterial.WOOD -> 531
						ToolMaterial.STONE -> 1179
						ToolMaterial.GOLD -> 288
						ToolMaterial.IRON -> 2250
						ToolMaterial.DIAMOND -> 14049
						ToolMaterial.NETHERITE -> 30074
						else -> tier.durability()
					}
				)
		}
	}

}





