package org.dellamorte.raum.shaders

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.LightMgr
import org.dellamorte.raum.engine.RenderMgr
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
  
  override fun bindAttributes(attribs: ArrayList<String>?) {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations(uniforms: ArrayList<String>?) {
    newLoc(
        "playerLoc",
        "projectionMatrix", "viewMatrix", "modelMatrix",
        "reflectionTexture", "refractionTexture",
        "dudvMap", "normalMap", "depthMap",
        "moveFactor", "camPos", "skyColour")
    newLoc(4, "lightPos", "lightColour", "attenuation")
  }
  
  fun loadUniformVars() {
    loadLights()
    loadSkyColour()
    loadViewMatrix()
    loadPlayerLoc()
  }
  
  fun connectTextureUnits() {
    loadInt("reflectionTexture", 0)
    loadInt("refractionTexture", 1)
    loadInt("dudvMap", 2)
    loadInt("normalMap", 3)
    loadInt("depthMap", 4)
  }
  
  fun loadSkyColour() {
    loadVector("skyColour", Vector3f(RenderMgr.red, RenderMgr.Companion.grn, RenderMgr.Companion.blu))
  }
  
  fun loadMoveFactor(factor: Double) = 
      loadFloat("moveFactor", factor)
  
  fun loadLights() {
    val self = this
    maxLights.times {
      self.apply {
        if (it < LightMgr.lights.size) {
          loadVector("lightPos[$it]", LightMgr.lights[it].pos)
          loadVector("lightColour[$it]", LightMgr.lights[it].color)
          loadVector("attenuation[$it]", LightMgr.lights[it].atten)
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
  
  fun loadViewMatrix() {
    loadMatrix("viewMatrix", GameMgr.camera.viewMatrix)
    loadVector("camPos", GameMgr.camera.pos)
  }
  
  fun loadModelMatrix(matrix: Matrix4f) =
      loadMatrix("modelMatrix", matrix)
}