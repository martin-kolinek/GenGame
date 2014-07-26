package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.terragen.marchingcubes.DefaultMarchingCubesComputerProvider
import org.kolinek.gengame.terragen.mesh.DefaultMeshProcessorProvider
import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TerrainGeneratorTest extends FunSuite {
    class TestComp
        extends DefaultTerrainGeneratorProvider
        with DefaultMarchingCubesComputerProvider
        with DefaultMeshProcessorProvider
        with DefaultTerragenDefinitionProvider

    test("TerrainGenerator works") {
        val comp = new TestComp
        val savedChunks = comp.terrainGenerator.generateChunks(Observable.items(Chunk.zero, Chunk(1.chunk, 1.chunk, 2.chunk))).toBlocking.toList
        assert(savedChunks.size > 0)
    }
}