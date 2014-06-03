package org.kolinek.gengame.terragen.marchingcubes

object MarchCubeCases {
    val baseMap: Map[Set[Int], List[(Int, Int, Int)]] = Map(
        Set.empty -> Nil,
        Set(0) -> List((0, 3, 4)),
        Set(0, 1) -> List((1, 3, 4), (1, 4, 5)),
        Set(0, 5) -> List((0, 9, 5), (3, 9, 0), (3, 4, 8), (3, 8, 9)),
        Set(0, 6) -> List((0, 3, 4), (6, 9, 10)),
        Set(0, 1, 2) -> List((5, 6, 4), (6, 2, 4), (2, 3, 4)),
        Set(0, 1, 6) -> List((5, 9, 4), (9, 10, 4), (4, 10, 3), (10, 6, 3), (6, 1, 3)),
        Set(0, 2, 7) -> List((0, 1, 4), (1, 6, 10), (10, 11, 4), (4, 1, 10), (2, 3, 7)),
        Set(0, 1, 2, 3) -> List((4, 5, 6), (4, 6, 7)),
        Set(0, 1, 3, 4) -> List((5, 1, 8), (1, 2, 7), (7, 11, 8), (8, 1, 7)),
        Set(0, 1, 6, 7) -> List((4, 5, 9), (4, 9, 11), (1, 3, 6), (3, 7, 6)),
        Set(0, 3, 6, 7) -> List((0, 2, 4), (2, 6, 4), (6, 9, 4), (9, 11, 4)),
        Set(0, 1, 2, 7) -> List((4, 5, 11), (5, 10, 11), (5, 6, 10), (2, 3, 7)),
        Set(0, 2, 5, 7) -> List((0, 1, 5), (6, 10, 9), (4, 8, 11), (5, 3, 7)),
        Set(2, 4, 5, 6) -> List((11, 5, 4), (10, 5, 11), (2, 5, 10), (2, 1, 5)),
        Set(1, 3, 4, 6, 7) -> List((9, 8, 5), (0, 4, 3), (1, 5, 6)),
        Set(2, 3, 4, 5, 7) -> List((1, 5, 3), (3, 5, 4), (6, 10, 9)),
        Set(2, 3, 4, 6, 7) -> List((1, 9, 3), (3, 9, 8), (3, 8, 4)),
        Set(2, 3, 4, 5, 6, 7) -> List((1, 5, 3), (3, 5, 4)),
        Set(1, 3, 4, 5, 6, 7) -> List((0, 4, 3), (2, 6, 1)),
        Set(1, 2, 3, 4, 5, 7) -> List((0, 4, 3), (6, 10, 9)),
        Set(0, 2, 3, 4, 5, 6, 7) -> List((0, 1, 5)),
        Set(0, 1, 2, 3, 4, 5, 6, 7) -> Nil)

    private def projectTriangleWithEdgeFunc(x: (Int, Int, Int), f: Int => Int) = (f(x._1), f(x._2), f(x._3))

    def projectTriangle(x: (Int, Int, Int), f: Int => Int) =
        projectTriangleWithEdgeFunc(x, CubeEdges.projectEdge(f, _))

    def getAllMarchingCubeCases = {
        for ((k, v) <- baseMap; f <- CubeProjections.getAll)
            yield k.map(f) -> v.map(projectTriangle(_, f))
    }
}