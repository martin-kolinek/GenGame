package org.kolinek.gengame.game.nifty

import rx.subjects.BehaviorSubject
import de.lessvoid.nifty.controls.TextField
import de.lessvoid.nifty.controls.TextFieldChangedEvent
import rx.lang.scala.JavaConversions._
import rx.lang.scala.Observable

trait TextFieldComponent {
    self: SimpleScreenController =>

    class NiftyTextField(id: String) {
        private val underlying = screen.findNiftyControl(id, classOf[TextField])
        private val textSubj = BehaviorSubject.create(underlying.getRealText)

        lazy val text: Observable[String] = textSubj.asObservable.distinct

        observable[TextFieldChangedEvent](underlying).map(_.getText).subscribe(textSubj)

        def setText(txt: String) = {
            textSubj.onNext(txt)
            underlying.setText(txt)
        }
    }
}