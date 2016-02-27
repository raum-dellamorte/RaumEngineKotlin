package org.dellamorte.raum.engine

import org.dellamorte.raum.entities.Particle
import org.dellamorte.raum.render.RenderParticle
import org.dellamorte.raum.textures.TextureParticle
import org.dellamorte.raum.tools.times
import java.util.*

/**
 * Created by Raum on 2016-02-24.
 */
class ParticleMgr {
  companion object {
    val particles = HashMap<TextureParticle, ArrayList<Particle>>()
    val rend = RenderParticle()
    
    fun render() {
      rend.render(particles)
    }
    
    fun update() {
      val mapParts = particles.entries.iterator()
      while (mapParts.hasNext()) {
        val partsList = mapParts.next().value
        val parts = partsList.iterator()
        while (parts.hasNext()) {
          val p = parts.next()
          if (p.update()) {
            parts.remove()
            if (partsList.isEmpty()) { mapParts.remove() }
          }
        }
        sortHighToLow(partsList)
      }
      
    }
    
    fun add(p: Particle) {
      val list = particles[p.texture] ?: ArrayList<Particle>()
      particles[p.texture] ?: particles.put(p.texture, list)
      list.add(p)
    }
    
    fun cleanUp() = rend.cleanUp()
    
    private fun sortHighToLow(list: ArrayList<Particle>) {
      list.size.times(1) {
        val item: Particle = list[it]
        if (item.distance > list[it - 1].distance) {
          sortUpHighToLow(list, it)
        }
      }
    }
    
    private fun sortUpHighToLow(list: ArrayList<Particle>, i: Int) {
      val item: Particle = list[i]
      var attemptPos: Int = i - 1
      while (attemptPos != 0 && list[attemptPos - 1].distance < item.distance) {
        attemptPos--
      }
      list.remove(item)
      list.add(attemptPos, item)
    }
  }
}