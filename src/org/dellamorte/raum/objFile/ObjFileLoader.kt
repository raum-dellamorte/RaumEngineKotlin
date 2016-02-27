package org.dellamorte.raum.objFile

import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.Integer.parseInt
import java.util.*

/**
 * Created by Raum on 2016-01-31.
 */
class ObjFileLoader {
  companion object {
    val resLoc: String = "res/obj/"
    
    fun loadObj(file: String): ModelData? {
      val isr: FileReader
      val objFile = File("$resLoc$file.obj")
      if (!objFile.exists()) return null
      isr = FileReader(objFile)
      val reader = BufferedReader(isr)
      var line: String?
      val verts = ArrayList<Vertex>()
      val txtrs = ArrayList<Vector2f>()
      val norms = ArrayList<Vector3f>()
      val indcs = ArrayList<Int>()
      try {
        while (true) {
          line = reader.readLine()
          if (line != null) {
            if (line.startsWith("v ")) {
              val currentLine = line.split(" ")
              val vertex = Vector3f(
                  currentLine[1],
                  currentLine[2],
                  currentLine[3])
              val newVertex = Vertex(verts.size, vertex);
              verts.add(newVertex)
            } else if (line.startsWith("vt ")) {
              val currentLine = line.split(" ")
              val texture = Vector2f(
                  currentLine[1],
                  currentLine[2])
              txtrs.add(texture)
            } else if (line.startsWith("vn ")) {
              val currentLine = line.split(" ")
              val normal = Vector3f(
                  currentLine[1],
                  currentLine[2],
                  currentLine[3])
              norms.add(normal)
            } else if (line.startsWith("f ")) {
              break
            }
          }
        }
        while ((line != null) && line.startsWith("f ")) {
          val currentLine = line.split(" ")
          val vertex1 = currentLine[1].split("/")
          val vertex2 = currentLine[2].split("/")
          val vertex3 = currentLine[3].split("/")
          processVertex(vertex1, verts, indcs)
          processVertex(vertex2, verts, indcs)
          processVertex(vertex3, verts, indcs)
          line = reader.readLine()
        }
        reader.close()
      } catch(e: IOException) {
        e.printStackTrace()
        return null
      }
      removeUnusedVertices(verts)
      val vertsArray = DoubleArray(verts.size * 3)
      val txtrsArray = DoubleArray(verts.size * 2)
      val normsArray = DoubleArray(verts.size * 3)
      val furthest = convertDataToArrays(
          verts, txtrs, norms, 
          vertsArray, txtrsArray, normsArray)
      val indicesArray = convertIndicesListToArray(indcs)
      val data = ModelData(vertsArray, txtrsArray, normsArray, indicesArray, furthest)
      return data
    }

    fun processVertex(vertex: List<String>, 
                      vertices: ArrayList<Vertex>, 
                      indices: ArrayList<Int>) {
      val index: Int = parseInt(vertex[0]) - 1
      var currentVertex: Vertex = vertices[index]
      var textureIndex: Int = parseInt(vertex[1]) - 1
      var normalIndex = parseInt(vertex[2]) - 1
      if (!currentVertex.isSet()) {
        currentVertex.textureIndex = textureIndex
        currentVertex.normalIndex = normalIndex
        indices.add(index)
      } else {
        dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices)
      }
    }
    
    fun convertIndicesListToArray(indices: ArrayList<Int>): IntArray {
      val indicesArray = IntArray(indices.size, { i: Int ->
        indices[i]
      })
      return indicesArray
    }
    
    fun convertDataToArrays(vertices: ArrayList<Vertex>, 
                            textures: ArrayList<Vector2f>, 
                            normals: ArrayList<Vector3f>, 
                            verticesArray: DoubleArray, 
                            texturesArray: DoubleArray, 
                            normalsArray: DoubleArray): Double {
      var furthestPoint = 0.0
      vertices.size.times { i: Int ->
        val currentVertex = vertices[i]
        if (currentVertex.length > furthestPoint) {
          furthestPoint = currentVertex.length
        }
        val position = currentVertex.pos
        val textureCoord = textures.get(currentVertex.textureIndex)
        val normalVector = normals.get(currentVertex.normalIndex)
        verticesArray[i * 3] = position.x
        verticesArray[(i * 3) + 1] = position.y
        verticesArray[(i * 3) + 2] = position.z
        texturesArray[i * 2] = textureCoord.x
        texturesArray[(i * 2) + 1] = 1 - textureCoord.y
        normalsArray[i * 3] = normalVector.x
        normalsArray[(i * 3) + 1] = normalVector.y
        normalsArray[(i * 3) + 2] = normalVector.z
      }
      return furthestPoint
    }

    fun dealWithAlreadyProcessedVertex(previousVertex:Vertex, newTextureIndex: Int, newNormalIndex: Int, indices: ArrayList<Int>, vertices: ArrayList<Vertex>) {
      if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
        indices.add(previousVertex.index)
      } else {
        val anotherVertex = previousVertex.dupVertex
        if (anotherVertex != null) {
          dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices)
        } else {
          val duplicateVertex = Vertex(vertices.size, previousVertex.pos)
          duplicateVertex.textureIndex = newTextureIndex
          duplicateVertex.normalIndex = newNormalIndex
          previousVertex.dupVertex = duplicateVertex
          vertices.add(duplicateVertex)
          indices.add(duplicateVertex.index)
        }
      }
    }
    
    fun removeUnusedVertices(vertices: ArrayList<Vertex>) {
      for (vertex in vertices) {
        if (!vertex.isSet()) {
          vertex.textureIndex = 0
          vertex.normalIndex = 0
        }
      }
    }
  }
}