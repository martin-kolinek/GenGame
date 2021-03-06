package org.kolinek.gengame.db

import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.util.OnCloseProvider
import rx.lang.scala.Observable

trait TestDatabaseProvider extends DatabaseProvider {
    private var dbThreadIdVar = -1L

    def dbThreadId = dbThreadIdVar

    def database = {
        if (dbThreadIdVar == -1L)
            dbThreadIdVar = Thread.currentThread().getId()
        Database.forURL("jdbc:sqlite::memory:", driver = "org.sqlite.JDBC")
    }
}

trait TestActionExecutor extends DatabaseActionExecutorProvider {
    self: DatabaseProvider with OnCloseProvider =>

    private lazy val session = database.createSession()

    lazy val databaseActionExecutor = new DatabaseActionExecutor {

        def executeAction[T](act: DatabaseAction[T]) = {
            Observable.items(act(session))
        }
    }

    onClose.foreach { _ =>
        session.close()
    }
}