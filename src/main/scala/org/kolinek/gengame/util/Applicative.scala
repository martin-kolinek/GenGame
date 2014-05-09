package org.kolinek.gengame.util

import shapeless._
import shapeless.ops.tuple.FlatMapper
import shapeless.syntax.std.tuple._
import shapeless.HList
import scala.language.higherKinds
import shapeless.Poly2
import shapeless.ops.hlist.RightFolder
import shapeless.ops.tuple.{ RightFolder => TRightFolder }
import UnaryTCConstraint._
import org.kolinek.gengame.macros.SequenceImpl
import scala.language.experimental.macros

trait Applicative[F[_]] {
    def ap[A, B](fa: => F[A])(f: => F[A => B]): F[B]
    def point[A](a: => A): F[A]
    def map[A, B](fa: F[A])(f: A => B): F[B] = ap(fa)(point(f))
}

trait ApplicativeSequence {
    object applicativeFolder extends Poly2 {
        implicit def caseApplicative[A, B <: HList, F[_]](implicit app: Applicative[F]) = at[F[A], F[B]] {
            (a, b) => app.ap(a)(app.map(b)(bb => (_: A) :: bb))
        }
    }

    def sequenceHlist[F[_]: Applicative, L <: HList: *->*[F]#λ, M <: HList](l: L)(implicit folder: RightFolder[L, F[HNil], applicativeFolder.type]) = l.foldRight(implicitly[Applicative[F]].point(HNil: HNil))(applicativeFolder)

    def sequence[T](t: T): Any = macro SequenceImpl.sequenceImpl[T]
}

trait OptionApplicative {
    implicit val optionApplicative = new Applicative[Option] {
        def ap[A, B](oa: => Option[A])(of: => Option[A => B]) = for {
            a <- oa
            f <- of
        } yield f(a)

        def point[A](a: => A) = Some(a)
    }
}