package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._

object EdgeDirection extends Enumeration {
    type EdgeDirection = Value
    val X = Value(0)
    val Y = Value(1)
    val Z = Value(2)

    def offset(v: Value) = v match {
        case X => Point(1l, 0, 0)
        case Y => Point(0l, 1, 0)
        case Z => Point(0l, 0, 1)
    }
}
