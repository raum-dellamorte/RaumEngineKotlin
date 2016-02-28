package org.dellamorte.raum.loaders

import org.lwjgl.opengl.*
import java.nio.FloatBuffer
import java.util.*

/**
 * Created by Raum on 2016-02-28.
 */
class LoaderVBO {
  companion object {
    val vbos = ArrayList<Int>()
    
    fun add(n: Int) { vbos.add(n) }
    
    operator fun get(i: Int): Int = vbos[i]
    
    operator fun set(i: Int, n: Int) { vbos[i] = n }
    
    fun createEmptyVBO(count: Int): Int {
      val vbo = GL15.glGenBuffers()
      vbos.add(vbo)
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (count * 4).toLong(), GL15.GL_STREAM_DRAW)
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
      return vbo
    }
  
    fun addInstancedAttribute(vao: Int, vbo: Int, attrib: Int, dataSize: Int, stride: Int, offset: Int) {
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL30.glBindVertexArray(vao)
      GL20.glVertexAttribPointer(attrib, dataSize, GL11.GL_FLOAT, false, stride * 4, (offset * 4).toLong())
      //GL33.glVertexAttribDivisor(attrib, 1)
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
      GL30.glBindVertexArray(0)
    }
    
    fun updateVBO(vbo: Int, data: FloatArray, buffer: FloatBuffer) {
      buffer.clear()
      buffer.put(data)
      buffer.flip()
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4.toLong(), GL15.GL_STREAM_DRAW)
      GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0.toLong(), buffer)
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }
  }
}