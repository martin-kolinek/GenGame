package org.kolinek.gengame.util

trait OptionHelpers {
    implicit class SomeOps[T](t: T) {
        def some: Option[T] = Some(t)
    }

    def none: Option[Nothing] = None
}