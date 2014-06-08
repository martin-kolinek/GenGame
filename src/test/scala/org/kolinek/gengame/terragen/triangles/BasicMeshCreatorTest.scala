package org.kolinek.gengame.terragen.triangles

import org.scalatest.FunSuite
import org.kolinek.gengame.terragen.TerrainPiece
import org.kolinek.gengame.geometry._
import spire.syntax.all._
import spire.compat._
import org.kolinek.gengame.terragen.mesh.TriangleArea
import org.kolinek.gengame.terragen.mesh.BasicMeshProcessor

class BasicMeshCreatorTest extends FunSuite {
    def pieceContainsTri(p: TerrainPiece, tri: (Position, Position, Position)) = {
        val positions = p.indexes.map(p.points)
        val tris = (for (Seq(a, b, c) <- positions.grouped(3))
            yield (a, b, c)).toSeq
        tris.contains(tri)
    }

    def pieceSize(p: TerrainPiece) = {
        val funcs: List[Position => PositionUnit] = List(x => x.x, x => x.y, x => x.z)
        val diffs = for {
            f <- funcs
            coord = p.points.map(f)
            min = coord.min
            max = coord.max
        } yield max - min
        diffs.max
    }

    test("BasicMeshCreator works on flat land") {
        val ccoords = for (x <- 1.cube to 20.cube; y <- 1.cube to 20.cube) yield (x, y)
        val coords = ccoords.map { case (x, y) => Point(x, y, 0.cube).lower }
        val coordToPt = ccoords.zipWithIndex.map(x => (x._1, x._2.toLong)).toMap
        val positions: Long => Position = coords.zipWithIndex.map(_.swap).map(x => (x._1.toLong, x._2)).toMap
        def triangleArea(minX: CubeUnit, maxX: CubeUnit, minY: CubeUnit, maxY: CubeUnit) = {
            val pairs = for (x <- minX to maxX; y <- minY to maxY)
                yield List((coordToPt(x, y), coordToPt(x + 1, y), coordToPt(x, y + 1)),
                (coordToPt(x + 1, y), coordToPt(x + 1, y + 1), coordToPt(x, y + 1)))
            pairs.flatten.map(Triangle.tupled)
        }

        val triangles = triangleArea(1.cube, 19.cube, 1.cube, 19.cube)
        val area = new TriangleArea(triangles, Set[Triangle](), positions)
        val msh = new BasicMeshProcessor(4.chunk, 40)
        val pieces = msh(area, BoundingBox(Point(3.0.pos, 3.0.pos, -10.pos), Point(6.0.pos, 6.0.pos, 10.pos)))
        assert(pieces._1.size > 0)
        assert(pieces._2.size > 0)
        val processed = triangleArea(3.cube, 5.cube, 3.cube, 5.cube)
        assert(processed.forall { tri =>
            pieces._1.exists { piece =>
                pieceContainsTri(piece, area.positionTri(tri))
            }
        })
        assert(pieces._1.map(pieceSize).max <= 4.chunk.upper.upper)

    }
}