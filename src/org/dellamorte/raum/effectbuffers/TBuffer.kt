package org.dellamorte.raum.effectbuffers

import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
import java.nio.ByteBuffer

/**
 * Created by Raum on 2016-01-28.
 */
class TBuffer(val name: String, var width: Int = 200, var height: Int = 200) {
  var id = 0
  
  fun initTexture() {
    id = GL11.glGenTextures()
    GameMgr.addTexture(name, id)
    val bfr: ByteBuffer? = null
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, bfr)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, id, 0)
  }
  
  fun initDepth() {
    id = GL11.glGenTextures()
    GameMgr.addTexture(name, id)
    val bfr: ByteBuffer? = null
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, bfr)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, id, 0)
  }
  
  fun cleanUp() = GL11.glDeleteTextures(id)
}