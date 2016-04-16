package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-03-01.
 */
class PosCtrl {
  companion object {
    val xVec = Vector3f(1,0,0)
    val yVec = Vector3f(0,1,0)
    val zVec = Vector3f(0,0,1)
  }
  val pos = Vector3f()
  var xPos: Double
    get() = pos.x
    set(n: Double) {
      pos.x = n
    }
  var yPos: Double
    get() = pos.y
    set(n: Double) {
      pos.y = n
    }
  var zPos: Double
    get() = pos.z
    set(n: Double) {
      pos.z = n
    }
  val posTest = Vector3f()
  var xPosTest: Double
    get() = posTest.x
    set(n: Double) {
      posTest.x = n
    }
  var yPosTest: Double
    get() = posTest.y
    set(n: Double) {
      posTest.y = n
    }
  var zPosTest: Double
    get() = posTest.z
    set(n: Double) {
      posTest.z = n
    }
  val vel = Vector3f()
  var xVel: Double
    get() = vel.x
    set(n: Double) {
      vel.x = n
    }
  var yVel: Double
    get() = vel.y
    set(n: Double) {
      vel.y = n
    }
  var zVel: Double
    get() = vel.z
    set(n: Double) {
      vel.z = n
    }
  val rot = Vector3f()
  var xRot: Double
    get() = rot.x
    set(n: Double) {
      rot.x = n
    }
  var yRot: Double
    get() = rot.y
    set(n: Double) {
      rot.y = n
    }
  var zRot: Double
    get() = rot.z
    set(n: Double) {
      rot.z = n
    }
  val rotv = Vector3f()
  var xRotVel: Double
    get() = rotv.x
    set(n: Double) {
      rotv.x = n
    }
  var yRotVel: Double
    get() = rotv.y
    set(n: Double) {
      rotv.y = n
    }
  var zRotVel: Double
    get() = rotv.z
    set(n: Double) {
      rotv.z = n
    }
  var drag = 0.0
  var rdrag = 0.0
  
  var scale = 1.0
  
  val gravity: Double get() = GameMgr.gravity
  val rate:Double get() = DisplayMgr.delta
  
  private val tr = Matrix4f()
  
  val transformMatrix: Matrix4f 
    get() {
      tr.setIdentity()
      Matrix4f.translate(pos, tr, tr)
      Matrix4f.rotate(Math.toRadians(xRot), xVec, tr, tr)
      Matrix4f.rotate(Math.toRadians(yRot), yVec, tr, tr)
      Matrix4f.rotate(Math.toRadians(zRot), zVec, tr, tr)
      Matrix4f.scale(Vector3f(scale, scale, scale), tr, tr)
      return tr
    }
  
  fun forward(speed: Double) {
    xVel = speed * Math.sin(Math.toRadians(yRot))
    zVel = speed * Math.cos(Math.toRadians(yRot))
  }
  
  fun upward(speed: Double) {
    yVel = speed
  }
  
  fun applyGravity() {
    yVel += gravity * rate
  }
  
  fun testPosUpdate() {
    xPosTest += (xVel * rate) - (drag * rate)
    yPosTest += (yVel * rate) - (drag * rate)
    zPosTest += (zVel * rate) - (drag * rate)
  }
  
  fun applyTest() {
    pos.set(posTest)
  }
  
  fun stop() { vel.set(0, 0, 0) }
}