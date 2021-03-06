package org.kolinek.gengame.terragen.mesh

import scala.annotation.tailrec
import org.kolinek.gengame.geometry._
import scala.collection.mutable.Queue
import scala.collection.mutable.HashSet
import spire.compat._
import spire.syntax.all._
import com.typesafe.scalalogging.slf4j.LazyLogging

class BasicMeshProcessor(val maxSize: ChunkUnit, maxDegrees: Double) extends MeshProcessor with LazyLogging {
    private val maxCosine = math.cos(math.toRadians(maxDegrees))
    private val maxPosSize = maxSize.upper.upper

    @tailrec
    private def generatePatches(ta: TriangleArea, acc: List[TrianglePatch]): List[TrianglePatch] = {
        logger.debug(s"Remaining TriangleArea size: ${ta.tris.size}")
        if (ta.empty)
            acc
        else {
            val ptch = createPatch(ta, ta.tris.head)
            logger.debug(s"Found patch size ${ptch.tris.size}")
            generatePatches(ta.withFinished(ptch.tris), ptch :: acc)
        }
    }

    def apply(ta: TriangleArea) = {
        logger.debug("Generating patches")
        val patches = generatePatches(ta, Nil)
        logger.debug("Creating terrain pieces")
        val terrainPieces = patches.map(_.toTerrainPiece)
        logger.debug("Terrain pieces done")
        terrainPieces
    }

    class PatchInfo(min: Position, max: Position, val normal: Position) {
        def addTri(a: Position, b: Position, c: Position) = {
            def extrema(old: Position, fun: (PositionUnit, PositionUnit) => PositionUnit) = {
                val ret = for {
                    p <- List(a, b, c)
                    (x, y) <- p.toList zip old.toList
                } yield fun(x, y)
                Point.fromList(ret)
            }

            val funcs: List[Position => PositionUnit] = List(x => x.x, x => x.y, x => x.z)
            val diffs = for {
                f <- funcs
                coord = List(a, b, c, min, max).map(f)
                mn = coord.min
                mx = coord.max
            } yield (mn, mx)
            val (mn, mx) = diffs.unzip
            val norm = normal + NormalCalculator.normalForTri(a, b, c).normalize
            new PatchInfo(Point.fromList(mn), Point.fromList(mx), norm)
        }
        def size = (max - min).reduce((x, y) => spire.math.max(x, y))
    }

    def initPatchInfo(a: Position, b: Position, c: Position) = new PatchInfo(a, a, NormalCalculator.normalForTri(a, b, c))

    def createPatch(area: TriangleArea, start: Triangle): TrianglePatch = {
        val queue = new Queue[Triangle]
        val positioned = area.positionTri(start)
        val closed = new HashSet[Triangle]
        var info = (initPatchInfo _).tupled(positioned)
        var ret: List[Triangle] = Nil
        queue.enqueue(start)
        while (!queue.isEmpty) {
            val next = queue.dequeue
            if (!closed.contains(next)) {
                closed.add(next)
                val nextpos = area.positionTri(next)
                val nextNormal = (NormalCalculator.normalForTri _).tupled(nextpos)
                val cos = (info.normal dot nextNormal).toDouble
                if (cos > maxCosine) {
                    val ni = (info.addTri _).tupled(nextpos)
                    if (ni.size < maxPosSize) {
                        ret = next :: ret
                        info = ni
                        val neighs = area.neighbours(next).
                            filterNot(closed.contains).
                            foreach(queue.enqueue(_))
                    }
                }
            }

        }
        new TrianglePatch(ret, area)
    }
}