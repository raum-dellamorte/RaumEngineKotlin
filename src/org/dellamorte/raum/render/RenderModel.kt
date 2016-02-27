package org.dellamorte.raum.render

import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Entity
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.shaders.ShaderModel
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-01-28.
 */
class RenderModel(val shader: ShaderModel) {
  constructor() : this(ShaderModel())
  
  init {
    shader.start()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.stop()
  }
  
  fun render(tmap: HashMap<ModelTextured, ArrayList<Entity>>) {
    for ((model: ModelTextured, ents: ArrayList<Entity>) in tmap) {
      prepareTexturedModel(model)
      for (ent: Entity in ents) {
        prepareInstance(ent)
        GL11.glDrawElements(
            GL11.GL_TRIANGLES,
            model.model.vertexCount,
            GL11.GL_UNSIGNED_INT, 0)
      }
      unbindTexturedModel()
    }
  }
  
  fun prepareTexturedModel(model: ModelTextured) {
    GL30.glBindVertexArray(model.model.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL20.glEnableVertexAttribArray(1)
    GL20.glEnableVertexAttribArray(2)
    val texture = model.texture
    shader.loadNumOfRows(texture.numOfRows)
    if (texture.hasTransparency) RenderMgr.disableCulling()
    shader.loadFakeLightingVariable(texture.useFakeLighting)
    shader.loadShineVariables(texture.shineDamper, texture.reflectivity)
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.texture.textureID)
  }
  
  fun unbindTexturedModel() {
    RenderMgr.enableCulling()
    GL20.glDisableVertexAttribArray(0)
    GL20.glDisableVertexAttribArray(1)
    GL20.glDisableVertexAttribArray(2)
    GL30.glBindVertexArray(0)
  }
  
  fun prepareInstance(entity: Entity) {
    shader.loadTransformationMatrix(Maths.createTransformationMatrix(
        entity.pos,
        entity.rx,
        entity.ry,
        entity.rz,
        entity.scale))
    shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset())
  }
}