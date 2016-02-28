package org.dellamorte.raum.engine

import org.dellamorte.raum.effectbuffers.FBWater
import org.dellamorte.raum.entities.*
import org.dellamorte.raum.input.Keyboard
import org.dellamorte.raum.input.MousePicker
import org.dellamorte.raum.loaders.LoaderModel
import org.dellamorte.raum.models.ModelRaw
import org.dellamorte.raum.models.ModelTextured
import org.dellamorte.raum.objFile.ObjFileLoader
import org.dellamorte.raum.render.RenderGui
import org.dellamorte.raum.terrains.Terrain
import org.dellamorte.raum.terrains.Water
import org.dellamorte.raum.text.FontType
import org.dellamorte.raum.text.GuiText
import org.dellamorte.raum.textures.*
import org.dellamorte.raum.tools.TerrainList
import org.dellamorte.raum.tools.times
import org.dellamorte.raum.vector.Vector2f
import org.dellamorte.raum.vector.Vector3f
import org.dellamorte.raum.vector.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.util.*

/**
 * Created by Raum on 2016-01-19.
 */

class GameMgr {
  companion object {
    val kb: Keyboard get() = DisplayMgr.keyboard
    val fonts = HashMap<String, FontType>()
    val tmap = HashMap<String, Int>()
    val ents = ArrayList<Entity>()
    val guis = ArrayList<GuiObj>()
    val lightList = ArrayList<Light>()
    val loader = LoaderModel()
    val guiRend = RenderGui()
    val world = TerrainList()
    val rand = Random()
    var drawWater = true
    var fbWater = FBWater()
    val particleSystems = ArrayList<ParticleSystem>()
    val clipPlanes = Array(3, { Vector4f() })
    var clipPlane = Vector4f(0.0, -1.0, 0.0, 10000.0)
    
    var gravity = -50.0
    
    lateinit var player: Player
    lateinit var camera: Camera
    lateinit var mousePicker: MousePicker
    
    
    init {
      clipPlanes[0] = Water.vecReflect()
      clipPlanes[1] = Water.vecRefract()
      clipPlanes[2] = Vector4f(0.0, -1.0, 0.0, 10000.0)
    }
    
    fun loadGame() {
      addFonts(
          "arial", "berlinSans", "calibri", "candara",
          "courier", "harrington", "pirate", "sans",
          "segoe", "segoeUI", "tahoma", "TimesNewRoman")
  
      addTextures(
          "HealthMeter", "HealthBarBG", "HealthBarFG", "cosmic",
          "mytexture", "grassy2", "mud", "path",
          "grassTexture", "fern", "stall", "playerTexture",
          "blendMap4", "pine", "waterDUDV", "normalMap")
  
      loadPlayer("person", "playerTexture", 50.0, 50.0)
  
      addLight(0.0, 1000.0, -7000.0, 1.0, 1.0, 1.0)
      addLight(
          185.0, 10.0, -293.0,
          2.0, 0.0, 0.0,
          1.0, 0.01, 0.002)
  
      guis.add(GuiObj(0.135, 0.08, 0.25).apply {
        loadTextures("HealthBarBG", "HealthBarFG", "HealthMeter")
        attachStatusBar(player, "Health", "HealthMeter")
      })
      getGuiText("Hello World", "candara", 2.5, 0.015, 0.02, 0.24, true)
      /*guis.add(GuiObj(0.80, 0.20, 0.25).apply { 
        loadTextures("reflectTexture")
      })
      guis.add(GuiObj(0.80, 0.80, 0.25).apply {
        loadTextures("refractTexture")
      })*/
  
      val terrainTexture = genTexturePackTerrain("grassy2", "mud", "mytexture", "path")
      val bmap = genTextureTerrain("blendMap4")
      addTerrain(0, 0, terrainTexture, bmap, "heightmap4")
      //addTerrain(-1, -1, terrainTexture, bmap, "heightmap")
  
      val grass = getModelTextured("grassModel", "grassTexture", true, true)
      val fern = getModelTextured("fern", "fern", true, true, 2)
      val tree = getModelTextured("pine", "pine")
  
      500.times {
        if ((it % 2) == 0) {
          addEntity(Entity(fern, rand.nextInt(4), randomTerrainVector(),
              0.0, rand.nextFloat() * 360.0, 0.0, 0.9))
        }
        if ((it % 3) == 0) {
          addEntity(Entity(tree, 0, randomTerrainVector(),
              0.0, rand.nextFloat() * 360.0, 0.0, 1.2))
        }
        if ((it % 5) == 0) {
          addEntity(Entity(grass, 0, randomTerrainVector(),
              0.0, rand.nextFloat() * 360.0, 0.0, 1.2))
        }
      }
  
      addEntity(Entity(
          getModelTextured("stall", "stall", 1, 10.0, 0.5), 0,
          Vector3f(0.0, 0.0, 0.0),
          0.0, 180.0, 0.0, 3.0))
      addEntity(Entity(
          getModelTextured("stall", "stall", 1, 10.0, 0.5), 0,
          Vector3f(100.0, 0.0, 0.0),
          0.0, 180.0, 0.0, 3.0))
      addEntity(Entity(
          getModelTextured("stall", "stall", 1, 10.0, 0.5), 0,
          Vector3f(0.0, 0.0, 100.0),
          0.0, 180.0, 0.0, 3.0))
      
      particleSystems.add(ParticleSystem(TextureParticle("cosmic", 4, false), 40.0, 10.0, 0.2, 1.0, 5.0).apply { 
        lifeError = 0.1
        speedError = 0.25
        scaleError = 0.8
        randomizeRotation()
      }) 
      
    }
    
    fun loadPlayer(model: String, texture: String, x: Double, z: Double) {
      player = Player(getModelTextured(model, texture), 0, Vector3f(x, 0.0, z), 0.0, 0.0, 0.0, 1.0)
      camera = Camera(player)
      mousePicker = MousePicker()
    }
    
    fun update() {
      player.move(world)
      camera.move()
      mousePicker.update()
      //if (kb.isKeyDown(GLFW.GLFW_KEY_P)) Particle(TextureParticle("cosmic",4), Vector3f(player.pos), Vector3f(0, 50, 0), 0.5, 4.0, -1.0, 4.0) // rot scale grav life
      for (ps in particleSystems) {
        ps.generateParticles(Vector3f(150,20,150))
      }
      ParticleMgr.update()
    }
    
    fun clipPlanePhase(phase: Int) {
      if ((phase < 0) or (phase >= clipPlanes.size)) return 
      clipPlane = clipPlanes[phase]
    }
    
    fun renderScene(withFBWater: Boolean = false) {
      update()
      if (withFBWater) {
        drawWater = false
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0)
        clipPlanePhase(0)
        fbWater.bindReflectFB()
        camera.reflection(Water.waterLevel)
        RenderMgr.renderScene()
        clipPlanePhase(1)
        fbWater.bindRefractFB()
        camera.restore()
        RenderMgr.renderScene()
        fbWater.unbind()
        GL11.glDisable(GL30.GL_CLIP_DISTANCE0)
        drawWater = true
      }
      clipPlanePhase(2)
      RenderMgr.renderScene()
      ParticleMgr.render()
    }
    
