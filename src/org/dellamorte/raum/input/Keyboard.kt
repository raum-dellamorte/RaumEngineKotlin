package org.dellamorte.raum.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback
import java.util.ArrayList
import java.util.HashMap
import kotlin.Boolean as KBoolean
import kotlin.Int as KInt
import kotlin.Long as KLong

/**
 * Created by Raum on 2016-01-18.
 */

class Keyboard : GLFWKeyCallback() {
  val keys = HashMap<KInt, KBoolean>()
  val actions = HashMap<String, (() -> Unit)?>()
  
  override fun invoke(window: KLong, 
                      key: KInt, 
                      scancode: KInt, 
                      action: KInt, 
                      mods: KInt) {
    //throw UnsupportedOperationException()
    keys[key] = action != GLFW.GLFW_RELEASE
    val runme = actions["w$window-k$key-a$action-m$mods"]
    if (runme != null) runme()
  }
  
  fun isKeyDown(key: KInt): KBoolean = keys[key] ?: false
  
  fun isKeyDown(vararg keys: KInt): KBoolean {
    for (key in keys) {
      val keyDown = isKeyDown(key)
      if (keyDown) return true
    }
    return false
  }
  
  fun addListener(window: KLong, key: KInt, action: KInt, mods: KInt, block: () -> Unit) {
    actions["w$window-k$key-a$action-m$mods"] = block
  }
}
