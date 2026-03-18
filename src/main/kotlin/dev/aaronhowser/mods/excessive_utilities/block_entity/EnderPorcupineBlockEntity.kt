package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toBlockPos
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine_menu.EnderPorcupineMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import kotlin.math.absoluteValue

class EnderPorcupineBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(ModBlockEntityTypes.ENDER_PORCUPINE.get(), pos, blockState), MenuProvider {

	var minimumOffset: BlockPos = BlockPos(-2, -2, -2)
		set(value) {
			field = value
			setChanged()
			updateOffsets()
		}

	var maximumOffset: BlockPos = BlockPos(2, 2, 2)
		set(value) {
			field = value
			setChanged()
			updateOffsets()
		}

	var allOffsets: List<BlockPos> = emptyList()
		private set

	init {
		updateOffsets()
	}

	private fun updateOffsets() {
		allOffsets = BlockPos.betweenClosed(minimumOffset, maximumOffset)
			.map(BlockPos::immutable)
			.sortedByDescending(BlockPos::getY)
	}

	// Every second, move to the next offset in the list
	fun getCurrentOffset(): Vec3i {
		val level = level ?: return Vec3i.ZERO

		val volume = allOffsets.size
		if (volume == 0) return Vec3i.ZERO

		val ticksPerStep = 20
		val step = (level.gameTime / ticksPerStep).toInt()
		val index = step % volume

		return allOffsets[index]
	}

	fun getLinkedPosition(): BlockPos = blockPos.offset(getCurrentOffset())

	override fun getGpUsage(): Double {
		val offset = getCurrentOffset()
		return offset.x.absoluteValue + offset.y.absoluteValue + offset.z.absoluteValue.toDouble()
	}

	fun getItemHandler(direction: Direction?): IItemHandler? {
		val stateThere = level?.getBlockState(getLinkedPosition())
		if (stateThere?.isBlock(ModBlockTagsProvider.ENDER_PORCUPINE_BLACKLIST).isTrue()) return null

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		return level?.getCapability(Capabilities.ItemHandler.BLOCK, getLinkedPosition(), direction)
	}

	fun getFluidHandler(direction: Direction?): IFluidHandler? {
		val stateThere = level?.getBlockState(getLinkedPosition())
		if (stateThere?.isBlock(ModBlockTagsProvider.ENDER_PORCUPINE_BLACKLIST).isTrue()) return null


		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		return level?.getCapability(Capabilities.FluidHandler.BLOCK, getLinkedPosition(), direction)
	}

	fun getEnergyHandler(direction: Direction?): IEnergyStorage? {
		val stateThere = level?.getBlockState(getLinkedPosition())
		if (stateThere?.isBlock(ModBlockTagsProvider.ENDER_PORCUPINE_BLACKLIST).isTrue()) return null

		@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
		return level?.getCapability(Capabilities.EnergyStorage.BLOCK, getLinkedPosition(), direction)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putLong(MINIMUM_OFFSET_NBT, minimumOffset.asLong())
		tag.putLong(MAXIMUM_OFFSET_NBT, maximumOffset.asLong())
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		minimumOffset = tag.getLong(MINIMUM_OFFSET_NBT).toBlockPos()
		maximumOffset = tag.getLong(MAXIMUM_OFFSET_NBT).toBlockPos()
	}

	private val containerData =
		object : ContainerData {
			override fun getCount(): Int = CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					MIN_X_DATA_INDEX -> minimumOffset.x
					MIN_Y_DATA_INDEX -> minimumOffset.y
					MIN_Z_DATA_INDEX -> minimumOffset.z
					MAX_X_DATA_INDEX -> maximumOffset.x
					MAX_Y_DATA_INDEX -> maximumOffset.y
					MAX_Z_DATA_INDEX -> maximumOffset.z
					CURRENT_OFFSET_X_DATA_INDEX -> getCurrentOffset().x
					CURRENT_OFFSET_Y_DATA_INDEX -> getCurrentOffset().y
					CURRENT_OFFSET_Z_DATA_INDEX -> getCurrentOffset().z
					else -> 0
				}
			}

			override fun set(index: Int, value: Int) {
				when (index) {
					MIN_X_DATA_INDEX -> minimumOffset = BlockPos(value, minimumOffset.y, minimumOffset.z)
					MIN_Y_DATA_INDEX -> minimumOffset = BlockPos(minimumOffset.x, value, minimumOffset.z)
					MIN_Z_DATA_INDEX -> minimumOffset = BlockPos(minimumOffset.x, minimumOffset.y, value)
					MAX_X_DATA_INDEX -> maximumOffset = BlockPos(value, maximumOffset.y, maximumOffset.z)
					MAX_Y_DATA_INDEX -> maximumOffset = BlockPos(maximumOffset.x, value, maximumOffset.z)
					MAX_Z_DATA_INDEX -> maximumOffset = BlockPos(maximumOffset.x, maximumOffset.y, value)
				}
			}
		}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return EnderPorcupineMenu(containerId, containerData)
	}

	// Syncs with client
	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	// Update immediately
	override fun setChanged() {
		super.setChanged()
		level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL_IMMEDIATE)
	}

	companion object {
		const val MINIMUM_OFFSET_NBT = "MinimumOffset"
		const val MAXIMUM_OFFSET_NBT = "MaximumOffset"

		const val CONTAINER_DATA_SIZE = 9
		const val MIN_X_DATA_INDEX = 0
		const val MIN_Y_DATA_INDEX = 1
		const val MIN_Z_DATA_INDEX = 2
		const val MAX_X_DATA_INDEX = 3
		const val MAX_Y_DATA_INDEX = 4
		const val MAX_Z_DATA_INDEX = 5
		const val CURRENT_OFFSET_X_DATA_INDEX = 6
		const val CURRENT_OFFSET_Y_DATA_INDEX = 7
		const val CURRENT_OFFSET_Z_DATA_INDEX = 8
	}

}