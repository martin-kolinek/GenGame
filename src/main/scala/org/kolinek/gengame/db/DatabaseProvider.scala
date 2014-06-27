package org.kolinek.gengame.db

import slick.driver.SQLiteDriver.simple._
import slick.jdbc.{ StaticQuery => Q }
import rx.lang.scala.Observable
import org.kolinek.gengame.threading.SingleThreadedScheduler
import rx.lang.scala.Scheduler
import org.kolinek.gengame.terragen.db.TerragenTables
import scala.slick.jdbc.{ StaticQuery => Q }
import scala.slick.jdbc.StaticQuery.staticQueryToInvoker

trait DatabaseProvider {
    def database: Observable[Database]
    def databaseScheduler: Scheduler
}

trait InMemoryDatabaseProvider extends DatabaseProvider with TerragenTables {
    lazy val database = {
        val db = Database.forURL("jdbc:sqlite::memory:", driver = "org.sqlite.JDBC")
        db.withTransaction { implicit s =>
            (doneChunksTable.ddl ++ meshesTable.ddl).create
            Q.updateNA("CREATE INDEX ix_donechunks ON donechunks(x, y, z)").execute
        }
        Observable.items(db).subscribeOn(databaseScheduler)
    }

    lazy val databaseScheduler = new SingleThreadedScheduler
}