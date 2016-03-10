package org.dellamorte.raum.engine

import org.dellamorte.raum.entities.Entity
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-03-09.
 */
class EntityPickerMgr {
  companion object {
    val current = ByteArray(4)
    val r: Int get() = current[0].toInt() and 0xFF
    val g: Int get() = current[1].toInt() and 0xFF
    val b: Int get() = current[2].toInt() and 0xFF
    val a: Int get() = current[3].toInt() and 0xFF
  
    fun matchVec(v: Vector4f): Boolean {
      if (Math.round(v.x * 255.0).toInt() != r) return false
      if (Math.round(v.y * 255.0).toInt() != g) return false
      if (Math.round(v.z * 255.0).toInt() != b) return false
      return true
    }
  
    fun entityAtMouse(): Entity? {
      var out: Entity? = null
      for (ent in GameMgr.ents) {
        if (!matchVec(ent.colourID)) continue
        out = ent
        break
      }
      return out
    }
  }
}