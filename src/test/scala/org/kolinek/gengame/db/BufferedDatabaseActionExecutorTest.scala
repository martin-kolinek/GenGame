package org.kolinek.gengame.db

import org.scalatest.FunSuite
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import slick.driver.SQLiteDriver.simple._
import rx.lang.scala.Observable
import org.kolinek.gengame.util.DefaultOnCloseProvider
import org.kolinek.gengame.db.schema.DefaultSchemaCreatorProvider

class BufferedDatabaseActionExecutorTest extends FunSuite {
    class TestComp extends TestDatabaseProvider
        with DefaultOnCloseProvider
        with BufferDatabaseActionExecutorProvider
        with DefaultSchemaCreatorProvider
        with DefaultErrorLoggingComponent

    test("BufferedDatabaseActionExecutor runs actions on database thread") {
        val comp = new TestComp
        info(s"current thread ${Thread.currentThread().getId()}")
        val act = new DatabaseAction[Long] {
            def apply(s: Session) = Thread.currentThread().getId()
        }
        val a1dbThread = comp.databaseActionExecutor.executeAction(act).toBlocking.single
        info(s"database thread: ${comp.dbThreadId}")
        assert(comp.dbThreadId !== -1L)
        assert(a1dbThread === comp.dbThreadId)
        assert(comp.databaseActionExecutor.executeAction(act).toBlocking.single === comp.dbThreadId)
        comp.close()
    }
}