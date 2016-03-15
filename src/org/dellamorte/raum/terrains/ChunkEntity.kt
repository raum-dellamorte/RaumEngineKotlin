package org.dellamorte.raum.terrains

import org.dellamorte.raum.engine.GameMgr
import org.dellamorte.raum.entities.Entity
import org.dellamorte.raum.entities.EntityGen
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Vector3f
import java.util.*

/**
 * Created by Raum on 2016-03-14.
 */
class ChunkEntity(val parent: Terrain, val id: Int) {
  val entities = ArrayList<Entity>()
  val drawEntities = ArrayList<Entity>()
  val col: Int = id % parent.chunkGridSize
  val row: Int = id / parent.chunkGridSize
  
  /**
  * Ideas on how to proceed:
   * In Minecraft, you'll see distant things show up after near things as they are loaded
   * I think that indicates the use of threads adding things to the list of things to be drawn
   * Using threads to manage visibility should allow for things in the distance to be deferred until they are processed
   * Right now I'm processing everything every frame
   * I need a couple of threads to work on what entities are in FOV of the camera
   * Threads should add entities to a thread safe buffer
   * One buffer for in view and one buffer for no longer in view
   * In view buffer will be added to the list sent to the draw call in the main thread each frame
   * List of visible objects will be processed by a thread to determine if they are still in view
   * What is no longer visible will be removed from the list sent to the draw call in the main thread each frame
   * With these operations being handled by a thread safe buffer, entities should pop up in the distance as they are added to the scene
   *  while not interrupting draw calls to be added
   * ChunkEntity objects should only have their contents tested for visibility if the chunk itself is visible
   * Chunk may be visible if the chunk is within 90 degrees of the camera vector
   * I think a test for visibility should sort entities/chunks by the dot product of vector to the camera and the camera's vector to the center of the scene
   * That should give a value closer to 1 for things near the center of the FOV and closer to 0 for things on the edge of the FOV
   * I'm not sure if things beyond 90 degrees give a negative value or null, but whichever it is, they won't be sent to the draw call
   * Distant things need to be rendered first, so entities must be prepended to the list sent to the draw call
   * They also need to be sorted by farthest from the camera first for correct transparency
   * Chunks and entities too far from the camera also need not be rendered
   * I think I have what I need to determine the vector values and lengths I need to do all this in the code I've already written
   * It's just a matter of figuring out what I need where
  **/
  
  fun genEntities(entityGen: EntityGen) {
    if (!hasAboveWater()) return
    GameMgr.rand.nextInt(entityGen.occurrence).times {
      val entity = Entity(
          entityGen.model,
          if (entityGen.maxIndex > 1) GameMgr.rand.nextInt(entityGen.maxIndex) else 0, 
          if (entityGen.aboveWater) randomTerrainVectorAboveWater() else randomTerrainVectorBelowWater(), 
          0.0, GameMgr.rand.nextFloat() * 360.0, 0.0,
          entityGen.maxScale - (GameMgr.rand.nextDouble() * entityGen.scaleVariance))
      entities.add(entity)
    }
  }
  
  private fun sortEntities() {
    drawEntities.clear()
    entities.size.times(1) {
      val item: Entity = entities[it]
      if (GameMgr.camera.angleToEntity(item) > 0.1) {
        drawEntities.add(item)
      }
    }
    drawEntities.size.times {
      val item: Entity = drawEntities[it]
      if (item.distance > drawEntities[it - 1].distance) {
        sortUpHighToLow(drawEntities, it)
      }
    }
  }
  
  private fun sortUpHighToLow(list: ArrayList<Entity>, i: Int) {
    val item: Entity = list[i]
    var attemptPos: Int = i - 1
    while (attemptPos != 0 && list[attemptPos - 1].distance < item.distance) {
      attemptPos--
    }
    list.remove(item)
    list.add(attemptPos, item)
  }
  
  fun randomTerrainVectorAboveWater(): Vector3f {
    val x = (GameMgr.rand.nextFloat() * 800.0) - 400.0 // FixMe: x and z need to be inside chunk
    val z = GameMgr.rand.nextFloat() * 600.0
    val y = parent.getHeightOfTerrain(x, z)
    if (y < parent.water.h) {
      return randomTerrainVectorAboveWater()
    }
    return Vector3f(x, y, z)
  }
  
  fun randomTerrainVectorBelowWater(): Vector3f {
    val x = (GameMgr.rand.nextFloat() * 800.0) - 400.0 // FixMe: x and z need to be inside chunk
    val z = GameMgr.rand.nextFloat() * 600.0
    val y = parent.getHeightOfTerrain(x, z)
    if (y > parent.water.h - 1.0) {
      return randomTerrainVectorBelowWater()
    }
    return Vector3f(x, y, z)
  }
  
  fun hasAboveWater(): Boolean {
    return true
  }
  
  fun hasBelowWater(): Boolean {
    return true
  }
}