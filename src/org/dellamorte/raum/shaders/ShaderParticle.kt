package org.dellamorte.raum.shaders

import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f

/**
 * Created by Raum on 2016-02-23.
 */
class ShaderParticle : Shader("particle") {
  override fun bindAttributes() {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations() {
    newLoc(
        "modelViewMatrix",
        "projectionMatrix",
        "texOffset1",
        "texOffset2",
        "texCoordInfo"
    )
  }
  
  fun loadModelViewMatrix(matrix: Matrix4f) = loadMatrix("modelViewMatrix", matrix)
  
  fun loadProjectionMatrix(matrix: Matrix4f) = loadMatrix("projectionMatrix", matrix)
  
  fun loadTexCoordInfo(offset1: Vector2f, offset2: Vector2f, numRows: Double, blend: Double) {
    loadVector("texOffset1", offset1)
    loadVector("texOffset2", offset2)
    loadVector("texCoordInfo", Vector2f(numRows, blend))
  }
}