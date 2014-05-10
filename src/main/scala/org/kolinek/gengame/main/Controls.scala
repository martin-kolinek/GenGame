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

trait ControlsComponent {
    def forward: Observable[Boolean]
    def back: Observable[Boolean]
    def left: Observable[Boolean]
    def right: Observable[Boolean]
    def up: Observable[Boolean]
    def down: Observable[Boolean]
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

    lazy val forward = createKeyBinding(controlsConfig.map(_.forward))
    lazy val back = createKeyBinding(controlsConfig.map(_.back))
    lazy val left = createKeyBinding(controlsConfig.map(_.left))
    lazy val right = createKeyBinding(controlsConfig.map(_.right))
    lazy val up = createKeyBinding(controlsConfig.map(_.up))
    lazy val down = createKeyBinding(controlsConfig.map(_.down))
}