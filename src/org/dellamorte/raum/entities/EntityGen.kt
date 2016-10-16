package org.dellamorte.raum.entities

import org.dellamorte.raum.models.ModelTextured

/**
 * Created by Raum on 2016-03-14.
 */
data class EntityGen(
    val model: ModelTextured, 
    val maxIndex: Int, val maxScale: Double, val scaleVariance: Double, val occurrence: Int, 
    val aboveWater: Boolean, val collidable: Boolean, 
    var c_height: Double = 0.0, var c_width: Double = 0.0, 
    var c_xOffset: Double = 0.0, var c_yOffset: Double = 0.0, var c_zOffset: Double = 0.0, 
    var c_xwOffset: Double = 0.0, var c_zwOffset: Double = 0.0)