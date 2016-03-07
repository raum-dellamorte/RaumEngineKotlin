package org.dellamorte.raum.entities

import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-27.
 */
class Light(val name: String, var pos: Vector3f, var color: Vector3f, var atten: Vector3f = Vector3f(1,0,0)) {
  
}