package org.kolinek.gengame.db.schema

import org.scalatest.FunSuite
import org.kolinek.gengame.db.TestDatabaseProvider
import org.kolinek.gengame.util.DefaultOnCloseProvider
import org.kolinek.gengame.db.TestActionExecutor
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import org.kolinek.gengame.db.DatabaseAction
import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.db.SingleSessionDatabaseActionExecutorProvider

class SchemaCreatorTest extends FunSuite with TerragenTables {
    class TestComp extends TestDatabaseProvider
            with SingleSessionDatabaseActionExecutorProvider
            with SchemaCreatorProvider
            with DefaultOnCloseProvider
            with DefaultErrorLoggingComponent {
        def schemaCreator = new SchemaCreator {
            def apply(s: Session) = {}
        }
    }

    test("Version 1 works") {
        val comp = new TestComp
        comp.databaseActionExecutor.executeAction(Version1SchemaVersion).toBlocking.single
        val version = comp.databaseActionExecutor.executeAction(DatabaseAction { s =>
            VersionTables.schemaVersionsTable.list(s).head.id
        }).toBlocking.single
        assert(version === 1)
        comp.close()
    }

    test("Version 2 works") {
        val comp = new TestComp
        comp.databaseActionExecutor.executeAction(Version1SchemaVersion).toBlocking.single
        comp.databaseActionExecutor.executeAction(Version2Terragen).toBlocking.single
        val version = comp.databaseActionExecutor.executeAction(DatabaseAction { s =>
            VersionTables.schemaVersionsTable.list(s).head.id
        }).toBlocking.single
        val (meshCount, doneChunksCount) = comp.databaseActionExecutor.executeAction(DatabaseAction { s =>
            (meshesTable.list(s).size, doneChunksTable.list(s).size)
        }).toBlocking.single
        assert(version === 2)
        assert(meshCount === 0)
        assert(doneChunksCount === 0)
        comp.close()
    }

    class TestSchemaUpdater(val version: Int, shouldBefore: TestSchemaUpdater*) extends SchemaUpdater {
        var executed = false

        def updateToWithoutVersion(s: Session) {
            shouldBefore.foreach(x => assert(x.executed))
            executed = true
        }
    }

    test("SchemaCreator init works") {
        val upd2 = new TestSchemaUpdater(2)
        val upd3 = new TestSchemaUpdater(3, upd2)
        val comp = new TestComp
        val schemaCreator = new DefaultSchemaCreator(upd2, upd3, Version1SchemaVersion)
        comp.databaseActionExecutor.executeAction(schemaCreator).toBlocking.single
        assert(upd2.executed)
        assert(upd3.executed)
    }

    test("SchemaCreator from middle works") {
        val upd2 = new TestSchemaUpdater(2)
        val upd3 = new TestSchemaUpdater(3)
        val comp = new TestComp
        val schemaCreator = new DefaultSchemaCreator(upd2, upd3, Version1SchemaVersion)

        comp.databaseActionExecutor.executeAction(Version1SchemaVersion).toBlocking.single
        comp.databaseActionExecutor.executeAction(upd2).toBlocking.single
        assert(upd2.executed)
        upd2.executed = false
        comp.databaseActionExecutor.executeAction(schemaCreator).toBlocking.single
        assert(!upd2.executed)
        assert(upd3.executed)
    }
}