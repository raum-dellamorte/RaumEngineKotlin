package org.dellamorte.raum.tools

import org.dellamorte.raum.entities.Camera
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f

/**
 * Created by Raum on 2016-01-26.
 */
class Maths {
  companion object {
    fun barryCentric(p1: Vector3f, p2:Vector3f, p3:Vector3f, pos: Vector2f): Double {
      val det: Double = ((p2.z - p3.z) * (p1.x - p3.x)) + ((p3.x - p2.x) * (p1.z - p3.z))
      val l1: Double = (((p2.z - p3.z) * (pos.x - p3.x)) + ((p3.x - p2.x) * (pos.y - p3.z))) / det
      val l2: Double = (((p3.z - p1.z) * (pos.x - p3.x)) + ((p1.x - p3.x) * (pos.y - p3.z))) / det
      val l3: Double = (1.0 - l1) - l2
      return ((l1 * p1.y) + (l2 * p2.y)) + (l3 * p3.y)
    }
    
    fun createTransformationMatrix(translation: Vector3f, rx: Double, ry: Double, rz: Double, scale: Double): Matrix4f {
      val matrix = Matrix4f()
      matrix.setIdentity()
      Matrix4f.translate(translation, matrix, matrix)
      Matrix4f.rotate(Math.toRadians(rx), Vector3f(1,0,0), matrix, matrix)
      Matrix4f.rotate(Math.toRadians(ry), Vector3f(0,1,0), matrix, matrix)
      Matrix4f.rotate(Math.toRadians(rz), Vector3f(0,0,1), matrix, matrix)
      Matrix4f.scale(Vector3f(scale, scale, scale), matrix, matrix)
      return matrix
    }
    
    fun createTransformationMatrix(translation: Vector2f, scale: Vector2f, drawOrder: Int): Matrix4f {
      val matrix = Matrix4f()
      matrix.setIdentity()
      val depth = -(drawOrder.toDouble() * 0.00001)
      val trans = Vector3f(translation.x, translation.y, depth)
      Matrix4f.translate(trans, matrix, matrix)
      Matrix4f.scale(Vector3f(scale.x, scale.y, 1.0), matrix, matrix)
      return matrix
    }
  
    fun createTransformationMatrix(translation: Vector2f, scale: Vector2f): Matrix4f =
      createTransformationMatrix(translation, scale, 0)
  
    fun createTransformationMatrixGui(translation:Vector2f, scale:Vector2f):Matrix4f {
      val matrix = Matrix4f()
      matrix.setIdentity()
      val trans = Vector3f((translation.x * 2.0) - 1.0, (translation.y * -2.0) + 1.0, 0.0)
      Matrix4f.translate(trans, matrix, matrix)
      Matrix4f.scale(Vector3f(scale.x, scale.y, 1.0), matrix, matrix)
      return matrix
    }
  
    fun createViewMatrix(camera: Camera): Matrix4f {
      val viewMatrix = Matrix4f()
      viewMatrix.setIdentity()
      Matrix4f.rotate(Math.toRadians(camera.pitch), Vector3f(1,0,0), viewMatrix, viewMatrix)
      Matrix4f.rotate(Math.toRadians(camera.yaw), Vector3f(0,1,0), viewMatrix, viewMatrix)
      val camPos = camera.pos
      val negCam = Vector3f(-camPos.x,-camPos.y,-camPos.z)
      Matrix4f.translate(negCam, viewMatrix, viewMatrix)
      return viewMatrix
    }
  }
}