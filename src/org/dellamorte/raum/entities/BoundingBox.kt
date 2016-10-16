package org.dellamorte.raum.entities

import org.dellamorte.raum.objFile.Vertex
import org.dellamorte.raum.vector.Vector3f
import java.util.*

/**
 * Created by Raum on 2016-03-17.
 */
class BoundingBox(val origin: Vector3f) {
  var xMin = 0.0
  var xMax = 0.0
  var yMin = 0.0
  var yMax = 0.0
  var zMin = 0.0
  var zMax = 0.0
  
  var height = 0.0
  var width = 0.0
  var xwOffset = 0.0
  var zwOffset = 0.0
  var xOffset = 0.0
  var yOffset = 0.0
  var zOffset = 0.0
  
//  fun genBox(vertices: ArrayList<Vertex>) {
//    for (vert in vertices) {
//      
//    }
//  }
  
  fun genBox(entityGen: EntityGen, scale: Double = 0.0) {
    // y is height!!!
    height = entityGen.c_height
    width = entityGen.c_width
    xOffset = entityGen.c_xOffset
    yOffset = entityGen.c_yOffset - 0.1 //if (entityGen.c_yOffset == 0.0) (if (height == 0.0) 0.0 else height / 2.0) else (entityGen.c_yOffset)
    zOffset = entityGen.c_zOffset
    xwOffset = entityGen.c_xwOffset
    zwOffset = entityGen.c_zwOffset
    genBox()
  }
  
  fun genBox(){
    var xm = (width + xwOffset) / 2.0
    var zm = (width + zwOffset) / 2.0
    xMax = origin.x + xm + xOffset
    xMin = origin.x - xm + xOffset
    yMax = origin.y + height + yOffset
    yMin = origin.y + yOffset
    zMax = origin.z + zm + zOffset
    zMin = origin.z - zm + zOffset
  }
  
  fun isPointInside(p: Vector3f): Boolean {
    return ((p.x >= xMin) && (p.x <= xMax)) &&
           ((p.y >= yMin) && (p.y <= yMax)) &&
           ((p.z >= zMin) && (p.z <= zMax))
  }
  
  fun isPointAtTop(p: Vector3f): Boolean = 
      ((p.x >= xMin) && (p.x <= xMax)) && 
          ((p.y >= (yMax - 0.5)) && (p.y <= (yMax + 0.1))) && 
          ((p.z >= zMin) && (p.z <= zMax))
}