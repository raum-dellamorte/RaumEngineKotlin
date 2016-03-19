package org.dellamorte.raum.entities

import org.dellamorte.raum.objFile.Vertex
import java.util.*

/**
 * Created by Raum on 2016-03-17.
 */
class BoundingBox() {
  var minX = 0.0
  var maxX = 0.0
  var minY = 0.0
  var maxY = 0.0
  var minZ = 0.0
  var maxZ = 0.0
  
  fun genBox(vertices: ArrayList<Vertex>) {
    for (vert in vertices) {
      
    }
  }
}