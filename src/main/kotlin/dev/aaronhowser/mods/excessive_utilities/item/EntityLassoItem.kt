package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.AaronExtensions.getMinimalTag
import dev.aaronhowser.mods.aaron.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.AaronExtensions.isEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.context.UseOnContext

class EntityLassoItem(
	val canHoldHostileMobs: Boolean,
	properties: Properties
) : Item(properties) {

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (player.isClientSide
			|| stack.has(ModDataComponents.ENTITY)
			|| interactionTarget.isEntity(ModEntityTypeTagsProvider.LASSO_BLACKLIST)
			|| (!canHoldHostileMobs && interactionTarget is Enemy)
		) {
			return InteractionResult.PASS
		}

		val entityNbt = interactionTarget.getMinimalTag(stripUniqueness = false)
		val customNbt = CustomData.of(entityNbt)

		stack.set(ModDataComponents.ENTITY, customNbt)

		return InteractionResult.SUCCESS
	}

	override fun useOn(context: UseOnContext): InteractionResult {
		val stack = context.itemInHand
		val entityData = stack.get(ModDataComponents.ENTITY) ?: return InteractionResult.PASS

		val level = context.level

		val clickedPos = context.clickedPos
		val clickedFace = context.clickedFace
		val clickedState = level.getBlockState(clickedPos)

		val posToSpawn = if (clickedState.isSuffocating(level, clickedPos)) {
			val relative = clickedPos.relative(clickedFace)
			if (level.getBlockState(relative).isSuffocating(level, relative)) {
				return InteractionResult.FAIL
			}

			relative
		} else {
			clickedPos
		}

		val entityTypeString = entityData.copyTag().getString("id")
		val entityType = level.registryAccess()
			.registryOrThrow(Registries.ENTITY_TYPE)
			.get(ResourceLocation.parse(entityTypeString))
			?: return InteractionResult.FAIL

		val entity = entityType.create(level) ?: return InteractionResult.FAIL
		entity.load(entityData.copyTag())
		entity.moveTo(posToSpawn.bottomCenter)
		level.addFreshEntity(entity)

		stack.remove(ModDataComponents.ENTITY)
		return InteractionResult.SUCCESS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}