package org.dellamorte.raum.render

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.entities.Particle
import org.dellamorte.raum.shaders.ShaderParticle
import org.dellamorte.raum.textures.TextureParticle
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import sun.plugin2.message.GetAppletMessage
import java.util.*

/**
 * Created by Raum on 2016-02-23.
 */
class RenderParticle {
  val positions = doubleArrayOf(-0.5,0.5,-0.5,-0.5,0.5,0.5,0.5,-0.5)
  val quad = GameMgr.loader.loadToVAO(positions, 2)
  
  private val camera: Camera get() = GameMgr.camera
  val shader = ShaderParticle()
  
  init {
    println("quad id ${quad.vaoID}")
    shader.start()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.stop()
  }
  
  fun render(particles: HashMap<TextureParticle, ArrayList<Particle>>) {
    val viewMatrix = camera.viewMatrix
    prepare()
    for (texture: TextureParticle in particles.keys) {
      GL13.glActiveTexture(GL13.GL_TEXTURE0)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)
      for (p in particles[texture]!!) {
        updateModelViewMatrix(p, viewMatrix)
        shader.loadTexCoordInfo(p.texOffset1, p.texOffset2, p.numRows, p.blend)
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.vertexCount)
      }
    }
    finishRendering()
  }
  
  fun updateModelViewMatrix(p: Particle, viewMatrix: Matrix4f) {
    val modelMatrix = Matrix4f()
    modelMatrix.translate(p.pos)
    modelMatrix.m00 = viewMatrix.m00
    modelMatrix.m01 = viewMatrix.m10
    modelMatrix.m02 = viewMatrix.m20
    modelMatrix.m10 = viewMatrix.m01
    modelMatrix.m11 = viewMatrix.m11
    modelMatrix.m12 = viewMatrix.m21
    modelMatrix.m20 = viewMatrix.m02
    modelMatrix.m21 = viewMatrix.m12
    modelMatrix.m22 = viewMatrix.m22
    modelMatrix.rotate(Math.toRadians(p.rot), Vector3f(0, 0, 1))
    modelMatrix.scale(Vector3f(p.scale, p.scale, p.scale))
    val modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null)
    shader.loadModelViewMatrix(modelViewMatrix)
  }
  
  fun cleanUp() = shader.cleanUp()
  
  fun prepare() {
    shader.start()
    GL30.glBindVertexArray(quad.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glDepthMask(false)
    RenderMgr.disableCulling()
  }
  
  fun finishRendering() {
    GL11.glDepthMask(true)
    GL11.glDisable(GL11.GL_BLEND)
    GL20.glDisableVertexAttribArray(0)
    GL30.glBindVertexArray(0)
    RenderMgr.enableCulling()
    shader.stop()
  }
}