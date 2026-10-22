package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getPovResult
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toVec3
import dev.aaronhowser.mods.excessive_utilities.item.BuildersWandItem
import dev.aaronhowser.mods.excessive_utilities.item.DestructionWandItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState
import net.minecraft.client.renderer.state.level.LevelRenderState
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.CustomBlockOutlineRenderer
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent
import java.util.function.Predicate

object WandRenderer {

	fun renderTargetBlocks(event: ExtractBlockOutlineRenderStateEvent) {
		val player = AaronClientUtil.localPlayer ?: return

		val builderWand = getHeldWand(player, isBuilder = true)
		val destructionWand = getHeldWand(player, isBuilder = false)

		if (builderWand == null && destructionWand == null) return

		val level = player.level()
		val pos = event.blockPos
		val face = player.getPovResult().direction
		event.addCustomRenderer(CustomBlockOutlineRenderer { state: BlockOutlineRenderState, buffer: MultiBufferSource.BufferSource, poseStack: PoseStack, _, levelState: LevelRenderState ->
			val cameraPos = levelState.cameraRenderState.pos
			var rendered = false
			if (builderWand != null) rendered = renderBuilderWand(level, pos, face, builderWand, player, poseStack, buffer, cameraPos) || rendered
			if (destructionWand != null) rendered = renderDestructionWand(level, pos, face, destructionWand, player, poseStack, buffer, cameraPos) || rendered
			rendered
		})

	}

	private fun renderBuilderWand(
		level: Level,
		targetPos: BlockPos,
		targetFace: Direction,
		wandStack: ItemStack,
		player: Player,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		cameraPos: Vec3
	): Boolean {
		val targetState = level.getBlockState(targetPos)
		if (targetState.isAir) return false
		val blockItem = targetState.block.asItem()
		if (blockItem == Items.AIR) return false

		val amountInInventory = if (player.hasInfiniteMaterials()) 9999 else player.inventory.countItem(blockItem)

		val maxAmount = wandStack.getOrDefault(ModDataComponents.AMOUNT_BLOCKS, 0)
		val amountCanPlace = minOf(maxAmount, amountInInventory)
		if (amountCanPlace <= 0) return false

		val positions = BuildersWandItem.getPositions(
			level,
			targetPos,
			targetState,
			targetFace,
			amountCanPlace
		)

		for (pos in positions) {
			val offset = cameraPos.vectorTo(pos.toVec3())

			AaronRenderUtil.renderCubeWireframeThroughWalls(
				poseStack,
				offset.x, offset.y, offset.z,
				offset.x + 1, offset.y + 1, offset.z + 1,
				color = 0xCCFFFFFF.toInt()
			)
		}

		return true
	}

	private fun renderDestructionWand(
		level: Level,
		targetPos: BlockPos,
		targetFace: Direction,
		wandStack: ItemStack,
		player: Player,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		cameraPos: Vec3
	): Boolean {
		val targetState = level.getBlockState(targetPos)
		if (targetState.isAir) return false

		val amount = wandStack.getOrDefault(ModDataComponents.AMOUNT_BLOCKS, 0)
		if (amount <= 0) return false

		val positions = DestructionWandItem.getPositions(
			level,
			targetPos,
			targetState.block,
			if (player.isSecondaryUseActive) null else targetFace,
			amount
		)

		for (pos in positions) {
			val offset = cameraPos.vectorTo(pos.toVec3())

			AaronRenderUtil.renderCubeWireframeThroughWalls(
				poseStack,
				offset.x, offset.y, offset.z,
				offset.x + 1, offset.y + 1, offset.z + 1,
				color = 0xCCFF8080.toInt()
			)
		}

		return true
	}

	private fun getHeldWand(player: Player, isBuilder: Boolean): ItemStack? {
		val predicate = if (isBuilder) {
			Predicate<ItemStack> { it.isItem(ModItems.BUILDERS_WAND) || it.isItem(ModItems.CREATIVE_BUILDERS_WAND) }
		} else {
			Predicate<ItemStack> { it.isItem(ModItems.DESTRUCTION_WAND) || it.isItem(ModItems.CREATIVE_DESTRUCTION_WAND) }
		}

		val mainHand = player.mainHandItem
		if (predicate.test(mainHand)) {
			return mainHand
		}

		val offHand = player.offhandItem
		if (predicate.test(offHand)) {
			return offHand
		}

		return null
	}

}
