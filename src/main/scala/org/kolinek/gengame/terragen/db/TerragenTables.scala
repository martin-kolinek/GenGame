package org.kolinek.gengame.terragen.db

import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.geometry._

trait TerragenTables extends GeometryMappers {
    class DoneChunks(tag: Tag) extends Table[(ChunkUnit, ChunkUnit, ChunkUnit)](tag, "donechunks") {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def x = column[ChunkUnit]("x")
        def y = column[ChunkUnit]("y")
        def z = column[ChunkUnit]("z")
        def level = column[Int]("level")
        def * = (x, y, z)
    }

    val doneChunksTable = TableQuery[DoneChunks]

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