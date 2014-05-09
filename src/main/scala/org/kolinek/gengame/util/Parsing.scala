package org.kolinek.gengame.util

import scala.util.Try

trait Parsing {
    def parseInt(str: String) = Try(str.toInt).toOption
}