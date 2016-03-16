package org.dellamorte.raum.terrains

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.models.ModelRaw
import org.dellamorte.raum.textures.TexturePackTerrain
import org.dellamorte.raum.textures.TextureTerrain
import org.dellamorte.raum.tools.DoubleArrays
import org.dellamorte.raum.tools.Maths
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Matrix4f
import org.dellamorte.raum.vector.Vector3f
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Created by Raum on 2016-01-26.
 */
class Terrain(gridX: Int, gridZ: Int,
              val textures: TexturePackTerrain,
              val blendMap: TextureTerrain,
              heightMap: String) {
  var size = 1000
  val maxHeight = 40.0
  val maxPixelColour = (256 * 256 * 256).toDouble()
  val x: Double = (gridX * size).toDouble()
  val z: Double = (gridZ * size).toDouble()
  val model: ModelRaw
  var vertexCount: Int = 0
  var heights = DoubleArrays(0, 0)
  val point = TerrainPoint(this)
  var withWater = true
  val water = Water(this)
  
  val chunkGridSize = 3
  val chunks = Array(chunkGridSize * chunkGridSize) { ChunkEntity(this, it) }
  
  private val posVec = Vector3f()
  private val transMat = Matrix4f()
  val transformationMatrix: Matrix4f
      get() = Maths.createTransformationMatrix(transMat, posVec.apply { set(x, 0.0, z) }, 0.0, 0.0, 0.0, 1.0)
  
  init {
    model = genTerrain(heightMap)
    for (chunk in chunks) {
      chunk.genEntities()
    }
  }
  
  fun pointInBorder(worldX: Double, worldZ: Double): Boolean {
    point.setWorldPoints(worldX, worldZ)
    return point.pointInBorder()
  }
  
  fun getHeightOfTerrain(worldX: Double, worldZ: Double): Double {
    point.setWorldPoints(worldX, worldZ)
    return point.getHeightOfTerrain()
  }
  
  private fun genTerrain(heightMap: String): ModelRaw {
    val loader = GameMgr.loader
    val image = getHeightMap(heightMap)
    vertexCount = image.height
    heights = DoubleArrays(vertexCount, vertexCount)
    var count = vertexCount * vertexCount
    val verts = DoubleArray(count * 3)
    val norms = DoubleArray(count * 3)
    val coords = DoubleArray(count * 2)
    val indices = IntArray((6 * (vertexCount - 1)) * (vertexCount - 1))
    var vertexPtr = 0
    vertexCount.times { i: Int ->
      vertexCount.times { j: Int ->
        verts[vertexPtr * 3] =
          ((j.toDouble() / (vertexCount - 1).toDouble()) * size.toDouble())
        val height = getHeight(j, i, image)
        heights[j][i] = height
        verts[(vertexPtr * 3) + 1] = height
        verts[(vertexPtr * 3) + 2] =
          ((i.toDouble() / (vertexCount - 1).toDouble()) * size.toDouble())
        val normal = calcNormal(j, i, image)
        norms[vertexPtr * 3] = normal.x
        norms[(vertexPtr * 3) + 1] = normal.y
        norms[(vertexPtr * 3) + 2] = normal.z
        coords[vertexPtr * 2] =
          (j.toDouble() / (vertexCount - 1).toDouble())
        coords[(vertexPtr * 2) + 1] =
          (i.toDouble() / (vertexCount - 1).toDouble())
        vertexPtr += 1
      }
    }
    var ptr = 0 
    (vertexCount - 1).times { gz: Int ->
      (vertexCount - 1).times { gx: Int ->
        val topLeft = (gz * vertexCount) + gx
        val topRight = topLeft + 1
        val bottomLeft = ((gz + 1) * vertexCount) + gx
        val bottomRight = bottomLeft + 1
        indices[ptr] = topLeft; ptr += 1
        indices[ptr] = bottomLeft; ptr += 1
        indices[ptr] = topRight; ptr += 1
        indices[ptr] = topRight; ptr += 1
        indices[ptr] = bottomLeft; ptr += 1
        indices[ptr] = bottomRight; ptr += 1
      }
    }
    return loader.loadToVAO(verts, coords, norms, indices)
  }
  
  private fun calcNormal(x: Int, z: Int, image: BufferedImage): Vector3f {
    val out = Vector3f(
      getHeight(x - 1, z, image) - getHeight(x + 1, z, image), 
      2.0,
      getHeight(x, z - 1, image) - getHeight(x, z + 1, image))
    out.normalize()
    return out
  }
  
  private fun getHeight(x: Int, z: Int, image: BufferedImage): Double {
    if (((x < 0) or (x >= image.height)) or ((z < 0) or (z >= image.height)))
        return 0.0
    val mp2 = maxPixelColour / 2.0
    val rgbValue = image.getRGB(x, z)
    if ((x == 1) and (z == 1)) println("rgbValue $rgbValue")
    return (((image.getRGB(x, z) + mp2 - 625033) / mp2) * maxHeight)
  }
  
  private fun getHeightMap(heightMap: String): BufferedImage {
    val hm = File("res/textures/$heightMap.png")
    return if (hm.exists()) {
      ImageIO.read(hm)
    } else {
      BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
    }
  }
}