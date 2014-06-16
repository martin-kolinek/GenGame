package org.kolinek.gengame.terragen

import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.game.AssetManagerProvider

trait TerrainAttacherComponent extends ErrorHelpers {
    self: SceneGraphProvider with LocalTerrainPiecesProvider with ErrorLoggingComponent with AssetManagerProvider =>

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