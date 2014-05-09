package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.Screen
import scala.reflect.ClassTag
import de.lessvoid.nifty.controls.Controller
import de.lessvoid.nifty.controls.NiftyControl
import org.bushe.swing.event.EventTopicSubscriber
import rx.lang.scala.Observable
import rx.subjects.PublishSubject
import rx.lang.scala.JavaConversions._
import rx.subjects.Subject
import rx.subjects.BehaviorSubject

trait SimpleScreenController
        extends ScreenController
        with TextFieldComponent
        with ButtonComponent {
    private var niftyVar: Nifty = null
    def nifty = niftyVar

    private var screenVar: Screen = null
    def screen = screenVar

    final def bind(nifty: Nifty, screen: Screen) = {
        niftyVar = nifty
        screenVar = screen
        setup()
    }

    final def observable[T: ClassTag](c: NiftyControl): Observable[T] = {
        val subj = PublishSubject.create[T]()
        val cls = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
        nifty.subscribe(screen, c.getId, cls, new EventTopicSubscriber[T] {
            def onEvent(id: String, ev: T) = subj.onNext(ev)
        })
        subj.asObservable
    }

    def setup() = {}

    def onStartScreen() = {}
    def onEndScreen() = {}
}