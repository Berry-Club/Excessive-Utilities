package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.registryHolder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.registries.datamaps.DataMapType

class NetherLavaDunkConversion(
	val output: ItemStack
) {

	companion object {
		val CODEC: Codec<NetherLavaDunkConversion> =
			ItemStack.CODEC.xmap(::NetherLavaDunkConversion, NetherLavaDunkConversion::output)

		val DATA_MAP: DataMapType<Item, NetherLavaDunkConversion> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("nether_lava_dunk_conversion"),
					Registries.ITEM,
					CODEC
				)
				.synced(CODEC, true)
				.build()

		fun tryConvert(itemEntity: ItemEntity) {
			val level = itemEntity.level() as? ServerLevel ?: return
			if (level.dimension() != Level.NETHER) return

			val item = itemEntity.item
			val output = item.item
				.registryHolder()
				.getData(DATA_MAP)
				?.output
				?: return

			val pos = itemEntity.blockPosition()
			val fluidState = level.getFluidState(pos)
			if (fluidState.isFluid(FluidTags.LAVA)) {
				val count = item.count
				val demonStack = output.copyWithCount(count)

				itemEntity.item = demonStack
				itemEntity.deltaMovement = Vec3.ZERO
			}
		}
	}

}