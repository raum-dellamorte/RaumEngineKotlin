package org.dellamorte.raum.input

import org.dellamorte.raum.engine.DisplayMgr
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWMouseButtonCallback
import java.util.*
import kotlin.Boolean as KBoolean
import kotlin.Int as KInt
import kotlin.Long as KLong

/**
 * Created by Raum on 2016-01-18.
 */

class Mouse : GLFWMouseButtonCallback() {
    val pos = MousePos()
    val scroll = MouseScroll()
    
    private val actions = HashMap<String, (() -> Unit)?>()
    private val buttons = Array<KBoolean>(12, {i -> false})
    init {}

    override fun invoke(window: KLong, button: KInt, action: KInt, mods: KInt) {
        //throw UnsupportedOperationException()
        buttons[button] = action != GLFW.GLFW_RELEASE
        val runme = actions["w$window-k$button-a$action-m$mods"]
        if (runme != null) runme()
    }

    fun isButtonDown(btn: KInt): KBoolean = buttons[btn]

    fun addListener(window: KLong, key: KInt, action: KInt, mods: KInt, block: () -> Unit) {
        actions["w$window-k$key-a$action-m$mods"] = block
    }
    
    fun addListenerLClick(block: () -> Unit) {
        addListener(DisplayMgr.window, GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS, 0, block)
    }
    
    fun addListenerRClick(block: () -> Unit) {
        addListener(DisplayMgr.window, GLFW.GLFW_MOUSE_BUTTON_RIGHT, GLFW.GLFW_PRESS, 0, block)
    }
    
    fun addListenerMClick(block: () -> Unit) {
        addListener(DisplayMgr.window, GLFW.GLFW_MOUSE_BUTTON_MIDDLE, GLFW.GLFW_PRESS, 0, block)
    }
}