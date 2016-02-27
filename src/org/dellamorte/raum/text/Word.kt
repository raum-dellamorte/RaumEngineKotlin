package org.dellamorte.raum.text

import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class Word(val fontSize: Double) {
    val characters = ArrayList<Character>()
    var width = 0.0
    fun addCharacter(character: Character) {
        characters.add(character)
        width += character.xAdvance * fontSize
    }
}