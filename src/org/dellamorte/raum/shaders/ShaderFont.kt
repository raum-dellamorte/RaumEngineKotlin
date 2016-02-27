package org.dellamorte.raum.shaders

import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-01-25.
 */
class ShaderFont : Shader("font") {
  
  override fun bindAttributes() {
    bindAttribute(0, "position")
    bindAttribute(1, "textureCoords")
  }
  
  override fun getAllUniformLocations() {
    newLoc("translation", "colour")
  }
  
  fun loadColour(colour: Vector3f) = 
      loadVector("colour", colour)
  
  fun loadTranslation(translation: Vector2f) = 
      loadVector("translation", translation)
  
  //fun loadBounds(bounds: Vector4f) = loadVector("bounds", bounds)
}