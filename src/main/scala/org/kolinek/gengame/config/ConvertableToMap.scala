package org.kolinek.gengame.config

import scala.collection.JavaConversions._

trait ConvertableToMap {
    self: Product =>

    def keys = {
        this.getClass().getDeclaredFields().map(_.getName)
    }

    def toMap: java.util.Map[String, Any] = {
        val scalaMap = (for ((key, value) <- keys zip this.productIterator.toSeq) yield {
            value match {
                case convToMap: ConvertableToMap => key -> convToMap.toMap
                case other => key -> other
            }
        }).toMap
        scalaMap
    }
}