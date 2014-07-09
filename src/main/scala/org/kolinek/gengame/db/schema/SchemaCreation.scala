package org.kolinek.gengame.db.schema

import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.db.DatabaseAction
import slick.jdbc.meta.MTable

trait SchemaCreator extends DatabaseAction[Unit] {
}

trait SchemaCreatorProvider {
    def schemaCreator: SchemaCreator
}

trait SchemaUpdater extends DatabaseAction[Unit] {
    val version: Int
    final def updateTo(s: Session) {
        updateToWithoutVersion(s)
        VersionTables.schemaVersionsTable.delete(s)
        VersionTables.schemaVersionsTable.insert(VersionTables.SchemaVersion(version))(s)
    }
    protected def updateToWithoutVersion(s: Session)

    def apply(s: Session) = updateTo(s)
}

object Version1SchemaVersion extends SchemaUpdater {
    val version = 1
    def updateToWithoutVersion(s: Session) = {
        VersionTables.schemaVersionsTable.ddl.create(s)
    }
}

class DefaultSchemaCreator(updaters: SchemaUpdater*) extends SchemaCreator {
    def apply(s: Session) {
        val version = if (MTable.getTables.list(s)
            .exists(_.name.name == VersionTables.schemaVersionsTable.baseTableRow.tableName)) {
            VersionTables.schemaVersionsTable.list(s).headOption.map(_.id).getOrElse(throw new Exception("Corrupted database"))
        } else 0

        updaters.filter(_.version > version).sortBy(_.version).foreach(_.updateTo(s))
    }
}

trait DefaultSchemaCreatorProvider extends SchemaCreatorProvider {
    def schemaCreator = new DefaultSchemaCreator(Version1SchemaVersion, Version2Terragen)
}