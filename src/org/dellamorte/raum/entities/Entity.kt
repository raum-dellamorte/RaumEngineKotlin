package org.dellamorte.raum.entities

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
  private val transMat = Matrix4f()
  val transformationMatrix: Matrix4f
      get() = Maths.createTransformationMatrix(transMat, pos, rx, ry, rz, scale)
  
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
}