package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.serialization.MapCodec
import com.mojang.math.Axis
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import org.joml.Vector3fc
import java.util.function.Consumer
import kotlin.math.sin
import kotlin.math.sqrt

class TesseractBEWLR(private val gui: Boolean = false) : SpecialModelRenderer<Void> {
    override fun extractArgument(stack: ItemStack): Void? = null
    override fun getExtents(output: Consumer<Vector3fc>) = Unit
    override fun submit(argument: Void?, poseStack: PoseStack, collector: SubmitNodeCollector, lightCoords: Int, overlayCoords: Int, hasFoil: Boolean, outlineColor: Int) {
        val time = (AaronClientUtil.localLevel?.gameTime ?: 0L).toFloat()
        poseStack.pushPose(); poseStack.translate(0.5, 0.5, 0.5); poseStack.scale(0.82f, 0.82f, 0.82f)
        if (gui) { poseStack.translate(-0.125, -0.125, 0.0); poseStack.scale(0.75f, 0.75f, 0.75f); poseStack.mulPose(Axis.XP.rotationDegrees(28f)); poseStack.mulPose(Axis.YP.rotationDegrees(-36f)) }
        collector.submitCustomGeometry(poseStack, RenderTypes.lines()) { pose, v -> renderCyclingSquares(pose, v, time) }
        poseStack.popPose()
    }
    private fun square(p: PoseStack.Pose, v: VertexConsumer, h: Float, z: Float, c: Int) { line(p,v,-h,-h,z,h,-h,z,c); line(p,v,h,-h,z,h,h,z,c); line(p,v,h,h,z,-h,h,z,c); line(p,v,-h,h,z,-h,-h,z,c) }
    private fun renderCyclingSquares(p: PoseStack.Pose, v: VertexConsumer, time: Float) {
        val inner = ClientConfig.CONFIG.tesseractInnerColor.get(); val outer = ClientConfig.CONFIG.tesseractOuterColor.get()
        for (i in 0 until 4) { val phase = time * ClientConfig.CONFIG.tesseractSpeed.get() + i * Mth.TWO_PI / 4f; square(p, v, 0.35f, sin(phase).toFloat() * 0.8f, if (i < 2) inner else outer) }
    }
    private fun line(p: PoseStack.Pose, v: VertexConsumer, x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float, c: Int) {
        val dx = x2 - x1
        val dy = y2 - y1
        val dz = z2 - z1
        val length = sqrt(dx * dx + dy * dy + dz * dz)
        require(length > 0f) { "Cannot render a Tesseract line with zero length" }
        val nx = dx / length
        val ny = dy / length
        val nz = dz / length
        v.addVertex(p, x1, y1, z1).setColor(c).setNormal(p, nx, ny, nz).setLineWidth(1.0f)
        v.addVertex(p, x2, y2, z2).setColor(c).setNormal(p, nx, ny, nz).setLineWidth(1.0f)
    }
    data class Unbaked(val gui: Boolean = false) : SpecialModelRenderer.Unbaked<Void> { companion object { val CODEC: MapCodec<Unbaked> = RecordCodecBuilder.mapCodec { it.group(Codec.BOOL.optionalFieldOf("gui", false).forGetter(Unbaked::gui)).apply(it, ::Unbaked) } }; override fun type()=CODEC; override fun bake(context: SpecialModelRenderer.BakingContext)=TesseractBEWLR(gui) }
}
