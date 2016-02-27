package org.dellamorte.raum.entities

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.textures.TextureGui
import java.util.*

/**
 * Created by Raum on 2016-01-27.
 */
class GuiObj(var x: Double, var y: Double, scale: Double) {
  var scaleX = scale
  var scaleY = scale
  val guiTextures = ArrayList<TextureGui>()
  val statBars = ArrayList<StatusBar>()
  
  fun loadTextures(vararg textures: String): GuiObj {
    for (txtr: String in textures) 
      guiTextures.add(GameMgr.getTextureGui(txtr, x, y, scaleX, scaleY))
    return this
  }
  
  fun update() { for (statBar in statBars) statBar.update() }
  
  fun attachStatusBar(player: Player, statType: String, texture: String) {
    var bar: TextureGui? = null
    for (tgui in guiTextures) {
      if (tgui.name != texture) continue
      bar = tgui
      break
    }
    if (bar == null) return
    statBars.add(
        StatusBar(player, bar, statType)
    )
  }
}