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

		fun addFuel(item: ItemLike, genType: GeneratorType, burnTime: Int, energy: Int) {
			fuelBuilder.add(
				item.asItem().builtInRegistryHolder(),
				GeneratorFuelDataMap(genType, burnTime, energy),
				false
			)
		}

		fun addFuel(itemTag: TagKey<Item>, genType: GeneratorType, fePerTick: Int, burnTicks: Int) {
			fuelBuilder.add(
				itemTag,
				GeneratorFuelDataMap(genType, fePerTick, burnTicks),
				false
			)
		}

		addFuel(Tags.Items.DYES_PINK, GeneratorType.PINK, 40, 10)
		addFuel(Tags.Items.DYED_PINK, GeneratorType.PINK, 40, 10)

		addFuel(Tags.Items.BONES, GeneratorType.DEATH, 40, 20 * 20)
		addFuel(Tags.Items.STORAGE_BLOCKS_BONE_MEAL, GeneratorType.DEATH, 120, 20 * 20)
		addFuel(Items.BONE_MEAL, GeneratorType.DEATH, 40, 20 * 10)
		addFuel(Items.ROTTEN_FLESH, GeneratorType.DEATH, 20, 20 * 20)
		addFuel(Items.SKELETON_SKULL, GeneratorType.DEATH, 100, 20 * 20)
		addFuel(Items.WITHER_SKELETON_SKULL, GeneratorType.DEATH, 150, 20 * 20)

		addFuel(Items.TNT, GeneratorType.EXPLOSIVE, 160, 20 * (2 * 60 + 40)) // 2:40
		addFuel(Items.TNT_MINECART, GeneratorType.EXPLOSIVE, 200, 20 * (2 * 60 + 40))
		addFuel(Tags.Items.GUNPOWDERS, GeneratorType.EXPLOSIVE, 160, 20 * 20)

		addFuel(Items.NETHER_STAR, GeneratorType.NETHER_STAR, 4_000, 20 * 60 * 2)
		addFuel(Items.FIREWORK_STAR, GeneratorType.NETHER_STAR, 20, 20)

	}

}