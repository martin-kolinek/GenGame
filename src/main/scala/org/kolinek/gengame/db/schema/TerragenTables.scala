package org.kolinek.gengame.db.schema

import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.db.GeometryMappers
import scala.slick.jdbc.{ StaticQuery => Q }
import com.typesafe.scalalogging.slf4j.LazyLogging

trait TerragenTables extends GeometryMappers {
    case class DoneChunk(id: Long, x: ChunkUnit, y: ChunkUnit, z: ChunkUnit)

    class DoneChunks(tag: Tag) extends Table[(ChunkUnit, ChunkUnit, ChunkUnit)](tag, "donechunks") {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def x = column[ChunkUnit]("x")
        def y = column[ChunkUnit]("y")
        def z = column[ChunkUnit]("z")
        def level = column[Int]("level")
        def * = (x, y, z)

        def chunks = (id, x, y, z) <> (DoneChunk.tupled, DoneChunk.unapply)
    }

    val doneChunksTable = TableQuery[DoneChunks]

    case class Mesh(id: Long, data: Array[Byte])

    class Meshes(tag: Tag) extends Table[(Array[Byte], Long)](tag, "mesh") {
        def id = column[Long]("ROWID", O.AutoInc, O.PrimaryKey)
        def chunkId = column[Long]("chunk_id")
        def data = column[Array[Byte]]("data")
        def chunk = foreignKey("FK_mesh_chunk", chunkId, doneChunksTable)(_.id)
        def * = (data, chunkId)

        def meshes = (id, data) <> (Mesh.tupled, Mesh.unapply)
    }

    val meshesTable = TableQuery[Meshes]
}

object Version2Terragen extends SchemaUpdater with TerragenTables with LazyLogging {
    val version = 2

    def updateToWithoutVersion(s: Session) {
        logger.info("Creating terragen tables")
        (doneChunksTable.ddl ++ meshesTable.ddl).create(s)
        Q.updateNA("CREATE INDEX ix_donechunks ON donechunks(x, y, z)").execute(s)
    }
} 