package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.block_entity.ResturbedMobSpawnerBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.util.Mth
import net.minecraft.client.renderer.blockentity.SpawnerRenderer
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.world.phys.Vec3
class ResturbedMobSpawnerBER(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<ResturbedMobSpawnerBlockEntity, SpawnerRenderState> {
 private val dispatcher: EntityRenderDispatcher = context.entityRenderer
 override fun createRenderState()=SpawnerRenderState()
 override fun extractRenderState(be:ResturbedMobSpawnerBlockEntity,s:SpawnerRenderState,partial:Float,camera:Vec3,overlay:ModelFeatureRenderer.CrumblingOverlay?){
  BlockEntityRenderState.extractBase(be,s,overlay)
  val level=be.level ?: return
  val entity=be.spawner.getOrCreateDisplayEntity(level,be.blockPos) ?: return
  s.displayEntity=dispatcher.extractEntity(entity,partial)
  s.displayEntity!!.lightCoords=s.lightCoords
  s.spin=Mth.lerp(partial.toDouble(),be.spawner.getOSpin(),be.spawner.getSpin()).toFloat()*10f
  s.scale=0.53125f
  val maxLength=maxOf(entity.bbWidth,entity.bbHeight)
  if(maxLength>1f) s.scale/=maxLength
 }
 override fun submit(s:SpawnerRenderState,p:PoseStack,c:SubmitNodeCollector,camera:CameraRenderState){
  val displayEntity=s.displayEntity ?: return
  SpawnerRenderer.submitEntityInSpawner(p,c,displayEntity,dispatcher,s.spin,s.scale,camera)
 }
}
