package org.dellamorte.raum.text

import java.io.File
import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class TextMeshCreator(metaFile: File) {
  companion object {
    val SPACE_ASCII = 32
    val LINE_HEIGHT = 0.03
  }
  val lineHeight = 0.03
  val spaceASCII = 32
  
  val metaData = MetaFile(metaFile)
  
  fun createTextMesh(text: GuiText): TextMeshData {
    return createQuadVerts(text)
  }
  
  private fun createStructure(text: GuiText): ArrayList<Line> {
    val chars = text.text.toCharArray()
    val lines = ArrayList<Line>()
    var currentLine = Line(metaData.spaceWidth, text.fontSize, text.lineMaxSize)
    var currentWord = Word(text.fontSize)
    for (char in chars) {
      val ascii = char.toInt()
      if (ascii == spaceASCII) {
        if (!currentLine.attemptToAddWord(currentWord)) {
          lines.add(currentLine)
          currentLine = Line(metaData.spaceWidth, text.fontSize, text.lineMaxSize)
          currentLine.attemptToAddWord(currentWord)
        }
        currentWord = Word(text.fontSize)
        continue
      }
      val character: Character = metaData[ascii]!!
      currentWord.addCharacter(character)
    }
    if (!currentLine.attemptToAddWord(currentWord)) {
      lines.add(currentLine)
      currentLine = Line(metaData.spaceWidth, text.fontSize, text.lineMaxSize)
      currentLine.attemptToAddWord(currentWord)
    }
    lines.add(currentLine)
    return lines
  }
  
  private fun createQuadVerts(text: GuiText): TextMeshData {
    val lines: ArrayList<Line> = createStructure(text)
    text.numberOfLines = lines.size
    var cursorX = 0.0
    var cursorY = 0.0
    val verts = ArrayList<Double>()
    val tCoords = ArrayList<Double>()
    
    for (line in lines) {
      if (text.isCentered) { cursorX = (line.maxLength - line.lineLength) / 2.0 }
      for (word in line.words) {
        for (letter in word.characters) {
          addVertsForCharacter(cursorX, cursorY, letter, text.fontSize, verts)
          addTexCoords(tCoords, letter.xTexCoord, letter.yTexCoord, letter.xMaxTexCoord, letter.yMaxTexCoord)
          cursorX += letter.xAdvance * text.fontSize
        }
        cursorX += metaData.spaceWidth * text.fontSize
      }
      cursorX = 0.0
      cursorY += lineHeight * text.fontSize
    }
    return TextMeshData(verts.toDoubleArray(), tCoords.toDoubleArray())
  }
  
  fun addVertsForCharacter(curserX: Double, curserY: Double, character: Character, fontSize: Double, vertices: ArrayList<Double>) {
    val x = curserX + (character.xOffset * fontSize)
    val y = curserY + (character.yOffset * fontSize)
    val maxX = x + (character.sizeX * fontSize)
    val maxY = y + (character.sizeY * fontSize)
    val properX = (2.0f * x) - 1.0f
    val properY = (-2.0f * y) + 1.0f
    val properMaxX = (2.0f * maxX) - 1.0f
    val properMaxY = (-2.0f * maxY) + 1.0f
    addVerts(vertices, properX, properY, properMaxX, properMaxY)
  }
  
  private fun addVerts(vertices: ArrayList<Double>, x: Double, y: Double, maxX: Double, maxY: Double) {
    vertices.add(x)
    vertices.add(y)
    vertices.add(x)
    vertices.add(maxY)
    vertices.add(maxX)
    vertices.add(maxY)
    vertices.add(maxX)
    vertices.add(maxY)
    vertices.add(maxX)
    vertices.add(y)
    vertices.add(x)
    vertices.add(y)
  }
  
  fun addTexCoords(texCoords:ArrayList<Double>, x: Double, y: Double, maxX: Double, maxY: Double) {
    texCoords.add(x)
    texCoords.add(y)
    texCoords.add(x)
    texCoords.add(maxY)
    texCoords.add(maxX)
    texCoords.add(maxY)
    texCoords.add(maxX)
    texCoords.add(maxY)
    texCoords.add(maxX)
    texCoords.add(y)
    texCoords.add(x)
    texCoords.add(y)
  }
}