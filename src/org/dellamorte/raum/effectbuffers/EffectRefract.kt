package org.dellamorte.raum.effectbuffers

/**
 * Created by Raum on 2016-01-28.
 */
class EffectRefract(val width: Int, val height: Int) {
  val fbuffer = BufferF(width, height)
  val texture = BufferT("refractTexture", width, height)
  val depth = BufferT("depthTexture", width, height)
  
  init {
    fbuffer.create()
    texture.initTexture()
    depth.initDepth()
    fbuffer.unbind()
  }
  
  fun cleanUp() {
    fbuffer.cleanUp()
    texture.cleanUp()
    depth.cleanUp()
  }
}