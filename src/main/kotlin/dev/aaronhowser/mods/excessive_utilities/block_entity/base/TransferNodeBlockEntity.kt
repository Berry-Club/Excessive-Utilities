package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.util.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.util.TransferNodePing
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class TransferNodeBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer, MenuProvider {

	protected val placedOnDirection: Direction = this.blockState.getValue(TransferNodeBlock.PLACED_ON)
	protected val placedOnPos: BlockPos = blockPos.relative(placedOnDirection)

	protected val ping = TransferNodePing(blockPos, placedOnDirection)

	var isRetrieval: Boolean = false
		set(value) {
			field = value
			setChanged()
		}

	protected var didWorkThisTick: Boolean = false

	protected val upgradeContainer: ImprovedSimpleContainer =
		object : ImprovedSimpleContainer(this, UPGRADE_CONTAINER_SIZE) {
			override fun canAddItem(stack: ItemStack): Boolean {
				val tag = if (isRetrieval) {
					ModItemTagsProvider.RETRIEVAL_NODE_UPGRADES
				} else {
					ModItemTagsProvider.TRANSFER_NODE_UPGRADES
				}

				return stack.isItem(tag)
			}
		}

	override fun getContainers(): List<Container> {
		return listOf(upgradeContainer)
	}

	protected fun getSpeedUpgradeCount(): Int {
		var count = 0

		for (stack in upgradeContainer.items) {
			if (stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)) {
				count += stack.count
			}
		}

		return count
	}

	override fun getGpUsage(): Double {
		if (!didWorkThisTick) return 0.0

		var usage = 0.0

		for (stack in upgradeContainer.items) {
			if (stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)) {
				usage += SpeedUpgradeItem.getGpCost(stack.count)
				continue
			}

		}

		return usage
	}

	protected var cooldown = 20

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		val isOverloaded = isOverloaded() && getGpUsage() > 0.0
		didWorkThisTick = false
		if (isOverloaded) return

		cooldown -= 1 + getSpeedUpgradeCount()

		while (cooldown <= 0) {
			activeTick(level)
			cooldown += 20
		}
	}

	protected open fun activeTick(level: ServerLevel) {
		if (isRetrieval) {
			pullerTick(level)
		} else {
			pusherTick(level)
		}

		spawnParticles(level)
	}

	protected open fun spawnParticles(level: ServerLevel) {
		if (!didWorkThisTick) return

		val pingPos = ping.currentPingPos

		for (i in 0 until 5) {
			val x = pingPos.x + 0.5 + level.random.nextRange(-0.5, 0.5)
			val y = pingPos.y + 0.5 + level.random.nextRange(-0.5, 0.5)
			val z = pingPos.z + 0.5 + level.random.nextRange(-0.5, 0.5)

			level.sendParticles(
				DustParticleOptions.REDSTONE,
				x, y, z,
				1,
				0.0, 0.0, 0.0,
				0.0
			)
		}
	}

	abstract fun pullerTick(level: ServerLevel)
	abstract fun pusherTick(level: ServerLevel)

	override fun getDisplayName(): Component = blockState.block.name

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putBoolean(IS_RETRIEVAL_NBT, isRetrieval)

		if (!upgradeContainer.isEmpty) {
			val upgradeTag = CompoundTag()
			upgradeTag.saveItems(upgradeContainer, registries)
			tag.put(UPGRADES_NBT, upgradeTag)
		}

	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		isRetrieval = tag.getBoolean(IS_RETRIEVAL_NBT)

		if (tag.contains(UPGRADES_NBT)) {
			val upgradeTag = tag.getCompound(UPGRADES_NBT)
			upgradeTag.loadItems(upgradeContainer, registries)
		}
	}

	companion object {
		const val UPGRADE_CONTAINER_SIZE = 6

		const val UPGRADES_NBT = "Upgrades"
		const val IS_RETRIEVAL_NBT = "IsRetrieval"
	}

}