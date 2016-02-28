package org.dellamorte.raum.vector

import java.nio.FloatBuffer

/**
 * Created by Raum on 2016-01-21.
 */
class Matrix4f : Matrix {
  companion object {
    fun setIdentity(m: Matrix4f): Matrix4f {
      m.m00 = 1.0
      m.m01 = 0.0
      m.m02 = 0.0
      m.m03 = 0.0
      m.m10 = 0.0
      m.m11 = 1.0
      m.m12 = 0.0
      m.m13 = 0.0
      m.m20 = 0.0
      m.m21 = 0.0
      m.m22 = 1.0
      m.m23 = 0.0
      m.m30 = 0.0
      m.m31 = 0.0
      m.m32 = 0.0
      m.m33 = 1.0
      return m
    }
  
    fun setZero(m: Matrix4f): Matrix4f {
      m.m00 = 0.0
      m.m01 = 0.0
      m.m02 = 0.0
      m.m03 = 0.0
      m.m10 = 0.0
      m.m11 = 0.0
      m.m12 = 0.0
      m.m13 = 0.0
      m.m20 = 0.0
      m.m21 = 0.0
      m.m22 = 0.0
      m.m23 = 0.0
      m.m30 = 0.0
      m.m31 = 0.0
      m.m32 = 0.0
      m.m33 = 0.0
      return m
    }
  
    fun load(src: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      out.m00 = src.m00
      out.m01 = src.m01
      out.m02 = src.m02
      out.m03 = src.m03
      out.m10 = src.m10
      out.m11 = src.m11
      out.m12 = src.m12
      out.m13 = src.m13
      out.m20 = src.m20
      out.m21 = src.m21
      out.m22 = src.m22
      out.m23 = src.m23
      out.m30 = src.m30
      out.m31 = src.m31
      out.m32 = src.m32
      out.m33 = src.m33
      return out
    }
  
    fun add(left: Matrix4f, right: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      out.m00 = left.m00 + right.m00
      out.m01 = left.m01 + right.m01
      out.m02 = left.m02 + right.m02
      out.m03 = left.m03 + right.m03
      out.m10 = left.m10 + right.m10
      out.m11 = left.m11 + right.m11
      out.m12 = left.m12 + right.m12
      out.m13 = left.m13 + right.m13
      out.m20 = left.m20 + right.m20
      out.m21 = left.m21 + right.m21
      out.m22 = left.m22 + right.m22
      out.m23 = left.m23 + right.m23
      out.m30 = left.m30 + right.m30
      out.m31 = left.m31 + right.m31
      out.m32 = left.m32 + right.m32
      out.m33 = left.m33 + right.m33
      return out
    }
  
    fun sub(left: Matrix4f, right: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      out.m00 = left.m00 - right.m00
      out.m01 = left.m01 - right.m01
      out.m02 = left.m02 - right.m02
      out.m03 = left.m03 - right.m03
      out.m10 = left.m10 - right.m10
      out.m11 = left.m11 - right.m11
      out.m12 = left.m12 - right.m12
      out.m13 = left.m13 - right.m13
      out.m20 = left.m20 - right.m20
      out.m21 = left.m21 - right.m21
      out.m22 = left.m22 - right.m22
      out.m23 = left.m23 - right.m23
      out.m30 = left.m30 - right.m30
      out.m31 = left.m31 - right.m31
      out.m32 = left.m32 - right.m32
      out.m33 = left.m33 - right.m33
      return out
    }
  
    fun mul(left: Matrix4f, right: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      val m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03
      val m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03
      val m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03
      val m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03
      val m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13
      val m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13
      val m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13
      val m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13
      val m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23
      val m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23
      val m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23
      val m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23
      val m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33
      val m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33
      val m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33
      val m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33
      out.m00 = m00
      out.m01 = m01
      out.m02 = m02
      out.m03 = m03
      out.m10 = m10
      out.m11 = m11
      out.m12 = m12
      out.m13 = m13
      out.m20 = m20
      out.m21 = m21
      out.m22 = m22
      out.m23 = m23
      out.m30 = m30
      out.m31 = m31
      out.m32 = m32
      out.m33 = m33
      return out
    }
    
    fun invert(src: Matrix4f, dest: Matrix4f?): Matrix4f? {
      val determinant = src.determinant()
      if (determinant != 0.0) {
        /*
        * m00 m01 m02 m03
        * m10 m11 m12 m13
        * m20 m21 m22 m23
        * m30 m31 m32 m33
        **/
        val out = dest ?: Matrix4f()
        val determinant_inv = 1f / determinant
        // first row
        val t00: Double = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33)
        val t01: Double = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33)
        val t02: Double = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33)
        val t03: Double = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32)
        // second row
        val t10: Double = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33)
        val t11: Double = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33)
        val t12: Double = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33)
        val t13: Double = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32)
        // third row
        val t20: Double = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33)
        val t21: Double = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33)
        val t22: Double = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33)
        val t23: Double = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32)
        // fourth row
        val t30: Double = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23)
        val t31: Double = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23)
        val t32: Double = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23)
        val t33: Double = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22)
        // transpose and divide by the determinant
        out.m00 = t00 * determinant_inv
        out.m11 = t11 * determinant_inv
        out.m22 = t22 * determinant_inv
        out.m33 = t33 * determinant_inv
        out.m01 = t10 * determinant_inv
        out.m10 = t01 * determinant_inv
        out.m20 = t02 * determinant_inv
        out.m02 = t20 * determinant_inv
        out.m12 = t21 * determinant_inv
        out.m21 = t12 * determinant_inv
        out.m03 = t30 * determinant_inv
        out.m30 = t03 * determinant_inv
        out.m13 = t31 * determinant_inv
        out.m31 = t13 * determinant_inv
        out.m32 = t23 * determinant_inv
        out.m23 = t32 * determinant_inv
        return out
      } else {
        return null
      }
    }
    
    fun negate(src: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      out.m00 = -src.m00
      out.m01 = -src.m01
      out.m02 = -src.m02
      out.m03 = -src.m03
      out.m10 = -src.m10
      out.m11 = -src.m11
      out.m12 = -src.m12
      out.m13 = -src.m13
      out.m20 = -src.m20
      out.m21 = -src.m21
      out.m22 = -src.m22
      out.m23 = -src.m23
      out.m30 = -src.m30
      out.m31 = -src.m31
      out.m32 = -src.m32
      out.m33 = -src.m33
      return out
    }
  
    fun rotate(angle: Double, axis: Vector3f, src: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      val c = Math.cos(angle.toDouble())
      val s = Math.sin(angle.toDouble())
      val oneminusc = 1.0 - c
      val xy = axis.x * axis.y
      val yz = axis.y * axis.z
      val xz = axis.x * axis.z
      val xs = axis.x * s
      val ys = axis.y * s
      val zs = axis.z * s
      val f00 = axis.x * axis.x * oneminusc + c
      val f01 = xy * oneminusc + zs
      val f02 = xz * oneminusc - ys
      // n[3] not used
      val f10 = xy * oneminusc - zs
      val f11 = axis.y * axis.y * oneminusc + c
      val f12 = yz * oneminusc + xs
      // n[7] not used
      val f20 = xz * oneminusc + ys
      val f21 = yz * oneminusc - xs
      val f22 = axis.z * axis.z * oneminusc + c
      val t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02
      val t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02
      val t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02
      val t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02
      val t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12
      val t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12
      val t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12
      val t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12
      out.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22
      out.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22
      out.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22
      out.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22
      out.m00 = t00
      out.m01 = t01
      out.m02 = t02
      out.m03 = t03
      out.m10 = t10
      out.m11 = t11
      out.m12 = t12
      out.m13 = t13
      return out
    }
    
    fun scale(vec:Vector3f, src: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      out.m00 = src.m00 * vec.x
      out.m01 = src.m01 * vec.x
      out.m02 = src.m02 * vec.x
      out.m03 = src.m03 * vec.x
      out.m10 = src.m10 * vec.y
      out.m11 = src.m11 * vec.y
      out.m12 = src.m12 * vec.y
      out.m13 = src.m13 * vec.y
      out.m20 = src.m20 * vec.z
      out.m21 = src.m21 * vec.z
      out.m22 = src.m22 * vec.z
      out.m23 = src.m23 * vec.z
      return out
    }

    fun transform(left: Matrix4f, right: Vector4f, dest: Vector4f?): Vector4f {
      val out = dest ?: Vector4f()
      val x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w
      val y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w
      val z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w
      val w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w
      out.x = x
      out.y = y
      out.z = z
      out.w = w
      return out
    }
  
    fun translate(vec:Vector3f, src:Matrix4f, dest:Matrix4f?):Matrix4f {
      val out = dest ?: Matrix4f()
      out.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z
      out.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z
      out.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z
      out.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z
      return out
    }
    
    fun translate(vec:Vector2f, src:Matrix4f, dest:Matrix4f?):Matrix4f {
      val out = dest ?: Matrix4f()
      out.m30 += src.m00 * vec.x + src.m10 * vec.y
      out.m31 += src.m01 * vec.x + src.m11 * vec.y
      out.m32 += src.m02 * vec.x + src.m12 * vec.y
      out.m33 += src.m03 * vec.x + src.m13 * vec.y
      return out
    }

    fun transpose(src: Matrix4f, dest: Matrix4f?): Matrix4f {
      val out = dest ?: Matrix4f()
      val m00 = src.m00
      val m01 = src.m10
      val m02 = src.m20
      val m03 = src.m30
      val m10 = src.m01
      val m11 = src.m11
      val m12 = src.m21
      val m13 = src.m31
      val m20 = src.m02
      val m21 = src.m12
      val m22 = src.m22
      val m23 = src.m32
      val m30 = src.m03
      val m31 = src.m13
      val m32 = src.m23
      val m33 = src.m33
      out.m00 = m00
      out.m01 = m01
      out.m02 = m02
      out.m03 = m03
      out.m10 = m10
      out.m11 = m11
      out.m12 = m12
      out.m13 = m13
      out.m20 = m20
      out.m21 = m21
      out.m22 = m22
      out.m23 = m23
      out.m30 = m30
      out.m31 = m31
      out.m32 = m32
      out.m33 = m33
      return out
    }

    fun determinant3x3(t00: Double, t01: Double, t02: Double, t10: Double, t11: Double, t12: Double, t20: Double, t21: Double, t22: Double): Double =
      t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20)
  }

  var m00: Double = 1.0
  var m01: Double = 0.0
  var m02: Double = 0.0
  var m03: Double = 0.0
  var m10: Double = 0.0
  var m11: Double = 1.0
  var m12: Double = 0.0
  var m13: Double = 0.0
  var m20: Double = 0.0
  var m21: Double = 0.0
  var m22: Double = 1.0
  var m23: Double = 0.0
  var m30: Double = 0.0
  var m31: Double = 0.0
  var m32: Double = 0.0
  var m33: Double = 1.0
  
  val m00f: Float get() = m00.toFloat()
  val m01f: Float get() = m01.toFloat()
  val m02f: Float get() = m02.toFloat()
  val m03f: Float get() = m03.toFloat()
  val m10f: Float get() = m10.toFloat()
  val m11f: Float get() = m11.toFloat()
  val m12f: Float get() = m12.toFloat()
  val m13f: Float get() = m13.toFloat()
  val m20f: Float get() = m20.toFloat()
  val m21f: Float get() = m21.toFloat()
  val m22f: Float get() = m22.toFloat()
  val m23f: Float get() = m23.toFloat()
  val m30f: Float get() = m30.toFloat()
  val m31f: Float get() = m31.toFloat()
  val m32f: Float get() = m32.toFloat()
  val m33f: Float get() = m33.toFloat()

  constructor() : super() {}

  override fun setIdentity(): Matrix =  Matrix4f.setIdentity(this)
  
  override fun setZero(): Matrix = Matrix4f.setZero(this)

  fun load(src: Matrix4f): Matrix4f = Matrix4f.load(src, this)

  override fun load(buf: FloatBuffer): Matrix {
    m00 = buf.get().toDouble()
    m01 = buf.get().toDouble()
    m02 = buf.get().toDouble()
    m03 = buf.get().toDouble()
    m10 = buf.get().toDouble()
    m11 = buf.get().toDouble()
    m12 = buf.get().toDouble()
    m13 = buf.get().toDouble()
    m20 = buf.get().toDouble()
    m21 = buf.get().toDouble()
    m22 = buf.get().toDouble()
    m23 = buf.get().toDouble()
    m30 = buf.get().toDouble()
    m31 = buf.get().toDouble()
    m32 = buf.get().toDouble()
    m33 = buf.get().toDouble()
    return this
  }

  override fun loadTranspose(buf: FloatBuffer): Matrix {
    m00 = buf.get().toDouble()
    m10 = buf.get().toDouble()
    m20 = buf.get().toDouble()
    m30 = buf.get().toDouble()
    m01 = buf.get().toDouble()
    m11 = buf.get().toDouble()
    m21 = buf.get().toDouble()
    m31 = buf.get().toDouble()
    m02 = buf.get().toDouble()
    m12 = buf.get().toDouble()
    m22 = buf.get().toDouble()
    m32 = buf.get().toDouble()
    m03 = buf.get().toDouble()
    m13 = buf.get().toDouble()
    m23 = buf.get().toDouble()
    m33 = buf.get().toDouble()
    return this
  }

  override fun store(buf: FloatBuffer): Matrix {
    buf.put(m00f)
    buf.put(m01f)
    buf.put(m02f)
    buf.put(m03f)
    buf.put(m10f)
    buf.put(m11f)
    buf.put(m12f)
    buf.put(m13f)
    buf.put(m20f)
    buf.put(m21f)
    buf.put(m22f)
    buf.put(m23f)
    buf.put(m30f)
    buf.put(m31f)
    buf.put(m32f)
    buf.put(m33f)
    return this
  }

  override fun storeTranspose(buf: FloatBuffer): Matrix {
    buf.put(m00f)
    buf.put(m10f)
    buf.put(m20f)
    buf.put(m30f)
    buf.put(m01f)
    buf.put(m11f)
    buf.put(m21f)
    buf.put(m31f)
    buf.put(m02f)
    buf.put(m12f)
    buf.put(m22f)
    buf.put(m32f)
    buf.put(m03f)
    buf.put(m13f)
    buf.put(m23f)
    buf.put(m33f)
    return this
  }
  
  fun store3f(buf: FloatBuffer): Matrix {
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
  
  override fun invert(): Matrix? = Matrix4f.invert(this, this)
  
  fun invert(dest: Matrix4f): Matrix4f? = Matrix4f.invert(this, dest)
  
  override fun negate(): Matrix = Matrix4f.negate(this, this)
  
  fun negate(dest: Matrix4f): Matrix4f = Matrix4f.negate(this, dest)
  
  fun rotate(angle: Double, axis: Vector3f): Matrix4f = rotate(angle, axis, this)
  
  fun rotate(angle: Double, axis: Vector3f, dest: Matrix4f): Matrix4f = Matrix4f.rotate(angle, axis, this, dest)
  
  fun scale(vec:Vector3f):Matrix4f = scale(vec, this)
  
  fun scale(vec:Vector3f, dest: Matrix4f):Matrix4f = Matrix4f.scale(vec, this, dest)
  
  fun translate(vec: Vector2f): Matrix4f = translate(vec, this)
  
  fun translate(vec: Vector2f, dest: Matrix4f): Matrix4f = Matrix4f.translate(vec, this, dest)
  
  fun translate(vec: Vector3f): Matrix4f = translate(vec, this)
  
  fun translate(vec: Vector3f, dest: Matrix4f): Matrix4f = Matrix4f.translate(vec, this, dest)
  
  override fun transpose(): Matrix = transpose(this)

  fun transpose(dest: Matrix4f): Matrix4f = Matrix4f.transpose(this, dest)

  override fun determinant(): Double {
    var f = m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33)
    f -= m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33)
    f += m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33)
    f -= m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32)
    return f
  }
  
  override fun toString(): String {
    return "[[$m00, $m01, $m02, $m03],\n [$m10, $m11, $m12, $m13],\n [$m20, $m21, $m22, $m23],\n [$m30, $m31, $m32, $m33]]"
  }
}