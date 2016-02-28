package org.dellamorte.raum.vector

import java.nio.FloatBuffer

/**
 * Created by Raum on 2016-01-21.
 */
class Matrix3f : Matrix {
  companion object {
    fun setIdentity(m: Matrix3f): Matrix3f {
      m.m00 = 1.0
      m.m01 = 0.0
      m.m02 = 0.0
      m.m10 = 0.0
      m.m11 = 1.0
      m.m12 = 0.0
      m.m20 = 0.0
      m.m21 = 0.0
      m.m22 = 1.0
      return m
    }

    fun setZero(m: Matrix3f): Matrix3f {
      m.m00 = 0.0
      m.m01 = 0.0
      m.m02 = 0.0
      m.m10 = 0.0
      m.m11 = 0.0
      m.m12 = 0.0
      m.m20 = 0.0
      m.m21 = 0.0
      m.m22 = 0.0
      return m
    }

    fun load(src: Matrix3f, dest: Matrix3f): Matrix3f {
      dest.m00 = src.m00
      dest.m01 = src.m01
      dest.m02 = src.m02
      dest.m10 = src.m10
      dest.m11 = src.m11
      dest.m12 = src.m12
      dest.m20 = src.m20
      dest.m21 = src.m21
      dest.m22 = src.m22
      return dest
    }

    fun add(left: Matrix3f, right: Matrix3f, dest: Matrix3f?): Matrix3f {
      val out = dest ?: Matrix3f()
      out.m00 = left.m00 + right.m00
      out.m01 = left.m01 + right.m01
      out.m02 = left.m02 + right.m02
      out.m10 = left.m10 + right.m10
      out.m11 = left.m11 + right.m11
      out.m12 = left.m12 + right.m12
      out.m20 = left.m20 + right.m20
      out.m21 = left.m21 + right.m21
      out.m22 = left.m22 + right.m22
      return out
    }

    fun sub(left: Matrix3f, right: Matrix3f, dest: Matrix3f?): Matrix3f {
      val out = dest ?: Matrix3f()
      out.m00 = left.m00 - right.m00
      out.m01 = left.m01 - right.m01
      out.m02 = left.m02 - right.m02
      out.m10 = left.m10 - right.m10
      out.m11 = left.m11 - right.m11
      out.m12 = left.m12 - right.m12
      out.m20 = left.m20 - right.m20
      out.m21 = left.m21 - right.m21
      out.m22 = left.m22 - right.m22
      return out
    }

    fun mul(left: Matrix3f, right: Matrix3f, dest: Matrix3f?): Matrix3f {
      val out = dest ?: Matrix3f()
      val m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02
      val m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02
      val m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02
      val m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12
      val m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12
      val m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12
      val m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22
      val m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22
      val m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22
      out.m00 = m00
      out.m01 = m01
      out.m02 = m02
      out.m10 = m10
      out.m11 = m11
      out.m12 = m12
      out.m20 = m20
      out.m21 = m21
      out.m22 = m22
      return out
    }

    fun invert(src: Matrix3f, dest: Matrix3f?):Matrix3f? {
      val determinant = src.determinant()
      if (determinant == 0.0) return null
      val out = dest ?: Matrix3f()
      /* do it the ordinary way
      *
      * inv(A) = 1/det(A) * adj(T), where adj(T) = transpose(Conjugate Matrix)
      *
      * m00 m01 m02
      * m10 m11 m12
      * m20 m21 m22
      **/
      val determinant_inv = 1.0f / determinant
      // get the conjugate matrix
      val t00 = src.m11 * src.m22 - src.m12 * src.m21
      val t01 = - src.m10 * src.m22 + src.m12 * src.m20
      val t02 = src.m10 * src.m21 - src.m11 * src.m20
      val t10 = - src.m01 * src.m22 + src.m02 * src.m21
      val t11 = src.m00 * src.m22 - src.m02 * src.m20
      val t12 = - src.m00 * src.m21 + src.m01 * src.m20
      val t20 = src.m01 * src.m12 - src.m02 * src.m11
      val t21 = -src.m00 * src.m12 + src.m02 * src.m10
      val t22 = src.m00 * src.m11 - src.m01 * src.m10
      out.m00 = t00*determinant_inv
      out.m11 = t11*determinant_inv
      out.m22 = t22*determinant_inv
      out.m01 = t10*determinant_inv
      out.m10 = t01*determinant_inv
      out.m20 = t02*determinant_inv
      out.m02 = t20*determinant_inv
      out.m12 = t21*determinant_inv
      out.m21 = t12*determinant_inv
      return out
    }

    fun negate(src: Matrix3f, dest: Matrix3f?):Matrix3f {
      val out = dest ?: Matrix3f()
      out.m00 = -src.m00
      out.m01 = -src.m02
      out.m02 = -src.m01
      out.m10 = -src.m10
      out.m11 = -src.m12
      out.m12 = -src.m11
      out.m20 = -src.m20
      out.m21 = -src.m22
      out.m22 = -src.m21
      return out
    }

    fun transform(left: Matrix3f, right: Vector3f, dest: Vector3f?): Vector3f {
      val out = dest ?: Vector3f()
      val x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z
      val y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z
      val z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z
      out.x = x
      out.y = y
      out.z = z
      return out
    }

    fun transpose(src: Matrix3f, dest: Matrix3f?): Matrix3f {
      val out = dest ?: Matrix3f()
      val m00 = src.m00
      val m01 = src.m10
      val m02 = src.m20
      val m10 = src.m01
      val m11 = src.m11
      val m12 = src.m21
      val m20 = src.m02
      val m21 = src.m12
      val m22 = src.m22
      out.m00 = m00
      out.m01 = m01
      out.m02 = m02
      out.m10 = m10
      out.m11 = m11
      out.m12 = m12
      out.m20 = m20
      out.m21 = m21
      out.m22 = m22
      return out
    }

  }

