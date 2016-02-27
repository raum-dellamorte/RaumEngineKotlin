package org.dellamorte.raum.text

import org.dellamorte.raum.engine.TextMgr
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-21.
 */
class GuiText(val text: String, val font: FontType,
              val fontSize: Double, val position: Vector2f,
              val lineMaxSize: Double, var isCentered: Boolean) {
  var numberOfLines = 1
  val colour = Vector3f()
  var textMeshVao = 0
  var vertexCount = 0
  init {
    TextMgr.loadText(this)
  }
  
  fun remove() = TextMgr.removeText(this)
    
  fun setColour(r: Float, g: Float, b: Float) = 
    setColour(r.toDouble(), g.toDouble(), b.toDouble())
  
  fun setColour(r: Double, g: Double, b: Double) =
    colour.set(r, g, b)
  
  fun setMeshInfo(vao: Int, vertCount: Int) {
    textMeshVao = vao
    vertexCount = vertCount
  }
}