package org.dellamorte.raum.shaders

import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f
import org.lwjgl.opengl.*
import java.util.*

/**
 * Created by Raum on 2016-01-30.
 */
class ShaderGui : Shader("gui") {
  override fun bindAttributes(attribs: ArrayList<String>?) {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations(uniforms: ArrayList<String>?) {
    newLoc("transformationMatrix", "numOfRows", "offset", "guiTexture", "flipYAxis")
  }
  
  fun connectTextureUnits() {
    loadInt("guiTexture", 0)
  }
  
  fun loadNumberOfRows(value: Int) =
      loadFloat("numOfRows", value)
  
  fun loadOffset(x: Double, y: Double) =
      loadVector("offset", Vector2f(x, y))
  
  fun loadTransformation(matrix: Matrix4f) =
      loadMatrix("transformationMatrix", matrix)
  
  fun flipY(flip: Boolean) = loadFloat("flipYAxis", if (flip) 1.0 else 0.0)
}