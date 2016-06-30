package org.dellamorte.raum.render

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.entities.GuiObj
import org.dellamorte.raum.shaders.ShaderGui
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Vector2f
import org.lwjgl.opengl.*
import java.util.*

/**
 * Created by Raum on 2016-01-28.
 */
class RenderGui(val shader: ShaderGui) {
  constructor() : this(ShaderGui())
  
  val positions = doubleArrayOf(-1.0,1.0,-1.0,-1.0,1.0,1.0,1.0,-1.0)
  val quad = GameMgr.loader.loadToVAO(positions)
  val fullscreen = Maths.createTransformationMatrixGui(Vector2f(0.5,0.5), Vector2f(1, 1))
  
  fun render(guis: ArrayList<GuiObj>) {
    shader.apply { 
      start()
      connectTextureUnits()
      loadOffset(0.0, 0.0)
      loadNumberOfRows(1)
    }
    GL30.glBindVertexArray(quad.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glDisable(GL11.GL_DEPTH_TEST)
    for (gui in guis) {
      for (gt in gui.guiTextures) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        //println("TextureGui ID: ${gt.id}")
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, gt.id)
        shader.loadTransformation(gt.transformation)
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.vertexCount)
      }
    }
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    GL11.glDisable(GL11.GL_BLEND)
    GL20.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    shader.stop()
  }
  
  fun render(texture: String) {
    shader.apply {
      start()
      connectTextureUnits()
      loadOffset(0.0, 0.0)
      loadNumberOfRows(1)
    }
    GL30.glBindVertexArray(quad.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glDisable(GL11.GL_DEPTH_TEST)
    GL11.glDisable(GL11.GL_CULL_FACE)
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    val id = GameMgr.getTexture(texture)
    //println("FullScreen Texture ID: $id")
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
    shader.loadTransformation(fullscreen)
    shader.flipY(true)
    GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.vertexCount)
    GL11.glEnable(GL11.GL_DEPTH_TEST)
    //GL11.glEnable(GL11.GL_CULL_FACE)
    GL11.glDisable(GL11.GL_BLEND)
    GL20.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    shader.flipY(false)
    shader.stop()
  }
  
  fun cleanUp() = shader.cleanUp()
}