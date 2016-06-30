package org.dellamorte.raum.engine

/**
 * Created by Raum on 2016-01-18.
 */
class MainGameLoop {
  companion object {
    @JvmStatic fun main(args: Array<String>) {
      DisplayMgr.start()
      DisplayMgr.setBGColor(RenderMgr.red, RenderMgr.grn, RenderMgr.blu, 1.0)
      GameMgr.loadGame()
      
      while (!DisplayMgr.isCloseRequested()) {
        DisplayMgr.prep()
        
        RenderMgr.renderPrimaryBuffer(true)
        RenderMgr.renderPostProcs()
        GameMgr.renderGui()
        DisplayMgr.updateDisplay()
      }
      TextMgr.cleanUp()
      GameMgr.cleanUp()
      DisplayMgr.close()
    }
  }
}

