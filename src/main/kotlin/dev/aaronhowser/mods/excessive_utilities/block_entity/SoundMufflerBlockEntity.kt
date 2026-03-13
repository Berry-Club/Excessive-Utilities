package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.SoundMufflerCarrier
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent

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

			if (level is SoundMufflerCarrier) {
				val radius = ClientConfig.CONFIG.soundMufflerRadius.get()
				val anyMufflerInRange = level.getSoundMufflerPositions().any { mufflerPosLong ->
					val pos = BlockPos.of(mufflerPosLong).center
					pos.closerThan(location, radius)
				}

				if (anyMufflerInRange) {
					event.sound = null
				}
			}
		}

	}

}