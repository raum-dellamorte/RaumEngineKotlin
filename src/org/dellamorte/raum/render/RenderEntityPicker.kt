package org.dellamorte.raum.render

import org.dellamorte.raum.effectbuffers.FBEntityPicker
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Entity
import org.dellamorte.raum.models.ModelRaw
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.shaders.ShaderEntityPicker
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-03-02.
 */
class RenderEntityPicker(val shader: ShaderEntityPicker) {
  constructor() : this(ShaderEntityPicker()) {}
  
  val shader2 = ShaderEntityPicker()
  
  companion object {
    val fbuffer = FBEntityPicker()
    val terrainColour = Vector4f(0, 0, 0, 1)
    
    fun cleanUp() {
      fbuffer.cleanUp()
    }
  }
  
  init {
    shader.start()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.stop()
    shader2.start()
    shader2.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader2.stop()
  }
  
  fun render() {
    fbuffer.bind()
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    GL11.glEnable(GL11.GL_CULL_FACE)
    GL11.glCullFace(GL11.GL_BACK)
    shader.start()
    shader.loadUniformVars()
    for ((model: ModelTextured, ents: ArrayList<Entity>) in RenderMgr.tmap) {
      enableVAA(model.model)
      for (ent: Entity in ents) {
        shader.loadTransformationMatrix(ent.transformationMatrix)
        shader.loadEntityColour(ent.colourID)
        drawModel(model.model)
      }
      disableVAA()
    }
    //shader.stop()
    //shader2.start()
    //shader2.loadUniformVars()
    shader.loadEntityColour(terrainColour)
    for (terrain: Terrain in RenderMgr.terrains.list) {
      enableVAA(terrain.model)
      shader.loadTransformationMatrix(terrain.transformationMatrix)
      drawModel(terrain.model)
      disableVAA()
    }
    shader.stop()
    fbuffer.unbind()
  }
  
  fun drawModel(model: ModelRaw) {
    GL11.glDrawElements(
        GL11.GL_TRIANGLES,
        model.vertexCount,
        GL11.GL_UNSIGNED_INT, 0)
  }
  
  fun enableVAA(model: ModelRaw) {
    GL30.glBindVertexArray(model.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL20.glEnableVertexAttribArray(1)
    GL20.glEnableVertexAttribArray(2)
  }
  
  fun disableVAA() {
    GL20.glDisableVertexAttribArray(0)
    GL20.glDisableVertexAttribArray(1)
    GL20.glDisableVertexAttribArray(2)
    GL30.glBindVertexArray(0)
  }
}