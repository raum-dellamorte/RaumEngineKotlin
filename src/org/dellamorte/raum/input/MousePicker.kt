package org.dellamorte.raum.input

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.RenderMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-02-03.
 */
class MousePicker() {
  private val RECURSION_COUNT = 200
  private val RAY_RANGE = 600.0
  var currentRay = Vector3f()
  private val camera: Camera get() = GameMgr.camera
  private val projectionMatrix: Matrix4f get() = RenderMgr.projectionMatrix
  private val viewMatrix: Matrix4f get() = GameMgr.camera.viewMatrix
  private val world: TerrainList get() = GameMgr.world
  private val mouseX: Double get() = DisplayMgr.mPos.xPos
  private val mouseY: Double get() = DisplayMgr.mPos.yPos
  private val terrain: Terrain? = null
  var currentTerrainPoint: Vector3f? = null
  
  fun update() {
    calculateMouseRay(currentRay)
    if (intersectionInRange(0.0, RAY_RANGE, currentRay)) {
      currentTerrainPoint = binarySearch(0, 0.0, RAY_RANGE, currentRay)
    } else {
      currentTerrainPoint = null
    }
  }
  
  private fun calculateMouseRay(mouseRay: Vector3f) {
    val eyeCoords = Matrix4f.transform(
        Matrix4f.invert(projectionMatrix, null)!!, 
        Vector4f((2.0 * mouseX) / DisplayMgr.width - 1.0, (2.0 * mouseY) / DisplayMgr.height - 1.0, -1.0, 1.0), null)
    val rayWorld = Matrix4f.transform(
        Matrix4f.invert(viewMatrix, null)!!, 
        Vector4f(eyeCoords.x, eyeCoords.y, -1.0, 0.0), null)
    mouseRay.set(rayWorld.x, rayWorld.y, rayWorld.z)
    mouseRay.normalize()
  }
  //**********************************************************
  
  private fun getPointOnRay(ray:Vector3f, distance: Double):Vector3f {
    val start = Vector3f(camera.pos)
    val scaledRay = Vector3f(ray.x * distance, ray.y * distance, ray.z * distance)
    return Vector3f.add(start, scaledRay, null)
  }
  private fun binarySearch(count:Int, start: Double, finish: Double, ray:Vector3f):Vector3f? {
    val half = start + ((finish - start) / 2f)
    if (count >= RECURSION_COUNT) {
      val endPoint = getPointOnRay(ray, half)
      val terrain = getTerrain(endPoint.x, endPoint.z)
      if (terrain != null) {
        return endPoint
      } else {
        return null
      }
    }
    if (intersectionInRange(start, half, ray)) {
      return binarySearch(count + 1, start, half, ray)
    } else {
      return binarySearch(count + 1, half, finish, ray)
    }
  }
  
  private fun intersectionInRange(start: Double, finish: Double, ray:Vector3f): Boolean {
    val startPoint = getPointOnRay(ray, start)
    val endPoint = getPointOnRay(ray, finish)
    if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
      return true
    } else {
      return false
    }
  }
  private fun isUnderGround(testPoint: Vector3f):Boolean {
    val terrain = getTerrain(testPoint.x, testPoint.z)
    var height = 0.0
    if (terrain != null) {
      height = terrain.getHeightOfTerrain(testPoint.x, testPoint.z)
    }
    if (testPoint.y < height) {
      return true
    } else {
      return false
    }
  }
  private fun getTerrain(worldX: Double, worldZ: Double): Terrain? {
    return world.getTerrainAt(worldX, worldZ)
  }
}