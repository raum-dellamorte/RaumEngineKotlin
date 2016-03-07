package org.dellamorte.raum.engine

import org.dellamorte.raum.entities.Light
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Vector3f
import java.util.*

/**
 * Created by Raum on 2016-03-02.
 */
class LightMgr {
  companion object {
    val lights = ArrayList<Light>()
    val activeLights = Array<Light?>(4, { i: Int -> null })
    
    operator fun get(i: Int) = lights[i]
    
    operator fun set(i: Int, light: Light) { lights[i] = light }
    
    fun add(name: String, pos: Vector3f, color: Vector3f, atten: Vector3f = Vector3f(1, 0, 0)) {
      lights.add(Light(name, pos, color, atten))
    }
  
    fun add(name: String,
                 pos1:Double, pos2:Double, pos3:Double,
                 color1:Double, color2:Double, color3:Double,
                 atten1:Double, atten2:Double, atten3:Double) {
      add(name,
          Vector3f(pos1, pos2, pos3),
          Vector3f(color1, color2, color3),
          Vector3f(atten1, atten2, atten3))
    }
  
    fun add(name: String, pos1:Double, pos2:Double, pos3:Double,
                 color1:Double, color2:Double, color3:Double) =
        add(name, pos1, pos2, pos3, color1, color2, color3, 1.0, 0.0, 0.0)
    
    fun update() {
      // needs to pick the most relevant lights.
      4.times { 
        if (lights.size > it) {
          activeLights[it] = lights[it]
        } else {
          activeLights[it] = null
        }
      }
    }
  }
}