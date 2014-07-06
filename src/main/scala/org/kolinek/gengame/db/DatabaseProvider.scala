package org.kolinek.gengame.db

import slick.driver.SQLiteDriver.simple._

trait DatabaseProvider {
    def database: Database
}

trait InMemoryDatabaseProvider extends DatabaseProvider {
    lazy val database = {
        Database.forURL("jdbc:sqlite::memory:", driver = "org.sqlite.JDBC")
    }
}