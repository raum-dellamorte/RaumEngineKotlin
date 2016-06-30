package org.dellamorte.raum.render

import org.lwjgl.opengl.GL11

/**
 * Created by Raum on 2016-06-30.
 */
class RenderPostProc(val name: String, var toDisplay: Boolean) {
  
  fun render() {
    
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4)
    
  }
}