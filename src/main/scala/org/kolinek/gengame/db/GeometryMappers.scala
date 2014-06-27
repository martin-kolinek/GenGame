package org.kolinek.gengame.db

import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.geometry._

trait GeometryMappers {
    implicit val positionTypeMapper = MappedColumnType.base[PositionUnit, Double](x => x.underlying, x => new PositionUnit(x))
    implicit val cubeTypeMapper = MappedColumnType.base[CubeUnit, Long](x => x.underlying, x => new CubeUnit(x))
    implicit val chunkTypeMapper = MappedColumnType.base[ChunkUnit, Long](x => x.underlying, x => new ChunkUnit(x))
}