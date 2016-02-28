package org.dellamorte.raum.textures

import org.dellamorte.raum.engine.GameMgr

/**
 * Created by Raum on 2016-02-25.
 */
class TextureParticle(val id: Int, val numOfRows: Int, val additive: Boolean) {
  constructor(tID: String, rows: Int, additive: Boolean) : this(GameMgr.getTexture(tID), rows, additive) {}
}