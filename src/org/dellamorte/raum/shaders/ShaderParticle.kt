package org.dellamorte.raum.shaders

import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f
import java.util.*

/**
 * Created by Raum on 2016-02-23.
 */
class ShaderParticle : Shader("particle") {
  override fun bindAttributes(attribs: ArrayList<String>?) {
    bindAttribute(0, "position")
    bindAttribute(1, "modelViewMatrix")
    bindAttribute(5, "texOffsets")
    bindAttribute(6, "blendFactor")
  }
  
  override fun getAllUniformLocations(uniforms: ArrayList<String>?) {
    newLoc(
        "projectionMatrix",
        "numOfRows"
    )
  }
  
  fun loadProjectionMatrix(matrix: Matrix4f) = loadMatrix("projectionMatrix", matrix)
  
  fun loadNumOfRows(n: Int) = loadFloat("numOfRows", n)
}