package org.kolinek.gengame.terragen

import org.kolinek.gengame.game.SceneGraphProvider
import scala.collection.mutable.HashMap
import org.kolinek.gengame.geometry._
import com.jme3.scene.Spatial
import com.jme3.scene.debug.WireBox
import com.jme3.scene.Geometry
import spire.syntax.all._
import org.kolinek.gengame.game.AssetManagerProvider
import com.jme3.material.Material
import com.jme3.math.ColorRGBA

trait CurrentTerrainChunksVisualizer {
    self: CurrentTerrainChunks with SceneGraphProvider with AssetManagerProvider =>

    private val loadedWireframes = new HashMap[Chunk, Spatial]

    for {
        terrainChunkAction <- terrainChunkActions
        sceneGraph <- sceneGraphRoot
        assetMgr <- assetManager
    } {
        terrainChunkAction match {
            case TerrainChunkLoad(ch) => {
                val geom = new Geometry(s"wirebox $ch", new WireBox(Chunk.chunkSize.upper.toFloat, Chunk.chunkSize.upper.toFloat, Chunk.chunkSize.upper.toFloat))
                val mat = new Material(assetMgr, "Common/MatDefs/Misc/Unshaded.j3md")
                mat.getAdditionalRenderState().setWireframe(true)
                mat.setColor("Color", ColorRGBA.Cyan)
                geom.setMaterial(mat)
                geom.setLocalTranslation(ch.lower.lower.toJmeVector)
                sceneGraph.attachChild(geom)
                loadedWireframes(ch) = geom
            }
            case TerrainChunkUnload(ch) => {
                loadedWireframes.get(ch).foreach(sceneGraph.detachChild)
            }
        }
    }
}