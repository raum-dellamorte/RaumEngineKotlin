package org.dellamorte.raum.effectbuffers

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

/**
 * Created by Raum on 2016-03-02.
 */
class FBEntityPicker {
  val entPicker = EffectEntityPicker(1280, 720)
  val imageBuffer = BufferUtils.createByteBuffer(1280 * 720 * 3)
  val image: ByteBuffer
      get() {
        imageToBuffer()
        return imageBuffer
      }
  
  fun cleanUp() {
    entPicker.cleanUp()
  }
  
  fun bind() = entPicker.fbuffer.bind()
  
  fun unbind() = entPicker.fbuffer.unbind()
  
  fun imageToBuffer() { GL11.glReadPixels(0, 0, 1280, 720, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image) }
}