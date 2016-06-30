package org.dellamorte.raum.engine

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
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
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
    val primaryBuffer = FBPostProc("Primary")
    val postProcs = HashMap<String, FBPostProc>()
    val postProcOrder = ArrayList<String>()
    val drawEntities = ArrayList<Entity>()
    val playerEntities: ArrayList<Entity> get() = GameMgr.player.nearbyObjects
    
    init {
      enableCulling()
    }
    
    fun enableCulling() {
      GL11.glEnable(GL11.GL_CULL_FACE)
      GL11.glCullFace(GL11.GL_BACK)
    }
    
    fun disableCulling() = GL11.glDisable(GL11.GL_CULL_FACE)
    
    fun prepare() {
      GL11.glEnable(GL11.GL_DEPTH_TEST)
      GL11.glClearColor(red.toFloat(), grn.toFloat(), blu.toFloat(), 1.0f)
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }
    
    fun render() {
      prepare()
      renderModel.shader.apply {
        start()
        loadUniformVars()
        renderModel.render(tmap)
        stop()
      }
      renderTerrain.shader.apply {
        start()
        loadUniformVars()
        renderTerrain.render(terrains)
        stop()
      }
      renderSkyBox.render(red, grn, blu)
    }
    
    fun renderScene() {
      drawEntities.clear()
      playerEntities.clear()
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
      render()
      ParticleMgr.render()
      if (GameMgr.drawWater) {
        renderWater.render(terrains)
        entityPicker.render()
      }
      //ParticleMgr.render()
      tmap.clear()
      terrains.clear()
    }
    
    fun renderPrimaryBuffer(withFBWater: Boolean = false) {
      GameMgr.apply {
        update()
        if (withFBWater) {
          drawWater = false
          GL11.glEnable(GL30.GL_CLIP_DISTANCE0)
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
          GL11.glDisable(GL30.GL_CLIP_DISTANCE0)
          drawWater = true
        }
        clipPlanePhase(2)
      }
      primaryBuffer.bind()
      RenderMgr.renderScene()
      primaryBuffer.unbind()
    }
  
    fun renderPostProcs() {
      var prevBuffer = "Primary"
      for (name in postProcOrder) {
        val postProc: FBPostProc? = postProcs[name]
        if (postProc != null) {
          
          prevBuffer = name
        }
      }
      GameMgr.guiRend.render("image$prevBuffer")
    }
    
    fun cleanUp() {
      renderModel.shader.cleanUp()
      renderTerrain.shader.cleanUp()
      entityPicker.shader.cleanUp()
    }
    
    fun addPostProc(name: String) {
      postProcs[name] = FBPostProc(name)
      postProcOrder.add(name)
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