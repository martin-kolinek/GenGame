package org.kolinek.gengame.main

import org.kolinek.gengame.terragen.TerrainAttacherComponent
import org.kolinek.gengame.game.SceneGraphProvider
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.terragen.VisitedChunksProvider
import org.kolinek.gengame.terragen.TerrainGeneratorProvider
import org.kolinek.gengame.terragen.DefaultVisitedChunksProvider
import org.kolinek.gengame.terragen.DefaultTerrainGeneratorProvider
import org.kolinek.gengame.terragen.marchingcubes.DefaultMarchingCubesComputerProvider
import org.kolinek.gengame.terragen.mesh.DefaultMeshProcessorProvider
import org.kolinek.gengame.terragen.DefaultTerragenDefinitionProvider
import org.kolinek.gengame.game.AssetManagerProvider
import org.kolinek.gengame.db.InMemoryDatabaseProvider
import org.kolinek.gengame.terragen.DefaultCurrentTerrainChunks
import org.kolinek.gengame.terragen.db.DefaultSavedTerrainPieceCreatorProvider
import org.kolinek.gengame.db.BufferDatabaseActionExecutorProvider
import org.kolinek.gengame.terragen.LocalTerrainPiecesProvider
import org.kolinek.gengame.util.DefaultOnCloseProvider
import org.kolinek.gengame.db.schema.DefaultSchemaCreatorProvider
import org.kolinek.gengame.terragen.DbLocalTerrainPiecesProvider
import org.kolinek.gengame.terragen.db.DefaultTerrainLoaderProvider
import org.kolinek.gengame.terragen.db.DefaultTerrainRetriverProvider
import org.kolinek.gengame.terragen.db.DefaultTerrainPieceSaverProvider
import com.typesafe.scalalogging.slf4j.LazyLogging
import org.kolinek.gengame.db.SingleSessionDatabaseActionExecutorProvider

trait TerrainGenerationModule
        extends TerrainAttacherComponent
        with InMemoryDatabaseProvider
        with DefaultOnCloseProvider
        with DefaultSchemaCreatorProvider
        with SingleSessionDatabaseActionExecutorProvider
        with DefaultSavedTerrainPieceCreatorProvider
        with DefaultCurrentTerrainChunks
        with DefaultVisitedChunksProvider
        with DefaultTerrainGeneratorProvider
        with DefaultMarchingCubesComputerProvider
        with DefaultMeshProcessorProvider
        with DefaultTerragenDefinitionProvider
        with DbLocalTerrainPiecesProvider
        with DefaultTerrainLoaderProvider
        with DefaultTerrainRetriverProvider
        with DefaultTerrainPieceSaverProvider
        with LazyLogging {
    self: CameraPositionComponent with SceneGraphProvider with ErrorLoggingComponent with AssetManagerProvider =>
}