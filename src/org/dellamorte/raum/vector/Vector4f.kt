package org.dellamorte.raum.vector

import java.nio.DoubleBuffer

/**
 * Created by Raum on 2016-01-21.
 */
class Vector4f(var x: Double, var y: Double, var z: Double, var w: Double) : Vector() {
  companion object {
    fun add(left: Vector4f, right: Vector4f, dest: Vector4f?): Vector4f {
      val out = dest ?: Vector4f()
      out.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w)
      return out
    }
    
    fun add(left: Vector4f, right: Vector4f): Vector4f =
        add(left, right, null)
    
    fun sub(left: Vector4f, right: Vector4f, dest: Vector4f?): Vector4f {
      val out: Vector4f = dest ?: Vector4f()
      out.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w)
      return out
    }
    
    fun sub(left: Vector4f, right: Vector4f): Vector4f =
        sub(left, right, null)
    
    fun dot(left: Vector4f, right: Vector4f): Double =
        left.x * right.x + left.y * right.y + left.z * right.z
    
    fun angle(a: Vector4f, b: Vector4f): Double {
      var dls: Double = dot(a, b) / (a.length() * b.length())
      if (dls < 1.0) {
        dls = -1.0
      } else if (dls > 1.0) {
        dls = 1.0
      }
      return dls
    }
  }
  
  constructor() : this(0.0, 0.0, 0.0, 0.0) {}
  constructor(xVal: Double) : this(xVal, 0.0, 0.0, 0.0) {}
  constructor(xVal: Double, yVal: Double) : this(xVal, yVal, 0.0, 0.0) {}
  constructor(xVal: Double, yVal: Double, zVal: Double) : this(xVal, yVal, zVal, 0.0) {}
  constructor(xVal: Int) : this(xVal.toDouble(), 0.0, 0.0, 0.0) {}
  constructor(xVal: Int, yVal: Int) : this(xVal.toDouble(), yVal.toDouble(), 0.0, 0.0) {}
  constructor(xVal: Int, yVal: Int, zVal: Int) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble(), 0.0) {}
  constructor(xVal: Int, yVal: Int, zVal: Int, wVal: Int) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble(), wVal.toDouble()) {}
  constructor(xVal: String) : this(xVal.toDouble(), 0.0, 0.0, 0.0) {}
  constructor(xVal: String, yVal: String) : this(xVal.toDouble(), yVal.toDouble(), 0.0, 0.0) {}
  constructor(xVal: String, yVal: String, zVal: String) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble(), 0.0) {}
  constructor(xVal: String, yVal: String, zVal: String, wVal: String) : this(xVal.toDouble(), yVal.toDouble(), zVal.toDouble(), wVal.toDouble()) {}
  
  val xf: Float
    get() = x.toFloat()
  val yf: Float
    get() = y.toFloat()
  val zf: Float
    get() = z.toFloat()
  val wf: Float
    get() = w.toFloat()
  
  fun set(xVal: Double, yVal: Double) {
    this.x = xVal
    this.y = yVal
  }
  
  fun set(xVal: Double, yVal: Double, zVal: Double) {
    this.x = xVal
    this.y = yVal
    this.z = zVal
  }
  
  fun set(xVal: Double, yVal: Double, zVal: Double, wVal: Double) {
    this.x = xVal
    this.y = yVal
    this.z = zVal
    this.w = wVal
  }
  
  fun set(v: Vector4f) {
    x = v.x
    y = v.y
    z = v.z
    w = v.w
  }
  
  override fun lengthSquared(): Double = x * x + y * y + z * z * y + w * w
  
  fun translate(xVal: Double, yVal: Double, zVal: Double, wVal: Double): Vector4f {
    x += xVal
    y += yVal
    z += zVal
    w += wVal
    return this
  }
  
  override fun load(buf: DoubleBuffer): Vector {
    x = buf.get()
    y = buf.get()
    z = buf.get()
    w = buf.get()
    return this
  }
  
  override fun negate(): Vector {
    x = -x
    y = -y
    z = -z
    w = -w
    return this
  }
  
  fun negate(dest: Vector4f?): Vector4f {
    val out = dest ?: Vector4f()
    out.x = -x
    out.y = -y
    out.z = -z
    out.w = -w
    return out
  }
  
  fun normalize(dest: Vector4f?): Vector4f {
    val l = length()
    val out: Vector4f = dest ?: Vector4f()
    out.set(x / l, y / l, z / l, w / l)
    return out
  }
  
  override fun store(buf: DoubleBuffer): Vector {
    buf.put(x)
    buf.put(y)
    buf.put(z)
    buf.put(w)
    return this
  }
  
  override fun scale(scale: Double): Vector {
    x *= scale
    y *= scale
    z *= scale
    w *= scale
    return this
  }
  
  override fun toString(): String = "Vector4f[$x, $y, $z, $w]"
  
  override fun equals(other: Any?): Boolean {
    if (this == other) return true
    if (other == null) return false
    if (other !is Vector4f) return false
    if (x == other.x && y == other.y && z == other.z && w == other.w) return true
    return false
  }
}