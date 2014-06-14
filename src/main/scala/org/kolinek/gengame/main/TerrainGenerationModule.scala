package org.kolinek.gengame.main

import org.kolinek.gengame.terragen.TerrainAttacherComponent
import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.terragen.DefaultTerrainPiecesProvider
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.terragen.VisitedChunksProvider
import org.kolinek.gengame.terragen.TerrainGeneratorProvider
import org.kolinek.gengame.terragen.DefaultVisitedChunksProvider
import org.kolinek.gengame.terragen.DefaultTerrainGeneratorProvider
import org.kolinek.gengame.terragen.marchingcubes.DefaultMarchingCubesComputerProvider
import org.kolinek.gengame.terragen.mesh.DefaultMeshProcessorProvider
import org.kolinek.gengame.terragen.DefaultTerragenDefinitionProvider
import org.kolinek.gengame.game.AssetManagerProvider
import org.kolinek.gengame.terragen.VisitedChunksAroundOrigin

trait TerrainGenerationModule
        extends TerrainAttacherComponent
        with DefaultTerrainPiecesProvider
        //with DefaultVisitedChunksProvider
        with VisitedChunksAroundOrigin
        with DefaultTerrainGeneratorProvider
        with DefaultMarchingCubesComputerProvider
        with DefaultMeshProcessorProvider
        with DefaultTerragenDefinitionProvider {
    self: CameraPositionComponent with SceneGraphProvider with ErrorLoggingComponent with AssetManagerProvider =>
}