package org.dellamorte.raum.shaders

import java.util.*

/**
 * Created by Raum on 2016-07-24.
 */
class ShaderPostProc(val name: String) : Shader(name) {
  override fun bindAttributes(attribs: ArrayList<String>?) {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations(uniforms: ArrayList<String>?) {
    if (uniforms != null) {
      
    }
  }
}