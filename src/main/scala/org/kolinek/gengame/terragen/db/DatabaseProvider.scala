package org.kolinek.gengame.terragen.db

import slick.driver.SQLiteDriver.simple._
import slick.jdbc.{ StaticQuery => Q }

trait DatabaseProvider {
    def database: Database
}

trait InMemoryDatabaseProvider extends DatabaseProvider with TerragenTables {
    val database = {
        val db = Database.forName(":memory:")
        db.withTransaction { implicit s =>
            (pointsTable.ddl ++ trianglesTable.ddl ++ doneChunksTable.ddl ++ doneMeshChunksTable.ddl ++ meshesTable.ddl).create
            /*sqlu"CREATE INDEX ix_points_pos ON points(direction, posx, posy, posz)".execute(s)
            sqlu"CREATE VIRTUAL TABLE meshbounds USING rtree (meshid int, minx float, maxx float, miny float, maxy float, minz float, maxz float)".execute
            sqlu"CREATE INDEX ix_trianglecubes ON triangles(cubeid)".execute
            sqlu"CREATE VIRTUAL TABLE donertree USING rtree_i32 (cubeid int, minx int, maxx int, miny int, maxy int, minz int, maxz int)".execute
            sqlu"CREATE INDEX ix_donecubes ON donecubes(x, y, z)".execute
            sqlu"CREATE INDEX ix_meshdonecubes ON meshdonecubes(x, y, z)".execute
            sqlu"CREATE INDEX ix_meshdef ON meshdef(meshid, sequenceno)".execute*/
        }
        db
    }
}