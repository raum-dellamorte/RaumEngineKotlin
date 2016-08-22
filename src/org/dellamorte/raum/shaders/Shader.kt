package org.dellamorte.raum.shaders

import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * Created by Raum on 2016-01-25.
 */
abstract class Shader {
  val matrixBuffer = BufferUtils.createFloatBuffer(16)
  val vertShaderFile: String
  val fragShaderFile: String
  var vertexShaderID: Int
  var fragmentShaderID: Int
  var programID: Int
  val locations = HashMap<String, Int>()
  
  constructor(shaderType: String, uniforms: ArrayList<String>? = null) {
    println("shaderType: $shaderType")
    vertShaderFile = shaderFile(shaderType, "Vert")
    fragShaderFile = shaderFile(shaderType, "Frag")
    vertexShaderID = loadShader(vertShaderFile, GL20.GL_VERTEX_SHADER)
    fragmentShaderID = loadShader(fragShaderFile, GL20.GL_FRAGMENT_SHADER)
    programID = GL20.glCreateProgram()
    GL20.glAttachShader(programID, vertexShaderID)
    GL20.glAttachShader(programID, fragmentShaderID)
    bindAttributes(null)
    GL20.glLinkProgram(programID)
    GL20.glValidateProgram(programID)
    getAllUniformLocations(null)
  }
  
  fun shaderFile(fname: String, type: String): String = "res/opengl/$fname$type.glsl"
  
  fun getLoc(key: String): Int = locations[key] ?: 0
  
  fun newLoc(vararg keys: String) {
    for (key in keys) {
      val loc = getUniformLocation(key)
      locations[key] = loc
      println("getLoc test: key $key : loc $loc : getLoc ${getLoc(key)}")
    }
  }
  
  fun newLoc(arrayLength: Int, vararg keys: String) {
    for (key in keys) {
      arrayLength.times {
        val keyi = "$key[$it]"
        val loc = getUniformLocation(keyi)
        locations[keyi] = loc
        println("getLoc test: key $keyi : loc $loc : getLoc ${getLoc(keyi)}")
      }
    }
  }
  
  fun setLoc(key: String, loc: Int): Unit { locations[key] = loc }
  
  fun getUniformLocation(uniformName:String): Int = 
    GL20.glGetUniformLocation(programID, uniformName)
  
  fun start() { GL20.glUseProgram(programID) }
  
  fun stop() { GL20.glUseProgram(0) }
  
  fun cleanUp() {
    stop()
    GL20.glDetachShader(programID, vertexShaderID)
    GL20.glDetachShader(programID, fragmentShaderID)
    GL20.glDeleteShader(vertexShaderID)
    GL20.glDeleteShader(fragmentShaderID)
    GL20.glDeleteProgram(programID)
  }
  
  fun bindAttribute(attribute: Int, variableName:String) =
      GL20.glBindAttribLocation(programID, attribute, variableName)
  
  fun loadInt(loc: String, value: Int) = 
      GL20.glUniform1i(getLoc(loc), value)
  
  fun loadFloat(loc: String, value: Int) =
      loadFloat(loc, value.toFloat())
  fun loadFloat(loc: String, value: Double) = 
      loadFloat(loc, value.toFloat())
  fun loadFloat(loc: String, value: Float) = 
      GL20.glUniform1f(getLoc(loc), value)
  
  fun loadVector(loc: String, vector: Vector4f) =
      GL20.glUniform4f(getLoc(loc), vector.xf, vector.yf, vector.zf, vector.wf)
  
  fun loadVector(loc: String, vector: Vector3f) =
      GL20.glUniform3f(getLoc(loc), vector.xf, vector.yf, vector.zf)
  
  fun loadVector(loc: String, vector: Vector2f) =
      GL20.glUniform2f(getLoc(loc), vector.xf, vector.yf)
  
  fun loadBoolean(loc: String, value: Boolean) =
      GL20.glUniform1f(getLoc(loc), if (value) 1.0f else 0.0f)
  
  fun loadMatrix(loc: String, matrix: Matrix4f) {
    matrix.store(matrixBuffer)
    matrixBuffer.flip()
    GL20.glUniformMatrix4fv(getLoc(loc), false, matrixBuffer)
  }
  
  private fun loadShader(file: String, type: Int): Int {
    val shaderSource = StringBuilder()
    try {
      val reader = BufferedReader(FileReader(file))
      while (true) {
        val line = reader.readLine() ?: break
        shaderSource.append(line).append("\n")
      }
      reader.close()
    } catch(e: IOException) {
      System.err.println("Could not read file!")
      e.printStackTrace()
      System.exit(-1)
    }
    //println(shaderSource)
    val shaderID = GL20.glCreateShader(type)
    GL20.glShaderSource(shaderID, shaderSource)
    GL20.glCompileShader(shaderID)
    if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
      //System.out.println(GL20.glGetShaderInfoLog(shaderID, 500))
      System.err.println("Could not compile shader at $file.")
      System.exit(-1)
    }
    return shaderID
  }
  
  abstract fun bindAttributes(attribs: ArrayList<String>? = null)
  
  abstract fun getAllUniformLocations(uniforms: ArrayList<String>? = null)
}