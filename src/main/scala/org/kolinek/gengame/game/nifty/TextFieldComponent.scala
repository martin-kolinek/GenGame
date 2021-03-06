package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.controls.TextField
import de.lessvoid.nifty.controls.TextFieldChangedEvent
import rx.lang.scala.JavaConversions._
import rx.lang.scala.Observable
import rx.lang.scala.subjects.BehaviorSubject

trait TextFieldComponent {
    self: SimpleScreenController =>

    class NiftyTextField(val id: String) extends SimpleNiftyControl {
        private val underlying = screen.findNiftyControl(id, classOf[TextField])
        private val textSubj = BehaviorSubject(underlying.getRealText)

        val text = textSubj.obs.distinctUntilChanged

        def events = List(textSubj.eitrans[TextFieldChangedEvent](_.getText))

        def setText(txt: String) = {
            textSubj.onNext(txt)
            underlying.setText(txt)
        }
    }
}