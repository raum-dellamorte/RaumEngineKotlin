package org.dellamorte.raum.tools

import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-03-05.
 */
class ColourID {
  companion object {
    var rCount = 100
    var gCount = 100
    var bCount = 0
    
    fun nextID(v: Vector4f) {
      if (bCount < 255) {
        bCount++
      } else {
        bCount = 0
        if (gCount < 255) {
          gCount++
        } else {
          gCount = 0
          if (rCount < 255) {
            rCount++
          } else {
            bCount = 1
            gCount = 0
            rCount = 0
          }
        }
      }
      v.set(rCount.toDouble() / 255.0, gCount.toDouble() / 255.0, bCount.toDouble() / 255.0)
    }
  }
}