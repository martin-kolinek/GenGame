package org.kolinek.gengame.db

import org.scalatest.FunSuite
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import slick.driver.SQLiteDriver.simple._
import rx.lang.scala.Observable

class BufferedDatabaseActionExecutorTest extends FunSuite {
    class TestComp extends InMemoryDatabaseProvider
        with BufferDatabaseActionExecutorProvider
        with DefaultErrorLoggingComponent

    test("BufferedDatabaseActionExecutor runs actions on database thread") {
        val comp = new TestComp
        info(s"current thread ${Thread.currentThread().getId()}")
        val dbThreadId = Observable.items(Unit).subscribeOn(comp.databaseScheduler).map { _ =>
            Thread.currentThread().getId()
        }.toBlocking.first
        info(s"database thread: $dbThreadId")
        val act = new DatabaseAction[Long] {
            def apply(s: Session) = Thread.currentThread().getId()
        }
        assert(comp.databaseActionExecutor.executeAction(act).toBlocking.single === dbThreadId)
        assert(comp.databaseActionExecutor.executeAction(act).toBlocking.single === dbThreadId)
    }
}