package org.dellamorte.raum.terrains

import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-01-28.
 */
class Water(val terrain: Terrain) {
  companion object {
    var waterLevel = -2.0
  
    fun vecReflect(): Vector4f = Vector4f(0.0, 1.0, 0.0, -waterLevel + 1.0)
  
    fun vecRefract(): Vector4f = Vector4f(0.0, -1.0, 0.0, waterLevel + 1.0)
  }
  
  val size: Double
    get() = terrain.size / 2.0
  val x: Double
    get() = terrain.x + size
  val z: Double
    get() = terrain.z + size
  val h: Double
    get() = Water.waterLevel
  
  fun toVector(): Vector3f = Vector3f(x, h, z)
}