package org.dellamorte.raum.vector

import java.nio.FloatBuffer

/**
 * Created by Raum on 2016-01-21.
 */
abstract class Matrix {
  abstract fun setIdentity(): Matrix
  abstract fun setZero(): Matrix
  abstract fun load(buf: FloatBuffer): Matrix
  abstract fun loadTranspose(buf: FloatBuffer): Matrix
  abstract fun store(buf: FloatBuffer): Matrix
  abstract fun storeTranspose(buf: FloatBuffer): Matrix
  abstract fun invert(): Matrix?
  abstract fun negate(): Matrix
  abstract fun transpose(): Matrix
  abstract fun determinant(): Double
}