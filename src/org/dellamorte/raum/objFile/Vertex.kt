package org.dellamorte.raum.objFile

import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-31.
 */
class Vertex(val index: Int, val pos: Vector3f) {
  val noIndex = -1
  val length = pos.length()
  var textureIndex = noIndex
  var normalIndex = noIndex
  var dupVertex: Vertex? = null
  
  fun isSet(): Boolean =
      ((textureIndex != noIndex) and (normalIndex != noIndex))
  
  fun hasSameTextureAndNormal(textureIndexOther: Int, normalIndexOther: Int): Boolean =
      ((textureIndexOther == textureIndex) and (normalIndexOther == normalIndex))
}