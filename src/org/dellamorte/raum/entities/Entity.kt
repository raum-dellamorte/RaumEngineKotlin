package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.tools.ColourID
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-01-26.
 */
open class Entity(val model: ModelTextured,
                  val index: Int,
                  val pos: Vector3f,
                  var rx: Double,
                  var ry: Double,
                  var rz: Double,
                  var scale: Double) {
  val posCtrl = PosCtrl()
  
  private val transMat = Matrix4f()
  val transformationMatrix: Matrix4f
      get() = Maths.createTransformationMatrix(transMat, pos, rx, ry, rz, scale)
  
  val collisionVec = Vector3f()
  var collisionAngle = 0.0
  
  val moveVec = Vector3f()
  val newPos = Vector3f()
  
  var distance = 0.0
  
  val colourID = Vector4f(0,0,0,1)
  
  init {
    ColourID.nextID(colourID)
  }
  
  fun getTextureXOffset(): Double {
    val num: Int = model.texture.numOfRows
    val col: Int = index % num
    return col.toDouble() / num.toDouble()
  }
  
  fun getTextureYOffset(): Double {
    val num: Int = model.texture.numOfRows
    val row: Int = index / num
    return row.toDouble() / num.toDouble()
  }
  
  fun incPos(dx: Double, dy: Double, dz: Double) {
    pos.x += dx
    pos.y += dy
    pos.z += dz
  }
  
  fun incRot(dx: Double, dy: Double, dz: Double) {
    rx += dx
    ry += dy
    rz += dz
  }
  
  fun isInScene(): Boolean = (GameMgr.camera.angleToEntity(this) > 0.54) and (distance < 350.0)
  
  fun touching(testPos: Vector3f): Boolean {
    Vector3f.sub(testPos, pos, collisionVec)
    return (collisionVec.length() <= 3.0)
  }
}