package org.dellamorte.raum.text

/**
 * Created by Raum on 2016-01-21.
 */
class Character(val id: Int,
                val xTexCoord: Double,
                val yTexCoord: Double,
                xTexSize: Double,
                yTexSize: Double,
                val xOffset: Double,
                val yOffset: Double,
                val sizeX: Double,
                val sizeY: Double,
                val xAdvance: Double) {
    val xMaxTexCoord: Double = xTexSize + xTexCoord
    val yMaxTexCoord: Double = yTexSize + yTexCoord
}