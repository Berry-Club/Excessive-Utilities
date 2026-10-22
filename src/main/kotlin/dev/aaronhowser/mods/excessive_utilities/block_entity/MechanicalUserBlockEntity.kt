package dev.aaronhowser.mods.excessive_utilities.block_entity

import com.mojang.authlib.GameProfile
import dev.aaronhowser.mods.aaron.entity.BetterFakePlayerFactory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.excessive_utilities.block.MechanicalInteractorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_user.MechanicalUserMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.item.ItemResource
import java.lang.ref.WeakReference
import java.util.*

class MechanicalUserBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : MechanicalInteractorBlockEntity(
	ModBlockEntityTypes.MECHANICAL_USER.get(),
	pos,
	blockState,
	BASE_CONTAINER_SIZE
) {

	var interactionMode = InteractionMode.GENERIC_CLICK
		private set
	var isLeftClick = false
		private set
	var useUpperLeftSlotOnly = false
		private set
	var isSneaking = false
		private set

	private var fakePlayerReference: WeakReference<FakePlayer>? = null
	private var fakePlayerUuid = UUID.randomUUID()

	private val menuData = object : ContainerData {
		override fun get(index: Int): Int {
			return when (index) {
				REDSTONE_MODE_DATA_INDEX -> redstoneMode.ordinal
				INTERACTION_MODE_DATA_INDEX -> interactionMode.ordinal
				IS_LEFT_CLICK_DATA_INDEX -> if (isLeftClick) 1 else 0
				USE_UPPER_LEFT_SLOT_ONLY_DATA_INDEX -> if (useUpperLeftSlotOnly) 1 else 0
				SNEAKING_DATA_INDEX -> if (isSneaking) 1 else 0
				else -> 0
			}
		}

		override fun set(index: Int, value: Int) {
			when (index) {
				REDSTONE_MODE_DATA_INDEX -> {
					setRedstoneMode(value)
					return
				}

				INTERACTION_MODE_DATA_INDEX -> interactionMode = InteractionMode.fromOrdinal(value)
				IS_LEFT_CLICK_DATA_INDEX -> isLeftClick = value != 0
				USE_UPPER_LEFT_SLOT_ONLY_DATA_INDEX -> useUpperLeftSlotOnly = value != 0
				SNEAKING_DATA_INDEX -> isSneaking = value != 0
				else -> return
			}

			setChanged()
		}

		override fun getCount(): Int = MENU_DATA_SIZE
	}

	private fun getFakePlayer(level: ServerLevel): FakePlayer {
		val existingPlayer = fakePlayerReference?.get()
		if (existingPlayer != null) return existingPlayer

		val profile = GameProfile(fakePlayerUuid, FAKE_PLAYER_NAME)
		val fakePlayer = BetterFakePlayerFactory.get(level, profile) {
			MechanicalUserFakePlayer(level, profile)
		}

		fakePlayer.isSilent = true
		fakePlayer.setOnGround(true)
		fakePlayerReference = WeakReference(fakePlayer)

		return fakePlayer
	}

	private fun getSelectedSlot(level: ServerLevel): Int {
		if (useUpperLeftSlotOnly) return 0

		val populatedSlots = mutableListOf<Int>()
		for (slot in 0 until INVENTORY_SIZE) {
			if (!container.getItem(slot).isEmpty) {
				populatedSlots.add(slot)
			}
		}

		if (populatedSlots.isEmpty()) return 0
		return populatedSlots[level.random.nextInt(populatedSlots.size)]
	}

	override fun operate(level: ServerLevel) {
		val facing = blockState.getValue(MechanicalInteractorBlock.FACING)
		val targetPos = blockPos.relative(facing)

		val selectedSlot = getSelectedSlot(level)
		val fakePlayer = getFakePlayer(level)

		fakePlayer.inventory.clearContent()
		for (slot in 0 until INVENTORY_SIZE) {
			fakePlayer.inventory.setItem(slot, container.getItem(slot).copy())
		}

		val facingVector = Vec3(facing.stepX.toDouble(), facing.stepY.toDouble(), facing.stepZ.toDouble())
		val fakePlayerEyePosition = blockPos.center
			.add(facingVector.scale(FAKE_PLAYER_FACE_OFFSET))

		fakePlayer.inventory.setSelectedSlot(selectedSlot)
		fakePlayer.isShiftKeyDown = isSneaking
		fakePlayer.setPos(fakePlayerEyePosition.subtract(0.0, fakePlayer.eyeHeight.toDouble(), 0.0))
		fakePlayer.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos.center)

		val hitResult = BlockHitResult(targetPos.center, facing.opposite, targetPos, false)
		when (interactionMode) {
			InteractionMode.GENERIC_CLICK -> genericClick(level, fakePlayer, targetPos, hitResult)
			InteractionMode.PLACE_BLOCK -> placeBlock(level, fakePlayer, targetPos, hitResult)
			InteractionMode.USE_ITEM_ON_BLOCK -> useItemOnBlock(fakePlayer, hitResult)
			InteractionMode.ACTIVATE_BLOCK_WITH_ITEM -> activateBlock(level, fakePlayer, targetPos, hitResult)
			InteractionMode.USE_ITEM -> useItem(level, fakePlayer)
			InteractionMode.ENTITY -> interactWithEntity(level, fakePlayer, targetPos)
		}

		copyInventoryBack(fakePlayer)
		fakePlayer.isShiftKeyDown = false
	}

	private fun genericClick(
		level: ServerLevel,
		fakePlayer: FakePlayer,
		targetPos: BlockPos,
		hitResult: BlockHitResult
	) {
		if (isLeftClick) {
			fakePlayer.gameMode.destroyBlock(targetPos)
			return
		}

		val result = fakePlayer.gameMode.useItemOn(
			fakePlayer,
			level,
			fakePlayer.mainHandItem,
			InteractionHand.MAIN_HAND,
			hitResult
		)

		if (result == InteractionResult.PASS) {
			fakePlayer.gameMode.useItem(fakePlayer, level, fakePlayer.mainHandItem, InteractionHand.MAIN_HAND)
		}
	}

	private fun useItemOnBlock(
		fakePlayer: FakePlayer,
		hitResult: BlockHitResult
	) {
		if (isLeftClick) {
			fakePlayer.gameMode.destroyBlock(hitResult.blockPos)
			return
		}

		val context = UseOnContext(fakePlayer, InteractionHand.MAIN_HAND, hitResult)
		fakePlayer.mainHandItem.useOn(context)
	}

	private fun placeBlock(
		level: ServerLevel,
		fakePlayer: FakePlayer,
		targetPos: BlockPos,
		hitResult: BlockHitResult
	) {
		if (isLeftClick) return
		if (fakePlayer.mainHandItem.item !is BlockItem) return
		if (!level.getBlockState(targetPos).canBeReplaced()) return

		val context = UseOnContext(fakePlayer, InteractionHand.MAIN_HAND, hitResult)
		fakePlayer.mainHandItem.useOn(context)
	}

	private fun activateBlock(
		level: ServerLevel,
		fakePlayer: FakePlayer,
		targetPos: BlockPos,
		hitResult: BlockHitResult
	) {
		val targetState = level.getBlockState(targetPos)
		if (isLeftClick) {
			targetState.attack(level, targetPos, fakePlayer)
			return
		}

		val itemResult = targetState.useItemOn(
			fakePlayer.mainHandItem,
			level,
			fakePlayer,
			InteractionHand.MAIN_HAND,
			hitResult
		)

		if (itemResult.consumesAction().not()) {
			targetState.useWithoutItem(level, fakePlayer, hitResult)
		}
	}

	private fun useItem(
		level: ServerLevel,
		fakePlayer: FakePlayer
	) {
		if (!isLeftClick) {
			fakePlayer.gameMode.useItem(
				fakePlayer,
				level,
				fakePlayer.mainHandItem,
				InteractionHand.MAIN_HAND
			)
		}
	}

	private fun interactWithEntity(
		level: ServerLevel,
		fakePlayer: FakePlayer,
		targetPos: BlockPos
	) {
		val entities = level.getEntities(
			fakePlayer,
			AABB(targetPos),
			Entity::isPickable
		)

		val targetEntity = entities.firstOrNull() ?: return

		if (isLeftClick) {
			if (targetEntity.isEntity(ModEntityTypeTagsProvider.MECHANICAL_USER_LEFT_CLICK_BLACKLIST)) return
			fakePlayer.attack(targetEntity)
		} else {
			if (targetEntity.isEntity(ModEntityTypeTagsProvider.MECHANICAL_USER_RIGHT_CLICK_BLACKLIST)) return
			fakePlayer.interactOn(targetEntity, InteractionHand.MAIN_HAND, targetPos.center)
		}
	}

	private fun copyInventoryBack(fakePlayer: Player) {
		for (slot in 0 until INVENTORY_SIZE) {
			container.setItem(slot, fakePlayer.inventory.getItem(slot).copy())
			fakePlayer.inventory.setItem(slot, ItemStack.EMPTY)
		}

		for (slot in INVENTORY_SIZE until fakePlayer.inventory.containerSize) {
			val stack = fakePlayer.inventory.getItem(slot)
			if (stack.isEmpty) continue

			val toInsert = stack.copy()
			val inserted = ResourceHandlerUtil.insertStacking(
				super.getItemHandler(null),
				ItemResource.of(toInsert),
				toInsert.count,
				null
			)
			if (inserted < toInsert.count) {
				toInsert.shrink(inserted)
				fakePlayer.drop(toInsert, false)
			}
		}

		fakePlayer.inventory.clearContent()
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return MechanicalUserMenu(containerId, playerInventory, container, menuData)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.putInt(INTERACTION_MODE_NBT, interactionMode.ordinal)
		tag.putBoolean(IS_LEFT_CLICK_NBT, isLeftClick)
		tag.putBoolean(USE_UPPER_LEFT_SLOT_ONLY_NBT, useUpperLeftSlotOnly)
		tag.putBoolean(SNEAKING_NBT, isSneaking)
		tag.putIntArray(FAKE_PLAYER_UUID_NBT, UUIDUtil.uuidToIntArray(fakePlayerUuid))
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		interactionMode = InteractionMode.fromOrdinal(tag.getIntOr(INTERACTION_MODE_NBT, 0))
		isLeftClick = tag.getBooleanOr(IS_LEFT_CLICK_NBT, false)
		useUpperLeftSlotOnly = tag.getBooleanOr(USE_UPPER_LEFT_SLOT_ONLY_NBT, false)
		isSneaking = tag.getBooleanOr(SNEAKING_NBT, false)
		fakePlayerUuid = tag.getIntArray(FAKE_PLAYER_UUID_NBT)
			.filter { it.size == 4 }
			.map(UUIDUtil::uuidFromIntArray)
			.orElse(fakePlayerUuid)
	}

	companion object {
		const val INTERACTION_MODE_DATA_INDEX = 1
		const val IS_LEFT_CLICK_DATA_INDEX = 2
		const val USE_UPPER_LEFT_SLOT_ONLY_DATA_INDEX = 3
		const val SNEAKING_DATA_INDEX = 4
		const val MENU_DATA_SIZE = 5

		private const val INTERACTION_MODE_NBT = "InteractionMode"
		private const val IS_LEFT_CLICK_NBT = "IsLeftClick"
		private const val USE_UPPER_LEFT_SLOT_ONLY_NBT = "UseUpperLeftSlotOnly"
		private const val SNEAKING_NBT = "Sneaking"
		private const val FAKE_PLAYER_UUID_NBT = "FakePlayerUuid"

		private const val FAKE_PLAYER_NAME = "ExcessiveUtilitiesMechanicalUser"
		private const val FAKE_PLAYER_FACE_OFFSET = 0.5001
	}

	enum class InteractionMode(val langKey: String) {
		GENERIC_CLICK(ModMenuLang.MECHANICAL_USER_MODE_GENERIC_CLICK),
		PLACE_BLOCK(ModMenuLang.MECHANICAL_USER_MODE_PLACE_BLOCK),
		USE_ITEM_ON_BLOCK(ModMenuLang.MECHANICAL_USER_MODE_USE_ITEM_ON_BLOCK),
		ACTIVATE_BLOCK_WITH_ITEM(ModMenuLang.MECHANICAL_USER_MODE_ACTIVATE_BLOCK_WITH_ITEM),
		USE_ITEM(ModMenuLang.MECHANICAL_USER_MODE_USE_ITEM),
		ENTITY(ModMenuLang.MECHANICAL_USER_MODE_ENTITY);

		companion object {
			fun fromOrdinal(ordinal: Int): InteractionMode {
				if (ordinal < 0) return entries.last()
				if (ordinal >= entries.size) return entries.first()

				return entries[ordinal]
			}
		}
	}

	private class MechanicalUserFakePlayer(
		level: ServerLevel,
		gameProfile: GameProfile
	) : FakePlayer(level, gameProfile) {
		override fun getAttackStrengthScale(adjustTicks: Float): Float = 1f
	}

}
