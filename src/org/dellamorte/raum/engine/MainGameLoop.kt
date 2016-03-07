package org.dellamorte.raum.engine

/**
 * Created by Raum on 2016-01-18.
 */
class MainGameLoop {
  companion object {
    @JvmStatic public fun main(args: Array<String>) {
      DisplayMgr.start()
      DisplayMgr.setBGColor(
          RenderMgr.red, RenderMgr.grn, 0.0, 1.0)
      
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

