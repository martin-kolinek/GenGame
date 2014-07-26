package org.kolinek.gengame.geometry

trait IsFloatPrecise[T] {
    def toFloat(t: T): Float
}