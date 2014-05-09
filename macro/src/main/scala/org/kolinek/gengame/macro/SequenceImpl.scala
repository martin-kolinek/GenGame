package org.kolinek.gengame.macros

import scala.reflect.macros.whitebox.Context

object SequenceImpl {
    def sequenceImpl[T](c: Context)(t: c.Expr[T]): c.Tree = {
        import c.universe._
        q"""
        {
            import shapeless._
            import org.kolinek.gengame.util.sequenceHlist
            import syntax.std.tuple._
            sequenceHlist($t.productElements).map(tupled)
        }
        """
    }
}