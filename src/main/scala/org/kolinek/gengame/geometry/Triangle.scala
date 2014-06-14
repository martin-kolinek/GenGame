package org.kolinek.gengame.geometry

import org.kolinek.gengame.terragen.mesh.NormalCalculator

case class Triangle(a: Int, b: Int, c: Int) {
    def toList = List(a, b, c)
}