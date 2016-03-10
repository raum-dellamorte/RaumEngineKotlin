package org.dellamorte.raum.engine

/**
 * Created by Raum on 2016-01-18.
 */

import org.lwjgl.*
import org.lwjgl.glfw.*
import org.lwjgl.opengl.*

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import java.nio.IntBuffer
import org.dellamorte.raum.input.Keyboard
import org.dellamorte.raum.input.Mouse
import org.dellamorte.raum.input.MousePos
import org.dellamorte.raum.input.MouseScroll
import org.lwjgl.system.MemoryUtil.*
import java.util.*

class DisplayMgr {
  companion object {
    var width: Int = 800
    var height: Int = 600
    var fpsCap: Int = 120
    private val errorCallback = GLFWErrorCallback.createPrint(System.err)
    var delta: Double = 0.0
    private var lastTime: Double = 0.0
    private var lastFpsTime: Double = 0.0
    var fps: Double = 0.0
    private var frames: Int = 0
    var window: Long = 0
    val keyboard = Keyboard()
    val mouse = Mouse()
    
    val debugMsgs = HashMap<String, String>()
    
    fun start(): Boolean {
      glfwSetErrorCallback(errorCallback)
      if ( glfwInit() != GLFW_TRUE ) {
        // throw IllegalStateException.new("Unable to initialize GLFW")
        println("Unable to initialize GLFW")
        return false
      }
      
      glfwDefaultWindowHints()
      glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
      glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
      glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
      glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
      
      window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL)
      if (window == NULL) {
        // throw RuntimeException("Failed to create the GLFW window")
        println("Failed to create the GLFW window")
        return false
      }
      
      glfwSetKeyCallback(window, keyboard)
      glfwSetMouseButtonCallback(window, mouse)
      glfwSetCursorPosCallback(window, mouse.pos)
      glfwSetScrollCallback(window, mouse.scroll)
      
      val vidmode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
      glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2)
      
      glfwMakeContextCurrent(window)
      glfwSwapInterval(1)
      glfwShowWindow(window)
      GL.createCapabilities()
      lastTime = getCurrentTime()
      lastFpsTime = lastTime
      return true
    }
    
    fun setBGColor(r: Double, g: Double, b: Double, a: Double) =
        glClearColor(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
    
    fun prep() {
      glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
      glfwPollEvents()
    }
    
    fun updateDisplay() {
      glfwSwapBuffers(window)
      if (keyboard.isKeyDown(GLFW_KEY_ESCAPE))
        glfwSetWindowShouldClose(window, GLFW_TRUE)
      val timeNow = getCurrentTime()
      frames++
      delta = (timeNow - lastTime)
      lastTime = timeNow
      val deltaFps = timeNow - lastFpsTime
      if (deltaFps >= 1.0) {
        fps = frames.toDouble() / deltaFps
        frames = 0
        lastFpsTime = timeNow
        println("FPS: $fps")
        for (key in debugMsgs.keys) println("$key: ${debugMsgs[key]!!}")
        debugMsgs.clear()
      }
    }
    
    fun isCloseRequested(): Boolean = glfwWindowShouldClose(window) == GLFW_TRUE
    
    fun close() {
      glfwDestroyWindow(window)
      keyboard.release()
      glfwTerminate()
      errorCallback.release()
    }
    
    fun getFrameTimeSeconds(): Float = delta.toFloat()
    
    fun getCurrentTime(): Double = glfwGetTime()
    
    fun updateWH() {
      val tmpW: IntBuffer = BufferUtils.createIntBuffer(1)
      val tmpH: IntBuffer = BufferUtils.createIntBuffer(1)
      glfwGetWindowSize(window, tmpW, tmpH)
      width = tmpW.get(0)
      height = tmpH.get(0)
    }
  }

}