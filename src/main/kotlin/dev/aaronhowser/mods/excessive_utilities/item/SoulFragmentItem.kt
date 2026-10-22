package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.attachment.SoulDebt
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttachmentTypes
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.level.Level

class SoulFragmentItem(properties: Properties) : Item(properties), TooltipItem {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResult {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide && !player.isFakePlayer) {
			SoulDebt.absorbSoulFragment(player, 1)
			stack.shrink(1)
		}

		return InteractionResult.SUCCESS
	}

	override fun appendTooltip(
		stack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: java.util.function.Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val player = AaronClientUtil.localPlayer ?: return
		val netSoulFragments = player.getData(ModAttachmentTypes.SOUL_DEBT).netSoulFragments

		if (netSoulFragments <= 0) {
			builder.accept(
				ModMenuLang.SOUL_DEBT
					.toComponent(-netSoulFragments)
					.withStyle(ChatFormatting.RED)
			)
		} else {
			builder.accept(
				ModMenuLang.SOUL_SURPLUS
					.toComponent(netSoulFragments)
					.withStyle(ChatFormatting.LIGHT_PURPLE)
			)
		}

		val attributeModifier = player
			.getAttribute(Attributes.MAX_HEALTH)
			?.getModifier(SoulDebt.ATTRIBUTE_MODIFIER_NAME)

		if (attributeModifier != null) {
			val amount = attributeModifier.amount
			if (amount != 0.0) {
				builder.accept(
					ModMenuLang.SOUL_HEALTH_MODIFIER
						.toComponent(if (amount > 0) "+$amount" else "$amount")
						.withStyle(ChatFormatting.GRAY)
				)
			}
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
	}

}







