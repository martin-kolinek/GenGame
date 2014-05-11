package org.kolinek.gengame.main

import rx.lang.scala.Observable
import com.jme3.input.KeyInput
import org.kolinek.gengame.config.ControlsConfigProvider
import org.kolinek.gengame.game.InputManagerProvider
import rx.lang.scala.subjects.BehaviorSubject
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import com.jme3.input.RawInputListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.MouseAxisTrigger
import java.awt.MouseInfo
import com.jme3.input.MouseInput
import com.jme3.input.controls.AnalogListener

trait ControlsComponent {
    def forward: Observable[Boolean]
    def back: Observable[Boolean]
    def left: Observable[Boolean]
    def right: Observable[Boolean]
    def up: Observable[Boolean]
    def down: Observable[Boolean]
    def rotateleft: Observable[Boolean]
    def rotateright: Observable[Boolean]
    def mouse: Observable[(Float, Float)]
}

trait DefaultControlsComponent extends ControlsComponent with ErrorHelpers {
    self: ControlsConfigProvider with InputManagerProvider with ErrorLoggingComponent =>

    private var counter = 0

    private def createKeyBinding(confObs: Observable[Seq[Int]]) = {
        val name = synchronized {
            val nm = counter.toString
            counter += 1
            nm
        }
        val subj = BehaviorSubject(false)
        confObs.foreach { mapp =>
            inputManager.foreach { inp =>
                if (inp.hasMapping(name))
                    inp.deleteMapping(name)
                inp.addMapping(name, mapp.map(new KeyTrigger(_)): _*)
            }
        }
        inputManager.foreach { inp =>
            inp.addListener(new ActionListener {
                def onAction(name: String, keyPressed: Boolean, tpf: Float) {
                    subj.onNext(keyPressed)
                }
            }, name)
        }
        subj.distinctUntilChanged
    }

    lazy val mouse = {
        val subj = BehaviorSubject((0.0f, 0.0f))
        val leftName = "MouseLeft"
        val rightName = "MouseRight"
        val upName = "MouseUp"
        val downName = "MouseDown"
        inputManager.foreach { inp =>
            inp.addMapping(leftName, new MouseAxisTrigger(MouseInput.AXIS_X, true))
            inp.addMapping(rightName, new MouseAxisTrigger(MouseInput.AXIS_X, false))
            inp.addMapping(upName, new MouseAxisTrigger(MouseInput.AXIS_Y, false))
            inp.addMapping(downName, new MouseAxisTrigger(MouseInput.AXIS_Y, true))

            class Listener(dir: (Float, Float)) extends AnalogListener {
                def onAnalog(s: String, value: Float, tpf: Float) {
                    subj.onNext(dir._1 * value -> dir._2 * value)
                }
            }

            inp.addListener(new Listener((1, 0)), rightName)
            inp.addListener(new Listener((-1, 0)), leftName)
            inp.addListener(new Listener((0, 1)), upName)
            inp.addListener(new Listener((0, -1)), downName)
        }
        subj
    }

    lazy val forward = createKeyBinding(controlsConfig.map(_.forward))
    lazy val back = createKeyBinding(controlsConfig.map(_.back))
    lazy val left = createKeyBinding(controlsConfig.map(_.left))
    lazy val right = createKeyBinding(controlsConfig.map(_.right))
    lazy val up = createKeyBinding(controlsConfig.map(_.up))
    lazy val down = createKeyBinding(controlsConfig.map(_.down))
    lazy val rotateleft = createKeyBinding(controlsConfig.map(_.rotleft))
    lazy val rotateright = createKeyBinding(controlsConfig.map(_.rotright))
}