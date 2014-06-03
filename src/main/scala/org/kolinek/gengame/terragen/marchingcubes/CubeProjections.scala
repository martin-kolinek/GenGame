package org.kolinek.gengame.terragen.marchingcubes

object CubeProjections {
    val projs = Vector(
        Vector(3, 2, 6, 7, 0, 1, 5, 4),
        Vector(3, 0, 1, 2, 7, 4, 5, 6),
        Vector(1, 5, 6, 2, 0, 4, 7, 3))

    def compose2(a: Vector[Int], b: Vector[Int]): Vector[Int] = a.map(b(_))

    def compose(args: Vector[Int]*): Vector[Int] =
        if (args.length == 1)
            args.head
        else
            compose2(args.head, compose(args.tail: _*))

    def applyN[T](func: Vector[Int], n: Int): Vector[Int] = {
        if (n == 0)
            func
        else
            compose(func, applyN(func, n - 1))
    }

    def getAll: Seq[Vector[Int]] = {
        val base = Set(Vector(0, 1, 2, 3, 4, 5, 6, 7))
        closure(base).toSeq
    }

    def closure(current: Set[Vector[Int]]): Set[Vector[Int]] = {
        val applied: Set[Vector[Int]] = for (c <- current; p <- projs)
            yield compose(c, p)
        val all = applied ++ current
        if (current == all)
            current
        else
            closure(all)
    }

}