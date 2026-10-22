package dev.aaronhowser.mods.excessive_utilities.block_entity

import com.mojang.authlib.GameProfile
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.ItemCatcher
import dev.aaronhowser.mods.excessive_utilities.block.MechanicalInteractorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_miner.MechanicalMinerMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.RangedResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.minecraft.util.Unit
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.lang.ref.WeakReference
import java.util.*

class MechanicalMinerBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : MechanicalInteractorBlockEntity(
	ModBlockEntityTypes.MECHANICAL_MINER.get(),
	pos,
	blockState,
	CONTAINER_SIZE
) {

	private var fakePlayerReference: WeakReference<FakePlayer>? = null
	private var fakePlayerUuid = UUID.randomUUID()

	private val menuData = object : ContainerData {
		override fun get(index: Int): Int {
			return when (index) {
				REDSTONE_MODE_DATA_INDEX -> redstoneMode.ordinal
				else -> 0
			}
		}

		override fun set(index: Int, value: Int) {
			if (index == REDSTONE_MODE_DATA_INDEX) {
				setRedstoneMode(value)
			}
		}

		override fun getCount(): Int = MENU_DATA_SIZE
	}

	private fun getFakePlayer(level: ServerLevel): FakePlayer {
		val existingPlayer = fakePlayerReference?.get()
		if (existingPlayer != null) return existingPlayer

		val profile = GameProfile(fakePlayerUuid, FAKE_PLAYER_NAME)
		val fakePlayer = FakePlayerFactory.get(level, profile)
		fakePlayer.isSilent = true
		fakePlayer.setOnGround(true)
		fakePlayerReference = WeakReference(fakePlayer)

		return fakePlayer
	}

	override fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource> {
		val inventoryHandler = RangedResourceHandler.of(VanillaContainerWrapper.of(container), 0, INVENTORY_SIZE)
		return object : DelegatingResourceHandler<ItemResource>(inventoryHandler) {
			override fun insert(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int = 0
		}
	}

	override fun canInsertItem(slot: Int, stack: ItemStack): Boolean {
		if (slot == ENCHANTMENT_SLOT) {
			return stack.isItem(Items.ENCHANTED_BOOK)
		}

		return super.canInsertItem(slot, stack)
	}

	override fun operate(level: ServerLevel) {
		val facing = blockState.getValue(MechanicalInteractorBlock.FACING)
		val targetPos = blockPos.relative(facing)
		val targetState = level.getBlockState(targetPos)

		if (targetState.isAir) return
		if (targetState.isBlock(ModBlockTagsProvider.MECHANICAL_MINER_BLACKLIST)) return
		if (targetState.getDestroySpeed(level, targetPos) < 0f) return

		val fakePlayer = getFakePlayer(level)
		fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, createMiningTool(level))

		val facingVector = Vec3(facing.stepX.toDouble(), facing.stepY.toDouble(), facing.stepZ.toDouble())
		fakePlayer.setPos(targetPos.center.subtract(facingVector).subtract(0.0, fakePlayer.eyeHeight.toDouble(), 0.0))

		var blockDestroyed = false
		val droppedItems = ItemCatcher.catchEntitiesDuring {
			blockDestroyed = fakePlayer.gameMode.destroyBlock(targetPos)
		}

		if (!blockDestroyed) return

		for (itemEntity in droppedItems) {
			val stack = itemEntity.item.copy()
			val accepted = ResourceHandlerUtil.insertStacking(super.getItemHandler(null), ItemResource.of(stack), stack.count, null)
			val remainder = stack.copyWithCount(stack.count - accepted)
			if (remainder.isEmpty) {
				itemEntity.discard()
			} else {
				itemEntity.item = remainder
			}
		}
	}

	private fun createMiningTool(level: ServerLevel): ItemStack {
		val miningTool = Items.DIAMOND_PICKAXE.defaultInstance
		miningTool.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)

		val enchantedBook = container.getItem(ENCHANTMENT_SLOT)
		if (!enchantedBook.isEmpty) {
			val enchantments = enchantedBook.getAllEnchantments(
				level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
			)
			EnchantmentHelper.setEnchantments(miningTool, enchantments)
		}

		return miningTool
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return MechanicalMinerMenu(containerId, playerInventory, container, menuData)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.putIntArray(FAKE_PLAYER_UUID_NBT, UUIDUtil.uuidToIntArray(fakePlayerUuid))
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		fakePlayerUuid = tag.getIntArray(FAKE_PLAYER_UUID_NBT).filter { it.size == 4 }.map(UUIDUtil::uuidFromIntArray).orElse(fakePlayerUuid)
	}

	companion object {
		const val ENCHANTMENT_SLOT = 10
		const val CONTAINER_SIZE = 11
		const val MENU_DATA_SIZE = 1

		private const val FAKE_PLAYER_NAME = "ExcessiveUtilitiesMechanicalMiner"
		private const val FAKE_PLAYER_UUID_NBT = "FakePlayerUuid"
	}

}
