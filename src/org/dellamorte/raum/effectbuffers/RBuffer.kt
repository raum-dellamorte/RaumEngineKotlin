package org.dellamorte.raum.effectbuffers

import org.dellamorte.raum.engine.DisplayMgr
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

/**
 * Created by Raum on 2016-01-28.
 */
class RBuffer(var width: Int = 200, var height: Int = 200) {
  var id = 0
  
  fun create() {
    id = GL30.glGenRenderbuffers()
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id)
    GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height)
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, id)
  }
  
  fun cleanUp() = GL30.glDeleteRenderbuffers(id)
  
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