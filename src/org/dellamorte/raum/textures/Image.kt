package org.dellamorte.raum.textures

import java.io.IOException
import org.lwjgl.BufferUtils
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer
import java.nio.IntBuffer
/**
 * Created by Raum on 2016-01-20.
 */
class Image(buffer: ByteBuffer) {
    val image: ByteBuffer
    val width: Int
    val height: Int
    val components: Int
    init {
        val w: IntBuffer = BufferUtils.createIntBuffer(1)
        val h: IntBuffer = BufferUtils.createIntBuffer(1)
        val c: IntBuffer = BufferUtils.createIntBuffer(1)
        image = STBImage.stbi_load_from_memory(buffer, w, h, c, 0)
        width = w.get(0)
        height = h.get(0)
        components = c.get(0)
    }

    fun dispose() = STBImage.stbi_image_free(image)
}