package org.dellamorte.raum.input

import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.glfw.GLFWScrollCallback

/**
 * Created by Raum on 2016-01-18.
 */

class MouseScroll : GLFWScrollCallback() {
    var scrollAction: (kotlin.Double, kotlin.Double) -> Unit = {dx, dy -> }
    init {}

    override fun invoke(window: kotlin.Long, dx: kotlin.Double, dy: kotlin.Double) {
        //throw UnsupportedOperationException()
        scrollAction(dx, dy)
    }

    fun setListener(block: (kotlin.Double, kotlin.Double) -> Unit) {
        scrollAction = block
    }
}