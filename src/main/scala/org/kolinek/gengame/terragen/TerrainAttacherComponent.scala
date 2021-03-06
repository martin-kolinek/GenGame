package org.kolinek.gengame.terragen

import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.game.AssetManagerProvider
import com.typesafe.scalalogging.slf4j.LazyLogging

trait TerrainAttacherComponent extends LazyLogging {
    self: SceneGraphProvider with LocalTerrainPiecesProvider =>

    for {
        tp <- localTerrainPieces
        root <- sceneGraphRoot
    } {
        tp match {
            case LoadTerrainPiece(savedMesh) => {
                savedMesh.attach(root)
            }
            case UnloadTerrainPiece(savedMesh) => {
                savedMesh.detach(root)
            }
        }
    }
}