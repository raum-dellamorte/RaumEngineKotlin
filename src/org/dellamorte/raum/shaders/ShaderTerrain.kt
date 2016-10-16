package org.dellamorte.raum.shaders

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.LightMgr
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.entities.Light
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f
import java.util.*

/**
 * Created by Raum on 2016-01-28.
 */
class ShaderTerrain : Shader("terrain") {
  val maxLights = 4
  
  override fun bindAttributes(attribs: ArrayList<String>?) {
    bindAttribute(0, "position")
    bindAttribute(1, "textureCoords")
  }
  
  override fun getAllUniformLocations(uniforms: ArrayList<String>?) {
    newLoc(
        "playerLoc",
        "transformationMatrix", "projectionMatrix", "viewMatrix",
        "shineDamper", "reflectivity",
        "skyColour", "useClipPlane", "plane", "blendMap",
        "bgTexture", "rTexture", "gTexture", "bTexture")
    newLoc(4, "lightPosition", "lightColour", "attenuation")
  }
  
  fun loadUniformVars() {
    loadSkyColour()
    loadUseClipPlane()
    loadClipPlane()
    loadViewMatrix()
    loadLights()
    loadPlayerLoc()
  }
  
  fun connectTextureUnits() {
    loadInt("bgTexture", 0)
    loadInt("rTexture", 1)
    loadInt("gTexture", 2)
    loadInt("bTexture", 3)
    loadInt("blendMap", 4)
  }
  
  fun loadSkyColour() {
    loadVector("skyColour", Vector3f(RenderMgr.red, RenderMgr.Companion.grn, RenderMgr.Companion.blu))
  }
  
  fun loadShineVariables(damper: Double, reflectivity: Double) {
    loadFloat("shineDamper", damper)
    loadFloat("reflectivity", reflectivity)
  }
  
  fun loadUseClipPlane() =
      loadBoolean("useClipPlane", !GameMgr.drawWater)
  
  fun loadClipPlane() {
    loadVector("plane", GameMgr.clipPlane)
  }
  
  fun loadLights() {
    val self = this
    maxLights.times {
      self.apply {
        if (it < LightMgr.lights.size) {
          loadVector("lightPosition[$it]", LightMgr.lights[it].pos)
          loadVector("lightColour[$it]", LightMgr.lights[it].color)
          loadVector("attenuation[$it]", LightMgr.lights[it].atten)
        } else {
          loadVector("lightPosition[$it]", Vector3f(0, 0, 0))
          loadVector("lightColour[$it]", Vector3f(0, 0, 0))
          loadVector("attenuation[$it]", Vector3f(1, 0, 0))
        }
      }
    }
  }
  
  fun loadTransformationMatrix(matrix:Matrix4f) =
      loadMatrix("transformationMatrix", matrix)
  
  fun loadViewMatrix() =
      loadMatrix("viewMatrix", GameMgr.camera.viewMatrix)
  
  fun loadProjectionMatrix(matrix: Matrix4f) = 
      loadMatrix("projectionMatrix", matrix)
}