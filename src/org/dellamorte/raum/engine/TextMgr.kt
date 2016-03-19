package org.dellamorte.raum.engine

import org.dellamorte.raum.loaders.LoaderModel
import org.dellamorte.raum.render.RenderFont
import org.dellamorte.raum.text.FontType
import org.dellamorte.raum.text.GuiText
import org.dellamorte.raum.text.TextMeshData
import org.dellamorte.raum.vector.Vector2f
import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class TextMgr {
  companion object {
    val activeText = HashMap<FontType, ArrayList<String>>()
    val texts = HashMap<String, GuiText>()
    val fonts = HashMap<String, FontType>()
    val rend = RenderFont()
    val ldr: LoaderModel = GameMgr.loader
    
    fun render() = rend.render()
  
    fun addFont(fname:String) {
      println("Adding Font: $fname")
      fonts[fname] = FontType(fname)
    }
  
    fun addFonts(vararg fnames: String) {
      for (fname in fnames) addFont(fname)
    }
    
    fun newText(label: String, text: String, fontName: String,
                fontSize: Double, x: Double, y: Double,
                lineMaxSize: Double, isCentered: Boolean, enable: Boolean = true) {
      val font = fonts[fontName] ?: return
      texts[label] = GuiText(label, text, font, fontSize, Vector2f(x, y), lineMaxSize, isCentered)
      if (enable) enableLabel(label)
    }
    
    fun enableLabel(label: String) {
      val text = texts[label] ?: return
      val font = text.font
      text.load()
      val textBatch = activeText[font] ?: ArrayList<String>()
      if (activeText[font] == null) { activeText[font] = textBatch }
      if (!textBatch.contains(label)) textBatch.add(text.label)
    }
    
    fun disableLabel(label: String) {
      val text = texts[label] ?: return
      val textBatch = activeText[text.font] ?: return
      textBatch.remove(label)
      if (textBatch.isEmpty()) activeText.remove(text.font)
    }
    
    fun update(label: String, newText: String) {
      val text: GuiText = texts[label] ?: return
      text.update(newText)
    }
    
    fun cleanUp() = rend.cleanUp()
  }
}