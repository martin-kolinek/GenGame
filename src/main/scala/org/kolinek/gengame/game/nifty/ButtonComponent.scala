package org.kolinek.gengame.game.nifty

import de.lessvoid.nifty.controls.Button
import de.lessvoid.nifty.controls.ButtonClickedEvent
import rx.subjects.PublishSubject

trait ButtonComponent {
    self: SimpleNiftyControlComponent with SimpleScreenController =>

    class NiftyButton(val id: String) extends SimpleNiftyControl {
        private val underlying = screen.findNiftyControl(id, classOf[Button])

        private val clickSubj = PublishSubject.create[ButtonClickedEvent]
        val clicks = clickSubj.obs.map(_ => {})

        def events = List(clickSubj.ei)

        def setEnabled(enabled: Boolean) = {
            if (enabled)
                underlying.enable()
            else
                underlying.disable()
        }
    }
}