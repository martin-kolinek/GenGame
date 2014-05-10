package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.screen.ScreenController
import de.lessvoid.nifty.Nifty
import de.lessvoid.nifty.screen.Screen
import scala.reflect.ClassTag
import de.lessvoid.nifty.controls.Controller
import de.lessvoid.nifty.controls.NiftyControl
import org.bushe.swing.event.EventTopicSubscriber
import rx.lang.scala.Observable
import rx.lang.scala.JavaConversions._
import rx.lang.scala.Subject

trait SimpleScreenController
        extends ScreenController
        with SimpleNiftyControlComponent
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

    trait EventInfo {
        def registerEvent(id: String)
    }

    private class EventInfoSpec[T: ClassTag](f: T => Unit) extends EventInfo {
        def registerEvent(id: String) {
            val cls = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
            nifty.subscribe(screen, id, cls, new EventTopicSubscriber[T] {
                def onEvent(id: String, ev: T) = f(ev)
            })
        }
    }

    protected implicit class SubjectOps[T: ClassTag](s: Subject[T]) {
        def ei: EventInfo = new EventInfoSpec[T](s.onNext _)
        def eitrans[R: ClassTag](func: R => T): EventInfo = new EventInfoSpec[R](p => s.onNext(func(p)))
        def obs: Observable[T] = s.asObservable
    }

    def controls: Seq[SimpleNiftyControl]

    def setup() = {}

    final def onStartScreen() = {
        for {
            c <- controls
            ei <- c.events
        } {
            ei.registerEvent(c.id)
        }
    }
    final def onEndScreen() = {
    }
}