package dev.aaronhowser.mods.excessive_utilities.item

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.packet.server_to_client.UpdatePlayerWingPacket
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem
import java.util.*
import kotlin.jvm.optionals.getOrNull

class AngelRingItem(properties: Properties) : Item(properties), ICurioItem {

	override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
		val entity = slotContext.entity()
		if (entity is ServerPlayer) {
			addGpConsumer(entity, stack)
			handleAttributeModifier(entity)
		}
	}

	override fun onEquip(slotContext: SlotContext, prevStack: ItemStack, stack: ItemStack) {
		val entity = slotContext.entity()
		if (entity is ServerPlayer) {
			val type = stack.get(ModDataComponents.ANGEL_RING_TYPE)
			val packet = UpdatePlayerWingPacket(entity, type)
			packet.messageAllPlayersTrackingEntityAndSelf(entity)
		}
	}

	override fun onUnequip(slotContext: SlotContext?, newStack: ItemStack?, stack: ItemStack?) {
		val entity = slotContext?.entity()
		if (entity is ServerPlayer) {
			val packet = UpdatePlayerWingPacket(entity, null)
			packet.messageAllPlayersTrackingEntityAndSelf(entity)
		}
	}

	override fun canEquipFromUse(slotContext: SlotContext?, stack: ItemStack?): Boolean {
		return true
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val type = stack.getOrDefault(ModDataComponents.ANGEL_RING_TYPE, Type.INVISIBLE)
		tooltipComponents.add(Component.translatable(type.langKey))
	}

	companion object {
		val ATTRIBUTE_MODIFIER_NAME = ExcessiveUtilities.modResource("angel_ring_flight")

		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.ANGEL_RING_TYPE, Type.INVISIBLE)
			}

		val PLAYER_WINGS: MutableMap<UUID, Type> = mutableMapOf()

		val RING_TYPE: ResourceLocation = ExcessiveUtilities.modResource("ring_type")
		fun hasEntityPredicate(
			stack: ItemStack,
			localLevel: Level?,
			holdingEntity: LivingEntity?,
			int: Int
		): Float {
			val type = stack.getOrDefault(ModDataComponents.ANGEL_RING_TYPE, Type.INVISIBLE)
			return type.ordinal.toFloat()
		}

		private fun addGpConsumer(player: ServerPlayer, ringStack: ItemStack): GridPowerContribution.HeldItem {
			val handler = GridPowerHandler.get(player.serverLevel()).getGrid(player)

			val currentConsumers = handler.getConsumers()
			val existing = currentConsumers
				.filterIsInstance<GridPowerContribution.HeldItem>()
				.firstOrNull { ItemStack.isSameItemSameComponents(it.gpStack, ringStack) }

			if (existing != null) {
				return existing
			}

			val new = object : GridPowerContribution.HeldItem(ringStack, player) {
				override fun isStillValid(): Boolean {
					if (!player.isAlive || player.isRemoved) return false

					var stillHasStack = false

					val wornCurios = CuriosApi.getCuriosInventory(player).getOrNull()?.equippedCurios
					if (wornCurios != null) {
						for (slot in 0 until wornCurios.slots) {
							val stack = wornCurios.getStackInSlot(slot)
							if (stack === ringStack) {
								stillHasStack = true
								break
							}
						}
					}

					if (!stillHasStack) {
						val attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)
						if (attribute != null && attribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)) {
							attribute.removeModifier(ATTRIBUTE_MODIFIER_NAME)
							player.abilities.flying = false
							player.onUpdateAbilities()
						}
					}

					return stillHasStack
				}

				override fun getAmount(): Double {
					if (player.hasInfiniteMaterials() || !player.abilities.flying) return 0.0

					return ServerConfig.CONFIG.angelRingGpCost.get()
				}

				private val stackCopy = ringStack.copy()
				override fun getDisplayStack(): ItemStack = stackCopy
				override fun getDisplayName(): Component = stackCopy.displayName
				override fun getDisplayText(): Component {
					val amount = getAmount()
					return Component.literal("$amount")
				}
			}

			handler.addConsumer(new)

			return new
		}

		private fun handleAttributeModifier(player: ServerPlayer) {
			val flightAttribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT) ?: return
			val currentlyHasModifier = flightAttribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)

			val handler = GridPowerHandler.get(player.serverLevel()).getGrid(player)
			if (!handler.isOverloaded()) {
				if (currentlyHasModifier) return

				flightAttribute.addTransientModifier(
					AttributeModifier(
						ATTRIBUTE_MODIFIER_NAME,
						1.0,
						AttributeModifier.Operation.ADD_VALUE
					)
				)
			} else {
				if (currentlyHasModifier) {
					flightAttribute.removeModifier(ATTRIBUTE_MODIFIER_NAME)
				}
			}
		}

		fun handleTrackingEvent(event: PlayerEvent.StartTracking) {
			val target = event.target as? Player ?: return
			val tracker = event.entity as? ServerPlayer ?: return

			val wornCurios = CuriosApi.getCuriosInventory(target).getOrNull()?.equippedCurios ?: return
			var firstRingType: Type? = null

			for (slot in 0 until wornCurios.slots) {
				val stack = wornCurios.getStackInSlot(slot)
				if (stack.isEmpty) continue

				val type = stack.get(ModDataComponents.ANGEL_RING_TYPE)
				if (type != null) {
					firstRingType = type
					break
				}
			}

			if (firstRingType == null) return

			val packet = UpdatePlayerWingPacket(target, firstRingType)
			packet.messagePlayer(tracker)
		}

	}

	enum class Type(
		val id: String
	) : StringRepresentable {
		INVISIBLE("invisible"),
		FEATHER("feather"),
		BUTTERFLY("butterfly"),
		DEMON("demon"),
		GOLD("gold"),
		BAT("bat")
		;

		val langKey: String = "tooltip.excessive_utilities.angel_ring.type.$id"
		val textureLocation: ResourceLocation = ExcessiveUtilities.modResource("textures/entity/angel_wing/$id.png")

		override fun getSerializedName(): String = id
		fun getStack(): ItemStack = ModItems.ANGEL_RING.withComponent(ModDataComponents.ANGEL_RING_TYPE.get(), this)

		companion object {
			val CODEC: Codec<Type> = StringRepresentable.fromValues { entries.toTypedArray() }
			val STREAM_CODEC: StreamCodec<ByteBuf, Type> = ByteBufCodecs.fromCodec(CODEC)
		}
	}

}