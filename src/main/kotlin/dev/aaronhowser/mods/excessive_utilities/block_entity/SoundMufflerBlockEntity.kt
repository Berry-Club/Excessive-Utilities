package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.SoundMufflerCarrier
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.tags.GameEventTags
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent
import net.neoforged.neoforge.event.VanillaGameEvent

class SoundMufflerBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.SOUND_MUFFLER.get(), pos, blockState) {

	override fun onLoad() {
		super.onLoad()
		val level = this.level
		if (level is SoundMufflerCarrier) {
			level.getSoundMufflerPositions().add(this.worldPosition.asLong())
		}
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
		if (level is SoundMufflerCarrier) {
			level.getSoundMufflerPositions().remove(this.worldPosition.asLong())
		}
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
		if (level is SoundMufflerCarrier) {
			level.getSoundMufflerPositions().add(this.worldPosition.asLong())
		}
	}


	companion object {
		fun SoundMufflerCarrier.getSoundMufflerPositions(): LongOpenHashSet = this.`eu$getSoundMufflerBlockPositions`()

		fun handleSoundSourceEvent(event: PlaySoundEvent) {
			val sound = event.sound ?: return
			val level = AaronClientUtil.localLevel ?: return
			val location = Vec3(sound.x, sound.y, sound.z)

			if (isMufflerInRange(level, location, ClientConfig.CONFIG.soundMufflerRadius.get())) {
				event.sound = null
			}
		}

		fun handleVanillaGameEvent(event: VanillaGameEvent) {
			val gameEvent = event.vanillaEvent
			if (!gameEvent.`is`(GameEventTags.VIBRATIONS)
				&& !gameEvent.`is`(GameEventTags.SHRIEKER_CAN_LISTEN)
				&& !gameEvent.`is`(GameEventTags.WARDEN_CAN_LISTEN)
			) {
				return
			}

			if (isMufflerInRange(event.level, event.eventPosition, ServerConfig.CONFIG.soundMufflerRadius.get())) {
				event.isCanceled = true
			}
		}

		private fun isMufflerInRange(level: Level, location: Vec3, radius: Double): Boolean {
			if (level !is SoundMufflerCarrier) return false

			return level.getSoundMufflerPositions().any { mufflerPosLong ->
				val pos = BlockPos.of(mufflerPosLong).center
				pos.closerThan(location, radius)
			}
		}

	}

}