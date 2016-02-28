package org.dellamorte.raum.vector

import java.nio.FloatBuffer

/**
 * Created by Raum on 2016-01-21.
 */
class Matrix2f : Matrix {
  constructor() {}
  
  var m00: Double = 1.0
  var m01: Double = 0.0
  var m10: Double = 0.0
  var m11: Double = 1.0
  
  val m00f: Float get() = m00.toFloat()
  val m01f: Float get() = m01.toFloat()
  val m10f: Float get() = m10.toFloat()
  val m11f: Float get() = m11.toFloat()

  override fun setIdentity(): Matrix {
    m00 = 1.0
    m01 = 0.0
    m10 = 0.0
    m11 = 1.0
    return this
  }

  override fun setZero(): Matrix {
    m00 = 0.0
    m01 = 0.0
    m10 = 0.0
    m11 = 0.0
    return this
  }

  override fun load(buf: FloatBuffer): Matrix {
    m00 = buf.get().toDouble()
    m01 = buf.get().toDouble()
    m10 = buf.get().toDouble()
    m11 = buf.get().toDouble()
    return this
  }

  override fun loadTranspose(buf: FloatBuffer): Matrix {
    m00 = buf.get().toDouble()
    m10 = buf.get().toDouble()
    m01 = buf.get().toDouble()
    m11 = buf.get().toDouble()
    return this
  }

  override fun store(buf: FloatBuffer): Matrix {
    buf.put(m00f)
    buf.put(m01f)
    buf.put(m10f)
    buf.put(m11f)
    return this
  }

  override fun storeTranspose(buf: FloatBuffer): Matrix {
    buf.put(m00f)
    buf.put(m10f)
    buf.put(m01f)
    buf.put(m11f)
    return this
  }

  override fun invert(): Matrix? {
    throw UnsupportedOperationException()
  }

  override fun negate(): Matrix {
    throw UnsupportedOperationException()
  }

  override fun transpose(): Matrix {
    throw UnsupportedOperationException()
  }

  override fun determinant(): Double {
    throw UnsupportedOperationException()
  }
}