package org.dellamorte.raum.effectbuffers

/**
 * Created by Raum on 2016-01-28.
 */
class EffectReflect(val width: Int, val height: Int) {
  val fbuffer = BufferF(width, height)
  val texture = BufferT("reflectTexture", width, height)
  val depth = BufferR(width, height)
  
  init {
    fbuffer.create()
    texture.initTexture()
    depth.create()
    fbuffer.unbind()
  }
  
  fun cleanUp() {
    fbuffer.cleanUp()
    texture.cleanUp()
    depth.cleanUp()
  }
  
}