package org.dellamorte.raum.tools

import org.dellamorte.raum.terrains.Terrain
import java.util.ArrayList

/**
 * Created by Raum on 2016-01-27.
 */
class TerrainList {
  val list = ArrayList<Terrain>()
  
  fun clear() = list.clear()
  
  fun size() = list.size
  
  fun getTerrainAt(x: Double, z: Double): Terrain? {
    for (terrain: Terrain in list) {
      if (!terrain.pointInBorder(x, z)) continue
      return terrain
    }
    return null
  }
  
  fun add(terrain: Terrain) = list.add(terrain)
}