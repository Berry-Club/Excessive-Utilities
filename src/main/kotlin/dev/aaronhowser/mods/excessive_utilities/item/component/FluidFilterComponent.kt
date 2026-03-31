package dev.aaronhowser.mods.excessive_utilities.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

data class FluidFilterComponent(
	val itemContents: ItemContainerContents,
	val flags: List<Flag>
) {

	val isInverted: Boolean = Flag.INVERTED in flags
	val useTags: Boolean = Flag.USE_TAGS in flags
	val ignoreAllComponents: Boolean = Flag.IGNORE_ALL_COMPONENTS in flags

	constructor() : this(ItemContainerContents.EMPTY, emptyList())

	fun withFlags(newFlags: List<Flag>): FluidFilterComponent = copy(flags = newFlags)
	fun withItems(newItems: ItemContainerContents): FluidFilterComponent = copy(itemContents = newItems)

	fun getItems(): NonNullList<ItemStack> {
		val list = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
		itemContents.copyInto(list)
		return list
	}

	fun getItem(index: Int): ItemStack {
		if (index !in 0 until CONTAINER_SIZE) return ItemStack.EMPTY
		return itemContents.getStackInSlot(index)
	}

	fun withSetItem(index: Int, itemStack: ItemStack): FluidFilterComponent {
		if (index !in 0 until CONTAINER_SIZE) return this

		val newList = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
		itemContents.copyInto(newList)
		newList[index] = itemStack

		return copy(itemContents = ItemContainerContents.fromItems(newList))
	}

	private val cache: MutableMap<CacheKey, Boolean> = mutableMapOf()

	fun passesFilter(checkedStack: FluidStack): Boolean {
		if (checkedStack.isEmpty) return isInverted

		val cacheKey = CacheKey(checkedStack)
		val cachedResult = cache[cacheKey]
		if (cachedResult != null) return cachedResult

		val result = checkPassesFilter(checkedStack)
		cache[cacheKey] = result
		return result
	}

	private fun checkPassesFilter(checkedStack: FluidStack): Boolean {
		for (slot in 0 until itemContents.slots) {
			val stackInFilter = itemContents.getStackInSlot(slot)
			if (stackInFilter.isEmpty) continue

			if (stackInFilter.isItem(ModItems.FLUID_FILTER)) {
				val passesNestedFilter = passesFilter(checkedStack)
				return passesNestedFilter != isInverted
			}

			val filterFluid = FluidUtil.getFluidContained(stackInFilter).orElse(FluidStack.EMPTY)
			if (filterFluid.isEmpty) continue

			if (useTags) {
				val tagsMatch = filterFluid.tags.anyMatch { checkedStack.isFluid(it) }
				if (tagsMatch) return !isInverted
			}

			if (ignoreAllComponents) {
				val sameFluid = FluidStack.isSameFluid(filterFluid, checkedStack)
				if (sameFluid) return !isInverted
			}

			val sameWithComponent = FluidStack.isSameFluidSameComponents(filterFluid, checkedStack)
			if (sameWithComponent) return !isInverted
		}

		// To reach here, checkedStack has to match with none of the stacks in the filter,
		// so return false (or true if inverted)

		return isInverted
	}

	companion object {
		const val CONTAINER_SIZE = 16

		val CODEC: Codec<FluidFilterComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ItemContainerContents.CODEC
						.optionalFieldOf("items", ItemContainerContents.EMPTY)
						.forGetter(FluidFilterComponent::itemContents),
					Flag.CODEC.listOf()
						.optionalFieldOf("flags", emptyList())
						.forGetter(FluidFilterComponent::flags)
				).apply(instance, ::FluidFilterComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, FluidFilterComponent> =
			StreamCodec.composite(
				ItemContainerContents.STREAM_CODEC, FluidFilterComponent::itemContents,
				Flag.STREAM_CODEC.apply(ByteBufCodecs.list()), FluidFilterComponent::flags,
				::FluidFilterComponent
			)
	}

	data class CacheKey(val item: Fluid, val componentHash: Int) {
		constructor(stack: FluidStack) : this(stack.fluid, stack.componentsPatch.hashCode())
	}

	enum class Flag(
		private val id: String,
		private val messageOn: String,
		private val messageOf: String
	) : StringRepresentable {
		INVERTED("inverted", ModMenuLang.INVERTED_ON, ModMenuLang.INVERTED_OFF),
		USE_TAGS("use_tags", ModMenuLang.USE_TAGS_ON, ModMenuLang.USE_TAGS_OFF),
		IGNORE_ALL_COMPONENTS("ignore_all_components", ModMenuLang.IGNORE_ALL_COMPONENTS_ON, ModMenuLang.IGNORE_ALL_COMPONENTS_OFF),
		;

		val bit: Int = 1 shl ordinal

		override fun getSerializedName(): String = id

		fun getMessage(isOn: Boolean): MutableComponent {
			val message = if (isOn) messageOn else messageOf
			return Component.translatable(message).withStyle(ChatFormatting.GRAY)
		}

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Flag> =
				StringRepresentable.fromEnum { entries.toTypedArray() }

			val STREAM_CODEC: StreamCodec<ByteBuf, Flag> =
				ByteBufCodecs.fromCodec(CODEC)

		}
	}

}