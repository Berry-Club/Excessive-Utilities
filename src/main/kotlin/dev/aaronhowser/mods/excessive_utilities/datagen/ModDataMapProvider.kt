package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorItemFuel
import dev.aaronhowser.mods.excessive_utilities.datamap.MagmaticGeneratorFuel
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {

		fun addFuel(
			map: DataMapType<Item, GeneratorItemFuel>,
			item: ItemLike,
			fePerTick: Int,
			burnTime: Int
		) {
			builder(map).add(
				item.asItem().builtInRegistryHolder(),
				GeneratorItemFuel(fePerTick, burnTime),
				false
			)
		}

		fun addFuel(
			map: DataMapType<Item, GeneratorItemFuel>,
			itemTag: TagKey<Item>,
			fePerTick: Int,
			burnTime: Int
		) {
			builder(map).add(
				itemTag,
				GeneratorItemFuel(fePerTick, burnTime),
				false
			)
		}

		addFuel(GeneratorItemFuel.ENDER, Tags.Items.ENDER_PEARLS, 40, 20 * (60 + 20)) // 1:20
		addFuel(GeneratorItemFuel.ENDER, Items.ENDER_EYE, 80, (20 * (2 * 60 + 40))) // 2:40

		addFuel(GeneratorItemFuel.PINK, Tags.Items.DYES_PINK, 40, 10)
		addFuel(GeneratorItemFuel.PINK, Tags.Items.DYED_PINK, 40, 10)

		addFuel(GeneratorItemFuel.DEATH, Tags.Items.BONES, 40, 20 * 20)
		addFuel(GeneratorItemFuel.DEATH, Tags.Items.STORAGE_BLOCKS_BONE_MEAL, 150, 20 * 20)
		addFuel(GeneratorItemFuel.DEATH, Items.BONE_MEAL, 40, 20 * 10)
		addFuel(GeneratorItemFuel.DEATH, Items.ROTTEN_FLESH, 20, 20 * 20)
		addFuel(GeneratorItemFuel.DEATH, Items.SKELETON_SKULL, 100, 20 * 20)
		addFuel(GeneratorItemFuel.DEATH, Items.WITHER_SKELETON_SKULL, 150, 20 * 20)

		addFuel(GeneratorItemFuel.EXPLOSIVE, Items.TNT, 160, 20 * (2 * 60 + 40))
		addFuel(GeneratorItemFuel.EXPLOSIVE, Items.TNT_MINECART, 200, 20 * (2 * 60 + 40))
		addFuel(GeneratorItemFuel.EXPLOSIVE, Tags.Items.GUNPOWDERS, 160, 20 * 20)

		addFuel(GeneratorItemFuel.NETHER_STAR, Items.NETHER_STAR, 4_000, 20 * 60 * 2)
		addFuel(GeneratorItemFuel.NETHER_STAR, Items.FIREWORK_STAR, 20, 20)

		addFuel(GeneratorItemFuel.HALITOSIS, Items.DRAGON_BREATH, 40, 20 * 60 * 10) // 10 minutes

		addFuel(GeneratorItemFuel.FROSTY, Items.ICE, 40, 20 * 2)
		addFuel(GeneratorItemFuel.FROSTY, Items.PACKED_ICE, 40, 20 * 2 * 9)
		addFuel(GeneratorItemFuel.FROSTY, Items.BLUE_ICE, 40, 20 * 2 * 9 * 9)
		addFuel(GeneratorItemFuel.FROSTY, Items.SNOWBALL, 40, 5)
		addFuel(GeneratorItemFuel.FROSTY, Items.SNOW_BLOCK, 40, 20)
		addFuel(GeneratorItemFuel.FROSTY, Items.SNOW, 40, 3)

		builder(MagmaticGeneratorFuel.MAP)
			.add(
				Tags.Fluids.LAVA,
				MagmaticGeneratorFuel(100_000 / 20, 125 * 20),
				false
			)
	}

}