package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import java.util.*

data class ItemFilterComponent(
	val itemContents: ItemContainerContents,
	val flags: List<Flag>
) {

	val isInverted = Flag.INVERTED in flags
	val useTags = Flag.USE_TAGS in flags
	val ignoreDamage = Flag.IGNORE_DAMAGE in flags
	val ignoreAllComponents = Flag.IGNORE_ALL_COMPONENTS in flags

	constructor() : this(ItemContainerContents.EMPTY, emptyList())

	fun withFlags(newFlags: List<Flag>): ItemFilterComponent = copy(flags = newFlags)
	fun withItems(newItems: ItemContainerContents): ItemFilterComponent = copy(itemContents = newItems)

	fun getItems(): NonNullList<ItemStack> {
		val list = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
		itemContents.copyInto(list)
		return list
	}

	fun getItem(index: Int): ItemStack {
		if (index !in 0 until CONTAINER_SIZE) return ItemStack.EMPTY
		return getItems()[index]
	}

	fun getWithSetItem(index: Int, itemStack: ItemStack): ItemFilterComponent? {
		if (index !in 0 until CONTAINER_SIZE) return null

		val newItems = getItems()
		newItems[index] = itemStack
		return copy(itemContents = ItemContainerContents.fromItems(newItems))
	}

	private val cache: MutableMap<CacheKey, Boolean> = mutableMapOf()

	fun passesFilter(checkedStack: ItemStack): Boolean {
		if (checkedStack.isEmpty) return isInverted

		val cacheKey = CacheKey(checkedStack)
		val cachedResult = cache[cacheKey]
		if (cachedResult != null) return cachedResult

		val result = checkPassesFilter(checkedStack)
		cache[cacheKey] = result
		return result
	}

	private fun checkPassesFilter(checkedStack: ItemStack): Boolean {
		for (slot in 0 until itemContents.slots) {
			val stackInFilter = itemContents.getStackInSlot(slot)
			if (stackInFilter.isEmpty) continue

			// Allow for nested Item Filters
			// If it passes the nested Filter, return true (unless the outer Filter is inverted)
			if (stackInFilter.isItem(ModItems.ITEM_FILTER)) {
				val passesNestedFilter = ItemFilterItem.Companion.passesFilter(stackInFilter, checkedStack)
				return passesNestedFilter != isInverted
			}

			// If using tags it ignores item + components and just checks if
			// the stack in the filter has any of the tags of the checked stack
			if (useTags) {
				val tagsMatch = stackInFilter.tags.anyMatch { checkedStack.isItem(it) }
				if (tagsMatch) return !isInverted
			}

			// At this point it can only pass it's the same item
			if (!stackInFilter.isItem(checkedStack.item)) continue

			// Everything after this is just comparing components
			// If we're ignoring components, none of that matters
			if (ignoreAllComponents) {
				return isInverted
			}

			// If we're ignoring damage, remove the damage component and compare the rest
			if (ignoreDamage) {
				val matchesNonDamageComponents = isSameComponentsWithoutDamage(stackInFilter, checkedStack)
				return matchesNonDamageComponents != isInverted
			}

			val matchesAllComponents = ItemStack.isSameItemSameComponents(stackInFilter, checkedStack)
			return matchesAllComponents != isInverted
		}

		// To reach here, checkedStack has to match with none of the stacks in the filter,
		// so return false (or true if inverted)

		return isInverted
	}

	companion object {
		const val CONTAINER_SIZE = 16

		val CODEC: Codec<ItemFilterComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ItemContainerContents.CODEC
						.optionalFieldOf("items", ItemContainerContents.EMPTY)
						.forGetter(ItemFilterComponent::itemContents),
					Flag.CODEC.listOf()
						.optionalFieldOf("flags", emptyList())
						.forGetter(ItemFilterComponent::flags)
				).apply(instance, ::ItemFilterComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ItemFilterComponent> =
			StreamCodec.composite(
				ItemContainerContents.STREAM_CODEC, ItemFilterComponent::itemContents,
				Flag.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemFilterComponent::flags,
				::ItemFilterComponent
			)

		private fun isSameComponentsWithoutDamage(leftStack: ItemStack, rightStack: ItemStack): Boolean {
			if (leftStack.item != rightStack.item) return false

			val leftMap = Reference2ObjectOpenHashMap<DataComponentType<*>, Optional<*>>()
			val rightMap = Reference2ObjectOpenHashMap<DataComponentType<*>, Optional<*>>()

			for ((type, value) in leftStack.componentsPatch.entrySet()) {
				if (type === DataComponents.DAMAGE) continue
				leftMap[type] = value
			}

			for ((type, value) in rightStack.componentsPatch.entrySet()) {
				if (type === DataComponents.DAMAGE) continue
				rightMap[type] = value
			}

			return leftMap == rightMap
		}
	}

	data class CacheKey(val item: Item, val componentHash: Int) {
		constructor(stack: ItemStack) : this(stack.item, stack.componentsPatch.hashCode())
	}

	enum class Flag(
		private val id: String,
		private val messageOn: String,
		private val messageOf: String
	) : StringRepresentable {
		INVERTED("inverted", ModMenuLang.INVERTED_ON, ModMenuLang.INVERTED_OFF),
		USE_TAGS("use_tags", ModMenuLang.USE_TAGS_ON, ModMenuLang.USE_TAGS_OFF),
		IGNORE_DAMAGE("ignore_damage", ModMenuLang.IGNORE_DAMAGE_ON, ModMenuLang.IGNORE_DAMAGE_OFF),
		IGNORE_ALL_COMPONENTS("ignore_all_components", ModMenuLang.IGNORE_ALL_COMPONENTS_ON, ModMenuLang.IGNORE_ALL_COMPONENTS_OFF)
		;

		override fun getSerializedName(): String = id

		fun getMessage(isOn: Boolean): MutableComponent {
			val message = if (isOn) messageOn else messageOf
			return Component.translatable(message)
		}

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Flag> =
				StringRepresentable.fromEnum { entries.toTypedArray() }

			val STREAM_CODEC: StreamCodec<ByteBuf, Flag> =
				ByteBufCodecs.fromCodec(CODEC)

		}
	}

}