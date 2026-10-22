package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getMinimalTag
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.Identifier
import net.minecraft.nbt.NbtOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.OwnableEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.util.ProblemReporter
import net.minecraft.world.level.storage.TagValueInput
import net.neoforged.neoforge.common.Tags

class EntityLassoItem(
	val canHoldHostileMobs: Boolean,
	properties: Properties
) : Item(properties), TooltipItem {

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (player.isClientSide
			|| stack.has(ModDataComponents.ENTITY_DATA)
			|| interactionTarget !is Mob
		) {
			return InteractionResult.PASS
		}

		if (interactionTarget.isEntity(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)) {
			player.status(ModMessageLang.LASSO_FAIL_BLACKLIST.toComponent())
			return InteractionResult.FAIL
		}

		if (canHoldHostileMobs) {
			if (interactionTarget !is Enemy) {
				player.status(ModMessageLang.LASSO_FAIL_HOSTILE_ONLY.toComponent())
				return InteractionResult.FAIL
			}
		} else {
			if (interactionTarget is Enemy) {
				player.status(ModMessageLang.LASSO_FAIL_PASSIVE_ONLY.toComponent())
				return InteractionResult.FAIL
			}
		}

		if (interactionTarget is OwnableEntity) {
			val owner = interactionTarget.owner
			if (owner != null && owner != player) {
				player.status(ModMessageLang.LASSO_FAIL_OWNERSHIP.toComponent())
				return InteractionResult.FAIL
			}
		}

		if (canHoldHostileMobs) {
			val canPickUp = interactionTarget.health <= (interactionTarget.maxHealth / 2f)
			if (!canPickUp) return InteractionResult.FAIL
		}

		val entityNbt = interactionTarget.getMinimalTag(stripUniqueness = false)
		val customNbt = CustomData.of(entityNbt)

		stack.set(ModDataComponents.ENTITY_DATA, customNbt)
		stack.set(ModDataComponents.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(interactionTarget.type))

		interactionTarget.remove(Entity.RemovalReason.DISCARDED)

		return InteractionResult.SUCCESS
	}

	override fun useOn(context: UseOnContext): InteractionResult {
		val stack = context.itemInHand
		if (!stack.has(ModDataComponents.ENTITY_DATA)) return InteractionResult.PASS

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

		val success = spawnEntityFromStack(stack, level, posToSpawn) != null
		if (!success) return InteractionResult.FAIL

		stack.remove(ModDataComponents.ENTITY_DATA)
		stack.remove(ModDataComponents.ENTITY_TYPE)

		return InteractionResult.SUCCESS
	}

	override fun appendTooltip(
		stack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: java.util.function.Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val entityType = stack.get(ModDataComponents.ENTITY_TYPE) ?: return
		val typeName = entityType.value().description

		val entityData = stack.get(ModDataComponents.ENTITY_DATA)

		if (entityData == null) {
			builder.accept(typeName.copy().withStyle(ChatFormatting.GRAY))
			return
		}

		val registries = context.registries() ?: return
		val customNameTag = entityData.copyTag().get("CustomName")
		if (customNameTag != null) {
			val customName = ComponentSerialization.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), customNameTag).result().orElse(null)
			if (customName != null) {
				builder.accept(ModMenuLang.LASSO_ENTITY_WITH_NAME.toGrayComponent(customName, typeName))
				return
			}
		}
		builder.accept(typeName)

	}

	override fun onDestroyed(
		itemEntity: ItemEntity,
		damageSource: DamageSource
	) {
		spawnEntityFromStack(
			itemEntity.item,
			itemEntity.level(),
			itemEntity.blockPosition()
		)
	}

	companion object {
		val HAS_ENTITY: Identifier = ExcessiveUtilities.modResource("has_entity")
		fun hasEntityPredicate(
			stack: ItemStack,
			localLevel: Level?,
			holdingEntity: LivingEntity?,
			int: Int
		): Float {
			return if (stack.has(ModDataComponents.ENTITY_DATA) || stack.has(ModDataComponents.ENTITY_TYPE)) 1f else 0f
		}

		private fun spawnEntityFromStack(
			stack: ItemStack,
			level: Level,
			pos: BlockPos
		): Entity? {
			val entityData = stack.get(ModDataComponents.ENTITY_DATA) ?: return null
			val entityTypeString = entityData.copyTag().getString("id").orElse(null) ?: return null

			val entityType = level.registryAccess()
				.lookupOrThrow(Registries.ENTITY_TYPE)
				.get(Identifier.parse(entityTypeString))

			val entityHolder = entityType.orElse(null) ?: return null
			val entity = entityHolder.value().create(level, EntitySpawnReason.LOAD) ?: return null
			entity.load(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), entityData.copyTag()))
			entity.setPos(pos.bottomCenter)
			level.addFreshEntity(entity)
			return entity
		}
	}

}











