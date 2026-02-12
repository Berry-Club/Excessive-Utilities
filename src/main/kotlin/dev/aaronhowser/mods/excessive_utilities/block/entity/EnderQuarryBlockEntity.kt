package dev.aaronhowser.mods.excessive_utilities.block.entity

import com.mojang.authlib.GameProfile
import dev.aaronhowser.mods.aaron.entity.BetterFakePlayerFactory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.event.EventHooks
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.filter

//TODO:
// Block States for inactive, active, and finished
class EnderQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	private val energyStorage = EnergyStorage(1_000_000)

	private var uuid: UUID? = null
	private var fakePlayer: WeakReference<FakePlayer>? = null

	var minPos: BlockPos? = null
		private set

	var maxPos: BlockPos? = null
		private set

	var targetPos: BlockPos? = null
		private set

	var boundaryType: BoundaryType? = null
		private set

	private fun initFakePlayer() {
		val level = level as? ServerLevel ?: return

		if (this.uuid == null) {
			this.uuid = UUID.randomUUID()
			setChanged()
		}

		val gameProfile = GameProfile(this.uuid, "EU_EnderQuarry")
		val fakePlayer = BetterFakePlayerFactory.get(level, gameProfile) { EnderQuarryFakePlayer(level, gameProfile) }

		fakePlayer.isSilent = true
		fakePlayer.setOnGround(true)

		this.fakePlayer = WeakReference(fakePlayer)
		setChanged()
	}

	fun trySetBoundaries(level: ServerLevel) {
		val horizontals = Direction.Plane.HORIZONTAL

		for (dir in horizontals) {
			val posThere = worldPosition.relative(dir)
			val stateThere = level.getBlockState(posThere)

			if (stateThere.isBlock(BlockTags.FENCES) && trySetBoundariesFromFences(level, posThere)) {
				boundaryType = BoundaryType.FENCE
				setChanged()
				return
			}

			if (stateThere.isBlock(ModBlocks.ENDER_MARKER) && trySetBoundariesFromMarkers(level, posThere)) {
				boundaryType = BoundaryType.MARKER
				setChanged()
				return
			}
		}

		minPos = null
		maxPos = null
		targetPos = null
		boundaryType = null

		setChanged()
	}

	private fun trySetBoundariesFromFences(level: ServerLevel, fencePos: BlockPos): Boolean {
		val firstFenceState = level.getBlockState(fencePos)
		if (!firstFenceState.isBlock(BlockTags.FENCES)) return false

		fun isValidFence(level: Level, checkedPos: BlockPos): Boolean {
			val state = level.getBlockState(checkedPos)
			if (!state.isBlock(BlockTags.FENCES)) return false

			val hasProperties = state.hasProperty(FenceBlock.NORTH)
					&& state.hasProperty(FenceBlock.EAST)
					&& state.hasProperty(FenceBlock.SOUTH)
					&& state.hasProperty(FenceBlock.WEST)

			if (!hasProperties) return false

			val adjacentFences = Direction.Plane.HORIZONTAL
				.count { dir ->
					val stateThere = level.getBlockState(checkedPos.relative(dir))
					stateThere.isBlock(BlockTags.FENCES)
				}

			return adjacentFences == 2

		}

		val dirToProperty = mapOf(
			Direction.NORTH to FenceBlock.NORTH,
			Direction.EAST to FenceBlock.EAST,
			Direction.SOUTH to FenceBlock.SOUTH,
			Direction.WEST to FenceBlock.WEST
		)

		val firstDirection = dirToProperty
			.entries
			.firstOrNull { (dir, property) -> firstFenceState.getValue(property) }
			?.key
			?: return false

		var currentDirection = firstDirection
		var amountTurns = 0

		val corners = mutableListOf(fencePos)

		val currentPos = fencePos.mutable()
		var iterations = 0

		val maxFences = ServerConfig.CONFIG.enderQuarryFencePerimeterLimit.get()

		while (iterations < maxFences) {
			iterations++

			currentPos.move(currentDirection)
			val currentState = level.getBlockState(currentPos)

			if (!isValidFence(level, currentPos)) return false

			val property = dirToProperty[currentDirection] ?: return false
			val canKeepGoing = currentState.getValue(property)

			if (!canKeepGoing) {
				val nextDirection = dirToProperty
					.entries
					.firstOrNull { (dir, property) ->
						dir != currentDirection.opposite && currentState.getValue(property)
					}
					?.key
					?: return false

				currentDirection = nextDirection
				amountTurns++

				if (amountTurns > 4) return false

				corners.add(currentPos.immutable())
			}

			if (currentPos == fencePos) break
		}

		if (corners.size < 4) return false

		val minX = corners.minOf(BlockPos::getX)
		val minZ = corners.minOf(BlockPos::getZ)
		val maxX = corners.maxOf(BlockPos::getX)
		val maxZ = corners.maxOf(BlockPos::getZ)

		if (minX == maxX || minZ == maxZ) return false
		if (minX + 1 == maxX || minZ + 1 == maxZ) return false

		val y = blockPos.y

		val min = BlockPos(minX, y, minZ)
		val max = BlockPos(maxX, y, maxZ)

		minPos = min
		maxPos = max

		targetPos = min.offset(1, 0, 1)
		return true
	}

	private fun trySetBoundariesFromMarkers(level: ServerLevel, markerPos: BlockPos): Boolean {
		val firstMarkerState = level.getBlockState(markerPos)
		if (!firstMarkerState.isBlock(ModBlocks.ENDER_MARKER)) return false

		val markers = mutableSetOf(markerPos)

		val searchDistance = ServerConfig.CONFIG.enderQuarryMarkerSearchDistance.get()

		val horizontals = Direction.Plane.HORIZONTAL
		for (dir in horizontals) {
			val checkPos = markerPos.relative(dir).mutable()
			var locationsChecked = 0

			while (locationsChecked < searchDistance) {
				locationsChecked++
				checkPos.move(dir)

				val checkState = level.getBlockState(checkPos)
				if (checkState.isBlock(ModBlocks.ENDER_MARKER)) {
					markers.add(checkPos.immutable())
					break
				}
			}

			if (markers.size == 3) break
		}

		if (markers.size < 3) return false

		val minX = markers.minOf(BlockPos::getX)
		val minZ = markers.minOf(BlockPos::getZ)
		val maxX = markers.maxOf(BlockPos::getX)
		val maxZ = markers.maxOf(BlockPos::getZ)

		if (minX == maxX || minZ == maxZ) return false
		if (minX + 1 == maxX || minZ + 1 == maxZ) return false

		val y = blockPos.y

		val min = BlockPos(minX, y, minZ)
		val max = BlockPos(maxX, y, maxZ)

		minPos = min
		maxPos = max

		targetPos = min.offset(1, 0, 1)
		return true
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(STORED_ENERGY_NBT, energyStorage.energyStored)

		val min = minPos
		val max = maxPos

		if (min != null && max != null) {
			tag.putLong(MIN_POS_NBT, min.asLong())
			tag.putLong(MAX_POS_NBT, max.asLong())
		}

		val target = targetPos
		if (target != null) {
			tag.putLong(TARGET_POS_NBT, target.asLong())
		}

		val bType = boundaryType
		if (bType != null) {
			tag.putString(BOUNDARY_TYPE_NBT, bType.id)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		val storedEnergyTag = tag.get(STORED_ENERGY_NBT)
		if (storedEnergyTag is IntTag) {
			energyStorage.deserializeNBT(registries, storedEnergyTag)
		}

		if (tag.contains(MIN_POS_NBT) && tag.contains(MAX_POS_NBT)) {
			minPos = BlockPos.of(tag.getLong(MIN_POS_NBT))
			maxPos = BlockPos.of(tag.getLong(MAX_POS_NBT))
		}

		if (tag.contains(TARGET_POS_NBT)) {
			targetPos = BlockPos.of(tag.getLong(TARGET_POS_NBT))
		}

		if (tag.contains(BOUNDARY_TYPE_NBT)) {
			val bTypeString = tag.getString(BOUNDARY_TYPE_NBT)
			boundaryType = BoundaryType.entries.firstOrNull { it.id == bTypeString }
		}
	}

	// Syncs with client
	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	override fun setChanged() {
		super.setChanged()
		level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL_IMMEDIATE)
	}

	companion object {
		const val MIN_POS_NBT = "MinPos"
		const val MAX_POS_NBT = "MaxPos"
		const val TARGET_POS_NBT = "TargetPos"
		const val STORED_ENERGY_NBT = "StoredEnergy"
		const val BOUNDARY_TYPE_NBT = "BoundaryType"
	}

	enum class BoundaryType(val id: String) : StringRepresentable {
		FENCE("fence"),
		MARKER("marker")
		;

		override fun getSerializedName(): String = id
	}

	class EnderQuarryFakePlayer(level: ServerLevel, gameProfile: GameProfile) : FakePlayer(level, gameProfile) {

		override fun take(entity: Entity, quantity: Int) {
			// Super would try to send a packet to everyone nearby, which is bad
		}

		// FakePlayer doesn't normally have access to Mining Efficiency and Block Break Speed attributes
		// so we have to manually apply them here
		// Block Breakers don't get potion effects, so we don't need to worry about that
		// Same for being under water or in the air, etc
		override fun getDigSpeed(state: BlockState, pos: BlockPos?): Float {
			var f = this.inventory.getDestroySpeed(state)

			if (f > 1f) {
				f += getStackAttributeValue(this.mainHandItem, Attributes.MINING_EFFICIENCY, registryAccess(), 0f).toFloat()
			}

			f *= getStackAttributeValue(this.mainHandItem, Attributes.BLOCK_BREAK_SPEED, registryAccess(), 1f).toFloat()

			@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
			f = EventHooks.getBreakSpeed(this, state, f, pos)

			return f
		}

		companion object {
			const val NAME = "IrregularImplementsBlockBreaker"

			private fun getStackAttributeValue(
				itemStack: ItemStack,
				attribute: Holder<Attribute>,
				registryAccess: RegistryAccess,
				baseValue: Float
			): Double {
				val modifiers = getModifiersForAttribute(attribute, itemStack, registryAccess)

				val baseIncrease = modifiers
					.filter { it.operation == AttributeModifier.Operation.ADD_VALUE }
					.sumOf { it.amount }

				val increasedBase = baseValue + baseIncrease

				val multipliedBase = modifiers
					.filter { it.operation == AttributeModifier.Operation.ADD_MULTIPLIED_BASE }
					.fold(increasedBase) { acc, modifier -> acc * modifier.amount }

				val multipliedTotal = modifiers
					.filter { it.operation == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL }
					.fold(multipliedBase) { acc, modifier -> acc * (1.0 + modifier.amount) }

				return multipliedTotal
			}

			private fun getModifiersForAttribute(
				attribute: Holder<Attribute>,
				itemStack: ItemStack,
				registryAccess: RegistryAccess,
			): List<AttributeModifier> {
				if (itemStack.isEmpty) return emptyList()

				val enchantmentModifiers = itemStack.getAllEnchantments(
					registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
				)
					.entrySet()
					.flatMap { (enchantHolder, level) ->
						enchantHolder.value().effects()
							.get(EnchantmentEffectComponents.ATTRIBUTES)
							?.filter { it.attribute.isHolder(attribute) }
							?.map { it.getModifier(level, EquipmentSlot.MAINHAND) }
							?: emptyList()
					}

				val stackModifiers = itemStack.attributeModifiers.modifiers
					.filter { it.slot.test(EquipmentSlot.MAINHAND) && it.attribute.isHolder(attribute) }
					.map { it.modifier }

				return enchantmentModifiers + stackModifiers
			}
		}
	}

}