  var m00: Double = 1.0
  var m01: Double = 0.0
  var m02: Double = 0.0
  var m10: Double = 0.0
  var m11: Double = 1.0
  var m12: Double = 0.0
  var m20: Double = 0.0
  var m21: Double = 0.0
  var m22: Double = 1.0
  
  val m00f: Float get() = m00.toFloat()
  val m01f: Float get() = m01.toFloat()
  val m02f: Float get() = m02.toFloat()
  val m10f: Float get() = m10.toFloat()
  val m11f: Float get() = m11.toFloat()
  val m12f: Float get() = m12.toFloat()
  val m20f: Float get() = m20.toFloat()
  val m21f: Float get() = m21.toFloat()
  val m22f: Float get() = m22.toFloat()

  constructor() : super() {}

  override fun setIdentity(): Matrix =  Matrix3f.setIdentity(this)

  override fun setZero(): Matrix = Matrix3f.setZero(this)

  fun load(src: Matrix3f): Matrix3f = Matrix3f.load(src, this)

  override fun load(buf: FloatBuffer): Matrix {
    m00 = buf.get().toDouble()
    m01 = buf.get().toDouble()
    m02 = buf.get().toDouble()
    m10 = buf.get().toDouble()
    m11 = buf.get().toDouble()
    m12 = buf.get().toDouble()
    m20 = buf.get().toDouble()
    m21 = buf.get().toDouble()
    m22 = buf.get().toDouble()
    return this
  }

  override fun loadTranspose(buf: FloatBuffer): Matrix {
    m00 = buf.get().toDouble()
    m10 = buf.get().toDouble()
    m20 = buf.get().toDouble()
    m01 = buf.get().toDouble()
    m11 = buf.get().toDouble()
    m21 = buf.get().toDouble()
    m02 = buf.get().toDouble()
    m12 = buf.get().toDouble()
    m22 = buf.get().toDouble()
    return this
  }

  override fun store(buf: FloatBuffer): Matrix {
    buf.put(m00f)
    buf.put(m01f)
    buf.put(m02f)
    buf.put(m10f)
    buf.put(m11f)
    buf.put(m12f)
    buf.put(m20f)
    buf.put(m21f)
    buf.put(m22f)
    return this
  }

  override fun storeTranspose(buf: FloatBuffer): Matrix {
    buf.put(m00f)
    buf.put(m10f)
    buf.put(m20f)
    buf.put(m01f)
    buf.put(m11f)
    buf.put(m21f)
    buf.put(m02f)
    buf.put(m12f)
    buf.put(m22f)
    return this
  }

  override fun invert(): Matrix? = invert(this)

  fun invert(dest: Matrix3f): Matrix3f? = Matrix3f.invert(this, dest)

  override fun negate(): Matrix = negate(this)

  fun negate(dest: Matrix3f?): Matrix3f = Matrix3f.negate(this, dest)

  override fun transpose(): Matrix = transpose(this)

  fun transpose(dest: Matrix3f?): Matrix3f = Matrix3f.transpose(this, dest)

  override fun toString(): String = "Matrix3f: \n[[$m00, $m01, $m02]\n [$m10, $m11, $m12]\n [$m20, $m21, $m22]]\n"

  override fun determinant(): Double =
      m00 * (m11 * m22 - m12 * m21) +
      m01 * (m12 * m20 - m10 * m22) +
      m02 * (m10 * m21 - m11 * m20)
}