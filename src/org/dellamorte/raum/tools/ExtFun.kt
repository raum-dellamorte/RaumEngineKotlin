package org.dellamorte.raum.tools

/**
 * Created by Raum on 2016-01-31.
 */

fun Double.cos(): Double = Math.cos(Math.toRadians(this))

fun Double.sin(): Double = Math.sin(Math.toRadians(this))

fun Double.squared(): Double = this * this

inline fun Int.times(block: (Int) -> Unit) {
  if (this <= 0) return
  for (i: Int in (1..this)) block(i - 1)
}

inline fun Int.times(n: Int, block: (Int) -> Unit) {
  if (n < 0) return
  if (this <= 0) return
  if (this <= n) return
  for (i: Int in ((n + 1)..this)) block(i - 1)
}

fun DoubleArray.toFloatArray(): FloatArray {
  val out = FloatArray(this.size)
  this.forEachIndexed { i, d -> out[i] = d.toFloat() }
  return out
}