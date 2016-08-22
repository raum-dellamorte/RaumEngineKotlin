package org.dellamorte.raum.render

import org.dellamorte.raum.effectbuffers.FBPostProc
import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.shaders.ShaderPostProc
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

/**
 * Created by Raum on 2016-06-30.
 */
class RenderPostProc(val name: String, var useDepth: Boolean, internal var fbo: FBPostProc?, var shader: ShaderPostProc) {
  constructor(name: String) : this(name, false, null, ShaderPostProc(name))
  constructor(name: String, useDepth: Boolean) : this(name, useDepth, null, ShaderPostProc(name))
  constructor(name: String, fbo: FBPostProc?) : this(name, false, fbo, ShaderPostProc(name))
  constructor(name: String, useDepth: Boolean, fbo: FBPostProc) : this(name, useDepth, fbo, ShaderPostProc(name))
  
  var omitSky = false //if (useDepth) true else false // prolly can just be "= useDepth" but I can't remember right now
  
  fun mkFBO() { fbo = FBPostProc(name) }
  fun rmFBO() { 
    fbo?.cleanUp()
    fbo = null
  }
  fun bind() = fbo?.bind()
  fun unbind() = fbo?.unbind()
  
  fun render(sourceName: String) {
    shader.start()
    
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("image$sourceName"))
    if (useDepth) {
      GL13.glActiveTexture(GL13.GL_TEXTURE1)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("depth$sourceName"))
    }
    if (omitSky) {
      GL13.glActiveTexture(GL13.GL_TEXTURE2)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, GameMgr.getTexture("omitSkyBuffer"))
    }
    
    fbo?.bind()
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4)
    fbo?.unbind()
    
    shader.stop()
  }
  
  fun cleanUp() {
    shader.cleanUp()
  }
}