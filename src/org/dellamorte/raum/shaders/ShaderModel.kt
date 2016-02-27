package org.dellamorte.raum.shaders

import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.entities.Light
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f
import java.util.*

/**
 * Created by Raum on 2016-01-25.
 */
class ShaderModel : Shader("model") {
  val maxLights = 4
  
  override fun bindAttributes() {
    bindAttribute(0, "position")
    bindAttribute(1, "textureCoords")
  }
  
  override fun getAllUniformLocations() {
    newLoc(
        "transformationMatrix",
        "projectionMatrix",
        "viewMatrix",
        "shineDamper",
        "reflectivity",
        "useFakeLighting",
        "skyColour",
        "numOfRows",
        "offset",
        "plane")
    newLoc(4, "lightPosition", "lightColour", "attenuation")
  }
  
  fun loadNumOfRows(i: Int) = 
      loadFloat("numOfRows", i.toFloat())
  
  fun loadOffset(x: Double, y: Double) = 
      loadVector("offset", Vector2f(x, y))
  
  fun loadSkyColour(r: Double, g: Double, b: Double) = 
      loadVector("skyColour", Vector3f(r, g, b))
  
  fun loadFakeLightingVariable(bool: Boolean) = 
      loadBoolean("useFakeLighting", bool)
  
  fun loadShineVariables(damper: Double, reflectivity: Double) {
    loadFloat("shineDamper", damper)
    loadFloat("reflectivity", reflectivity)
  }
  
  fun loadLights(lights: ArrayList<Light>) {
    val self = this
    maxLights.times {
      self.apply {
        if (it < lights.size) {
          loadVector("lightPosition[$it]", lights[it].pos)
          loadVector("lightColour[$it]", lights[it].color)
          loadVector("attenuation[$it]", lights[it].atten)
        } else {
          loadVector("lightPosition[$it]", Vector3f(0, 0, 0))
          loadVector("lightColour[$it]", Vector3f(0, 0, 0))
          loadVector("attenuation[$it]", Vector3f(1, 0, 0))
        }
      }
    }
  }
  
  fun loadClipPlane(plane: Vector4f) =
      loadVector("plane", plane)
  
  fun loadTransformationMatrix(matrix: Matrix4f) =
      loadMatrix("transformationMatrix", matrix)
  
  fun loadViewMatrix(camera: Camera) =
      loadMatrix("viewMatrix", Maths.createViewMatrix(camera))
  
  fun loadProjectionMatrix(matrix: Matrix4f) =
      loadMatrix("projectionMatrix", matrix)
  
}