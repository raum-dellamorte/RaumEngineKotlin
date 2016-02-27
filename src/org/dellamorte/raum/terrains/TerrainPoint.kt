package org.dellamorte.raum.terrains

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.tools.DoubleArrays
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-02-10.
 */
class TerrainPoint(val terrain: Terrain) {
  val x: Double
      get(): Double = terrain.x
  val z: Double
      get(): Double = terrain.z
  val size: Int
      get(): Int = terrain.size
  val heights: DoubleArrays
      get() = terrain.heights
  val htln: Double
      get(): Double = (terrain.heights.size - 1).toDouble()
  val gridSquareSize: Double
      get(): Double = size / htln
  var xWorld = 0.0
  var zWorld = 0.0
  var xTerrain = 0.0
  var zTerrain = 0.0
  var xGrid = 0
  var zGrid = 0
  
  fun setWorldPoints(xp: Double, zp: Double) {
    xWorld = xp
    zWorld = zp
    xTerrain = xWorld - x
    zTerrain = zWorld - z
    xGrid = Math.floor(xTerrain / gridSquareSize.toDouble()).toInt()
    zGrid = Math.floor(zTerrain / gridSquareSize.toDouble()).toInt()
  }
  
  fun pointInBorder(): Boolean {
    return (((xGrid > -1) and (zGrid > -1)) and ((xGrid < htln) and (zGrid < htln)))
  }
  
  fun getHeightOfTerrain(): Double {
    if (!pointInBorder()) { return 0.0 }
    val xCoord: Double = (xTerrain % gridSquareSize).toDouble() / gridSquareSize.toDouble()
    val zCoord: Double = (zTerrain % gridSquareSize).toDouble() / gridSquareSize.toDouble()
    val answer: Double = if (xCoord <= (1 - zCoord)) {
      Maths.barryCentric(
          Vector3f(0.0, heights[xGrid][zGrid], 0.0),
          Vector3f(1.0, heights[xGrid + 1][zGrid], 0.0),
          Vector3f(0.0, heights[xGrid][zGrid + 1], 1.0),
          Vector2f(xCoord, zCoord))
    } else {
      Maths.barryCentric(
          Vector3f(1.0, heights[xGrid + 1][zGrid], 0.0),
          Vector3f(1.0, heights[xGrid + 1][zGrid + 1], 1.0),
          Vector3f(0.0, heights[xGrid][zGrid + 1], 1.0),
          Vector2f(xCoord, zCoord))
    }
    //DisplayMgr.debugMessage = "htln: $htln \nxWorld: $xWorld zWorld: $zWorld \nxTerrain: $xTerrain zTerrain: $zTerrain \ngridSquareSize: $gridSquareSize xGrid: $xGrid zGrid: $zGrid \nxCoord: $xCoord zCoord: $zCoord \nanswer: $answer"
    return answer
  }
}