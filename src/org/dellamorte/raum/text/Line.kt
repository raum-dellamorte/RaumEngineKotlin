package org.dellamorte.raum.text

import java.util.*

/**
 * Created by Raum on 2016-01-21.
 */
class Line(val spaceWidth: Double, val fontSize: Double, val maxLength: Double) {
    val words = ArrayList<Word>()
    var lineLength = 0.0
    val spaceSize = spaceWidth * fontSize

    fun attemptToAddWord(word: Word): Boolean {
        var plusLength = word.width
        if (!words.isEmpty()) plusLength += spaceSize
        if (lineLength + plusLength <= maxLength) {
            words.add(word)
            lineLength += plusLength
            return true
        } else {
            return false
        }
    }
}