package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toVec3
import dev.aaronhowser.mods.excessive_utilities.item.BuildersWandItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.event.RenderHighlightEvent
import java.util.function.Predicate
import kotlin.math.sqrt

object WandRenderer {

	fun renderTargetBlocks(event: RenderHighlightEvent.Block) {
		val player = AaronClientUtil.localPlayer ?: return

		val builderWand = getHeldWand(player, isBuilder = true)
		val destructionWand = getHeldWand(player, isBuilder = false)

		if (builderWand == null && destructionWand == null) return

		val level = player.level()
		val hit = event.target
		val pos = hit.blockPos
		val face = hit.direction

		val poseStack = event.poseStack
		val bufferSource = event.multiBufferSource
		val cameraPos = event.camera.position

		if (builderWand != null) {
			val success = renderBuilderWand(level, pos, face, builderWand, player, poseStack, bufferSource, cameraPos)
			if (success) event.isCanceled = true
		}

	}

	private fun renderBuilderWand(
		level: Level,
		clickedPos: BlockPos,
		face: Direction,
		wandStack: ItemStack,
		player: Player,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		cameraPos: Vec3
	): Boolean {
		val clickedState = level.getBlockState(clickedPos)
		if (clickedState.isAir) return false
		val blockItem = clickedState.block.asItem()
		if (blockItem == Items.AIR) return false

		val amountInInventory = player.inventory.countItem(blockItem)

		val maxAmount = wandStack.getOrDefault(ModDataComponents.AMOUNT_BLOCKS, 0)
		val amountCanPlace = minOf(maxAmount, amountInInventory)
		if (amountCanPlace <= 0) return false

		val positions = BuildersWandItem.getPositions(
			level,
			clickedPos,
			clickedState,
			face,
			amountCanPlace
		)

		val linesConsumer = bufferSource.getBuffer(RenderType.lines())

		for (pos in positions) {
			val offset = cameraPos.vectorTo(pos.toVec3()).toVector3f()

			renderCubeWireframe(
				poseStack,
				linesConsumer,
				offset.x, offset.y, offset.z,
				offset.x + 1, offset.y + 1, offset.z + 1,
				r = 1f, g = 1f, b = 1f, a = 1f
			)
		}

		return true
	}

	private fun renderDestructionWand(destructionWand: ItemStack, event: RenderHighlightEvent.Block) {
		val amount = destructionWand.get(ModDataComponents.AMOUNT_BLOCKS) ?: return

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

	private fun renderCubeWireframe(
		poseStack: PoseStack,
		vertexConsumer: VertexConsumer,
		minX: Float, minY: Float, minZ: Float,
		maxX: Float, maxY: Float, maxZ: Float,
		r: Float, g: Float, b: Float, a: Float
	) {
		val pose = poseStack.last()

		val nnn = floatArrayOf(minX, minY, minZ) // Negative X, Negative Y, Negative Z
		val nnp = floatArrayOf(minX, minY, maxZ) // Negative X, Negative Y, Positive Z
		val npn = floatArrayOf(minX, maxY, minZ) // Negative X, Positive Y, Negative Z
		val npp = floatArrayOf(minX, maxY, maxZ) // Negative X, Positive Y, Positive Z
		val pnn = floatArrayOf(maxX, minY, minZ) // Positive X, Negative Y, Negative Z
		val pnp = floatArrayOf(maxX, minY, maxZ) // Positive X, Negative Y, Positive Z
		val ppn = floatArrayOf(maxX, maxY, minZ) // Positive X, Positive Y, Negative Z
		val ppp = floatArrayOf(maxX, maxY, maxZ) // Positive X, Positive Y, Positive Z

		val lines = listOf(
			// Bottom face
			nnn to nnp,
			nnp to pnp,
			pnp to pnn,
			pnn to nnn,

			// Top face
			npn to npp,
			npp to ppp,
			ppp to ppn,
			ppn to npn,

			// Vertical edges
			nnn to npn,
			nnp to npp,
			pnn to ppn,
			pnp to ppp
		)

		for (edge in lines) {
			val (start, end) = edge
			drawLine(
				vertexConsumer, pose,
				start[0], start[1], start[2],
				end[0], end[1], end[2],
				r, g, b, a
			)
		}

	}

	private fun drawLine(
		vertexConsumer: VertexConsumer,
		pose: PoseStack.Pose,
		x1: Float, y1: Float, z1: Float,
		x2: Float, y2: Float, z2: Float,
		r: Float, g: Float, b: Float, a: Float
	) {
		var dx = x2 - x1
		var dy = y2 - y1
		var dz = z2 - z1

		val length = sqrt(dx * dx + dy * dy + dz * dz)
		if (length == 0f) return

		dx /= length
		dy /= length
		dz /= length

		vertexConsumer
			.addVertex(pose, x1, y1, z1)
			.setColor(r, g, b, a)
			.setNormal(pose, dx, dy, dz)

		vertexConsumer
			.addVertex(pose, x2, y2, z2)
			.setColor(r, g, b, a)
			.setNormal(pose, dx, dy, dz)
	}

}