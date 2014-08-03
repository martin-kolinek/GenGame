package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.terragen.marchingcubes.DefaultMarchingCubesComputerProvider
import org.kolinek.gengame.terragen.mesh.DefaultMeshProcessorProvider
import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.kolinek.gengame.util.Timing

@RunWith(classOf[JUnitRunner])
class TerrainGeneratorTest extends FunSuite {
    class TestComp
        extends DefaultTerrainGeneratorProvider
        with DefaultMarchingCubesComputerProvider
        with DefaultMeshProcessorProvider
        with DefaultTerragenDefinitionProvider

    val chunks = for {
        x <- -1 to 1
        y <- -1 to 1
        z <- -1 to 1
    } yield Chunk(x.chunk, y.chunk, z.chunk)

    test("TerrainGenerator works") {
        val comp = new TestComp
        info(Timing.timed {
            val savedChunks = comp.terrainGenerator.generateChunks(Observable.from(chunks)).toBlocking.toList
            assert(savedChunks.size > 0)
        }.toString)
    }
}