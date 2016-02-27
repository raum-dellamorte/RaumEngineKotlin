package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.GameMgr.Companion.gravity
import org.dellamorte.raum.input.Keyboard
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.vector.Vector3f
import org.lwjgl.glfw.GLFW
import java.util.*

/**
 * Created by Raum on 2016-01-26.
 */
class Player(model: ModelTextured, index: Int,
             pos: Vector3f,
             rx: Double, ry: Double, rz: Double,
             scale: Double) : Entity(model, index, pos,
                                     rx, ry, rz, scale) {
  val kb: Keyboard get() = DisplayMgr.keyboard
  var runSpeed = 20.0
  var turnSpeed = 160.0
  val gravity: Double get() = GameMgr.gravity
  var jumpPower = 30.0
  var jumpCount = 0
  var jumpWait = false
  var maxJumpCount = 1
  var curSpeed = 0.0
  var curTurnSpeed = 0.0
  var upwardSpeed = 0.0
  var isInAir = false
  val stats = HashMap<String, Double>().apply { withDefault { 0.0 } }
  
  init {
    stats["Health"] = 100.0
  }
  
  fun getStat(stat: String): Double = stats[stat] ?: 0.0
  
  fun setStat(stat: String, newVal: Double) = stats.set(stat, newVal)
  
  fun incStat(stat: String, amount: Double, max: Double = 100.0) {
    val v: Double? = stats[stat]
    if (v != null) {
      var nv = v + amount
      if (nv > max) 
        nv = max
      stats[stat] = nv
    } else {
      return 
    }
  }
  
  fun decStat(stat: String, amount: Double, max: Double = 100.0) {
    val v: Double? = stats[stat]
    if (v != null) {
      var nv = v - amount
      if (nv > max) 
        nv = max
      stats[stat] = nv
    } else {
      return
    }
  }
  
  fun isAlive(): Boolean {
    val health = stats["Health"]
    if (health != null) {
      return health > 0.0
    } else {
      return false
    }
  }
  
  fun jump() {
    if ((jumpCount <= maxJumpCount) or !isInAir) {
      upwardSpeed = jumpPower
      isInAir = true
      jumpCount++
    }
  }
  
  fun move(terrains: TerrainList) {
    checkInputs()
    val rate = DisplayMgr.delta
    incRot(0.0, curTurnSpeed * rate, 0.0)
    val distance = curSpeed * rate
    val dx = distance * (Math.sin(Math.toRadians(ry)))
    val dz = distance * (Math.cos(Math.toRadians(ry)))
    val terrain: Terrain? = terrains.getTerrainAt(pos.x, pos.z)
    val terrainHt = if (terrain == null) 0.0 else terrain.getHeightOfTerrain(pos.x, pos.z)
    upwardSpeed += gravity * rate
    if (isInAir) {
      incPos(dx, upwardSpeed * rate, dz)
      if (pos.y < terrainHt) {
        upwardSpeed = 0.0
        isInAir = false
        pos.y = terrainHt
        jumpCount = 0
        println("Move: Terrain Height: $terrainHt")
      }
    } else {
      incPos(dx, 0.0, dz)
      pos.y = terrainHt
    }
  }
  
  private fun checkInputs() {
    if (kb.isKeyDown(GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP)) {
      curSpeed = runSpeed
    } else if (kb.isKeyDown(GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN)) {
      curSpeed = -runSpeed
    } else {
      curSpeed = 0.0
    }
    if (kb.isKeyDown(GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT)) {
      curTurnSpeed = -turnSpeed
    } else if (kb.isKeyDown(GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT)) {
      curTurnSpeed = turnSpeed
    } else {
      curTurnSpeed = 0.0
    }
    if (kb.isKeyDown(GLFW.GLFW_KEY_SPACE) and !jumpWait) { 
      jumpWait = true
      jump() 
    } else if (!kb.isKeyDown(GLFW.GLFW_KEY_SPACE) and jumpWait) {
      jumpWait = false
    }
    
  }
}