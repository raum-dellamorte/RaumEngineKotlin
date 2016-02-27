package org.dellamorte.raum.tools

/**
 * Created by Raum on 2016-01-31.
 */

inline fun Int.times(block: (Int) -> Unit) {
  for (i: Int in (1..this)) block(i - 1)
}

inline fun Int.times(n: Int, block: (Int) -> Unit) {
  for (i: Int in ((n + 1)..this)) block(i - 1)
}

fun DoubleArray.toFloatArray(): FloatArray {
  val out = FloatArray(this.size)
  this.forEachIndexed { i, d -> out[i] = d.toFloat() }
  return out
}