package org.dellamorte.raum.tools

/**
 * Created by Raum on 2016-01-27.
 */
class DoubleArrays(szh: Int, szw: Int) {
  val array = Array<DoubleArray>(szh, { i: Int -> DoubleArray(szw) })
  
  operator fun get(i: Int) = array[i]
  
  val size: Int
    get() = array.size
}