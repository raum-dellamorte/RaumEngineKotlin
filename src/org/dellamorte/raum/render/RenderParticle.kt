package org.dellamorte.raum.render

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.entities.Particle
import org.dellamorte.raum.loaders.LoaderVBO
import org.dellamorte.raum.shaders.ShaderParticle
import org.dellamorte.raum.textures.TextureParticle
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import sun.plugin2.message.GetAppletMessage
import java.util.*

/**
 * Created by Raum on 2016-02-23.
 */
class RenderParticle {
  val positions = doubleArrayOf(-0.5,0.5,-0.5,-0.5,0.5,0.5,0.5,-0.5)
  val quad = GameMgr.loader.loadToVAO(positions, 2)
  val maxInstances = 10000
  val instanceDataLength = 21
  
  private val camera: Camera get() = GameMgr.camera
  val shader = ShaderParticle()
  var vbo = LoaderVBO.createEmptyVBO(maxInstances * instanceDataLength)
  val buffer = BufferUtils.createFloatBuffer(maxInstances * instanceDataLength)
  var ptr = 0
  
  init {
    LoaderVBO.addInstancedAttribute(quad.vaoID, vbo, 1, 4, instanceDataLength, 0)
    LoaderVBO.addInstancedAttribute(quad.vaoID, vbo, 2, 4, instanceDataLength, 4)
    LoaderVBO.addInstancedAttribute(quad.vaoID, vbo, 3, 4, instanceDataLength, 8)
    LoaderVBO.addInstancedAttribute(quad.vaoID, vbo, 4, 4, instanceDataLength, 12)
    LoaderVBO.addInstancedAttribute(quad.vaoID, vbo, 5, 4, instanceDataLength, 16)
    LoaderVBO.addInstancedAttribute(quad.vaoID, vbo, 6, 1, instanceDataLength, 20)
    shader.start()
    shader.loadProjectionMatrix(RenderMgr.projectionMatrix)
    shader.stop()
  }
  
  fun render(particles: HashMap<TextureParticle, ArrayList<Particle>>) {
    val viewMatrix = camera.viewMatrix
    prepare()
    for (texture: TextureParticle in particles.keys) {
      bindTexture(texture)
      val parts = particles[texture]!!
      ptr = 0
      val vboData = FloatArray(parts.size * instanceDataLength)
      for (p in parts) {
        updateModelViewMatrix(p, viewMatrix, vboData)
        updateTexCoordInfo(p, vboData)
      }
      LoaderVBO.updateVBO(vbo, vboData, buffer)
      GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.vertexCount, parts.size)
    }
    finishRendering()
  }
  
  fun bindTexture(texture: TextureParticle) {
    if (texture.additive) {
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE)
    } else {
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    }
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)
    shader.loadNumOfRows(texture.numOfRows)
  }
  
  fun updateTexCoordInfo(p: Particle, out: FloatArray) {
    out[ptr++] = p.texOffset1.xf
    out[ptr++] = p.texOffset1.yf
    out[ptr++] = p.texOffset2.xf
    out[ptr++] = p.texOffset2.yf
    out[ptr++] = p.blendf
  }
  
  fun updateModelViewMatrix(p: Particle, viewMatrix: Matrix4f, out: FloatArray) {
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
    //shader.loadModelViewMatrix(modelViewMatrix)
    storeMatrixData(modelViewMatrix, out)
  }
  
  fun storeMatrixData(matrix: Matrix4f, out: FloatArray) {
    out[ptr++] = matrix.m00.toFloat()
    out[ptr++] = matrix.m01.toFloat()
    out[ptr++] = matrix.m02.toFloat()
    out[ptr++] = matrix.m03.toFloat()
    out[ptr++] = matrix.m10.toFloat()
    out[ptr++] = matrix.m11.toFloat()
    out[ptr++] = matrix.m12.toFloat()
    out[ptr++] = matrix.m13.toFloat()
    out[ptr++] = matrix.m20.toFloat()
    out[ptr++] = matrix.m21.toFloat()
    out[ptr++] = matrix.m22.toFloat()
    out[ptr++] = matrix.m23.toFloat()
    out[ptr++] = matrix.m30.toFloat()
    out[ptr++] = matrix.m31.toFloat()
    out[ptr++] = matrix.m32.toFloat()
    out[ptr++] = matrix.m33.toFloat()
  }
  
  fun cleanUp() = shader.cleanUp()
  
  fun prepare() {
    shader.start()
    GL30.glBindVertexArray(quad.vaoID)
    GL20.glEnableVertexAttribArray(0)
    GL20.glEnableVertexAttribArray(1)
    GL20.glEnableVertexAttribArray(2)
    GL20.glEnableVertexAttribArray(3)
    GL20.glEnableVertexAttribArray(4)
    GL20.glEnableVertexAttribArray(5)
    GL20.glEnableVertexAttribArray(6)
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glDepthMask(false)
  }
  
  fun finishRendering() {
    GL11.glDepthMask(true)
    GL11.glDisable(GL11.GL_BLEND)
    GL20.glDisableVertexAttribArray(0)
    GL20.glDisableVertexAttribArray(1)
    GL20.glDisableVertexAttribArray(2)
    GL20.glDisableVertexAttribArray(3)
    GL20.glDisableVertexAttribArray(4)
    GL20.glDisableVertexAttribArray(5)
    GL20.glDisableVertexAttribArray(6)
    GL30.glBindVertexArray(0)
    shader.stop()
  }
}