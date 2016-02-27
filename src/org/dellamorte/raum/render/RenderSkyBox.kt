package org.dellamorte.raum.render

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.shaders.ShaderSkyBox
import org.dellamorte.raum.vector.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

/**
 * Created by Raum on 2016-01-28.
 */
class RenderSkyBox(val shader: ShaderSkyBox) {
  constructor() : this(ShaderSkyBox())
  private val camera: Camera get() = GameMgr.camera
  
  var size = 500.0
  val verts = doubleArrayOf(
      -size,  size, -size,
      -size, -size, -size,
       size, -size, -size,
       size, -size, -size,
       size,  size, -size,
      -size,  size, -size,
    
      -size, -size,  size,
      -size, -size, -size,
      -size,  size, -size,
      -size,  size, -size,
      -size,  size,  size,
      -size, -size,  size,
    
       size, -size, -size,
       size, -size,  size,
       size,  size,  size,
       size,  size,  size,
       size,  size, -size,
       size, -size, -size,
    
      -size, -size,  size,
      -size,  size,  size,
       size,  size,  size,
       size,  size,  size,
       size, -size,  size,
      -size, -size,  size,
    
      -size,  size, -size,
       size,  size, -size,
       size,  size,  size,
       size,  size,  size,
      -size,  size,  size,
      -size,  size, -size,
    
      -size, -size, -size,
      -size, -size,  size,
       size, -size, -size,
       size, -size, -size,
      -size, -size,  size,
       size, -size,  size
  )
  
  val cube = GameMgr.loader.loadToVAO(verts, 3)
  val dtexture = GameMgr.loader.loadCubeMap("day")
  val ntexture = GameMgr.loader.loadCubeMap("night")
  var time = 0.0
  
  init {
    shader.start()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.stop()
  }
  
  fun render(r: Double, g: Double, b: Double) {
    shader.start()
    shader.connectTextureUnits()
    shader.loadViewMatrix(camera)
    shader.loadFogColour(r, g, b)
    GL30.glBindVertexArray(cube.vaoID)
    GL20.glEnableVertexAttribArray(0)
    bindTextures()
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.vertexCount)
    GL20.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    shader.stop()
  }
  
  fun bindTextures() {
    time += DisplayMgr.getFrameTimeSeconds() * 200.0
    time %= 24000.0
    val texture1: Int
    val texture2: Int
    val blendFactor: Double
    if ((time >= 0) && (time < 5000.0)) {
      texture1 = ntexture
      texture2 = ntexture
      blendFactor = time / 5000.0
    } else if ((time >= 5000.0) && (time < 8000.0)) {
      texture1 = ntexture
      texture2 = dtexture
      blendFactor = (time - 5000.0) / (8000.0 - 5000.0)
    } else if ((time >= 8000.0) && (time < 21000.0)) {
      texture1 = dtexture
      texture2 = dtexture
      blendFactor = (time - 8000.0) / (21000.0 - 8000.0)
    } else {
      texture1 = dtexture
      texture2 = ntexture
      blendFactor = (time - 21000.0) / (24000.0 - 21000.0)
    }
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1)
    GL13.glActiveTexture(GL13.GL_TEXTURE1)
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2)
    shader.loadBlendFactor(blendFactor)
  }
}