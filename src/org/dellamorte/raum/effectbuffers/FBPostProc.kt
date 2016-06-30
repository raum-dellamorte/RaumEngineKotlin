package org.dellamorte.raum.effectbuffers

import org.dellamorte.raum.engine.DisplayMgr

/**
 * Created by Raum on 2016-03-02.
 */
class FBPostProc(val textureName: String) {
  val width: Int
    get() = DisplayMgr.width
  val height: Int
    get() = DisplayMgr.height
  var image = EffectPostProc(textureName, width, height)
  
  fun refresh() {
    image.cleanUp()
    image = EffectPostProc(textureName, width, height)
  }
  
  fun cleanUp() {
    image.cleanUp()
  }
  
  fun bind() = image.fbuffer.bind()
  
  fun unbind() = image.fbuffer.unbind()
}