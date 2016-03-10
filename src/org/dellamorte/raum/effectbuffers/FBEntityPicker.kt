package org.dellamorte.raum.effectbuffers

import org.dellamorte.raum.engine.DisplayMgr
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer
import java.nio.IntBuffer

/**
 * Created by Raum on 2016-03-02.
 */
class FBEntityPicker {
  val width = DisplayMgr.width
  val height = DisplayMgr.height
  val entPicker = EffectEntityPicker(width, height)
  
  fun cleanUp() {
    entPicker.cleanUp()
  }
  
  fun bind() = entPicker.fbuffer.bind()
  
  fun unbind() = entPicker.fbuffer.unbind()
}