    fun cleanUp() {
      guiRend.cleanUp()
      fbWater.cleanUp()
      RenderMgr.cleanUp()
      ParticleMgr.cleanUp()
      loader.cleanUp()
    }
    
    fun renderGui() {
      for (gui in guis) { gui.update() }
      guiRend.render(guis)
      TextMgr.render()
    }
    
    fun addEntity(entity: Entity) = ents.add(entity)
    
    fun addTerrain(terrain: Terrain) = world.add(terrain)
  
    fun addTerrain(gridX: Int, gridZ: Int,
                   textures: TexturePackTerrain,
                   blendMap: TextureTerrain,
                   heightMap: String) {
      addTerrain(Terrain(gridX, gridZ,textures, blendMap, heightMap))
    }
    
    fun addLight(
        pos1:Double, pos2:Double, pos3:Double, 
        color1:Double, color2:Double, color3:Double, 
        atten1:Double, atten2:Double, atten3:Double) {
      lightList.add(Light(Vector3f(pos1, pos2, pos3),
          Vector3f(color1, color2, color3),
          Vector3f(atten1, atten2, atten3)))
    }
    
    fun addLight(pos1:Double, pos2:Double, pos3:Double, 
                 color1:Double, color2:Double, color3:Double) =
        addLight(pos1, pos2, pos3, color1, color2, color3, 1.0, 0.0, 0.0)
    
