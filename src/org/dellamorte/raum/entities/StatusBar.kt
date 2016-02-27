package org.dellamorte.raum.entities

import org.dellamorte.raum.textures.TextureGui

/**
 * Created by Raum on 2016-01-28.
 */
class StatusBar(val player: Player, val bar: TextureGui, val statType: String) {
  
  fun update() = 
      bar.setXScalePercent(player.stats[statType] ?: 0.0)
}