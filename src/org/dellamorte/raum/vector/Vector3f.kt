package org.dellamorte.raum.vector

import java.nio.DoubleBuffer
/**
 * Created by Raum on 2016-01-21.
 */
class Vector3f(var x: Double, var y: Double, var z: Double) : Vector() {
  companion object {
    fun add(left: Vector3f, right: Vector3f, dest: Vector3f?): Vector3f {
      val out: Vector3f = dest ?: Vector3f()
      out.set(left.x + right.x, left.y + right.y, left.z + right.z)
      return out
    }

    fun add(left: Vector3f, right: Vector3f): Vector3f =
      add(left, right, null)

    fun sub(left: Vector3f, right: Vector3f, dest: Vector3f?): Vector3f {
      val out: Vector3f = dest ?: Vector3f()
      out.set(left.x - right.x, left.y - right.y, left.z - right.z)
      return out
    }

    fun sub(left: Vector3f, right: Vector3f): Vector3f =
      sub(left, right, null)

    fun cross(left: Vector3f, right: Vector3f, dest: Vector3f?): Vector3f {
      val out: Vector3f = dest ?: Vector3f()
      out.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x)
      return out
    }

    fun cross(left: Vector3f, right: Vector3f): Vector3f =
        cross(left, right, null)

    fun dot(left: Vector3f, right: Vector3f): Double =
        left.x * right.x + left.y * right.y + left.z * right.z

    fun angle(a: Vector3f, b: Vector3f): Double {
      var dls: Double = dot(a, b) / (a.length() * b.length())
      if (dls < 1.0) {
        dls = -1.0
      } else if (dls > 1.0) {
        dls = 1.0
      }
      return dls
    }
  }

  constructor() : this(0.0, 0.0, 0.0) {}
  constructor(v: Vector3f) : this(v.x, v.y, v.z) {}
  constructor(v: Vector4f) : this(v.x, v.y, v.z) {}
  constructor(xVal: Double) : this(xVal, 0.0, 0.0) {}
  constructor(xVal: Double, yVal: Double) : this(xVal, yVal, 0.0) {}
  constructor(xVal: Float) : this(xVal.toDouble(), 0.0, 0.0) {}
  constructor(xVal: Float, yVal: Float) : this(xVal.toDouble(), yVal.toDouble(), 0.0) {}
  constructor(xVal: Float, yVal: Float, zVal: Float) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble()) {}
  constructor(xVal: Int) : this(xVal.toDouble(), 0.0, 0.0) {}
  constructor(xVal: Int, yVal: Int) : this(xVal.toDouble(), yVal.toDouble(), 0.0) {}
  constructor(xVal: Int, yVal: Int, zVal: Int) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble()) {}
  constructor(xVal: String) : this(xVal.toDouble(), 0.0, 0.0) {}
  constructor(xVal: String, yVal: String) : this(xVal.toDouble(), yVal.toDouble(), 0.0) {}
  constructor(xVal: String, yVal: String, zVal: String) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble()) {}
  
  val xf: Float
    get() = x.toFloat()
  val yf: Float
    get() = y.toFloat()
  val zf: Float
    get() = z.toFloat()
  
  operator fun plus(v: Vector3f): Vector3f {
    return add(this, v)
  }
  
  fun set(xVal: Double, yVal: Double) {
    this.x = xVal
    this.y = yVal
  }

  fun set(xVal: Double, yVal: Double, zVal: Double) {
    this.x = xVal
    this.y = yVal
    this.z = zVal
  }
  
  fun set(v: Vector3f) {
    x = v.x
    y = v.y
    z = v.z
  }
  
  fun set(v: Vector4f) {
    x = v.x
    y = v.y
    z = v.z
  }

  override fun lengthSquared(): Double = x * x + y * y + z * z

  fun translate(xVal: Double, yVal: Double, zVal: Double): Vector3f {
    x += xVal
    y += yVal
    z += zVal
    return this
  }

  override fun load(buf: DoubleBuffer): Vector {
    x = buf.get()
    y = buf.get()
    z = buf.get()
    return this
  }

  override fun negate(): Vector {
    x = -x
    y = -y
    z = -z
    return this
  }

  fun negate(dest: Vector3f?): Vector3f {
    val out: Vector3f = dest ?: Vector3f()
    out.x = -x
    out.y = -y
    out.z = -z
    return out
  }

  fun normalize(dest: Vector3f?): Vector3f {
    val l = length()
    val out: Vector3f = dest ?: Vector3f()
    out.set(x / l, y / l, z / l)
    return out
  }

  override fun store(buf: DoubleBuffer): Vector {
    buf.put(x)
    buf.put(y)
    buf.put(z)
    return this
  }

  override fun scale(scale: Double): Vector {
    x *= scale
    y *= scale
    z *= scale
    return this
  }

  override fun toString(): String = "Vector3f[$x, $y, $z]"

  override fun equals(other: Any?): Boolean {
    if (this == other) return true
    if (other == null) return false
    if (other !is Vector3f) return false
    if (x == other.x && y == other.y && z == other.z) return true
    return false
  }
}