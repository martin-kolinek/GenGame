package org.kolinek.gengame.util

import shapeless.Poly1
import shapeless.ops.tuple.FlatMapper
import shapeless.syntax.std.tuple._

trait LowPriorityFlatten extends Poly1 {
    implicit def default[T] = at[T](Tuple1(_))
}
object tupleFlatten extends LowPriorityFlatten {
    implicit def caseTuple[P <: Product](implicit fm: FlatMapper[P, tupleFlatten.type]) =
        at[P](_.flatMap(tupleFlatten))
}
