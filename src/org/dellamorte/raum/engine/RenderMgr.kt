package org.dellamorte.raum.engine

import org.dellamorte.raum.effectbuffers.FBO
import org.dellamorte.raum.effectbuffers.FBPostProc
import org.dellamorte.raum.entities.Entity
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.render.*
import org.dellamorte.raum.terrains.ChunkEntity
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.terrains.Water
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.*
import java.nio.FloatBuffer
import java.util.*

/**
 * Created by Raum on 2016-01-26.
 */
class RenderMgr {
  companion object {
    var fov = 70.0
    var nearPlane = 0.1
    var farPlane = 1000.0
    var red = 0.5444
    var grn = 0.62
    var blu = 0.69
    val projectionMatrix = createProjectionMatrix()
    
    val tmap = HashMap<ModelTextured, ArrayList<Entity>>()
    val terrains = TerrainList()
    val renderTerrain = RenderTerrain()
    val renderWater = RenderWater()
    val renderModel = RenderModel()
    val renderSkyBox = RenderSkyBox()
    val entityPicker = RenderEntityPicker()
    val primaryBuffer: FBO //FBPostProc 
      get() = GameMgr.primaryBuffer
    val postProcs = HashMap<String, RenderPostProc>()
    val postProcOrder = ArrayList<String>()
    val drawEntities = ArrayList<Entity>()
    val playerEntities: ArrayList<Entity> get() = GameMgr.player.nearbyObjects
    
    var fogfilter = 1
    val fogMode: IntArray = arrayOf(GL_EXP, GL_EXP2, GL_LINEAR).toIntArray()
    
    init {
      enableCulling()
    }
    
    fun enableCulling() {
      glEnable(GL_CULL_FACE)
      glCullFace(GL_BACK)
    }
    
    fun disableCulling() = glDisable(GL_CULL_FACE)
    
    fun enableFog() {
      val fogColour = BufferUtils.createFloatBuffer(4)
      fogColour.put(red.toFloat()).put(grn.toFloat()).put(blu.toFloat()).put(1.0f).flip()
      
      glFogi(GL_FOG_MODE, fogMode[fogfilter])            // Fog Mode
      glFogfv(GL_FOG_COLOR, fogColour)                   // Set Fog Color
      glFogf(GL_FOG_DENSITY, 0.35f)                      // How Dense Will The Fog Be
      glHint(GL_FOG_HINT, GL_DONT_CARE)                  // Fog Hint Value
      glFogf(GL_FOG_START, 0.0f)                         // Fog Start Depth
      glFogf(GL_FOG_END, 2.0f)                           // Fog End Depth
      glEnable(GL_FOG)
    }
    
    fun prepare() {
      glEnable(GL_DEPTH_TEST)
      glClearColor(red.toFloat(), grn.toFloat(), blu.toFloat(), 1.0f)
      glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }
    
    fun scenePrep() {
      drawEntities.clear()
      playerEntities.clear()
      tmap.clear()
      terrains.clear()
      for (ter: Terrain in GameMgr.world.list) {
        processTerrain(ter)
        for (chunk: ChunkEntity in ter.chunks) {
          chunk.processEntitiesToLists()
        }
      }
      for (ent in GameMgr.ents) {
        if (ent.isInScene()) drawEntities.add(ent)
      }
      sortEntities()
      for (ent: Entity in drawEntities) {
        processEntity(ent)
      }
      processEntity(GameMgr.player)
    }
    
    fun renderScene() {
      prepare()
      renderModel.render(tmap)
      renderTerrain.render(terrains)
      renderSkyBox.render(red, grn, blu)
      if (GameMgr.drawWater) {
        renderWater.render(terrains)
        ParticleMgr.render()
        entityPicker.render()
      } else {
        ParticleMgr.render()
      }
    }
    
    fun renderPrimaryBuffer(withFBWater: Boolean = false) {
      scenePrep()
      GameMgr.apply {
        update()
        if (withFBWater) {
          drawWater = false
          glEnable(GL_CLIP_DISTANCE0)
          clipPlanePhase(0)
          fbWater.bindReflectFB()
          camera.reflection(Water.waterLevel)
          RenderMgr.renderScene()
          //ParticleMgr.render()
          clipPlanePhase(1)
          fbWater.bindRefractFB()
          camera.restore()
          RenderMgr.renderScene()
          //ParticleMgr.render()
          fbWater.unbind()
          glDisable(GL_CLIP_DISTANCE0)
          drawWater = true
        }
        clipPlanePhase(2)
      }
      primaryBuffer.bind()
      //enableFog()
      RenderMgr.renderScene()
      primaryBuffer.unbind()
      primaryBuffer.bindToRead()
    }
  
    fun renderPostProcs() {
      PostProcMgr.render()
    }
    
    fun cleanUp() {
      renderModel.shader.cleanUp()
      renderTerrain.shader.cleanUp()
      entityPicker.shader.cleanUp()
    }
    
    fun processTerrain(terrain: Terrain) = terrains.add(terrain)
    
    fun processEntity(entity: Entity) {
      val tbox = tmap[entity.model] ?: ArrayList<Entity>()
      tbox.add(entity)
      tmap[entity.model] = tbox
    }
    
    fun createProjectionMatrix(): Matrix4f {
      val pMatrix = Matrix4f()
      DisplayMgr.updateWH()
      val aspectRatio = DisplayMgr.width.toDouble() / DisplayMgr.height.toDouble()
      val yScale = 1.0 / Math.tan(Math.toRadians(fov / 2.0))
      val xScale = yScale / aspectRatio
      val frustumLength = farPlane - nearPlane
      pMatrix.m00 = xScale
      pMatrix.m11 = yScale
      pMatrix.m22 = -((farPlane + nearPlane) / frustumLength)
      pMatrix.m23 = -1.0
      pMatrix.m32 = -(((2 * nearPlane) * farPlane) / frustumLength)
      pMatrix.m33 = 0.0
      return pMatrix
    }
  
    private fun sortEntities() {
      drawEntities.size.times(1) {
        val item: Entity = drawEntities[it]
        if (item.distance > drawEntities[it - 1].distance) {
          sortUpHighToLow(drawEntities, it)
        }
      }
    }
  
    private fun sortUpHighToLow(list: ArrayList<Entity>, i: Int) {
      val item: Entity = list[i]
      var attemptPos: Int = i - 1
      while (attemptPos != 0 && list[attemptPos - 1].distance < item.distance) {
        attemptPos--
      }
      list.remove(item)
      list.add(attemptPos, item)
    }
  }
}