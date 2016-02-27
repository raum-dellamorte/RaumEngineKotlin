package org.dellamorte.raum.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWMouseButtonCallback
import java.util.*

/**
 * Created by Raum on 2016-01-18.
 */

class Mouse : GLFWMouseButtonCallback() {
    val actions = HashMap<String, (() -> Unit)?>()
    val buttons = Array<kotlin.Boolean>(12, {i -> false})
    init {}

    override fun invoke(window: kotlin.Long, button: kotlin.Int, action: kotlin.Int, mods: kotlin.Int) {
        //throw UnsupportedOperationException()
        buttons[button] = action != GLFW.GLFW_RELEASE
        val runme = actions["w${window}-k${button}-a${action}-m${mods}"]
        if (runme != null) runme()
    }

    fun isButtonDown(btn: kotlin.Int): kotlin.Boolean = buttons[btn]

    fun addListener(window: Long, key: Int, action: Int, mods: Int, block: () -> Unit) {
        actions["w${window}-k${key}-a${action}-m${mods}"] = block
    }
}