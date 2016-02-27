package org.dellamorte.raum.vector

import java.nio.FloatBuffer

/**
 * Created by Raum on 2016-01-21.
 */
class Matrix2f : Matrix {
  constructor() {}

  override fun setIdentity(): Matrix {
    throw UnsupportedOperationException()
  }

  override fun setZero(): Matrix {
    throw UnsupportedOperationException()
  }

  override fun load(buf: FloatBuffer): Matrix {
    throw UnsupportedOperationException()
  }

  override fun loadTranspose(buf: FloatBuffer): Matrix {
    throw UnsupportedOperationException()
  }

  override fun store(buf: FloatBuffer): Matrix {
    throw UnsupportedOperationException()
  }

  override fun storeTranspose(buf: FloatBuffer): Matrix {
    throw UnsupportedOperationException()
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