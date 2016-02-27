package org.dellamorte.raum.textures

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f

/**
 * Created by Raum on 2016-01-27.
 */
class TextureGui(val name: String, val pos: Vector2f, val scale: Vector2f) {
  val origScale = Vector2f(scale.x, scale.y)
  val origPos = Vector2f(pos.x, pos.y)
  val id: Int = GameMgr.getTexture(name)
  val transformation: Matrix4f get() = Maths.createTransformationMatrixGui(pos, scale)
  
  fun setXScalePercent(scl: Double) { scale.x =  (scl / 100.0) * origScale.x }
  fun setYScalePercent(scl: Double) { scale.y =  (scl / 100.0) * origScale.y }
}