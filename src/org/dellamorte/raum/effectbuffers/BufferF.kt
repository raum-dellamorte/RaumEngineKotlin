package org.dellamorte.raum.effectbuffers

import org.dellamorte.raum.engine.DisplayMgr
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

/**
 * Created by Raum on 2016-01-28.
 */
class BufferF(var width: Int = 200, var height: Int = 200) {
  var id = 0
  
  fun create() {
    id = GL30.glGenFramebuffers()
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id)
    GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0)
  }
  
  fun cleanUp() = GL30.glDeleteFramebuffers(id)
  
  fun bind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id)
    GL11.glViewport(0, 0, width, height)
  }
  
  fun unbind() {
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    GL11.glViewport(0, 0, DisplayMgr.width, DisplayMgr.height)
  }
}