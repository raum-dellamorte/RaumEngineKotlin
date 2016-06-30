package org.dellamorte.raum.entities

import org.dellamorte.raum.models.ModelTextured

/**
 * Created by Raum on 2016-03-14.
 */
data class EntityGen(val model: ModelTextured, val maxIndex: Int, val maxScale: Double, val scaleVariance: Double, val occurrence: Int, val aboveWater: Boolean, val collidable: Boolean)