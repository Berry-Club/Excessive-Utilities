package dev.aaronhowser.mods.excessive_utilities.command

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.excessive_utilities.handler.CursedEarthHandler
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object SetCursedCommand : AaronCommandHelper {

	const val TARGETS = "targets"
	const val VALUE = "value"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("set-cursed") {
			requires { it.hasPermission(2) }

			executes {
				val source = it.source
				val targets = listOf(source.playerOrException)
				setCursed(source, targets, value = true)
			}

			thenArgument(VALUE, BoolArgumentType.bool()) {
				executes {
					val source = it.source
					val targets = listOf(source.playerOrException)
					val value = BoolArgumentType.getBool(it, VALUE)
					setCursed(source, targets, value)
				}

				thenArgument(TARGETS, EntityArgument.entities()) {
					executes {
						val source = it.source
						val targets = EntityArgument.getEntities(it, TARGETS)
						val value = BoolArgumentType.getBool(it, VALUE)
						setCursed(source, targets, value)
					}
				}
			}

		}
	}

	private fun setCursed(
		source: CommandSourceStack,
		targets: Collection<Entity>,
		value: Boolean
	): Int {
		var amountSet = 0

		for (target in targets) {
			if (target !is LivingEntity) continue

			val isAlreadyCursed = CursedEarthHandler.isCursed(target)
			if (isAlreadyCursed == value) continue

			CursedEarthHandler.setCursed(target, value)
			amountSet++
		}

		return amountSet
	}

}