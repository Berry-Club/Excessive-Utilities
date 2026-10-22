package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.joml.Vector3fc
import java.util.function.Consumer
class ManualMillBEWLR : SpecialModelRenderer<ItemStack> {
 private val resolver: ItemModelResolver = Minecraft.getInstance().itemModelResolver
 private val gear = ItemStackRenderState()
 private val gearStack by lazy { ModItems.REDSTONE_GEAR.toStack() }
 override fun extractArgument(stack: ItemStack) = stack.copy()
 override fun getExtents(output: Consumer<Vector3fc>) = Unit
 override fun submit(argument: ItemStack?, poseStack: PoseStack, collector: SubmitNodeCollector, lightCoords: Int, overlayCoords: Int, hasFoil: Boolean, outlineColor: Int) {
  resolver.updateForTopItem(gear, gearStack, ItemDisplayContext.NONE, AaronClientUtil.localLevel, null, 0)
  val time=(AaronClientUtil.localLevel?.gameTime ?: 0L).toFloat()
  poseStack.pushPose()
  poseStack.translate(0.5,0.4,0.5)
  poseStack.mulPose(Axis.YP.rotationDegrees(time*0.8f))
  poseStack.mulPose(Axis.XP.rotationDegrees(90f))
  gear.submit(poseStack,collector,lightCoords,overlayCoords,outlineColor)
  poseStack.popPose()
 }
 data object Unbaked : SpecialModelRenderer.Unbaked<ItemStack> { val CODEC: MapCodec<Unbaked> = MapCodec.unit(Unbaked); override fun type()=CODEC; override fun bake(context: SpecialModelRenderer.BakingContext)=ManualMillBEWLR() }
}
