package org.dellamorte.raum.text

import org.dellamorte.raum.engine.GameMgr
//import org.dellamorte.raum.loaders.LoaderModel
import java.io.File

/**
 * Created by Raum on 2016-01-21.
 */
class FontType(font: String) {
    val textureAtlas = GameMgr.loader.loadTexture(font)
    val loader = TextMeshCreator(File("res/fonts/$font.fnt"))

    fun loadText(text: GuiText) = loader.createTextMesh(text)
}