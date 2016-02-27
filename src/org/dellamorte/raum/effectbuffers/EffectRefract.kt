package org.dellamorte.raum.effectbuffers

/**
 * Created by Raum on 2016-01-28.
 */
class EffectRefract(val width: Int, val height: Int) {
  val fbuffer = FBuffer(width, height)
  val texture = TBuffer("refractTexture", width, height)
  val depth = TBuffer("depthTexture", width, height)
  
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