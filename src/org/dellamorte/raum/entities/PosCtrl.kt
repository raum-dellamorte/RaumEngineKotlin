package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-03-01.
 */
class PosCtrl {
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
  
  val rate:Double get() = DisplayMgr.delta
  
  fun update() {
    xPos += (xVel * rate) - (drag * rate)
    yPos += (yVel * rate) - (drag * rate)
    zPos += (zVel * rate) - (drag * rate)
  }
  
  
}