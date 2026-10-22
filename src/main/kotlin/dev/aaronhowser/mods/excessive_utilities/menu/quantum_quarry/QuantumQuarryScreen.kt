package dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.base.components.EnergyBar
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import java.text.DecimalFormat
import kotlin.jvm.optionals.getOrNull

class QuantumQuarryScreen(
	menu: QuantumQuarryMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<QuantumQuarryMenu>(menu, playerInventory, title, BACKGROUND) {
	override val titleLabelSprite: Identifier = LABEL

	override fun baseInit() {
		super.baseInit()

		val energyBar = EnergyBar(
			x = leftPos + 149,
			y = topPos + 21,
			maxGetter = { 1_000_000 },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	override fun extractLabels(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		super.extractLabels(graphics, mouseX, mouseY)

		val x = "%,d".format(menu.getTargetX())
		val y = "%,d".format(menu.getTargetY())
		val z = "%,d".format(menu.getTargetZ())

		graphics.text(
			font,
			Component.literal("X: $x"),
			35,
			20,
			LABEL_TEXT_COLOR,
			false
		)

		graphics.text(
			font,
			Component.literal("Y: $y"),
			35,
			30,
			LABEL_TEXT_COLOR,
			false
		)

		graphics.text(
			font,
			Component.literal("Z: $z"),
			35,
			40,
			LABEL_TEXT_COLOR,
			false
		)

		val biomeRegistry = AaronClientUtil.localLevel
			?.registryAccess()
			?.lookup(Registries.BIOME)?.orElseThrow()

		if (biomeRegistry != null) {
			val biomeId = menu.getBiomeId()
			val biome = biomeRegistry.get(biomeId).orElse(null)
			if (biome != null) {
				val biomeKey = biome.unwrapKey().orElseThrow()
				if (biomeKey != null) {
					val component = AaronClientUtil.getBiomeDisplay(biomeKey)
					graphics.text(
						font,
						component,
						35,
						50,
						LABEL_TEXT_COLOR,
						false
					)
				}
			}
		}

		val amountMined = menu.getAmountBlocksBroken()
		val progress = menu.getProgress()

		val minedString = DecimalFormat("#,##0.##").format(amountMined + progress)

		graphics.text(
			font,
			Component.literal("$minedString mined"),
			35,
			60,
			LABEL_TEXT_COLOR,
			false
		)

	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/quantum_quarry.png"), 176, 198)

		val LABEL: Identifier =
			ExcessiveUtilities.modResource("label/quantum_quarry")
	}

}
