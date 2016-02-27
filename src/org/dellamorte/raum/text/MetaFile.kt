package org.dellamorte.raum.text

import org.dellamorte.raum.engine.DisplayMgr
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class MetaFile(file:File) {
  private var aspectRatio:Double = 0.toDouble()
  private var verticalPerPixelSize:Double = 0.toDouble()
  private var horizontalPerPixelSize:Double = 0.toDouble()
  var spaceWidth:Double = 0.toDouble()
  lateinit private var padding:IntArray
  private var paddingWidth:Int = 0
  private var paddingHeight:Int = 0
  private val metaData = HashMap<Int, Character>()
  lateinit private var reader:BufferedReader
  private val values = HashMap<String, String>()
  
  init{
    this.aspectRatio = DisplayMgr.width.toDouble() / DisplayMgr.height.toDouble()
    openFile(file)
    loadPaddingData()
    loadLineSizes()
    val imageWidth = getValueOfVariable("scaleW")
    loadCharacterData(imageWidth)
    close()
  }
  
  operator fun get(ascii: Int): Character? { return metaData[ascii] }
  
  /**
   * Read in the next line and store the variable values.
   *
   * @return {@code true} if the end of the file hasn't been reached.
   */
  private fun processNextLine():Boolean {
    values.clear()
    var line:String? = null
    try {
      line = reader.readLine()
    } catch (e1:IOException) {}
    if (line == null) { return false }
    for (part in line.split((SPLITTER).toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
      val valuePairs = part.split(("=").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
      if (valuePairs.size == 2) {
        values.put(valuePairs[0], valuePairs[1])
      }
    }
    return true
  }
  
  /**
   * Gets the {@code int} value of the variable with a certain name on the
   * current line.
   *
   * @param variable
   * - the name of the variable.
   * @return The value of the variable.
   */
  private fun getValueOfVariable(variable:String):Int {
    val value = values[variable] ?: return 0
    return value.toInt()
  }
  
  /**
   * Gets the array of ints associated with a variable on the current line.
   *
   * @param variable
   * - the name of the variable.
   * @return The int array of values associated with the variable.
   */
  private fun getValuesOfVariable(variable:String):IntArray {
    val numbers = values[variable]!!.split((NUMBER_SEPARATOR).toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    val actualValues = IntArray(numbers.size)
    for (i in actualValues.indices) {
      actualValues[i] = Integer.parseInt(numbers[i])
    }
    return actualValues
  }
  
  /**
   * Closes the font file after finishing reading.
   */
  private fun close() {
    try {
      reader.close()
    } catch (e:IOException) {
      e.printStackTrace()
    }
  }
  
  /**
   * Opens the font file, ready for reading.
   *
   * @param file
   * - the font file.
   */
  private fun openFile(file:File) {
    try {
      reader = BufferedReader(FileReader(file))
    } catch (e:Exception) {
      e.printStackTrace()
      System.err.println("Couldn't read font meta file!")
    }
  }
  
  /**
   * Loads the data about how much padding is used around each character in
   * the texture atlas.
   */
  private fun loadPaddingData() {
    processNextLine()
    padding = getValuesOfVariable("padding")
    paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT]
    paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM]
  }
  
  /**
   * Loads information about the line height for this font in pixels, and uses
   * this as a way to find the conversion rate between pixels in the texture
   * atlas and screen-space.
   */
  private fun loadLineSizes() {
    processNextLine()
    val lineHeightPixels = getValueOfVariable("lineHeight") - paddingHeight
    verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT / lineHeightPixels.toDouble()
    horizontalPerPixelSize = verticalPerPixelSize / aspectRatio
  }
  
  /**
   * Loads in data about each character and stores the data in the
   * {@link Character} class.
   *
   * @param imageWidth
   * - the width of the texture atlas in pixels.
   */
  private fun loadCharacterData(imageWidth:Int) {
    processNextLine()
    processNextLine()
    while (processNextLine())
    {
      val c = loadCharacter(imageWidth)
      if (c != null)
      {
        metaData.put(c.id, c)
      }
    }
  }
  
  /**
   * Loads all the data about one character in the texture atlas and converts
   * it all from 'pixels' to 'screen-space' before storing. The effects of
   * padding are also removed from the data.
   *
   * @param imageSize
   * - the size of the texture atlas in pixels.
   * @return The data about the character.
   */
  private fun loadCharacter(imageSize:Int):Character? {
    val id = getValueOfVariable("id")
    if (id == TextMeshCreator.SPACE_ASCII)
    {
      this.spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize
      return null
    }
    val xTex = (getValueOfVariable("x").toDouble() + (padding[PAD_LEFT] - DESIRED_PADDING)) / imageSize
    val yTex = (getValueOfVariable("y").toDouble() + (padding[PAD_TOP] - DESIRED_PADDING)) / imageSize
    val width = getValueOfVariable("width") - (paddingWidth - (2 * DESIRED_PADDING))
    val height = getValueOfVariable("height") - ((paddingHeight) - (2 * DESIRED_PADDING))
    val quadWidth = width * horizontalPerPixelSize
    val quadHeight = height * verticalPerPixelSize
    val xTexSize = width.toDouble() / imageSize
    val yTexSize = height.toDouble() / imageSize
    val xOff = (getValueOfVariable("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize
    val yOff = (getValueOfVariable("yoffset") + (padding[PAD_TOP] - DESIRED_PADDING)) * verticalPerPixelSize
    val xAdvance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize
    return Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance)
  }
  companion object {
    private val PAD_TOP = 0
    private val PAD_LEFT = 1
    private val PAD_BOTTOM = 2
    private val PAD_RIGHT = 3
    private val DESIRED_PADDING = 8
    private val SPLITTER = " "
    private val NUMBER_SEPARATOR = ","
  }
}