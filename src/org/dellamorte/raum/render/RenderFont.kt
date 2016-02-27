package org.dellamorte.raum.render

import org.dellamorte.raum.shaders.ShaderFont
import org.dellamorte.raum.text.FontType
import org.dellamorte.raum.text.GuiText
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class RenderFont(val shader: ShaderFont) {
  constructor() : this(ShaderFont()) {}
  
  fun render(texts: HashMap<FontType, ArrayList<GuiText>>) {
    prepare()
    for ((font: FontType, gtxts: ArrayList<GuiText>) in texts) { //texts.keySet.each do |font:FontType|
      GL13.glActiveTexture(GL13.GL_TEXTURE0)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.textureAtlas)
      for (text in gtxts) {
        renderText(text)
      }
    }
    endRendering()
  }
  
  fun cleanUp() = shader.cleanUp()
  
  fun prepare() {
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    GL11.glDisable(GL11.GL_DEPTH_TEST)
    shader.start()
  }
  
  fun renderText(text: GuiText) {
    GL30.glBindVertexArray(text.textMeshVao)
    GL20.glEnableVertexAttribArray(0)
    GL20.glEnableVertexAttribArray(1)
    shader.loadColour(text.colour)
    shader.loadTranslation(text.position)
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.vertexCount)
    GL20.glDisableVertexAttribArray(0)
    GL20.glDisableVertexAttribArray(1)
    GL30.glBindVertexArray(0)
  }
  
  fun endRendering() {
    shader.stop()
    GL11.glDisable(GL11.GL_BLEND)
    GL11.glEnable(GL11.GL_DEPTH_TEST)
  }
}