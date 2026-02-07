package dev.aaronhowser.mods.excessive_utilities.datagen

import dev.aaronhowser.mods.excessive_utilities.datamap.GeneratorFuelDataMap
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

		fun addFuel(item: ItemLike, genType: String, burnTime: Int, energy: Int) {
			fuelBuilder.add(
				item.asItem().builtInRegistryHolder(),
				GeneratorFuelDataMap(genType, burnTime, energy),
				false
			)
		}

		fun addFuel(itemTag: TagKey<Item>, genType: String, fePerTick: Int, burnTicks: Int) {
			fuelBuilder.add(
				itemTag,
				GeneratorFuelDataMap(genType, fePerTick, burnTicks),
				false
			)
		}

		addFuel(Tags.Items.DYES_PINK, "pink", 200, 200)
		addFuel(Tags.Items.DYED_PINK, "pink", 150, 200)
		addFuel(Tags.Items.BONES, "death", 100, 200)
		addFuel(Items.BONE_MEAL, "death", 100, 200)
		addFuel(Items.BONE_BLOCK, "death", 100, 200)
		addFuel(Items.ROTTEN_FLESH, "death", 50, 200)

	}

}