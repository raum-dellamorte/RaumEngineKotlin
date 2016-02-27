package org.dellamorte.raum.engine

import org.dellamorte.raum.loaders.LoaderModel
import org.dellamorte.raum.render.RenderFont
import org.dellamorte.raum.text.FontType
import org.dellamorte.raum.text.GuiText
import org.dellamorte.raum.text.TextMeshData
import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class TextMgr {
    companion object {
        val texts = HashMap<FontType, ArrayList<GuiText>>()
        val rend = RenderFont()
        val ldr: LoaderModel = GameMgr.loader

        fun render() = rend.render(texts)

        fun loadText(text: GuiText) {
            val font = text.font
            val data: TextMeshData = font.loadText(text)
            val vao = ldr.loadToVAO(data.vertexPositions, data.textureCoords)
            text.setMeshInfo(vao, data.vertexCount)
            val textBatch = texts[font] ?: ArrayList<GuiText>()
            if (texts[font] == null) { texts[font] = textBatch }
            textBatch.add(text)
        }

        fun removeText(text: GuiText) {
            val textBatch = texts[text.font]
            if (textBatch != null) {
                textBatch.remove(text)
                if (textBatch.isEmpty()) texts.remove(text.font)
            }
        }

        fun cleanUp() = rend.cleanUp()
    }
}