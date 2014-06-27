package org.kolinek.gengame.terragen.db

import com.jme3.scene.Mesh
import org.kolinek.gengame.geometry._
import com.jme3.scene.Node
import com.jme3.scene.Geometry
import com.jme3.asset.AssetManager
import com.jme3.export.binary.BinaryImporter
import com.jme3.material.Material
import com.jme3.material.RenderState.FaceCullMode
import rx.lang.scala.Observable
import org.kolinek.gengame.game.AssetManagerProvider

class SavedTerrainPieceCreator(assetManager: AssetManager) {
    val importer = new BinaryImporter

    def createTerrainPiece(data: Array[Byte], id: Long) = {
        val mesh = importer.load(data) match {
            case msh: Mesh => msh
            case _ => throw new AssertionError
        }
        val geom = new Geometry("terrainpiece", mesh)
        val material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        geom.setMaterial(material)
        new SavedTerrainPiece(geom, id)
    }
}

trait SavedTerrainPieceCreatorProvider {
    def savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator]
}

trait DefaultSavedTerrainPieceCreatorProvider extends SavedTerrainPieceCreatorProvider {
    self: AssetManagerProvider =>
    def savedTerrainPieceCreator = assetManager.map(new SavedTerrainPieceCreator(_))
}

class SavedTerrainPiece(geom: Geometry, id: Long) {

    def attach(root: Node) {
        root.attachChild(geom)
    }
    def detach(root: Node) {
        root.detachChild(geom)
    }
}

case class SavedChunk(chunk: Chunk, pieces: Observable[SavedTerrainPiece])