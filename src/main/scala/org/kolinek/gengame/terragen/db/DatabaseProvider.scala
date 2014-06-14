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
            (doneChunksTable.ddl ++ meshesTable.ddl).create
            Q.updateNA("CREATE INDEX ix_points_pos ON points(direction, posx, posy, posz)").execute
            Q.updateNA("CREATE VIRTUAL TABLE meshbounds USING rtree (meshid int, minx float, maxx float, miny float, maxy float, minz float, maxz float)").execute
            Q.updateNA("CREATE INDEX ix_trianglecubes ON triangles(cubeid)").execute
            Q.updateNA("CREATE INDEX ix_donecubes ON donechunks(x, y, z)").execute
            Q.updateNA("CREATE INDEX ix_meshdonecubes ON donemeshchunks(x, y, z)").execute
        }
        db
    }
}