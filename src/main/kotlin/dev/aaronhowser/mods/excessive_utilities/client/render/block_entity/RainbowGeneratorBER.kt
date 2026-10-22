package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.RainbowGeneratorBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.util.RandomSource
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.world.phys.Vec3
import java.awt.Color
import org.joml.Quaternionf
import org.joml.Vector3f
class RainbowGeneratorBER(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<RainbowGeneratorBlockEntity, RainbowGeneratorBER.State> {
 class State : BlockEntityRenderState() { var time=0f; var width=0f; var length=0f; var active=false }
 override fun createRenderState()=State()
 override fun extractRenderState(be: RainbowGeneratorBlockEntity,s:State,partial:Float,camera:Vec3,overlay:ModelFeatureRenderer.CrumblingOverlay?){ BlockEntityRenderState.extractBase(be,s,overlay); s.active=be.blockState.getValue(GeneratorBlock.LIT); s.width=ClientConfig.CONFIG.rainbowGeneratorRayWidth.get().toFloat(); s.length=ClientConfig.CONFIG.rainbowGeneratorRayLength.get().toFloat(); s.time=((be.level?.gameTime ?: 0L)+partial)*ClientConfig.CONFIG.rainbowGeneratorTimeFactor.get().toFloat() }
 override fun submit(s:State,p:PoseStack,c:SubmitNodeCollector,camera:CameraRenderState){
  if(!s.active||s.width<=0||s.length<=0)return
  val center=Color.HSBtoRGB((s.time%1f),1f,1f)
  p.pushPose()
  p.translate(.5,.5,.5)
  c.submitCustomGeometry(p, RenderTypes.dragonRays()) { pose, vertexConsumer ->
   val random = RandomSource.create(432L)
   val zero = Vector3f()
   val first = Vector3f()
   val second = Vector3f()
   val third = Vector3f()
   val rotation = Quaternionf()
   for (rayIndex in 0 until 15) {
    rotation.identity()
     .rotateXYZ(random.nextFloat() * (Math.PI * 2).toFloat(), random.nextFloat() * (Math.PI * 2).toFloat(), random.nextFloat() * (Math.PI * 2).toFloat())
     .rotateXYZ(random.nextFloat() * (Math.PI * 2).toFloat(), random.nextFloat() * (Math.PI * 2).toFloat(), random.nextFloat() * (Math.PI * 2).toFloat() + s.time * (Math.PI / 2).toFloat())
    first.set(-0.8660254f * s.width, s.length, -0.5f * s.width)
    second.set(0.8660254f * s.width, s.length, -0.5f * s.width)
    third.set(0f, s.length, s.width)
    rotation.transform(first)
    rotation.transform(second)
    rotation.transform(third)
    vertexConsumer.addVertex(pose, zero).setColor(center)
    vertexConsumer.addVertex(pose, first).setColor(center and 0x00FFFFFF)
    vertexConsumer.addVertex(pose, second).setColor(center and 0x00FFFFFF)
    vertexConsumer.addVertex(pose, zero).setColor(center)
    vertexConsumer.addVertex(pose, second).setColor(center and 0x00FFFFFF)
    vertexConsumer.addVertex(pose, third).setColor(center and 0x00FFFFFF)
    vertexConsumer.addVertex(pose, zero).setColor(center)
    vertexConsumer.addVertex(pose, third).setColor(center and 0x00FFFFFF)
    vertexConsumer.addVertex(pose, first).setColor(center and 0x00FFFFFF)
   }
  }
  p.popPose()
 }
}
