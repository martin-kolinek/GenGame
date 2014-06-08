package org.kolinek.gengame.terragen.db

import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.geometry._

trait TerragenTables extends GeometryMappers {
    class Points(tag: Tag) extends Table[(Int, CubeUnit, CubeUnit, CubeUnit, PositionUnit, PositionUnit, PositionUnit)](tag, "points") {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def direction = column[Int]("direction")
        def posx = column[CubeUnit]("posx")
        def posy = column[CubeUnit]("posy")
        def posz = column[CubeUnit]("posz")
        def realx = column[PositionUnit]("realx")
        def realy = column[PositionUnit]("realy")
        def realz = column[PositionUnit]("realz")
        def * = (direction, posx, posy, posz, realx, realy, realz)
    }
    val pointsTable = TableQuery[Points]

    class Triangles(tag: Tag) extends Table[(Long, Long, Long, Boolean)](tag, "triangles") {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def pointaId = column[Long]("pointa")
        def pointa = foreignKey("pt_a_fk", pointaId, pointsTable)(_.id)
        def pointbId = column[Long]("pointb")
        def pointb = foreignKey("pt_b_fk", pointbId, pointsTable)(_.id)
        def pointcId = column[Long]("pointc")
        def pointc = foreignKey("pt_c_fk", pointcId, pointsTable)(_.id)
        def finished = column[Boolean]("finished")
        def * = (pointaId, pointbId, pointcId, finished)
    }

    val trianglesTable = TableQuery[Triangles]

    class DoneChunks(tag: Tag, name: String) extends Table[(ChunkUnit, ChunkUnit, ChunkUnit)](tag, name) {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def x = column[ChunkUnit]("x")
        def y = column[ChunkUnit]("y")
        def z = column[ChunkUnit]("z")
        def * = (x, y, z)
    }

    val doneChunksTable = TableQuery[DoneChunks]((tag: Tag) => new DoneChunks(tag, "donechunks"))
    val doneMeshChunksTable = TableQuery[DoneChunks]((tag: Tag) => new DoneChunks(tag, "donemeshchunks"))

    class Meshes(tag: Tag) extends Table[(Int)](tag, "mesh") {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def level = column[Int]("level")
        def data = column[Array[Byte]]("data")
        def * = level
    }

    val meshesTable = TableQuery[Meshes]

    class MeshBounds(tag: Tag) extends Table[(Long, PositionUnit, PositionUnit, PositionUnit, PositionUnit, PositionUnit, PositionUnit)](tag, "meshbounds") {
        def meshId = column[Long]("meshid")
        def mesh = foreignKey("mesh_fk", meshId, meshesTable)(_.id)
        def minx = column[PositionUnit]("minx")
        def maxx = column[PositionUnit]("maxx")
        def miny = column[PositionUnit]("miny")
        def maxy = column[PositionUnit]("maxy")
        def minz = column[PositionUnit]("minz")
        def maxz = column[PositionUnit]("maxz")
        def * = (meshId, minx, maxx, miny, maxy, minz, maxz)
    }

    val meshBoundsTable = TableQuery[MeshBounds]
}