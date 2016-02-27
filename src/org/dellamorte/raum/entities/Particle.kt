package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.engine.ParticleMgr
import org.dellamorte.raum.textures.TextureParticle
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-02-23.
 */
class Particle(val texture: TextureParticle, var pos: Vector3f, var velocity: Vector3f, var rot: Double, var scale: Double, var gravEffect: Double, var life: Double) {
  var elapsedTime = 0.0
  private val camera: Camera get() = GameMgr.camera
  private val gravity: Double get() = GameMgr.gravity
  private val delta: Double get() = DisplayMgr.delta
  var distance: Double = 0.0
  var texOffset1 = Vector2f()
  var texOffset2 = Vector2f()
  var blend: Double = 0.0
  val numRows: Double get() = texture.numOfRows.toDouble()
  
  init {
    ParticleMgr.add(this)
  }
  
  fun update(): Boolean {
    velocity.y += gravity * gravEffect * delta
    val change = Vector3f(velocity).apply { scale(delta) }
    Vector3f.add(change, pos, pos)
    distance = Vector3f.sub(camera.pos, pos, null).lengthSquared()
    updateTexCoords()
    elapsedTime += delta
    return elapsedTime > life
  }
  
  private fun updateTexCoords() {
    val lifeFactor: Double = elapsedTime / life
    val stageCount: Int = texture.numOfRows * texture.numOfRows
    val atlasProgression: Double = lifeFactor * stageCount.toDouble()
    val index1: Int = Math.floor(atlasProgression).toInt()
    val index2: Int = if (index1 < (stageCount - 1.0)) index1 + 1 else index1
    blend = atlasProgression % 1.0
    setTexOffset(texOffset1, index1)
    setTexOffset(texOffset2, index2)
  }
  
  private fun setTexOffset(offset: Vector2f, index: Int) {
    val col: Int = index % numRows.toInt()
    val row: Int = index / numRows.toInt()
    offset.x = col / numRows
    offset.y = row / numRows
  }
}