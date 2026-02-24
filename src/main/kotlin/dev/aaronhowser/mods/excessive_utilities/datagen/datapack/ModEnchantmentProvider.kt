package dev.aaronhowser.mods.excessive_utilities.datagen.datapack

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEnchantmentTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment

//TODO: Improve numbers
object ModEnchantmentProvider {

	private fun createRk(name: String): ResourceKey<Enchantment> =
		ResourceKey.create(Registries.ENCHANTMENT, ExcessiveUtilities.modResource(name))

	val BLADERANG: ResourceKey<Enchantment> = createRk("bladerang")
	val BOOMEREAPERANG: ResourceKey<Enchantment> = createRk("boomereaperang")
	val BURNERANG: ResourceKey<Enchantment> = createRk("burnerang")
	val KABOOMERANG: ResourceKey<Enchantment> = createRk("kaboomerang")
	val ZOOMERANG: ResourceKey<Enchantment> = createRk("zoomerang")

	fun bootstrap(context: BootstrapContext<Enchantment>) {
		val itemGetter = context.lookup(Registries.ITEM)
		val enchantmentGetter = context.lookup(Registries.ENCHANTMENT)

		context.register(
			BLADERANG,
			Enchantment.enchantment(
				Enchantment.definition(
					itemGetter.getOrThrow(ModItemTagsProvider.MAGICAL_BOOMERANG_ENCHANTABLE),
					1,
					5,
					Enchantment.dynamicCost(4, 0),
					Enchantment.dynamicCost(150, 0),
					1,
					EquipmentSlotGroup.HAND
				)
			).build(BLADERANG.location())
		)


		context.register(
			BOOMEREAPERANG,
			Enchantment.enchantment(
				Enchantment.definition(
					itemGetter.getOrThrow(ModItemTagsProvider.MAGICAL_BOOMERANG_ENCHANTABLE),
					1,
					1,
					Enchantment.dynamicCost(4, 0),
					Enchantment.dynamicCost(150, 0),
					1,
					EquipmentSlotGroup.HAND
				)
			)
				.exclusiveWith(enchantmentGetter.getOrThrow(ModEnchantmentTagsProvider.BOOMERANG_BLOCK_INTERACTION))
				.build(BOOMEREAPERANG.location())
		)

		context.register(
			BURNERANG,
			Enchantment.enchantment(
				Enchantment.definition(
					itemGetter.getOrThrow(ModItemTagsProvider.MAGICAL_BOOMERANG_ENCHANTABLE),
					1,
					1,
					Enchantment.dynamicCost(4, 0),
					Enchantment.dynamicCost(150, 0),
					1,
					EquipmentSlotGroup.HAND
				)
			).build(BURNERANG.location())
		)

		context.register(
			KABOOMERANG,
			Enchantment.enchantment(
				Enchantment.definition(
					itemGetter.getOrThrow(ModItemTagsProvider.MAGICAL_BOOMERANG_ENCHANTABLE),
					1,
					3,
					Enchantment.dynamicCost(4, 0),
					Enchantment.dynamicCost(150, 0),
					1,
					EquipmentSlotGroup.HAND
				)
			)
				.exclusiveWith(enchantmentGetter.getOrThrow(ModEnchantmentTagsProvider.BOOMERANG_BLOCK_INTERACTION))
				.build(KABOOMERANG.location())
		)

		context.register(
			ZOOMERANG,
			Enchantment.enchantment(
				Enchantment.definition(
					itemGetter.getOrThrow(ModItemTagsProvider.MAGICAL_BOOMERANG_ENCHANTABLE),
					1,
					3,
					Enchantment.dynamicCost(4, 0),
					Enchantment.dynamicCost(150, 0),
					1,
					EquipmentSlotGroup.HAND
				)
			).build(ZOOMERANG.location())
		)

	}

}