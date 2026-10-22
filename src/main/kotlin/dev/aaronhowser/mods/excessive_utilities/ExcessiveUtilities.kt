package dev.aaronhowser.mods.excessive_utilities

import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModRegistries
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn

@Mod(ExcessiveUtilities.MOD_ID)
class ExcessiveUtilities(
	modContainer: ModContainer
) {

	init {
		ModRegistries.register(MOD_BUS)

		runWhenOn(Dist.CLIENT) {
			val screenFactory = IConfigScreenFactory { container, screen -> ConfigurationScreen(container, screen) }
			modContainer.registerExtensionPoint(IConfigScreenFactory::class.java, screenFactory)
		}

		modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG_SPEC)
		modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG_SPEC)
	}

	companion object {
		const val MOD_ID = "excessive_utilities"

		@JvmField
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		fun modResource(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

		val IS_ATHENA_LOADED: Boolean by lazy {
			ModList.get().isLoaded("athena")
		}
	}

}