package org.dellamorte.raum.text

/**
 * Created by Raum on 2016-01-21.
 */
class TextMeshData(val vertexPositions: DoubleArray, val textureCoords: DoubleArray) {
    val vertexCount: Int
    init {
        vertexCount = vertexPositions.size / 2
    }
}