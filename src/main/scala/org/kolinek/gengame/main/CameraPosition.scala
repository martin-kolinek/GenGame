package org.kolinek.gengame.main

import org.kolinek.gengame.geometry._
import org.kolinek.gengame.game.UpdateStep
import rx.lang.scala.Observable
import org.kolinek.gengame.game.JmeCameraComponent
import rx.lang.scala.subjects.BehaviorSubject
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.game.UpdateComponent
import org.kolinek.gengame.util._
import org.kolinek.gengame.threading._

trait CameraPositionComponent {
    def cameraPosition: Observable[Position]
}

trait DefaultCameraPosition extends CameraPositionComponent {
    self: JmeCameraComponent with UpdateComponent =>

    jmeCamera.foreach { cam =>
        val loc = cam.getLocation()
        Position(loc.getX.pos, loc.getY.pos, loc.getZ.pos)
    }

    lazy val cameraPosition = for {
        _ <- updates
        cam <- jmeCamera
    } yield {
        val loc = cam.getLocation()
        Position(loc.getX.pos, loc.getY.pos, loc.getZ.pos)
    }
}