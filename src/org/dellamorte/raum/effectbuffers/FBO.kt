package org.dellamorte.raum.effectbuffers
/**
 * Adapted to Kotlin by Raum Dellamorte from ThinMatrix tutorial on YouTube
 */
import java.nio.ByteBuffer
import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
/**
 * Creates an FBO of a specified width and height, with the desired type of
 * depth buffer attachment.
 *
 * @param width
 * - the width of the FBO.
 * @param height
 * - the height of the FBO.
 * @param depthBufferType
 * - an int indicating the type of depth buffer attachment that
 * this FBO should use.
 */
class FBO(val name: String, val width:Int = 0, val height:Int = 0, val depthBufferType:Int = 0) {
  companion object {
    val NONE = 0
    val DEPTH_TEXTURE = 1
    val DEPTH_RENDER_BUFFER = 2
  }
  val imageName = "image$name"
  val depthName = "depth$name"
  val imageTexture: Int
    get() = GameMgr.getTexture(imageName)
  val depthTexture:Int
    get() = GameMgr.getTexture(depthName)
  private var frameBuffer:Int = 0
  private var depthBuffer:Int = 0
  private var colourBuffer:Int = 0
  init{
    initFB(depthBufferType)
  }
  /**
   * Deletes the frame buffer and its attachments when the game closes.
   */
  fun cleanUp() {
    GL30.glDeleteFramebuffers(frameBuffer)
    GL11.glDeleteTextures(imageTexture)
    GL11.glDeleteTextures(depthTexture)
    GL30.glDeleteRenderbuffers(depthBuffer)
    GL30.glDeleteRenderbuffers(colourBuffer)
  }
  /**
   * Binds the frame buffer, setting it as the current render target. Anything
   * rendered after this will be rendered to this FBO, and not to the screen.
   */
  fun bind() {
    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer)
    GL11.glViewport(0, 0, width, height)
  }
  /**
   * Unbinds the frame buffer, setting the default frame buffer as the current
   * render target. Anything rendered after this will be rendered to the
   * screen, and not this FBO.
   */
  fun unbind() {
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0)
    GL11.glViewport(0, 0, DisplayMgr.width, DisplayMgr.height)
  }
  /**
   * Binds the current FBO to be read from (not used in tutorial 43).
   */
  fun bindToRead() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer)
    GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0)
  }
  /**
   * Creates the FBO along with a colour buffer texture attachment, and
   * possibly a depth buffer.
   *
   * @param type
   * - the type of depth buffer attachment to be attached to the
   * FBO.
   */
  private fun initFB(type:Int) {
    createFB()
    createTextureAttachment()
    if (type == DEPTH_RENDER_BUFFER)
    {
      createDepthBufferAttachment()
    }
    else if (type == DEPTH_TEXTURE)
    {
      createDepthTextureAttachment()
    }
    unbind()
  }
  /**
   * Creates a new frame buffer object and sets the buffer to which drawing
   * will occur - colour attachment 0. This is the attachment where the colour
   * buffer texture is.
   *
   */
  private fun createFB() {
    frameBuffer = GL30.glGenFramebuffers()
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer)
    GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0)
  }
  /**
   * Creates a texture and sets it as the colour buffer attachment for this
   * FBO.
   */
  private fun createTextureAttachment() {
    GameMgr.addTexture(imageName, GL11.glGenTextures())
    val image = imageTexture
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, image)
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, image, 0)
  }
  /**
   * Adds a depth buffer to the FBO in the form of a texture, which can later
   * be sampled.
   */
  private fun createDepthTextureAttachment() {
    GameMgr.addTexture(depthName, GL11.glGenTextures())
    val depth = depthTexture
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, depth)
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, null as ByteBuffer?)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth, 0)
    //GL11.glCopyTexImage2D()
  }
  /**
   * Adds a depth buffer to the FBO in the form of a render buffer. This can't
   * be used for sampling in the shaders.
   */
  private fun createDepthBufferAttachment() {
    depthBuffer = GL30.glGenRenderbuffers()
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer)
    GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height)
    GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer)
  }
}