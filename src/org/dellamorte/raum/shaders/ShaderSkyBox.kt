package org.dellamorte.raum.shaders

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import java.util.*

/**
 * Created by Raum on 2016-01-30.
 */
class ShaderSkyBox : Shader("skybox") {
  val rotSpeed = 0.15
  var rotation = 0.0
  
  override fun bindAttributes(attribs: ArrayList<String>?) {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations(uniforms: ArrayList<String>?) {
    newLoc(
        "projectionMatrix", "viewMatrix", "fogColour",
        "cubeMap1", "cubeMap2", "blendFactor"
    )
  }
  
  fun connectTextureUnits() {
    loadInt("cubeMap1", 0)
    loadInt("cubeMap2", 1)
  }
  
  fun loadViewMatrix(camera: Camera) {
    val matrix = camera.viewMatrix
    matrix.m30 = 0.0
    matrix.m31 = 0.0
    matrix.m32 = 0.0
    rotation += rotSpeed * (DisplayMgr.getFrameTimeSeconds())
    val rads = Math.toRadians(rotation)
    Matrix4f.rotate(rads, Vector3f(0, 1, 0), matrix, matrix)
    loadMatrix("viewMatrix", matrix)
  }
  
  fun loadProjectionMatrix(matrix: Matrix4f) =
      loadMatrix("projectionMatrix", matrix)
  
  fun loadFogColour(r: Double, g: Double, b: Double) = 
      loadVector("fogColour", Vector3f(r,g,b))
  
  fun loadBlendFactor(factor: Double) =
      loadFloat("blendFactor", factor)
  
}