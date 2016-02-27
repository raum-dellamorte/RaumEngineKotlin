package org.dellamorte.raum.input

import org.lwjgl.glfw.GLFWCursorPosCallback

/**
 * Created by Raum on 2016-01-18.
 */

class MousePos : GLFWCursorPosCallback() {
    var xPos = 0.0
    var yPos = 0.0
    var dx = 0.0
    var dy = 0.0
    init {}

    override fun invoke(window: kotlin.Long, xpos: kotlin.Double, ypos: kotlin.Double) {
        //throw UnsupportedOperationException()
        dx = if (xPos == 0.0) 0.0 else (xPos - xpos)
        dy = if (yPos == 0.0) 0.0 else (yPos - ypos)
        xPos = xpos
        yPos = ypos
    }

    fun getX() = xPos
    fun getY() = yPos

    fun getDX(): kotlin.Double {
        val out = dx
        dx = 0.0
        return out
    }

    fun getDY(): kotlin.Double {
        val out = dy
        dy = 0.0
        return out
    }
}