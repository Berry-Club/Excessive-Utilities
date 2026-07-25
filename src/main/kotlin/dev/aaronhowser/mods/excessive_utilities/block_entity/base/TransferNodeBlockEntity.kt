package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.item.EnderFrequencyItem
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import dev.aaronhowser.mods.excessive_utilities.handler.ender_frequency.EnderFrequencyNetwork
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class TransferNodeBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer, MenuProvider {

	val nodeType: TransferNodeBlock.Type
		get() = (blockState.block as TransferNodeBlock).type

	protected val placedOnDirection: Direction = this.blockState.getValue(TransferNodeBlock.PLACED_ON)
	protected val placedOnPos: BlockPos = blockPos.relative(placedOnDirection)

	protected var ping = TransferNodePing.SearchMode.RANDOM.create(blockPos, placedOnDirection)
		private set

	var isRetrieval: Boolean = false
		set(value) {
			field = value
			setChanged()
		}

	protected var didWorkThisTick: Boolean = false

	protected val upgradeContainer: ImprovedSimpleContainer =
		object : ImprovedSimpleContainer(this, UPGRADE_CONTAINER_SIZE) {
			override fun canAddItem(stack: ItemStack): Boolean {
				if (!super.canAddItem(stack)) return false
				return canPlaceUpgrade(stack)
			}

			override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
				return canPlaceUpgrade(stack)
			}

			override fun getMaxStackSize(stack: ItemStack): Int {
				if (isSearchUpgrade(stack)) return 1
				return super.getMaxStackSize(stack)
			}
		}

	private var registeredReceiverFrequency: EnderFrequencyComponent? = null

	private fun canPlaceUpgrade(stack: ItemStack): Boolean {
		val tag = if (isRetrieval) {
			ModItemTagsProvider.RETRIEVAL_NODE_UPGRADES
		} else {
			ModItemTagsProvider.TRANSFER_NODE_UPGRADES
		}

		if (!stack.isItem(tag)) return false

		if (stack.item is EnderFrequencyItem) {
			for (upgradeStack in upgradeContainer.items) {
				if (upgradeStack.item is EnderFrequencyItem) return false
			}
		}

		if (stack.isItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE)) {
			return upgradeContainer.countItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE.get()) == 0
					&& upgradeContainer.countItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE.get()) == 0
		}

		if (stack.isItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE)) {
			return upgradeContainer.countItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE.get()) == 0
					&& upgradeContainer.countItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE.get()) == 0
		}

		return true
	}

	private fun isSearchUpgrade(stack: ItemStack): Boolean {
		return stack.isItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE)
				|| stack.isItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE)
	}

	override fun getContainers(): List<Container> {
		return listOf(upgradeContainer)
	}

	protected fun hasStackUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.STACK_UPGRADE.get()) > 0
	}

	protected fun hasCreativeUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.CREATIVE_UPGRADE.get()) > 0
	}

	protected fun hasPseudoRoundRobinUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.PSEUDO_ROUND_ROBIN_UPGRADE.get()) > 0
	}

	protected fun hasDepthFirstSearchUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE.get()) > 0
	}

	protected fun hasBreadthFirstSearchUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE.get()) > 0
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
		refreshEnderFrequencyRegistration()
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

	override fun onLoad() {
		super.onLoad()
		refreshEnderFrequencyRegistration()
	}

	override fun setRemoved() {
		unregisterEnderReceiver()
		super.setRemoved()
	}

	private fun refreshEnderFrequencyRegistration() {
		val serverLevel = level as? ServerLevel ?: return
		val current = getEnderFrequency(EnderFrequencyItem.Role.RECEIVER).takeIf { isRetrieval }
		if (current == registeredReceiverFrequency) return

		unregisterEnderReceiver()
		if (current != null) {
			EnderFrequencyNetwork.get(serverLevel.server).registerReceiver(this, current)
			registeredReceiverFrequency = current
		}
	}

	private fun unregisterEnderReceiver() {
		val frequency = registeredReceiverFrequency ?: return
		val serverLevel = level as? ServerLevel
		if (serverLevel != null) {
			EnderFrequencyNetwork.get(serverLevel.server).unregisterReceiver(this, frequency)
		}

		registeredReceiverFrequency = null
	}

	private fun getTransmitterFrequency(): EnderFrequencyComponent? {
		if (isRetrieval) return null
		return getEnderFrequency(EnderFrequencyItem.Role.TRANSMITTER)
	}

	protected fun hasConfiguredTransmitter(): Boolean {
		return getTransmitterFrequency() != null
	}

	protected fun visitEnderReceivers(
		level: ServerLevel,
		visitor: (TransferNodeBlockEntity) -> Boolean
	): Boolean {
		val frequency = getTransmitterFrequency() ?: return false
		return EnderFrequencyNetwork.get(level.server)
			.visitReceivers(level.server, frequency, nodeType, visitor)
	}

	protected fun hasConfiguredReceiver(): Boolean {
		return isRetrieval && getEnderFrequency(EnderFrequencyItem.Role.RECEIVER) != null
	}

	private fun getEnderFrequency(role: EnderFrequencyItem.Role): EnderFrequencyComponent? {
		for (stack in upgradeContainer.items) {
			val item = stack.item as? EnderFrequencyItem ?: continue
			if (item.role != role) continue
			return stack.get(ModDataComponents.ENDER_FREQUENCY)
		}

		return null
	}

	fun isReceiverFor(frequency: EnderFrequencyComponent, type: TransferNodeBlock.Type): Boolean {
		return isRetrieval && nodeType == type
				&& getEnderFrequency(EnderFrequencyItem.Role.RECEIVER) == frequency
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

	private fun pullerTick(level: ServerLevel) {
		pushIntoParent(level)

		if (getBufferAmount() > 0) {
			if (!hasPseudoRoundRobinUpgrade()) {
				ping.reset()
			}
			return
		}

		pullFromPingPos(level)

		if (getBufferAmount() <= 0 || hasPseudoRoundRobinUpgrade()) {
			updatePingSearchMode()
			ping.march(level)
		}
	}

	private fun pusherTick(level: ServerLevel) {
		pullFromParent(level)

		if (getBufferAmount() <= 0) {
			if (!hasPseudoRoundRobinUpgrade()) {
				ping.reset()
			}
			return
		}

		val amountBefore = getBufferAmount()
		pushIntoPingPos(level)

		if (getBufferAmount() == amountBefore || hasPseudoRoundRobinUpgrade()) {
			updatePingSearchMode()
			ping.march(level)
		}
	}

	private fun updatePingSearchMode() {
		val searchMode = when {
			hasBreadthFirstSearchUpgrade() -> TransferNodePing.SearchMode.BREADTH_FIRST
			hasDepthFirstSearchUpgrade() || hasPseudoRoundRobinUpgrade() -> TransferNodePing.SearchMode.DEPTH_FIRST
			else -> TransferNodePing.SearchMode.RANDOM
		}

		if (ping.searchMode == searchMode) return
		ping = searchMode.create(blockPos, placedOnDirection)
	}

	protected abstract fun getBufferAmount(): Int
	protected abstract fun pullFromParent(level: ServerLevel)
	protected abstract fun pushIntoParent(level: ServerLevel)
	protected abstract fun pullFromPingPos(level: ServerLevel)
	protected abstract fun pushIntoPingPos(level: ServerLevel)

	protected fun <T : Any> getCapabilitiesAroundPing(
		level: ServerLevel,
		getCapability: (BlockPos, Direction) -> T?
	): List<T> {
		val capabilities = mutableListOf<T>()
		for (direction in ping.getNextDirections(level)) {
			val neighborPos = ping.currentPingPos.relative(direction)
			val capability = getCapability(neighborPos, direction.opposite) ?: continue
			capabilities.add(capability)
		}

		return capabilities
	}

	protected open fun getContainerDataCount(): Int = PING_CONTAINER_DATA_SIZE

	protected open fun getContainerData(index: Int): Int {
		return when (index) {
			X_DATA_INDEX -> ping.currentPingPos.x
			Y_DATA_INDEX -> ping.currentPingPos.y
			Z_DATA_INDEX -> ping.currentPingPos.z
			else -> 0
		}
	}

	protected val containerData: ContainerData =
		object : ContainerData {
			override fun getCount(): Int = getContainerDataCount()

			override fun get(index: Int): Int = getContainerData(index)

			override fun set(index: Int, value: Int) {
			}
		}

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
		const val PING_CONTAINER_DATA_SIZE = 3

		const val X_DATA_INDEX = 0
		const val Y_DATA_INDEX = 1
		const val Z_DATA_INDEX = 2

		const val UPGRADES_NBT = "Upgrades"
		const val IS_RETRIEVAL_NBT = "IsRetrieval"
	}

}