    fun addTexture(fname: String) { 
      println("Adding Texture: $fname")
      tmap[fname] = loader.loadTexture(fname)
    }
  
    fun addTexture(fname: String, id: Int) {
      println("Adding Texture: $fname")
      tmap[fname] = id
    }
    
    fun addTextures(vararg fnames: String) {
      for (fname in fnames) addTexture(fname)
    }
    
    fun addFont(fname:String) { 
      println("Adding Font: $fname")
      fonts[fname] = FontType(fname) 
    }
    
    fun addFonts(vararg fnames: String) { 
      for (fname in fnames) addFont(fname) 
    }
    
    fun getTexture(texture: String): Int {
      val out = tmap[texture]
      if (out != null) return out
      println("Failed to getTexture $texture")
      return -1
    }
    
    fun getModel(objName: String): ModelRaw {
      val data = ObjFileLoader.loadObj(objName)!!
      return loader.loadToVAO(data.verts, data.tCoords, data.norms, data.indices)
    }
    
    fun getModelTextured(objName:String, textureName:String, trans: Boolean, fakeLt: Boolean,
                         textureRows: Int, shineDamperVal:Double, reflectivityVal:Double): ModelTextured {
      val model: ModelRaw = getModel(objName)
      val tID: Int = getTexture(textureName)
      val texture = Texture(tID).apply {
        shineDamper = shineDamperVal
        reflectivity = reflectivityVal
        hasTransparency = trans
        useFakeLighting = fakeLt
        numOfRows = textureRows
      }
      return ModelTextured(model, texture)
    }
    
    fun getModelTextured(objName:String, textureName:String, textureRows: Int, shineDamper:Double, reflectivity:Double): ModelTextured {
      return getModelTextured(objName, textureName, false, false, textureRows, shineDamper, reflectivity)
    }
    
    fun getModelTextured(objName:String, textureName:String, trans: Boolean, fakeLt: Boolean, textureRows: Int): ModelTextured {
      return getModelTextured(objName, textureName, trans, fakeLt, textureRows, 1.0, 0.0)
    }
    
    fun getModelTextured(objName:String, textureName:String, trans: Boolean, fakeLt: Boolean): ModelTextured {
      return getModelTextured(objName, textureName, trans, fakeLt, 1, 1.0, 0.0)
    }
    
    fun getModelTextured(objName:String, textureName:String, textureRows: Int): ModelTextured {
      return getModelTextured(objName, textureName, false, false, textureRows, 1.0, 0.0)
    }
    
    fun getModelTextured(objName:String, textureName:String): ModelTextured =
        getModelTextured(objName, textureName, false, false, 1, 1.0, 0.0)
    
    fun genTextureTerrain(file: String): TextureTerrain = TextureTerrain(getTexture(file))
    
    fun genTexturePackTerrain(bg:String, r:String, g:String, b:String):TexturePackTerrain =
        TexturePackTerrain(genTextureTerrain(bg), genTextureTerrain(r),
                            genTextureTerrain(g), genTextureTerrain(b))
    
    fun getTextureGui(texture: String, a: Double, b: Double, c: Double, d: Double): TextureGui =
        TextureGui(texture, Vector2f(a, b), Vector2f(c, d))
    
    fun getGuiText(text:String, font:String, size:Double, x:Double, y:Double, w:Double, center: Boolean = false) =
        GuiText(text, fonts[font]!!, size, Vector2f(x, y), w, center)
    
    fun randomTerrainVector(): Vector3f {
      val x = (rand.nextFloat() * 800.0) - 400.0
      val z = rand.nextFloat() * -600.0
      val trrn: Terrain? = world.getTerrainAt(x, z)
      val y = if (trrn == null) 0.0 else trrn.getHeightOfTerrain(x, z)
      return Vector3f(x,y,z)
    }
  }
}