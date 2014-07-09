package org.kolinek.gengame.db

import org.scalatest.FunSuite
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import org.kolinek.gengame.util.DefaultOnCloseProvider
import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.db.schema.DefaultSchemaCreatorProvider

class SimpleSessionDatabaseActionExecutorTest extends FunSuite {
    class TestComp extends TestDatabaseProvider
        with DefaultErrorLoggingComponent
        with DefaultOnCloseProvider
        with DefaultSchemaCreatorProvider
        with SingleSessionDatabaseActionExecutorProvider

    test("SimpleSessionDatabaseActionExecutor works") {
        val comp = new TestComp
        info(s"current thread ${Thread.currentThread().getId()}")
        val act = new DatabaseAction[Long] {
            def apply(s: Session) = Thread.currentThread().getId()
        }
        val a1dbThread = comp.databaseActionExecutor.executeAction(act).toBlocking.single
        info(s"database thread: ${comp.dbThreadId}")
        assert(comp.dbThreadId !== -1L)
        assert(comp.dbThreadId !== Thread.currentThread().getId())
        assert(a1dbThread === comp.dbThreadId)
        assert(comp.databaseActionExecutor.executeAction(act).toBlocking.single === comp.dbThreadId)
        comp.close()
    }

    test("SimpleSessionDatabaseActionExecutor executes action without subscriber") {
        val comp = new TestComp
        var executed = false
        comp.databaseActionExecutor.executeAction(DatabaseAction(s => executed = true))
        comp.databaseActionExecutor.executeAction(DatabaseAction(s => {})).toBlocking.single
        assert(executed)
    }
}