package org.dellamorte.raum.effectbuffers

/**
 * Created by Raum on 2016-03-02.
 */
class EffectEntityPicker(val width: Int, val height: Int) {
  val fbuffer = FBuffer(width, height)
  val texture = TBuffer("entityPickerTexture", width, height)
  val depth = RBuffer(width, height)
  
  init {
    fbuffer.create()
    texture.initTexture()
    depth.create()
    fbuffer.unbind()
  }
  
  fun cleanUp() {
    fbuffer.cleanUp()
    texture.cleanUp()
  }
}