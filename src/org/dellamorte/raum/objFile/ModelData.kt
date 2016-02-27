package org.dellamorte.raum.objFile

/**
 * Created by Raum on 2016-01-31.
 */
data class ModelData(val verts: DoubleArray, 
                     val tCoords: DoubleArray, 
                     val norms: DoubleArray, 
                     val indices: IntArray, 
                     val farPoint: Double)