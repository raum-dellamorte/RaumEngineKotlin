package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.GameMgr.Companion.gravity
import org.dellamorte.raum.input.Keyboard
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.tools.cos
import org.dellamorte.raum.tools.sin
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Matrix4f
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
  var dpos = 0.0
  
  val bboxes = ArrayList<BoundingBox>()
//  var notOnBB: Boolean = true
  
  var isInAir = false
  val stats = HashMap<String, Double>().apply { withDefault { 0.0 } }
  
  val nearbyObjects = ArrayList<Entity>()
  var nearbyFactor = 50.0
  
  private val posMat = Matrix4f()
  val posMatrix: Matrix4f get() = Maths.createPlayerPosMatrix(posMat, this)
  
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
  
  private fun calcHigherPosOnArc(newPos: Vector3f, offsetLR: Double = 0.0) {
    dpos += 0.5
    val len = Vector3f.length(pos, altNewPos)
    val hDist: Double = len * dpos.cos()
    val vDist: Double = len * dpos.sin()
    // theta = y rotation of player (ry + 180.0) + angular left or right offset (offsetLR) if we want to check if it's possible to move slightly left or right
    val theta = ry + 180.0 + offsetLR // I think left/counterclockwise is negative and right/clockwise is positive...
    val xOffset = hDist * theta.sin()
    val zOffset = hDist * theta.cos()
    newPos.x = pos.x - xOffset
    newPos.z = pos.z - zOffset
    newPos.y = pos.y + vDist
  }
  
  private fun higherHeight(ht1: Double, ht2: Double): Double {
    if (ht1 > ht2) return ht1
    return ht2
  }
  
  private fun checkOverTerrainHt(ht: Double) {
    if (newPos.y < ht) {
      dy = 0.0
      isInAir = false
      newPos.y = ht
      jumpCount = 0
    } else if (!isInAir and (newPos.y > ht)) {
      isInAir = true
    }
  }
  
  fun getBBoxes(testPos: Vector3f) {
    bboxes.clear()
    for (ent in nearbyObjects) {
      if (!ent.touching(testPos)) continue
      bboxes.add(ent.bbox)  // In the future I may want to use the Entity in case I need to reference what I'm standing on.
    }
  }
  
  fun move() {
    dpos = 0.0
    checkInputs()
    incRot(0.0, curTurnSpeed * rate, 0.0)
    val distance = curSpeed * rate
    val dx = distance * ry.sin()
    val dz = distance * ry.cos()
    dy -= gravity * rate * rate
    newPos.set(pos.x + dx, pos.y, pos.z + dz)
//    altNewPos.set(newPos)
    if (isInAir) newPos.y += dy
    var surfaceHt: Double = GameMgr.terrainHt(newPos.x, newPos.z)
    var surfaceHtAlt: Double = surfaceHt
//    var boxOn: BoundingBox? = null
//    var boxBy: BoundingBox? = null
    getBBoxes(newPos)
    for (bb in bboxes) {
      if (bb.isPointAtTop(newPos) && (dy <= 0.0)) {
        isInAir = false
        dy = 0.0
        surfaceHtAlt = higherHeight(surfaceHtAlt, bb.yMax)
//        boxOn = bb
        continue
      } else if (bb.isPointInside(newPos)) {
//        boxBy = bb
        newPos.set(pos.x, newPos.y, pos.z)
      }
    }
    if (surfaceHtAlt > surfaceHt) surfaceHt = surfaceHtAlt
    checkOverTerrainHt(surfaceHt)
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