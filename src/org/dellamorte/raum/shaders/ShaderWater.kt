package org.dellamorte.raum.shaders

import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.entities.Light
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import java.util.*

/**
 * Created by Raum on 2016-01-28.
 */
class ShaderWater : Shader("water") {
  val maxLights = 4
  
  override fun bindAttributes() {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations() {
    newLoc(
        "projectionMatrix", "viewMatrix", "modelMatrix",
        "reflectionTexture", "refractionTexture",
        "dudvMap", "normalMap", "depthMap",
        "moveFactor", "camPos", "skyColour")
    newLoc(4, "lightPos", "lightColour", "attenuation")
  }
  
  fun connectTextureUnits() {
    loadInt("reflectionTexture", 0)
    loadInt("refractionTexture", 1)
    loadInt("dudvMap", 2)
    loadInt("normalMap", 3)
    loadInt("depthMap", 4)
  }
  
  fun loadSkyColour(r: Double, g: Double, b: Double) {
    loadVector("skyColour", Vector3f(r,g,b))
  }
  
  fun loadMoveFactor(factor: Double) = 
      loadFloat("moveFactor", factor)
  
  fun loadLights(lights: ArrayList<Light>) {
    val self = this
    maxLights.times {
      self.apply {
        if (it < lights.size) {
          loadVector("lightPos[$it]", lights[it].pos)
          loadVector("lightColour[$it]", lights[it].color)
          loadVector("attenuation[$it]", lights[it].atten)
        } else {
          loadVector("lightPos[$it]", Vector3f(0, 0, 0))
          loadVector("lightColour[$it]", Vector3f(0, 0, 0))
          loadVector("attenuation[$it]", Vector3f(1, 0, 0))
        }
      }
    }
  }
  
  fun loadProjectionMatrix(matrix: Matrix4f) =
      loadMatrix("projectionMatrix", matrix)
  
  fun loadViewMatrix(camera: Camera) {
    loadMatrix("viewMatrix", Maths.createViewMatrix(camera))
    loadVector("camPos", camera.pos)
  }
  
  fun loadModelMatrix(matrix: Matrix4f) =
      loadMatrix("modelMatrix", matrix)
}