package org.dellamorte.raum.effectbuffers

/**
 * Created by Raum on 2016-01-28.
 */
class EffectPostProc(val name: String, val width: Int, val height: Int) {
  val fbuffer = BufferF(width, height)
  val texture = BufferT("image$name", width, height)
  val depth = BufferT("depth$name", width, height)
  //val depthR = BufferR(width, height)
  
  init {
    fbuffer.create()
    texture.initTexture()
    depth.initDepth()
    //depthR.create()
    fbuffer.unbind()
  }
  
  fun cleanUp() {
    fbuffer.cleanUp()
    texture.cleanUp()
    //depthR.cleanUp()
    depth.cleanUp()
  }
}