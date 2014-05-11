package org.kolinek.gengame.main

import org.kolinek.gengame.geometry._
import org.kolinek.gengame.game.UpdateStep
import rx.lang.scala.Observable
import org.kolinek.gengame.game.JmeCameraComponent
import rx.lang.scala.subjects.BehaviorSubject
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait CameraPosition {
    def cameraPosition: Observable[Position]
}

trait DefaultCameraPosition extends UpdateStep with CameraPosition with ErrorHelpers {
    self: JmeCameraComponent with ErrorLoggingComponent =>

    private lazy val camPosSubj = BehaviorSubject(PositionConst.zero)

    abstract override def update(tpf: Float) {
        super.update(tpf)
        jmeCamera.foreach { cam =>
            val loc = cam.getLocation()
            PositionConst(loc.getX.pos, loc.getY.pos, loc.getZ.pos)
        }
    }

    lazy val cameraPosition = camPosSubj
}