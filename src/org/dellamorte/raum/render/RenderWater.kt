package org.dellamorte.raum.render

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.entities.Light
import org.dellamorte.raum.models.ModelRaw
import org.dellamorte.raum.shaders.ShaderWater
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.terrains.Water
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.vector.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-01-28.
 */
class RenderWater() {
  private val camera: Camera get() = GameMgr.camera
  val shader = ShaderWater()
  val quad: ModelRaw = GameMgr.loader.loadToVAO(
      arrayOf(-1.0, -1.0,
              -1.0,  1.0,
               1.0, -1.0,
               1.0, -1.0,
              -1.0,  1.0,
               1.0,  1.0).toDoubleArray(), 2)
  var moveFactor = 0.0
  
  init {
    shader.start()
    shader.connectTextureUnits()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.stop()
    println("RenderWater.quad: vaoID ${quad.vaoID}, vertexCount ${quad.vertexCount}")
  }
  
  fun render(terrains: TerrainList) {
    //println("waters size ${terrains.list.size}")
    shader.start()
    shader.loadUniformVars()
    moveFactor += 0.01 * DisplayMgr.delta
    moveFactor %= 1
    shader.loadMoveFactor(moveFactor)
    GL30.glBindVertexArray(quad.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("reflectTexture"))
    GL13.glActiveTexture(GL13.GL_TEXTURE1)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("refractTexture"))
    GL13.glActiveTexture(GL13.GL_TEXTURE2)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("waterDUDV"))
    GL13.glActiveTexture(GL13.GL_TEXTURE3)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("normalMap"))
    GL13.glActiveTexture(GL13.GL_TEXTURE4)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("depthTexture"))
  
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    for (terrain: Terrain in terrains.list) {
      if (terrain.withWater) {
        val modelMatrix = Maths.createTransformationMatrix( terrain.water.toVector(), 0.0, 0.0, 0.0, terrain.water.size)
        shader.loadModelMatrix(modelMatrix)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.vertexCount)
      }
    }
    GL11.glDisable(GL11.GL_BLEND)
    GL20.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    shader.stop()
  }
}