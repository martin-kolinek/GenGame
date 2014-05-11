package org.kolinek.gengame.reporting

import rx.lang.scala.Observable
import org.kolinek.gengame.game.UpdateComponent
import org.kolinek.gengame.util._

trait FPSCounterComponent {
    def fps: Observable[Int]
}

trait DefaultFPSCounterComponent extends FPSCounterComponent {
    self: UpdateComponent =>

    lazy val fps = {
        case class FPSInfo(frames: Int, current: Float, fps: Option[Int]) {
            def next(tpf: Float) = {
                val nextCur = current + tpf
                if (nextCur >= 1.0f) {
                    FPSInfo(0, nextCur - 1.0f, Some(frames + 1))
                } else {
                    FPSInfo(frames + 1, nextCur, None)
                }
            }
        }
        updates
            .scan(FPSInfo(0, 0, None))(_ next _)
            .collectPartFunc {
                case FPSInfo(_, _, Some(fps)) => fps
            }
    }
}