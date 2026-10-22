package dev.aaronhowser.mods.excessive_utilities.client.item

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.excessive_utilities.block.DrumBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.DrumBlockEntity
import dev.aaronhowser.mods.excessive_utilities.item.*
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.util.ARGB

class ItemColorSource(val index: Int) : ItemTintSource {
    override fun calculate(stack: ItemStack, level: ClientLevel?, owner: LivingEntity?): Int {
        return when (val item = stack.item) {
            is HeatingCoilItem -> HeatingCoilItem.getItemColor(stack, index)
            is SunCrystalItem -> SunCrystalItem.getItemColor(stack, index)
            is UnstableIngotItem -> UnstableIngotItem.getColor(stack, index)
            is PaintbrushItem -> ARGB.opaque(PaintbrushItem.getItemColor(stack, index))
            is BlockItem -> {
                if (item.block is DrumBlock) DrumBlockEntity.getItemStackColor(stack, index) else {
                    val color = DyeColor.entries.firstOrNull { ModBlocks.getLapisCaelestis(it).asItem() == item }
                    ARGB.opaque(requireNotNull(color) { "No item color defined for $item" }.textureDiffuseColor)
                }
            }
            else -> error("No item color defined for $item")
        }
    }
    override fun type(): MapCodec<ItemColorSource> = CODEC
    companion object {
        val CODEC: MapCodec<ItemColorSource> = Codec.intRange(0, 2).fieldOf("index").xmap(::ItemColorSource, ItemColorSource::index)
    }
}
