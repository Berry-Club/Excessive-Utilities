package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorFuelDataMap
import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorType
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.DataMapProvider
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
	packOutput: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DataMapProvider(packOutput, lookupProvider) {

	override fun gather(provider: HolderLookup.Provider) {
		val fuelBuilder = builder(GeneratorFuelDataMap.DATA_MAP_TYPE)

		fun addFuel(item: ItemLike, generatorType: GeneratorType, fePerTick: Int, burnTime: Int) {
			fuelBuilder.add(
				item.asItem().builtInRegistryHolder(),
				GeneratorFuelDataMap(generatorType, fePerTick, burnTime),
				false
			)
		}

		fun addFuel(itemTag: TagKey<Item>, generatorType: GeneratorType, fePerTick: Int, burnTime: Int) {
			fuelBuilder.add(
				itemTag,
				GeneratorFuelDataMap(generatorType, fePerTick, burnTime),
				false
			)
		}

		addFuel(Tags.Items.ENDER_PEARLS, GeneratorType.ENDER, 40, 20 * (60 + 20)) // 1:20
		addFuel(Items.ENDER_EYE, GeneratorType.ENDER, 80, (20 * (2 * 60 + 40))) // 2:40

		addFuel(Tags.Items.DYES_PINK, GeneratorType.PINK, 40, 10)
		addFuel(Tags.Items.DYED_PINK, GeneratorType.PINK, 40, 10)

		addFuel(Tags.Items.BONES, GeneratorType.DEATH, 40, 20 * 20)
		addFuel(Tags.Items.STORAGE_BLOCKS_BONE_MEAL, GeneratorType.DEATH, 150, 20 * 20)
		addFuel(Items.BONE_MEAL, GeneratorType.DEATH, 40, 20 * 10)
		addFuel(Items.ROTTEN_FLESH, GeneratorType.DEATH, 20, 20 * 20)
		addFuel(Items.SKELETON_SKULL, GeneratorType.DEATH, 100, 20 * 20)
		addFuel(Items.WITHER_SKELETON_SKULL, GeneratorType.DEATH, 150, 20 * 20)

		addFuel(Items.TNT, GeneratorType.EXPLOSIVE, 160, 20 * (2 * 60 + 40))
		addFuel(Items.TNT_MINECART, GeneratorType.EXPLOSIVE, 200, 20 * (2 * 60 + 40))
		addFuel(Tags.Items.GUNPOWDERS, GeneratorType.EXPLOSIVE, 160, 20 * 20)

		addFuel(Items.NETHER_STAR, GeneratorType.NETHER_STAR, 4_000, 20 * 60 * 2)
		addFuel(Items.FIREWORK_STAR, GeneratorType.NETHER_STAR, 20, 20)

		addFuel(Items.DRAGON_BREATH, GeneratorType.HALITOSIS, 40, 20 * 60 * 10) // 10 minutes

		addFuel(Items.ICE, GeneratorType.FROSTY, 40, 20 * 2)
		addFuel(Items.PACKED_ICE, GeneratorType.FROSTY, 40, 20 * 2 * 9)
		addFuel(Items.BLUE_ICE, GeneratorType.FROSTY, 40, 20 * 2 * 9 * 9)
		addFuel(Items.SNOWBALL, GeneratorType.FROSTY, 40, 5)
		addFuel(Items.SNOW_BLOCK, GeneratorType.FROSTY, 40, 20)
		addFuel(Items.SNOW, GeneratorType.FROSTY, 40, 3)

	}

}