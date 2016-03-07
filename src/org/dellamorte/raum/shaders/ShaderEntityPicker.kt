package org.dellamorte.raum.shaders

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector4f

/**
 * Created by Raum on 2016-03-02.
 */
class ShaderEntityPicker : Shader("entityPicker") {
  override fun bindAttributes() {
    bindAttribute(0, "position")
  }
  
  override fun getAllUniformLocations() {
    newLoc(
        "transformationMatrix",
        "projectionMatrix",
        "viewMatrix",
        "entityColour"
    )
  }
  
  fun loadUniformVars() {
    loadViewMatrix()
  }
  
  fun loadEntityColour(colour: Vector4f) =
      loadVector("entityColour", colour)
  
  fun loadTransformationMatrix(matrix: Matrix4f) =
      loadMatrix("transformationMatrix", matrix)
  
  fun loadViewMatrix() =
      loadMatrix("viewMatrix", GameMgr.camera.viewMatrix)
  
  fun loadProjectionMatrix(matrix: Matrix4f) =
      loadMatrix("projectionMatrix", matrix)
}