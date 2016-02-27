package org.dellamorte.raum.vector

import java.nio.DoubleBuffer

/**
 * Created by Raum on 2016-01-21.
 */
abstract class Vector {
  fun length(): Double = Math.sqrt(lengthSquared().toDouble())

  fun normalize(): Vector {
    val len = length()
    if (len != 0.0) {
      val l = 1.0f / len
      return scale(l)
    } else {
      throw IllegalStateException("Zero length vector")
    }
  }

  abstract fun load(buf: DoubleBuffer): Vector
  abstract fun store(buf: DoubleBuffer): Vector
  abstract fun lengthSquared(): Double
  abstract fun negate(): Vector
  abstract fun scale(scale: Double): Vector
}