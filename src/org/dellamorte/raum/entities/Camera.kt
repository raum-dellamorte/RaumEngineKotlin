package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-27.
 */
class Camera(var player: Player) {
  val pos = Vector3f(0,5,0)
  val posBak = Vector3f(0,5,0)
  var pitch = 20.0
  var pitchBak = 20.0
  var yaw = 0.0
  var yawBak = 0.0
  var roll = 0.0
  var rollBak = 0.0
  var distFromPlayer = 30.0
  var angleAroundPlayer = 0.0
  
  private val toPos = Vector3f()
  private val toPlayerPos = Vector3f()
  
  private val viewMat = Matrix4f()
  val viewMatrix: Matrix4f get() = Maths.createViewMatrix(viewMat, this)
  
  init {
    attachListeners()
  }
  
  fun attachListeners() {
    var camera = this
    DisplayMgr.mouse.scroll.setListener { dx: Double, dy: Double ->
      camera.distFromPlayer -= dy * 0.5
    }
  }
  
  fun store() {
    posBak.set(pos.x, pos.y, pos.z)
    pitchBak = pitch
    yawBak = yaw
    rollBak = roll
  }
  
  fun restore() {
    pos.set(posBak.x, posBak.y, posBak.z)
    pitch = pitchBak
    yaw = yawBak
    roll = rollBak
  }
  
  fun move() {
    calcPitch()
    calcAngle()
    calcCamPos()
  }
  
  private fun calcPitch() {
    if (DisplayMgr.mouse.isButtonDown(2)) pitch -= DisplayMgr.mouse.pos.getDY() * 0.1
  }
  
  private fun calcAngle() {
    if (DisplayMgr.mouse.isButtonDown(2)) angleAroundPlayer -= DisplayMgr.mouse.pos.getDX() * 0.3
  }
  
  private fun calcCamPos() {
    val hDist: Double = calcHDistance()
    val vDist: Double = calcVDistance() + 10.0
    val theta = player.ry + angleAroundPlayer
    val xOffset = hDist * Math.sin(Math.toRadians(theta))
    val zOffset = hDist * Math.cos(Math.toRadians(theta))
    pos.x = player.pos.x - xOffset
    pos.z = player.pos.z - zOffset
    pos.y = player.pos.y + vDist
    yaw = 180.0 - (player.ry + angleAroundPlayer)
  }
  
  private fun calcHDistance(): Double = 
      distFromPlayer * Math.cos(Math.toRadians(pitch))
  private fun calcVDistance(): Double = 
      distFromPlayer * Math.sin(Math.toRadians(pitch))
  
  fun reflection(height: Double) {
    store()
    val dist = 2.0 * (pos.y - height)
    pos.y -= dist
    invertPitch()
  }
  
  fun invertPitch() {
    pitch = -pitch
  }
  
  fun distToPos(vec: Vector3f): Double {
    return Vector3f.sub(vec, pos, toPos).length()
  }
  
  fun angleToEntity(entity: Entity): Double {
    entity.distance = distToPos(entity.pos)
    toPos.normalize()
    Vector3f.sub(player.pos, pos, toPlayerPos)
    toPlayerPos.normalize()
    return Vector3f.dot(toPlayerPos, toPos)
  }
}