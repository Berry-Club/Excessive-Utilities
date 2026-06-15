package dev.aaronhowser.mods.excessive_utilities.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ModCommands {

	fun register(
		dispatcher: CommandDispatcher<CommandSourceStack>,
		buildContext: CommandBuildContext
	) {
		val root = dispatcher.register(
			Commands.literal("excessive-utilities")
				.then(SetCursedCommand.register())
		)
	}

}