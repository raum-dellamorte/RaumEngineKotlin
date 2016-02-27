package org.dellamorte.raum.effectbuffers

/**
 * Created by Raum on 2016-01-28.
 */
class FBWater {
  val reflect = EffectReflect(320, 180)
  val refract = EffectRefract(1280, 720)
  
  fun cleanUp() {
    reflect.cleanUp()
    refract.cleanUp()
  }
  
  fun bindReflectFB() = reflect.fbuffer.bind()
  fun bindRefractFB() = refract.fbuffer.bind()
  
  fun unbind() { 
    reflect.fbuffer.unbind()
    refract.fbuffer.unbind()
  }
}