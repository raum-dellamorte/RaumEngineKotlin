package org.dellamorte.raum.render

import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.models.ModelRaw
import org.dellamorte.raum.shaders.ShaderTerrain
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-01-28.
 */
class RenderTerrain {
  val shader = ShaderTerrain()
  
  init {
    shader.start()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.connectTextureUnits()
    shader.stop()
  }
  
  fun render(world: TerrainList) {
    for (terrain in world.list) {
      prepareTerrain(terrain)
      loadModelMatrix(terrain)
      GL11.glDrawElements(
          GL11.GL_TRIANGLES,
          terrain.model.vertexCount,
          GL11.GL_UNSIGNED_INT, 0)
      unbindTexturedModel()
    }
  }
  
  fun prepareTerrain(terrain: Terrain) {
    val model = terrain.model
    GL30.glBindVertexArray(model.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL20.glEnableVertexAttribArray(1)
    GL20.glEnableVertexAttribArray(2)
    bindTextures(terrain)
    shader.loadShineVariables(1.0, 0.0)
  }
  
  fun bindTextures(terrain: Terrain) {
    val textures = terrain.textures
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.bgTexture.id)
    GL13.glActiveTexture(GL13.GL_TEXTURE1)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.rTexture.id)
    GL13.glActiveTexture(GL13.GL_TEXTURE2)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.gTexture.id)
    GL13.glActiveTexture(GL13.GL_TEXTURE3)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.bTexture.id)
    GL13.glActiveTexture(GL13.GL_TEXTURE4)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.blendMap.id)
  }
  
  fun unbindTexturedModel() {
    GL20.glDisableVertexAttribArray(0)
    GL20.glDisableVertexAttribArray(1)
    GL20.glDisableVertexAttribArray(2)
    GL30.glBindVertexArray(0)
  }
  
  fun loadModelMatrix(terrain:Terrain) {
    val transformationMatrix = Maths.createTransformationMatrix(
        Vector3f(terrain.x, 0.0, terrain.z),
        0.0, 0.0, 0.0, 1.0)
    shader.loadTransformationMatrix(transformationMatrix)
  }
}