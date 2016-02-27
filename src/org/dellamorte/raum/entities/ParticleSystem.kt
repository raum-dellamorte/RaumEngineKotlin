package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.textures.TextureParticle
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f
import java.util.*

/**
 * Created by Raum on 2016-02-25.
 */
class ParticleSystem(val texture: TextureParticle, var pps: Double, var averageSpeed: Double, var gravityComplient: Double, var averageLifeLength: Double, var averageScale: Double) {
  var speedError = 0.0
      set(error: Double) { error * averageSpeed }
  var lifeError = 0.0
      set(error: Double) { error * averageLifeLength }
  var scaleError = 0.0
    set(error: Double) { error * averageScale }
  var randomRotation = false
  private var direction: Vector3f? = null
  private var directionDeviation = 0.0
  private var random = Random()
  
  /**
   * @param direction - The average direction in which particles are emitted.
   * @param deviation - A value between 0 and 1 indicating how far from the chosen direction particles can deviate.
   */
  fun setDirection(dir:Vector3f, deviation: Double) {
    direction = Vector3f(dir)
    directionDeviation = deviation * Math.PI
  }
  
  fun randomizeRotation() { randomRotation = true }
  
  fun generateParticles(systemCenter: Vector3f) {
    val delta = DisplayMgr.delta
    val particlesToCreate = pps * delta
    val count = Math.floor(particlesToCreate).toInt()
    val partialParticle: Double = particlesToCreate % 1.0
    count.times { emitParticle(systemCenter) }
    if (Math.random() < partialParticle) { emitParticle(systemCenter) }
  }
  
  private fun emitParticle(center:Vector3f) {
    var velocity: Vector3f
    if (direction != null) {
      velocity = generateRandomUnitVectorWithinCone(direction!!, directionDeviation)
    } else {
      velocity = generateRandomUnitVector()
    }
    velocity.normalize()
    velocity.scale(generateValue(averageSpeed, speedError))
    val scale = generateValue(averageScale, scaleError)
    val lifeLength = generateValue(averageLifeLength, lifeError)
    Particle(texture, Vector3f(center), velocity, generateRotation(), scale, gravityComplient, lifeLength)
  }
  
  private fun generateValue(average: Double, errorMargin: Double): Double {
    val offset = (random.nextFloat().toDouble() - 0.5) * 2.0 * errorMargin
    return average + offset
  }
  
  private fun generateRotation(): Double {
    if (randomRotation) {
      return random.nextFloat().toDouble() * 360.0
    } else {
      return 0.0
    }
  }
  
  private fun generateRandomUnitVectorWithinCone(coneDirection: Vector3f, angle: Double):Vector3f {
    val cosAngle = Math.cos(angle)
    val random = Random()
    val theta = random.nextFloat().toDouble() * 2.0 * Math.PI
    val z = cosAngle + (random.nextFloat().toDouble() * (1.0 - cosAngle))
    val rootOneMinusZSquared = Math.sqrt(1.0 - z * z)
    val x = rootOneMinusZSquared * Math.cos(theta)
    val y = rootOneMinusZSquared * Math.sin(theta)
    val direction = Vector4f(x, y, z, 1.0)
    if (coneDirection.x != 0.0 || coneDirection.y != 0.0 || (coneDirection.z != 1.0 && coneDirection.z != -1.0)) {
      val rotateAxis = Vector3f.cross(coneDirection, Vector3f(0, 0, 1), null)
      rotateAxis.normalize()
      val rotateAngle = Math.acos(Vector3f.dot(coneDirection, Vector3f(0, 0, 1)))
      val rotationMatrix = Matrix4f()
      rotationMatrix.rotate(-rotateAngle, rotateAxis)
      Matrix4f.transform(rotationMatrix, direction, direction)
    } else if (coneDirection.z == -1.0) {
      direction.z *= -1
    }
    return Vector3f(direction)
  }
  private fun generateRandomUnitVector():Vector3f {
    val theta = (random.nextFloat().toDouble() * 2.0 * Math.PI)
    val z = (random.nextFloat().toDouble() * 2.0) - 1.0
    val rootOneMinusZSquared = Math.sqrt(1.0 - (z * z))
    val x = rootOneMinusZSquared * Math.cos(theta.toDouble())
    val y = rootOneMinusZSquared * Math.sin(theta.toDouble())
    return Vector3f(x, y, z)
  }
}