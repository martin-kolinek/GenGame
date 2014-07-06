package org.kolinek.gengame.db.schema

import slick.driver.SQLiteDriver.simple._

object VersionTables {
    case class SchemaVersion(id: Int)

    class SchemaVersions(tag: Tag) extends Table[SchemaVersion](tag, "schema_version") {
        def id = column[Int]("id")
        def * = id <> (SchemaVersion.apply, SchemaVersion.unapply)
    }

    val schemaVersionsTable = TableQuery[SchemaVersions]
}