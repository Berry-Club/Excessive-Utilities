package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.joml.Vector3fc
import java.util.function.Consumer
class OpiniumCoreBEWLR : SpecialModelRenderer<ItemStack> {
 private val resolver: ItemModelResolver = Minecraft.getInstance().itemModelResolver
 override fun extractArgument(stack: ItemStack)=stack.copy()
 override fun getExtents(output: Consumer<Vector3fc>)=Unit
 override fun submit(argument: ItemStack?, poseStack: PoseStack, collector: SubmitNodeCollector, lightCoords:Int, overlayCoords:Int, hasFoil:Boolean, outlineColor:Int) {
  val stack=argument ?: return; val contents=stack.get(ModDataComponents.OPINIUM_CORE_CONTENTS) ?: return
  val inner=ItemStackRenderState(); val outer=ItemStackRenderState()
  resolver.updateForTopItem(inner,contents.getCenter(),ItemDisplayContext.NONE,null,null,0)
  resolver.updateForTopItem(outer,contents.getOuter(),ItemDisplayContext.NONE,null,null,0)
  val time=(AaronClientUtil.localLevel?.gameTime ?: 0L).toFloat()
  poseStack.pushPose(); poseStack.translate(.5,.5,.5); poseStack.mulPose(Axis.YP.rotationDegrees(time*4f)); poseStack.mulPose(Axis.XP.rotationDegrees(time*2f)); poseStack.scale(.5f,.5f,.5f); inner.submit(poseStack,collector,lightCoords,overlayCoords,outlineColor); poseStack.popPose()
  for (i in 0 until 8) { poseStack.pushPose(); poseStack.translate(.5,.5,.5); poseStack.mulPose(Axis.YP.rotationDegrees(i*45f+time*2f)); poseStack.translate(.3,0.0,0.0); poseStack.scale(.25f,.25f,.25f); outer.submit(poseStack,collector,lightCoords,overlayCoords,outlineColor); poseStack.popPose() }
 }
 data object Unbaked : SpecialModelRenderer.Unbaked<ItemStack> { val CODEC:MapCodec<Unbaked> = MapCodec.unit(Unbaked); override fun type()=CODEC; override fun bake(context:SpecialModelRenderer.BakingContext)=OpiniumCoreBEWLR() }
}
