package org.dellamorte.raum.tools

/**
 * Created by Raum on 2016-01-26.
 */
class FloatArrays(szh: Int, szw: Int) {
  val x = Array<FloatArray>(szh, { i: Int -> FloatArray(szw) })
}