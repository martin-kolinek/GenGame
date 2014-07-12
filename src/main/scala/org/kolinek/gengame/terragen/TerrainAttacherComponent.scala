package org.kolinek.gengame.terragen

import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.game.AssetManagerProvider

trait TerrainAttacherComponent {
    self: SceneGraphProvider with LocalTerrainPiecesProvider with AssetManagerProvider =>

    for {
        tp <- localTerrainPieces
        root <- sceneGraphRoot
        am <- assetManager
    } {
        tp match {
            case LoadTerrainPiece(savedMesh) => savedMesh.attach(root)
            case UnloadTerrainPiece(savedMesh) => savedMesh.detach(root)
        }
    }
}