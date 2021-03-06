package org.dellamorte.raum.text

import org.dellamorte.raum.engine.TextMgr
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-21.
 */
class GuiText(val label: String, var text: String, val font: FontType,
              val fontSize: Double, val position: Vector2f,
              val lineMaxSize: Double, var isCentered: Boolean) {
  var numberOfLines = 1
  val colour = Vector3f()
  var textMeshVao = 0
  var vertexCount = 0
  var loaded = false
  
  fun load() {
    if (loaded) return
    val data: TextMeshData = font.loadText(this)
    val vao = TextMgr.ldr.loadToVAO(data.vertexPositions, data.textureCoords)
    setMeshInfo(vao, data.vertexCount)
    loaded = true
  }
  
  fun update(str: String) {
    text = str
    if (textMeshVao == 0) return
    TextMgr.ldr.rmVaoID(textMeshVao)
    loaded = false
    load()
  }
  
  fun setColour(r: Float, g: Float, b: Float) = 
    setColour(r.toDouble(), g.toDouble(), b.toDouble())
  
  fun setColour(r: Double, g: Double, b: Double) =
    colour.set(r, g, b)
  
  fun setMeshInfo(vao: Int, vertCount: Int) {
    textMeshVao = vao
    vertexCount = vertCount
  }
}