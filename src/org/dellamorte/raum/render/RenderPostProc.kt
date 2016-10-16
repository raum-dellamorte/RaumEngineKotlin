package org.dellamorte.raum.render

import org.dellamorte.raum.effectbuffers.FBO
import org.dellamorte.raum.effectbuffers.FBPostProc
import org.dellamorte.raum.engine.DisplayMgr
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.shaders.ShaderPostProc
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*

/**
 * Created by Raum on 2016-06-30.
 */
class RenderPostProc(val name: String, var useDepth: Boolean, internal var fbo: FBO?, var shader: ShaderPostProc) {
  constructor(name: String) : this(name, false, null, ShaderPostProc(name))
  constructor(name: String, useDepth: Boolean) : this(name, useDepth, null, ShaderPostProc(name))
  constructor(name: String, fbo: FBO?) : this(name, false, fbo, ShaderPostProc(name))
  constructor(name: String, useDepth: Boolean, fbo: FBO) : this(name, useDepth, fbo, ShaderPostProc(name))
  
  var omitSky = false //if (useDepth) true else false // prolly can just be "= useDepth" but I can't remember right now
  
  fun mkFBO() { fbo = FBO(name, DisplayMgr.width, DisplayMgr.height, FBO.DEPTH_RENDER_BUFFER) }
  fun rmFBO() { 
    fbo?.cleanUp()
    fbo = null
  }
  fun bind() = fbo?.bind()
  fun unbind() = fbo?.unbind()
  
  fun render(sourceName: String, depthName: String = "Primary") {
    shader.start()
    
    glActiveTexture(GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, GameMgr.getTexture("image$sourceName"))
    if (useDepth) {
      glActiveTexture(GL_TEXTURE1)
      glBindTexture(GL_TEXTURE_2D, GameMgr.getTexture("depth$depthName"))
    }
    if (omitSky) {
      glActiveTexture(GL_TEXTURE2)
      glBindTexture(GL_TEXTURE_2D, GameMgr.getTexture("omitSkyBuffer"))
    }
    
    bind()
    glClear(GL_COLOR_BUFFER_BIT);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    unbind()
    
    shader.stop()
  }
  
  fun cleanUp() {
    shader.cleanUp()
  }
}