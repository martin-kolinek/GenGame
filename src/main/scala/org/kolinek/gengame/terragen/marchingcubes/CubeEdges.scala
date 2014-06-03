package org.kolinek.gengame.terragen.marchingcubes

object CubeEdges {
    def getEdge(a: Int, b: Int): Int =
        if (a > b) getEdge(b, a)
        else (a, b) match {
            case (0, 1) => 0
            case (1, 2) => 1
            case (2, 3) => 2
            case (0, 3) => 3
            case (0, 4) => 4
            case (1, 5) => 5
            case (2, 6) => 6
            case (3, 7) => 7
            case (4, 5) => 8
            case (5, 6) => 9
            case (6, 7) => 10
            case (4, 7) => 11
            case _ => throw new IllegalArgumentException("Edge " + (a, b).toString() + " does not exist");
        }
    def getPoints(a: Int): (Int, Int) = a match {
        case 0 => (0, 1)
        case 1 => (1, 2)
        case 2 => (2, 3)
        case 3 => (0, 3)
        case 4 => (0, 4)
        case 5 => (1, 5)
        case 6 => (2, 6)
        case 7 => (3, 7)
        case 8 => (4, 5)
        case 9 => (5, 6)
        case 10 => (6, 7)
        case 11 => (4, 7)
        case _ => throw new IllegalArgumentException("Edge " + a.toString() + " does not exist")
    }

    def projectEdge(nodeFunc: Int => Int, edge: Int) = {
        val (x, y) = getPoints(edge)
        getEdge(nodeFunc(x), nodeFunc(y))
    }
}