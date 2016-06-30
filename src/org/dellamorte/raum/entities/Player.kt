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
  var runSpeed = 60.0
  var turnSpeed = 160.0
  val gravity: Double get() = GameMgr.gravity
  var jumpPower = 0.8
  var jumpCount = 0
  var jumpWait = false
  var maxJumpCount = 1
  var curSpeed = 0.0
  var curTurnSpeed = 0.0
  var dy = 0.0
  var isInAir = false
  val stats = HashMap<String, Double>().apply { withDefault { 0.0 } }
  
  val nearbyObjects = ArrayList<Entity>()
  var nearbyFactor = 50.0
  
  private val rate: Double get() = DisplayMgr.delta
  
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
      dy = jumpPower
      isInAir = true
      jumpCount++
    }
  }
  
  fun move() {
    checkInputs()
    incRot(0.0, curTurnSpeed * rate, 0.0)
    val distance = curSpeed * rate
    val dx = distance * Math.sin(Math.toRadians(ry))
    val dz = distance * Math.cos(Math.toRadians(ry))
    val terrainHt = GameMgr.terrainHt(pos.x + dx, pos.z + dz)
    dy -= gravity * rate * rate
    newPos.set(pos.x + dx, pos.y + dy, pos.z + dz)
    if (newPos.y < terrainHt) {
      dy = 0.0
      isInAir = false
      newPos.y = terrainHt
      jumpCount = 0
    } else if (!isInAir and (newPos.y > terrainHt)) {
      isInAir = true
      
    }
    for (ent in nearbyObjects) {
      if (ent.touching(newPos)) {
        if (!ent.touching(pos)) newPos.set(pos)
      }
    }
    pos.set(newPos)
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