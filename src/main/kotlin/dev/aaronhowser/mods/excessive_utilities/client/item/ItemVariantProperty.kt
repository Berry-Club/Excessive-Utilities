package dev.aaronhowser.mods.excessive_utilities.client.item

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.excessive_utilities.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.excessive_utilities.item.*
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.ItemOwner
import net.minecraft.world.item.ItemStack

class ItemVariantProperty(val predicate: Identifier) : RangeSelectItemModelProperty {
    init { require(predicate in SUPPORTED) { "Unknown item model predicate: $predicate" } }
    override fun get(stack: ItemStack, level: ClientLevel?, owner: ItemOwner?, seed: Int): Float {
        val entity = owner?.asLivingEntity()
        return when (predicate) {
            EntityLassoItem.HAS_ENTITY -> EntityLassoItem.hasEntityPredicate(stack, level, entity, seed)
            AngelRingItem.RING_TYPE -> AngelRingItem.hasEntityPredicate(stack, level, entity, seed)
            ModItemModelProvider.ENDER_SHARD_COUNT -> stack.count.toFloat()
            WateringCanItem.IS_BROKEN_PREDICATE -> WateringCanItem.isBroken(stack, level, entity, seed)
            MagicalBoomerangItem.THROWN_PREDICATE -> MagicalBoomerangItem.isThrown(stack, level, entity, seed)
            PULL -> if (entity != null && entity.useItem == stack) (stack.getUseDuration(entity) - entity.useItemRemainingTicks) / 20f else 0f
            PULLING -> if (entity != null && entity.isUsingItem && entity.useItem == stack) 1f else 0f
            else -> error("Unknown item model predicate: $predicate")
        }
    }
    override fun type(): MapCodec<ItemVariantProperty> = CODEC
    companion object {
        private val PULL = Identifier.withDefaultNamespace("pull")
        private val PULLING = Identifier.withDefaultNamespace("pulling")
        private val SUPPORTED = setOf(EntityLassoItem.HAS_ENTITY, AngelRingItem.RING_TYPE, ModItemModelProvider.ENDER_SHARD_COUNT, WateringCanItem.IS_BROKEN_PREDICATE, MagicalBoomerangItem.THROWN_PREDICATE, PULL, PULLING)
        val CODEC: MapCodec<ItemVariantProperty> = Identifier.CODEC.fieldOf("predicate").xmap(::ItemVariantProperty, ItemVariantProperty::predicate)
    }
}
