package org.kolinek.gengame.geometry

import org.kolinek.gengame.terragen.mesh.NormalCalculator

case class Triangle(a: Long, b: Long, c: Long) {
    def toList = List(a, b, c)
}