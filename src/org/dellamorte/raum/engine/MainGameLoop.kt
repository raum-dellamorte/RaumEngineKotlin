package org.dellamorte.raum.engine

import org.dellamorte.raum.entities.Entity
import org.dellamorte.raum.entities.GuiObj
import org.dellamorte.raum.entities.StatusBar
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-18.
 */
class MainGameLoop {
  companion object {
    @JvmStatic public fun main(args: Array<String>) {
      DisplayMgr.start()
      DisplayMgr.setBGColor(
          RenderMgr.red, RenderMgr.grn, RenderMgr.blu, 1.0)
      
      GameMgr.loadGame()
      
      while (!DisplayMgr.isCloseRequested()) {
        DisplayMgr.prep()
        
        GameMgr.renderScene(true)
        GameMgr.renderGui()
        DisplayMgr.updateDisplay()
      }
      TextMgr.cleanUp()
      GameMgr.cleanUp()
      DisplayMgr.close()
    }
  }
}

