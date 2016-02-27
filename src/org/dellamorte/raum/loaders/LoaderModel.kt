package org.dellamorte.raum.loaders

import org.dellamorte.raum.models.ModelRaw
import org.dellamorte.raum.textures.Image
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.tools.toFloatArray
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import org.lwjgl.system.jemalloc.JEmalloc.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * Created by Raum on 2016-01-19.
 */
class LoaderModel {
  val vaos = ArrayList<Int>()
  val vbos = ArrayList<Int>()
  val textures = ArrayList<Int>()
  val skyOrder: ArrayList<String> = arrayListOf("right", "left", "top", "bottom", "back", "front")
  
  fun loadToVAO(positions: DoubleArray, textureCoords: DoubleArray, normals: DoubleArray, indices: IntArray): ModelRaw {
    val vaoID = createVAO()
    bindIndicesBuffer(indices)
    storeDataInAttributeList(0, 3, positions)
    storeDataInAttributeList(1, 2, textureCoords)
    storeDataInAttributeList(2, 3, normals)
    unbindVAO()
    return ModelRaw(vaoID, indices.size)
  }
  
  fun loadToVAO(positions: DoubleArray, textureCoords: DoubleArray): Int {
    val vaoID = createVAO()
    storeDataInAttributeList(0, 2, positions)
    storeDataInAttributeList(1, 2, textureCoords)
    unbindVAO()
    return vaoID
  }
  
  fun loadToVAO(positions: DoubleArray, dimensions: Int): ModelRaw {
    val vaoID = createVAO()
    storeDataInAttributeList(0, dimensions, positions)
    unbindVAO()
    return ModelRaw(vaoID, positions.size / dimensions)
  }
  
  fun loadToVAO(positions: DoubleArray): ModelRaw = loadToVAO(positions, 2)

  fun loadCubeMap(sky: String): Int {
    val texID = GL11.glGenTextures()
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID)
    val loc = "res/skybox/$sky/"
    skyOrder.size.times {
      val data = loadImage("$loc${skyOrder[it]}.png")
      GL11.glTexImage2D(
          GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + it, 0,
          GL11.GL_RGBA, data.width, data.height, 0,
          (if (data.components == 4) GL11.GL_RGBA else GL11.GL_RGB), 
          GL11.GL_UNSIGNED_BYTE, data.image)
    }
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
    textures.add(texID)
    return texID
  }

  fun loadTexture(fileName: String): Int {
    val texID = GL11.glGenTextures()
    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID)
    val data = loadImage("res/textures/$fileName.png")
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, data.width, data.height, 0,
            (if (data.components == 4) GL11.GL_RGBA else GL11.GL_RGB), GL11.GL_UNSIGNED_BYTE, data.image)
    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR)
    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0f)
    textures.add(texID)
    return texID
  }

  fun cleanUp() {
    for (vao in vaos) GL30.glDeleteVertexArrays(vao)
    for (vbo in vbos) GL15.glDeleteBuffers(vbo)
    for (tID in textures) GL11.glDeleteTextures(tID)
  }

  private fun createVAO(): Int {
    val vaoID = GL30.glGenVertexArrays()
    vaos.add(vaoID)
    GL30.glBindVertexArray(vaoID)
    return vaoID
  }

  private fun unbindVAO() {
    GL30.glBindVertexArray(0)
  }

  private fun bindIndicesBuffer(indices: IntArray) {
    val vboID = GL15.glGenBuffers()
    vbos.add(vboID)
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID)
    val buffer = storeDataInIntBuffer(indices)
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
  }

  private fun storeDataInAttributeList(attributeNumber: Int, coordSize: Int, data: DoubleArray) {
    val vboID = GL15.glGenBuffers()
    vbos.add(vboID)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
    val buffer = storeDataInFloatBuffer(data)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
    GL20.glVertexAttribPointer(attributeNumber, coordSize, GL11.GL_FLOAT, false, 0, 0)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
  }

  private fun storeDataInIntBuffer(data: IntArray): IntBuffer {
    val buffer: IntBuffer = BufferUtils.createIntBuffer(data.size)
    buffer.put(data)
    buffer.flip()
    return buffer
  }

  private fun storeDataInFloatBuffer(data: DoubleArray): FloatBuffer {
    val buffer: FloatBuffer = BufferUtils.createFloatBuffer(data.size)
    buffer.put(data.toFloatArray())
    buffer.flip()
    return buffer
  }

  private fun loadImage(path: String): Image = Image(fileToByteBuffer(path))

  private fun fileToByteBuffer(path: String): ByteBuffer {
    val buffer: ByteBuffer
    val file = File(path)
    if (file.canRead()) {
      val fis = FileInputStream(file)
      val fc: FileChannel = fis.channel
      buffer = je_malloc((fc.size() + 1).toLong())
      while (fc.read(buffer) != -1) {}
      fc.close()
      fis.close()
      buffer.flip()
    } else {
      buffer = je_malloc(0.toLong())
      buffer.flip()
      println("File at $path doesn't exist.")
    }
    return buffer
  }
}
