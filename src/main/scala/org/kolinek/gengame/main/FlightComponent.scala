package org.kolinek.gengame.main

import org.kolinek.gengame.geometry.Position
import rx.lang.scala.Observable
import org.kolinek.gengame.util.tupleFlatten
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.game.UpdateComponent
import org.kolinek.gengame.util._
import spire.syntax.vectorSpace._
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.game.JmeCameraComponent
import com.jme3.math.Vector3f
import com.jme3.math.Matrix4f
import com.jme3.math.Matrix3f
import com.jme3.math.Quaternion
import org.kolinek.gengame.game.InputManagerProvider

case class CameraStep(translation: Position, rotationRight: Float, rotationUp: Float, roll: Float)

trait FlightComponent {
    def cameraSteps: Observable[CameraStep]
}

trait DefaultFlightComponent extends FlightComponent {
    self: ControlsComponent with UpdateComponent =>

    lazy val cameraSteps: Observable[CameraStep] = {
        def value(v: PositionUnit)(obs: Observable[Boolean]) = obs.map(b => if (b) v else 0.pos)
        def axis(pos: Observable[Boolean], neg: Observable[Boolean]) = {
            (value(1.pos)(pos) combineLatest value(-1.pos)(neg)).map {
                case (p, n) => p + n
            }
        }

        val multipliers = (axis(left, right)
            combineLatest axis(up, down)
            combineLatest axis(forward, back)
            combineLatest axis(rotateleft, rotateright))
            .map(tupleFlatten)
            .map {
                case (lr, ud, fb, roll) => Position(lr, ud, fb) -> roll
            }

        val trans = updates.withLatest(multipliers).map {
            case (tpf, (mult, roll)) => CameraStep((5.pos * tpf.pos) *: mult, 0, 0, roll.underlying.toFloat * 0.01f)
        }
        val rot = mouse.map {
            case (x, y) => CameraStep(Position.zero, x, y, 0)
        }
        trans merge rot
    }
}

trait JmeFlightComponent extends ErrorHelpers {
    self: FlightComponent with JmeCameraComponent with ErrorLoggingComponent with InputManagerProvider =>

    cameraSteps.foreach { c =>
        jmeCamera.foreach { jc =>
            val rot = jc.getRotation()
            val rotMat = rot.toRotationMatrix()
            val loc = jc.getLocation()
            val tran = c.translation.toJmeVector
            rotMat.mult(tran, tran)
            tran.addLocal(loc)
            jc.setLocation(tran)

            val xRot = new Matrix3f
            xRot.fromAngleAxis(c.rotationRight, new Vector3f(0, -1, 0))
            val yRot = new Matrix3f
            yRot.fromAngleAxis(c.rotationUp, new Vector3f(-1, 0, 0))
            val zRot = new Matrix3f
            zRot.fromAngleAxis(c.roll, new Vector3f(0, 0, -1))
            rotMat.multLocal(xRot)
            rotMat.multLocal(yRot)
            rotMat.multLocal(zRot)
            val q = new Quaternion
            q.fromRotationMatrix(rotMat)
            q.normalizeLocal()
            jc.setRotation(q)

        }
    }

    inputManager.foreach { inp =>
        inp.setCursorVisible(false)
    }
}

trait FlightModule
        extends DefaultFlightComponent
        with JmeFlightComponent {
    self: UpdateComponent with ControlsComponent with JmeCameraComponent with ErrorLoggingComponent with InputManagerProvider =>
}