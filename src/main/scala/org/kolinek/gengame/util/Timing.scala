package org.kolinek.gengame.util

import scala.concurrent.duration._

object Timing {
    def timed(act: => Unit) = {
        val current = System.currentTimeMillis
        act
        (System.currentTimeMillis - current).milliseconds
    }
}