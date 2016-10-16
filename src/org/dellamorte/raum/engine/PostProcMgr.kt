package org.dellamorte.raum.engine

import org.dellamorte.raum.render.RenderPostProc
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Vector2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-07-25.
 */
class PostProcMgr {
  companion object {
    val POSITIONS = doubleArrayOf(-1.0,1.0,-1.0,-1.0,1.0,1.0,1.0,-1.0)
    val quad = GameMgr.loader.loadToVAO(POSITIONS)
    //val FULLSCREEN = Maths.createTransformationMatrixGui(Vector2f(0.5,0.5), Vector2f(1, 1))
    
    val order = ArrayList<String>()
    val mainProcs = HashMap<String, RenderPostProc>()
    //val subProcs = HashMap<String, ArrayList<RenderPostProc>>() // should be able to keep supProcs inside a PostProc, so this is a maybe
  
    fun addPostProc(rpp: RenderPostProc) {
      mainProcs[rpp.name] = rpp
      order.add(rpp.name)
    }
    
    fun render() {
      var last = "Primary"
      if (order.size > 0) {
        prep()
        for (name in order) {
          val postProc: RenderPostProc? = mainProcs[name]
          if (postProc != null) {
            postProc.render(last)
            last = name
          }
        }
        post()
      } else {
        GameMgr.guiRend.render("imagePrimary")
      }
    }
    
    fun prep() {
      GL30.glBindVertexArray(quad.vaoID)
      GL20.glEnableVertexAttribArray(0)
//      GL11.glDisable(GL11.GL_DEPTH_TEST)
    }
    
    fun post() {
//      GL11.glEnable(GL11.GL_DEPTH_TEST)
      GL20.glDisableVertexAttribArray(0)
      GL30.glBindVertexArray(0)
    }
  }
}