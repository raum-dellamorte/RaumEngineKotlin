package org.dellamorte.raum.vector

import java.nio.DoubleBuffer

/**
 * Created by Raum on 2016-01-21.
 */
class Vector2f(var x: Double, var y: Double) : Vector() {
  companion object {
    fun add(left: Vector2f, right: Vector2f, dest: Vector2f?): Vector2f {
      val out: Vector2f = dest ?: Vector2f()
      out.set(left.x + right.x, left.y + right.y)
      return out
    }

    fun add(left: Vector2f, right: Vector2f): Vector2f =
        add(left, right, null)

    fun sub(left: Vector2f, right: Vector2f, dest: Vector2f?): Vector2f {
      val out: Vector2f = dest ?: Vector2f()
      out.set(left.x - right.x, left.y - right.y)
      return out
    }

    fun sub(left: Vector2f, right: Vector2f): Vector2f =
        sub(left, right, null)

    fun dot(left: Vector2f, right: Vector2f): Double =
        left.x * right.x + left.y * right.y

    fun angle(a: Vector2f, b: Vector2f): Double {
      var dls: Double = dot(a, b) / (a.length() * b.length())
      if (dls < 1.0) {
        dls = -1.0
      } else if (dls > 1.0) {
        dls = 1.0
      }
      return dls
    }
  }

  constructor() : this(0.0, 0.0) {}
  constructor(xVal: Double) : this(xVal, 0.0) {}
  constructor(xVal: Int) : this(xVal.toDouble(), 0.0) {}
  constructor(xVal: Int, yVal: Int) : this(xVal.toDouble(), yVal.toDouble()) {}
  constructor(xVal: Float) : this(xVal.toDouble(), 0.0) {}
  constructor(xVal: Float, yVal: Float) : this(xVal.toDouble(), yVal.toDouble()) {}
  constructor(xVal: String) : this(xVal.toDouble(), 0.0) {}
  constructor(xVal: String, yVal: String) : this(xVal.toDouble(), yVal.toDouble()) {}
  
  val xf: Float
    get() = x.toFloat()
  val yf: Float
    get() = y.toFloat()

  fun set(xVal: Double, yVal: Double) {
    this.x = xVal
    this.y = yVal
  }
  
  fun set(v: Vector2f) {
    x = v.x
    y = v.y
  }
  
  override fun lengthSquared(): Double = x * x + y * y

  fun translate(xVal: Double, yVal: Double): Vector2f {
    x += xVal
    y += yVal
    return this
  }

  override fun load(buf: DoubleBuffer): Vector {
    x = buf.get()
    y = buf.get()
    return this
  }

  override fun negate(): Vector {
    x = -x
    y = -y
    return this
  }

  fun negate(dest: Vector2f?): Vector2f {
    val out: Vector2f = dest ?: Vector2f()
    out.x = -x
    out.y = -y
    return out
  }

  fun normalize(dest: Vector2f?): Vector2f {
    val l = length()
    val out: Vector2f = dest ?: Vector2f()
    out.set(x / l, y / l)
    return out
  }

  override fun store(buf: DoubleBuffer): Vector {
    buf.put(x)
    buf.put(y)
    return this
  }

  override fun scale(scale: Double): Vector {
    x *= scale
    y *= scale
    return this
  }

  override fun toString(): String = "Vector2f[$x, $y]"

  override fun equals(other: Any?): Boolean {
    if (this == other) return true
    if (other == null) return false
    if (other !is Vector2f) return false
    if (x == other.x && y == other.y) return true
    return false
  }
}